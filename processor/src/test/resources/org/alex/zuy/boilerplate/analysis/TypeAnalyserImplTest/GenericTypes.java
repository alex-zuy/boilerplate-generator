package com.example;

import java.util.List;
import java.util.Map;

@Trigger
public interface GenericTypes {

    List<Integer> _oneTypeArg();

    Map<String, Integer> _twoTypeArgs();
}
