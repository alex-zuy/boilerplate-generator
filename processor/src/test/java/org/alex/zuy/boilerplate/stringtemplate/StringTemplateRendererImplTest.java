package org.alex.zuy.boilerplate.stringtemplate;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class StringTemplateRendererImplTest {

    private StringTemplateRenderer renderer = new StringTemplateRendererImpl();

    @Test
    public void testTemplateWithOnePlaceholder() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Alex");
        String actual = renderer.render("Hello ${name}!", data);
        assertEquals("Hello Alex!", actual);
    }

    @Test
    public void testTemplateWithTwoPlaceholdersWithSameName() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Alex");
        String actual = renderer.render("${name} is not ${name}", data);
        assertEquals("Alex is not Alex", actual);
    }

    @Test
    public void testTemplateWithTwoPlaceholders() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Alex");
        data.put("language", "English");
        String actual = renderer.render("${name} speaks ${language}", data);
        assertEquals("Alex speaks English", actual);
    }

    @Test
    public void testUnspecifiedPlaceholdersLeftAsIs() throws Exception {
        String template = "I am ${variable}";
        String actual = renderer.render(template, new HashMap<>());
        assertEquals(template, actual);
    }
}
