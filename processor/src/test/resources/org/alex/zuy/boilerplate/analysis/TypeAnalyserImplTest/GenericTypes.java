package com.example;

import java.util.List;
import java.util.Map;

@Trigger
public interface GenericTypes<R> {

    List<Integer> _oneTypeArg();

    Map<String, Integer> _twoTypeArgs();

    <T> List<T> _parameterizedMethod();

    R _parameterizedClassMethod();
}
