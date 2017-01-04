package com.example;

@Trigger
public class MultipleSettersPerProperty {

    protected int getWidth() {
        return 0;
    }

    protected void setWidth(int w) { }

    protected void setWidth(String w) { }
}
