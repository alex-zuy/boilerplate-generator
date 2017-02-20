package org.alex.zuy.boilerplate.beans;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BeansTest {

    @Test
    public void testUserProperties() throws Exception {
        eq(UserProperties.FULL_NAME, "fullName");
        eq(UserProperties.ID, "id");
    }

    @Test
    public void testOwnedEntityProperties() throws Exception {
        eq(UserOwnedEntityProperties.OWNER, "owner");
        eq(UserOwnedEntityProperties.OWNER_ID, "ownerId");
        eq(UserOwnedEntityProperties.owner().fullNameProperty(), "owner.fullName");
        eq(UserOwnedEntityProperties.owner().idProperty(), "owner.id");
    }

    @Test
    public void testShortCodeProperties() throws Exception {
        eq(ShortCodeProperties.NUMBER, "number");
    }

    @Test
    public void testProductProperties() throws Exception {
        eq(ProductProperties.ID, "id");
        eq(ProductProperties.id().numberProperty(), "id.number");
    }

    private static void eq(Object expected, Object actual) {
        assertEquals(expected, actual);
    }
}
