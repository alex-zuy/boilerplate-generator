package org.alex.zuy.boilerplate.beans;

import org.alex.zuy.boilerplate.IncludeMarker;

public class ClassWithNestedBean {

    @IncludeMarker
    public static class NestedBean {

        private Product product;

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }
}
