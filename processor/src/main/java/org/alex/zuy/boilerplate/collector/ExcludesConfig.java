package org.alex.zuy.boilerplate.collector;

import java.util.List;
import java.util.regex.Pattern;

import org.immutables.value.Value;

@Value.Immutable
public interface ExcludesConfig {

    List<String> typeAnnotations();

    List<Pattern> patterns();
}
