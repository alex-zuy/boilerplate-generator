package com.github.alex.zuy.boilerplate.boilerplate.beans;

import com.github.alex.zuy.boilerplate.boilerplate.IncludeMarker;

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
