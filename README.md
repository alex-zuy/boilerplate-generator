[![Build Status](https://travis-ci.org/alex-zuy/boilerplate-generator.svg?branch=master)](https://travis-ci.org/alex-zuy/boilerplate-generator)

# Boilerplate generator

The project deals with generation of boilerplate code and more specifically with
string constants (e.g. names of bean properties).

## Rationale

### Bean Properties Names

The ubiquitous in Java World [Java Beans](http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html)
pattern often forces you to refer to properties of beans using their names.
Some cases are:
* validation of beans (especially in web applications). You may need to validate bean
property and put error message in some sort of map with bean properties used as key.
* advanced UI framework. For example framework may provide table widget which will show
list of beans using list of property names you provide to it.

You can always use string literal to specify property name when needed. Say you
have a bean with following properties:
```java
public class User {
    
    private String login;
    
    private String name;
    
    // getters-setters ...
}
```
When you are validating this bean you can do:
```java
public class UserValidator {
    
    public Map<String, String> validate(User user) {
        
        Map<String, String> errors = new HashMap<>();
        
        if(user.getLogin() == null || user.getLogin().isEmpty()) {
            errors.put("login", "Login must be present");              //hardcoded property name
        }
        
        return errors;
    }
}
```
Going this way you will hardcode this name each time you need it. If bean property name
changes you must carefully check all the code to ensure that you've updated hardcoded
name in each place.

You can try to solve this problem by using string constants:
```java
public class UserValidator {
    
    private static final String LOGIN_PROPERTY = "login"; //define constant to avoid hardcoding name
    
    public Map<String, String> validate(User user) {
        
        Map<String, String> errors = new HashMap<>();
        
        if(user.getLogin() == null || user.getLogin().isEmpty()) {
            errors.put(LOGIN_PROPERTY, "Login must be present");      //use manually created constant
        }
        
        return errors;
    }
}
```
Now you can update property name by editing  a single line of code. However this
constant is still some kind of code duplication, as you already defined property name
when defined getter/setter pair in bean class. Also you still can easily forget to
update it when property name changes.

At some point you may need to use property names constants in several classes and you
will be forced to define them in one separate place:
```java
public class UserProperties {
    public static final String LOGIN_PROPERTY = "login";
}
```
With this solution we still have following problems:
1. We need to update constants manually.
2. We need to manually write such 'constants' classes.

This project aims at solving those last two points!
