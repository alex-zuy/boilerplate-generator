package com.github.alex.zuy.boilerplate.boilerplate.beans;

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

    @Test
    public void testNestedBeanProperties() throws Exception {
        eq(NestedBeanProperties.PRODUCT, "product");
        eq(NestedBeanProperties.product().id().numberProperty(), "product.id.number");
        eq(BeanReferringToNestedBeanProperties.NESTED_BEAN, "nestedBean");
        eq(BeanReferringToNestedBeanProperties.nestedBean().product().idProperty(), "nestedBean.product.id");
    }

    @Test
    public void testInterface() throws Exception {
        eq(EntityInterfaceProperties.PRODUCT, "product");
        eq(EntityInterfaceProperties.product().idProperty(), "product.id");
        eq(EntityInterfaceHolderProperties.ENTITY, "entity");
        eq(EntityInterfaceHolderProperties.entity().productProperty(), "entity.product");
    }


    private static void eq(Object expected, Object actual) {
        assertEquals(expected, actual);
    }
}
