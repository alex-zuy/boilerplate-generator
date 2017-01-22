<#-- @ftlvariable name="typeFormatter" type="org.alex.zuy.boilerplate.codegeneration.TypeFormatter" -->
<#macro printModifiers modifiers>
    <#list modifiers as modifier>
    ${modifier}
    </#list>
</#macro>

<#macro renderTemplate templatePath data>
    <#include templatePath />
</#macro>

<#function formatType type>
    <#return typeFormatter.format(type)/>
</#function>
