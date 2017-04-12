<#-- @ftlvariable name="type" type="org.alex.zuy.boilerplate.sourcemodel.TypeDescription" -->
<#import "utility.ftl" as util>

<@util.printModifiers modifiers=type.modifiers/>
${type.kind}
${type.simpleName}
<#if type.extendedType??>
extends ${util.formatType(type.extendedType)}
</#if>
<#list type.implementedTypes>
    implements
        <#items as implementedType>
        ${util.formatType(implementedType)}<#sep>, </#sep>
        </#items>
</#list>

{

<#list type.fields as field>

    <@util.printModifiers modifiers = field.modifiers/>
    ${util.formatType(field.type)}
    ${field.name}
    <#if field.hasInitialValue??>
        = <@util.renderTemplate templatePath=field.templateName data=field.data/>
    </#if>
    ;

</#list>

<#list type.methods as method>

    <@util.printModifiers modifiers=method.modifiers/>
    <#if method.returnType??>
    ${util.formatType(method.returnType)}
    </#if>
    ${method.name}
    (
        <#list method.parameters as parameter>
            ${util.formatType(parameter.type)}
            ${parameter.name}
            <#sep>, </#sep>
        </#list>
    )
    {
        <@util.renderTemplate templatePath=method.templateName data=method.data/>
    }

</#list>

}
