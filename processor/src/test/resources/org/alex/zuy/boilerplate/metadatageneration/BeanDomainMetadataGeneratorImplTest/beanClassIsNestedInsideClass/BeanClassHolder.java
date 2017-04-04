package com.example;

public class BeanClassHolder {

    @Marker
    public static class InnerClass {

        private PersonBean person;

        public PersonBean getPerson() {
            return person;
        }

        public void setPerson(PersonBean person) {
            this.person = person;
        }
    }
}
