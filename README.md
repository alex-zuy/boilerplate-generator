# Boilerplate generator
[![Build Status](https://travis-ci.org/alex-zuy/boilerplate-generator.svg?branch=master)](https://travis-ci.org/alex-zuy/boilerplate-generator)

The project deals with generation of property name constants for Java Beans classes. Implemented
as annotation processor it does not requires complex sutup, build tools integrations, IDE plugins, etc.
Just add as dependency and you are good to go.

<!-- Link references -->
[JavaBeansSpec]: http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html
[JavaAnnotationProcessing]: https://www.jcp.org/en/jsr/detail?id=269
[BoilerplateGeneratorMavenCentralSearch]: http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.alex-zuy.boilerplate%22%20AND%20a%3A%22processor%22
[BoilerplateGeneratorJavaDoc]: https://alex-zuy.github.io/boilerplate-generator/
[BreadCrumbsProject]: https://github.com/alexradzin/BeanCrumbs


## Index
1. [Features](#features)
2. [Rationale](#rationale)
3. [Setup](#setup)
4. [Usage](#usage-in-client-code)
5. [Java Doc][BoilerplateGeneratorJavaDoc]
6. [Similar projects](#similar)

## Features <a id="features"></a>

With Boilerplate generator you can:
1. Can
    * Use bean property names in your code in safe way (using constants declared in generated code).
    * Construct nested properties paths (see [usage](#usage-in-client-code) section)
2. Don't need to
    * Setup build tool plugins (javac`s builtin compiler API is used)

## Rationale <a id="rationale"></a>

### Bean Property Names <a id=""></a>

The ubiquitous in Java World [Java Beans][JavaBeansSpec]
pattern often forces you to refer to properties of beans using their names.
Some cases are:
* validation of beans (especially in web applications). You may need to validate bean
property and put error message in some sort of map with bean properties used as key.
* advanced UI frameworks. For example framework may provide table widget which will show
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
2. We need to manually write such 'constants' classes - boilerplate code.

This project aims at solving those last two points!

## Setup <a id="setup"></a>
Boilerplate generator is implemented as Annotation Processor ([JSR 269: Pluggable Annotation Processing API][JavaAnnotationProcessing]).
To use it in your project you need to:
1. [Add it as dependency](#usage-add-dependency) in you project.
2. [Configure](#usage-configure) it by defining annotations in your code.

### Adding a dependency <a id="usage-add-dependency"></a>

Boilerplate generator is [available][BoilerplateGeneratorMavenCentralSearch]
from Maven Central, so you can get it in your project just by adding
dependency to you pom.xml:
```xml
<dependency>
    <groupId>com.github.alex-zuy.boilerplate</groupId>
    <artifactId>processor</artifactId>
    <!-- Update version to the latest available -->
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```
### Configuring generator <a id="setup-configure"></a>
Boilerplate generator strives to not pollute your code with its annotations.
For this reason it does not provides annotations to do that, so you will need to
define your own annotations to mark classes you want to process (or you can use annotations
which already present in your project). There are no requirements to this annotations,
so it can be as simple as:
```java
package  com.example.app;

public @interface IncludeMarker {}
```
Using this annotation you can configure boilerplate generator to generate metadata classes
for all classes marked with `@IncludeMarker` annotation. Here is example configuration (in package-info.java):
```java
@BeanMetadataConfiguration(
    supportClassesConfiguration = @SupportClassesConfiguration(
        basePackage = "com.example.app.generated.support"),
    domainConfiguration = @DomainConfiguration(
            includes = @DomainConfiguration.Includes(
                typeAnnotations = {"com.example.app.IncludeMarker"}))
)
package com.example.app;
```
Given bean class Person:
```java
package com.example.app;

@IncludeMarker
public class Person {
    
    private String name;
    
    // getters-setters ...
}
```
Boilerplate generator will generate class `Person_p` which you can use to refer to `Person`\`s class properties.
For more information about configuration and available options please refer to [JavaDoc][BoilerplateGeneratorJavaDoc].

## Using generated classes <a id="usage-in-client-code"></a>

Once you added boilerplate generator to your project you can use it. Let's assume that
you have following bean classes (getters-setters omitted for brevity):

Address.java
```java
public class Address {
    
    String street;
    
    int houseNumber;
    
    int apartmentNumber;
}
```

Apartment.java
```java
public class Apartment {
    
    Address address;
    
    float square;
}
```

Citizen.java
```java
public class Citizen {
    
    String firstName;
    
    String lastName;
    
    Apartment apartment;
}
```
To access property names of `Citizen` you can just use constants:
```java
assert "firstName".equals(Citizen_p.FIRST_NAME)
```
To access nested property of `Citizen` bean you need to navigate
through property chain starting from `Citizen_p` class\`s 'relationships' methods
and continue navigation using relationships classes methods. For example,
we can construct path to citizen`s apartment square:
```java
assert "apartment.square".equals(Citizen_p.apartment().squareProperty())
```

Or we can navigate to apartment`s address properties:
```java
assert "apartment.address.street".equals(Citizen_p.apartment().address().streetProperty())
```

You can reuse part of property path to avoid duplication:
```java
Address_r addressChain = Citizen_p.apartment().address();
assert "apartment.address.street".equals(addressChain.streetProperty())
assert "apartment.address.houseNumber".equals(addressChain.houseNumberProperty())
```

## Similar projects <a id="similar"></a>

This project is similar to [Bread Crumbs][BreadCrumbsProject] project in its goals
and implementation details (both are annotation processors).

This project is different to Bread Crumbs in:
1. More flexible configuration (especially in configuring generated code style).
2. Support for nested properties.
