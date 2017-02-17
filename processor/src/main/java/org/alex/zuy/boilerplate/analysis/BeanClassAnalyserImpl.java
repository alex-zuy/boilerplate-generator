package org.alex.zuy.boilerplate.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.alex.zuy.boilerplate.domain.BeanClass;
import org.alex.zuy.boilerplate.domain.BeanProperty;
import org.alex.zuy.boilerplate.domain.BeanProperty.AccessModifier;
import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.utils.CollectionUtils;
import org.alex.zuy.boilerplate.utils.StringUtils;

public class BeanClassAnalyserImpl implements BeanClassAnalyser {

    private static final Pattern GETTER_METHOD_NAME = Pattern.compile("(is|get)[A-Z]\\w*");

    private static final Pattern SETTER_METHOD_NAME = Pattern.compile("set[A-Z]\\w*");

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

    @Override
    public BeanClass analyse(TypeElement classElement) {
        try {
            return analyseImpl(classElement);
        }
        catch (BeanClassAnalysisException bcae) {
            throw bcae;
        }
        catch (Exception e) {
            throw new BeanClassAnalysisException("Unknown error", e, classElement.getQualifiedName().toString());
        }
    }

    /* will be fixed later */
    @SuppressWarnings("PMD.AvoidPrintStackTrace")
    private BeanClass analyseImpl(TypeElement classElement) {
        List<BeanProperty> properties = new ArrayList<>();

        getAllPropertyAccessors(classElement).forEach((propertyName, accessors) -> {
            try {
                List<ExecutableElement> gettingMethods = accessors.stream().filter(this::isGetterMethod).collect(
                    Collectors.toList());
                List<ExecutableElement> settingMethods = accessors.stream().filter(this::isSetterMethod).collect(
                    Collectors.toList());

                List<ExecutableElement> significantGetterMethods = filterOutOverriddenAndImplementedMethods(
                    gettingMethods, classElement);

                if (significantGetterMethods.size() > 1) {
                    throw new BeanClassAnalysisException("Class can`t have multiple getters for the same property",
                        classElement.getQualifiedName().toString());
                }

                if (!significantGetterMethods.isEmpty() && !settingMethods.isEmpty()) {
                    ExecutableElement gettingMethod = significantGetterMethods.get(0);
                    Type<?> propertyType = typeAnalyser.analyse(gettingMethod.getReturnType());
                    properties.add(
                        new BeanProperty(propertyName, propertyType, getPropertyAccessModifier(gettingMethod)));
                }
            }
            catch (TypeAnalyser.UnsupportedTypeException e) {
                e.printStackTrace();
            }
        });

        return new BeanClass(typeAnalyser.analyse(classElement.asType()), properties);
    }

    private List<ExecutableElement> filterOutOverriddenAndImplementedMethods(List<ExecutableElement> methods,
        TypeElement classElement) {
        return methods.stream()
            .filter(method -> methods.stream()
                .noneMatch(overrider -> overrider != method
                    && elementUtils.overrides(overrider, method, classElement)))
            .collect(Collectors.toList());
    }

    private Map<String, List<ExecutableElement>> getAllPropertyAccessors(TypeElement classElement) {
        return Stream.concat(Stream.of(classElement.asType()), collectTypeSupertypes(classElement).stream())
            .map(type -> (DeclaredType) type)
            .filter(type -> !((TypeElement) type.asElement()).getQualifiedName().contentEquals(CLASS_NAME_OBJECT))
            .map(type -> collectTypeDeclaredPropertyAccessors((TypeElement) type.asElement()))
            .reduce(new HashMap<>(), (result, accessorsInSuperType) -> {
                accessorsInSuperType.forEach((propertyName, accessors) -> {
                    result.computeIfAbsent(propertyName, (key) -> new ArrayList<>()).addAll(accessors);
                });
                return result;
            });
    }

    private List<TypeMirror> collectTypeSupertypes(TypeElement typeElement) {
        List<TypeMirror> superTypes = new ArrayList<>();
        new Object() {

            void collectSupertypes(Element element) {
                TypeElement typeElement = (TypeElement) element;
                Stream.concat(Stream.of(typeElement.getSuperclass()), typeElement.getInterfaces().stream())
                    .filter(type -> type.getKind() == TypeKind.DECLARED)
                    .forEach(type -> {
                        superTypes.add(type);
                        collectSupertypes(((DeclaredType) type).asElement());
                    });
            }
        }.collectSupertypes(typeElement);

        return superTypes.stream()
            .filter(type -> superTypes.stream().noneMatch(
                anotherType -> type != anotherType && typeUtils.isSameType(type, anotherType)))
            .collect(Collectors.toList());
    }

    private Map<String, List<ExecutableElement>> collectTypeDeclaredPropertyAccessors(TypeElement typeElement) {
        return typeElement.getEnclosedElements().stream()
            .filter(element -> ElementKind.METHOD.equals(element.getKind()))
            .map(element -> (ExecutableElement) element)
            .filter(executableElement -> isGetterMethod(executableElement) || isSetterMethod(executableElement))
            .collect(Collectors.groupingBy(this::getAccessorMethodPropertyName));
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

    private boolean isSetterMethod(ExecutableElement executableElement) {
        return SETTER_METHOD_NAME.matcher(executableElement.getSimpleName().toString()).matches()
            && isVoidMethod(executableElement) && executableElement.getParameters().size() == 1
            && isInstanceMethod(executableElement);
    }

    private boolean isGetterMethod(ExecutableElement executableElement) {
        return GETTER_METHOD_NAME.matcher(executableElement.getSimpleName().toString()).matches()
            && !isVoidMethod(executableElement) && executableElement.getParameters().size() == 0
            && isInstanceMethod(executableElement);
    }

    private boolean isVoidMethod(ExecutableElement element) {
        return element.getReturnType().getKind().equals(TypeKind.VOID);
    }

    private boolean isInstanceMethod(ExecutableElement element) {
        return !element.getModifiers().contains(Modifier.STATIC);
    }
}
