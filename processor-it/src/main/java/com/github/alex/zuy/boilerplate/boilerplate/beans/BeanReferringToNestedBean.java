package com.github.alex.zuy.boilerplate.boilerplate.beans;

import com.github.alex.zuy.boilerplate.boilerplate.IncludeMarker;

@IncludeMarker
public class BeanReferringToNestedBean {

    private ClassWithNestedBean.NestedBean nestedBean;

    public ClassWithNestedBean.NestedBean getNestedBean() {
        return nestedBean;
    }

    public void setNestedBean(ClassWithNestedBean.NestedBean nestedBean) {
        this.nestedBean = nestedBean;
    }
}
