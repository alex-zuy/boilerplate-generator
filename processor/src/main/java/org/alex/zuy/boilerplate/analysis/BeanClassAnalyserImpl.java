package org.alex.zuy.boilerplate.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

import org.alex.zuy.boilerplate.domain.BeanClass;
import org.alex.zuy.boilerplate.domain.BeanProperty;
import org.alex.zuy.boilerplate.domain.BeanProperty.AccessModifier;
import org.alex.zuy.boilerplate.domain.Types;
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

    private TypeAnalyserImpl typeAnalyser;

    public BeanClassAnalyserImpl(TypeAnalyserImpl typeAnalyser) {
        this.typeAnalyser = typeAnalyser;
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

                if (gettingMethods.size() > 1) {
                    throw new BeanClassAnalysisException("Class can`t have multiple getters for the same property",
                        classElement.getQualifiedName().toString());
                }

                if (!gettingMethods.isEmpty() && !settingMethods.isEmpty()) {
                    ExecutableElement gettingMethod = gettingMethods.get(0);
                    Types.Type<?> propertyType = typeAnalyser.analyse(gettingMethod.getReturnType());
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

    private Map<String, List<ExecutableElement>> getAllPropertyAccessors(TypeElement classElement) {
        return classElement.getEnclosedElements().stream()
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
