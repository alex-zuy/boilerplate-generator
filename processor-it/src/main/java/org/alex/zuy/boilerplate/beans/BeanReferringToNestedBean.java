package org.alex.zuy.boilerplate.beans;

import org.alex.zuy.boilerplate.IncludeMarker;

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
