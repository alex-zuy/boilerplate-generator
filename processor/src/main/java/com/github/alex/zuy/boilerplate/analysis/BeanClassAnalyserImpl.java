package com.github.alex.zuy.boilerplate.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.github.alex.zuy.boilerplate.domain.BeanClass;
import com.github.alex.zuy.boilerplate.domain.BeanProperty;
import com.github.alex.zuy.boilerplate.domain.BeanProperty.AccessModifier;
import com.github.alex.zuy.boilerplate.domain.types.Type;
import com.github.alex.zuy.boilerplate.services.ProcessorContext;
import com.github.alex.zuy.boilerplate.utils.CollectionUtils;
import com.github.alex.zuy.boilerplate.utils.StringUtils;

public class BeanClassAnalyserImpl implements BeanClassAnalyser {

    private static final Pattern GETTER_METHOD_NAME = Pattern.compile("(is|get)[A-Z]\\w*");

    private static final Pattern PATTERN_PROPERTY_NAME_FROM_ACCESSOR_METHOD = Pattern.compile(
        "(?:get|set|is)(?<propertyName>\\w*)");

    private static final String CAPTURE_GROUP_NAME_PROPERTY_NAME = "propertyName";

    private static final Map<Modifier, AccessModifier> MODIFIERS_MAPPING =
        CollectionUtils.<Modifier, AccessModifier>getMapBuilder(HashMap::new)
            .put(Modifier.PRIVATE, AccessModifier.PRIVATE)
            .put(Modifier.PROTECTED, AccessModifier.PROTECTED)
            .put(Modifier.PUBLIC, AccessModifier.PUBLIC)
            .build();

    private static final String CLASS_NAME_OBJECT = "java.lang.Object";

    private final Elements elementUtils;

    private final Types typeUtils;

    private TypeAnalyser typeAnalyser;

    @Inject
    public BeanClassAnalyserImpl(TypeAnalyser typeAnalyser, ProcessorContext processorContext) {
        this.typeAnalyser = typeAnalyser;
        elementUtils = processorContext.getElementUtils();
        typeUtils = processorContext.getTypeUtils();
    }

    /* will be fixed later */
    @SuppressWarnings("PMD.AvoidPrintStackTrace")
    @Override
    public BeanClass analyse(TypeElement classElement) {
        try {
            List<BeanProperty> properties = new ArrayList<>();
            DeclaredType declaredType = (DeclaredType) classElement.asType();

            List<ExecutableElement> allMethods = getAllMethods(classElement);
            Map<String, ExecutableElement> gettersByName = collectGetters(allMethods, declaredType);

            gettersByName.forEach((propertyName, getter) -> {
                try {
                    ExecutableType gettingMethod = asMemberOf(declaredType, getter);
                    Type<?> propertyType = typeAnalyser.analyse(gettingMethod.getReturnType());
                    properties.add(new BeanProperty(propertyName, propertyType, getPropertyAccessModifier(getter)));
                }
                catch (UnsupportedTypeException e) {
                    /* we need to make user aware of this failure but it should not stop entire processing */
                    e.printStackTrace();
                }
            });

            return new BeanClass(typeAnalyser.analyse(classElement.asType()), properties);
        }
        catch (UnsupportedTypeException e) {
            throw new BeanClassAnalysisException("Failed to analyse bean class type", e,
                classElement.getQualifiedName().toString());
        }
    }

    private Map<String, ExecutableElement> collectGetters(List<ExecutableElement> methods, DeclaredType declaredType) {
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        List<ExecutableElement> getters = methods.stream()
            .filter(element -> isGetterMethod(asMemberOf(declaredType, element), element))
            .collect(Collectors.toList());
        return getters.stream()
            .filter(method -> getters.stream()
                .noneMatch(overrider -> overrider != method && elementUtils.overrides(overrider, method, typeElement)))
            .collect(Collectors.toMap(this::getAccessorMethodPropertyName, Function.identity()));
    }

    private List<ExecutableElement> getAllMethods(TypeElement classElement) {
        return Stream.concat(Stream.of(classElement.asType()), collectTypeSupertypes(classElement).stream())
            .map(type -> (DeclaredType) type)
            .filter(type -> !((TypeElement) type.asElement()).getQualifiedName().contentEquals(CLASS_NAME_OBJECT))
            .flatMap(type -> collectTypeDeclaredMethods(type).stream())
            .collect(Collectors.toList());
    }

    private List<TypeMirror> collectTypeSupertypes(TypeElement classTypeElement) {
        List<TypeMirror> superTypes = new ArrayList<>();
        new Object() {

            void collectSupertypes(DeclaredType declaredType) {
                typeUtils.directSupertypes(declaredType).stream()
                    .filter(type -> type.getKind() == TypeKind.DECLARED)
                    .forEach(type -> {
                        superTypes.add(type);
                        collectSupertypes((DeclaredType) type);
                    });
            }
        }.collectSupertypes((DeclaredType) classTypeElement.asType());

        return superTypes.stream()
            .filter(type -> superTypes.stream().noneMatch(
                anotherType -> type != anotherType && typeUtils.isSameType(type, anotherType)))
            .collect(Collectors.toList());
    }

    private List<ExecutableElement> collectTypeDeclaredMethods(DeclaredType declaredType) {
        return declaredType.asElement().getEnclosedElements().stream()
            .filter(element -> ElementKind.METHOD.equals(element.getKind()))
            .map(element -> (ExecutableElement) element)
            .collect(Collectors.toList());
    }

    private AccessModifier getPropertyAccessModifier(ExecutableElement accessorMethod) {
        return accessorMethod.getModifiers().stream()
            .filter(MODIFIERS_MAPPING::containsKey)
            .findFirst()
            .map(MODIFIERS_MAPPING::get)
            .orElse(AccessModifier.DEFAULT);
    }

    private String getAccessorMethodPropertyName(ExecutableElement method) {
        Matcher matcher = PATTERN_PROPERTY_NAME_FROM_ACCESSOR_METHOD.matcher(method.getSimpleName().toString());
        if (matcher.matches()) {
            String propertyName = matcher.group(CAPTURE_GROUP_NAME_PROPERTY_NAME);
            return StringUtils.decapitalizeUpperCamelcaseName(propertyName);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    private boolean isGetterMethod(ExecutableType executableType, ExecutableElement executableElement) {
        return GETTER_METHOD_NAME.matcher(executableElement.getSimpleName().toString()).matches()
            && !isVoidMethod(executableType) && executableElement.getParameters().size() == 0
            && isInstanceMethod(executableElement);
    }

    private ExecutableType asMemberOf(DeclaredType declaredType, ExecutableElement element) {
        return (ExecutableType) typeUtils.asMemberOf(declaredType, element);
    }

    private boolean isVoidMethod(ExecutableType executableType) {
        return executableType.getReturnType().getKind().equals(TypeKind.VOID);
    }

    private boolean isInstanceMethod(ExecutableElement element) {
        return !element.getModifiers().contains(Modifier.STATIC);
    }
}
