package com.example;

@Trigger
class NestedTypesInNamedPackage {

    InnerClass innerClass() {
        return null;
    }

    InnerClassHolder.InnerClassHolderInnerClass innerClassHolderInnerClass() {
        return null;
    }

    public class InnerClass {

    }

    public class InnerClassHolder {

        public class InnerClassHolderInnerClass {

        }
    }
}
