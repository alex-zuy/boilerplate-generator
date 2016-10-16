package org.alex.zuy.boilerplate.collector.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.lang.model.element.TypeElement;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class TypeElementsSetMatcher extends TypeSafeMatcher<Collection<TypeElement>> {

    private final SortedSet<String> expectedTypeElements;

    private final StringBuilder mismatchDescription = new StringBuilder();

    private TypeElementsSetMatcher(String... expectedTypeElements) {
        this.expectedTypeElements = new TreeSet<>();
        this.expectedTypeElements.addAll(Arrays.asList(expectedTypeElements));
    }

    @Override
    protected boolean matchesSafely(Collection<TypeElement> typeElements) {
        return isAllTypeElementsUnique(typeElements) && isAllTypeElementsMatch(typeElements);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(mismatchDescription.toString());
    }

    private boolean isAllTypeElementsMatch(Collection<TypeElement> typeElements) {
        SortedSet<String> actualQualifiersSet = toQualifiersSet(typeElements);
        if (actualQualifiersSet.equals(expectedTypeElements)) {
            return true;
        } else {
            Set<String> intersection = new HashSet<>(actualQualifiersSet);
            intersection.retainAll(expectedTypeElements);

            actualQualifiersSet.stream()
                .filter(qualifier -> !intersection.contains(qualifier))
                .forEach(qualifier -> mismatchDescription.append(
                    String.format("TypeElement \"%s\" was not expected; ", qualifier)));

            expectedTypeElements.stream()
                .filter(qualifier -> !intersection.contains(qualifier))
                .forEach(qualifier -> mismatchDescription.append(
                    String.format("TypeElement \"%s\" was not present; ", qualifier)));

            return false;
        }
    }

    private boolean isAllTypeElementsUnique(Collection<TypeElement> typeElements) {
        SortedSet<String> uniqueQualifiers = toQualifiersSet(typeElements);
        if (uniqueQualifiers.size() == typeElements.size()) {
            return true;
        } else {
            uniqueQualifiers.forEach(qualifier -> {
                long entriesCount = typeElements.stream()
                    .filter(typeElement -> typeElement.getQualifiedName().contentEquals(qualifier))
                    .count();
                if (entriesCount > 1) {
                    mismatchDescription.append(
                        String.format("TypeElement \"%s\" appears %d times", qualifier, entriesCount));
                }
            });

            return false;
        }
    }

    public static TypeElementsSetMatcher isSetOfTypeElements(String... typeElements) {
        return new TypeElementsSetMatcher(typeElements);
    }

    private static SortedSet<String> toQualifiersSet(Collection<TypeElement> typeElements) {
        return typeElements.stream()
            .map(typeElement -> typeElement.getQualifiedName().toString())
            .collect(Collectors.toCollection(TreeSet::new));
    }
}
