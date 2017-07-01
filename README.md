# Boilerplate generator
[![Build Status](https://travis-ci.org/alex-zuy/boilerplate-generator.svg?branch=master)](https://travis-ci.org/alex-zuy/boilerplate-generator)

The project deals with generation of boilerplate code and more specifically with
string constants (e.g. names of bean properties).

<!-- List of link references -->
[JavaBeansSpec]: http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html
[JavaAnnotationProcessing]: https://www.jcp.org/en/jsr/detail?id=269
[BoilerplateGeneratorMavenCentralSearch]: http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.alex-zuy.boilerplate%22%20AND%20a%3A%22processor%22
[BoilerplateGeneratorJavaDoc]: https://alex-zuy.github.io/boilerplate-generator/


## Index
1. [Rationale](#rationale)
2. [Features](#features)
3. [Usage](#usage)
4. [Java Doc][BoilerplateGeneratorJavaDoc]

## Rationale <a id="rationale"></a>

### Bean Properties Names <a id=""></a>

The ubiquitous in Java World [Java Beans][JavaBeansSpec]
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
2. We need to manually write such 'constants' classes - boilerplate code.

This project aims at solving those last two points!

## Features <a id="features"></a>

Domain, nested properties

**TODO**

## Usage <a id="usage"></a>
Boilerplate generator is implemented as Annotation Processor ([JSR 269: Pluggable Annotation Processing API][JavaAnnotationProcessing]).
To use it in your project you need to:
1. [Add it as dependency](#usage-add-dependency) in you project.
2. [Configure](#usage-configure) it by defining annotations in your code.
 
### Adding a dependency <a id="usage-add-dependency"></a>

Boilerplate generator is [available][BoilerplateGeneratorMavenCentralSearch]
from Maven Central, so you can get it in your project just by adding
dependency to you pom.xml:
<!-- We can link artifact version using recource filtering -->
```xml
<dependency>
    <groupId>com.github.alex-zuy.boilerplate</groupId>
    <artifactId>processor</artifactId>
    <!-- Update version to the latest available -->
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```
### Configuring generator <a id="usage-configure"></a>
Boilerplate generator strives to not pollute your code with its annotations.
For this reason it does not provides annotations to do that, so you will need to
define your own annotations to mark classes you want to process (or use annotations
you already have in your project). There are no requirements to this annotations,
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
