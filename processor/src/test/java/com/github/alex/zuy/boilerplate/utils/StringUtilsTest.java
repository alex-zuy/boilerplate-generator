package com.github.alex.zuy.boilerplate.utils;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void test_capitalizeLowerCamelcaseName() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("h", "H");
        data.put("H", "H");
        data.put("html", "Html");
        data.put("firstName", "FirstName");
        data.forEach((input, expected) -> assertEquals(expected, StringUtils.capitalizeLowerCamelcaseName(input)));
    }

    @Test
    public void test_decapitalizeUpperCamelcaseName() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("H", "h");
        data.put("HTML", "HTML");
        data.put("HTMLString", "HTMLString");
        data.put("HtmlString", "htmlString");
        data.put("GeneratedHTML", "generatedHTML");
        data.forEach((input, expected) -> assertEquals(expected, StringUtils.decapitalizeUpperCamelcaseName(input)));
    }

    @Test
    public void test_camelcaseToUpperSnakeCase() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("isGood", "IS_GOOD");
        data.put("is1970", "IS_1970");
        data.put("HTML", "HTML");
        data.put("HTMLElement", "HTML_ELEMENT");
        data.put("stringNumber1truncated", "STRING_NUMBER_1_TRUNCATED");
        data.forEach((input, expected) -> assertEquals(expected, StringUtils.camelcaseToUpperSnakeCase(input)));
    }
}
