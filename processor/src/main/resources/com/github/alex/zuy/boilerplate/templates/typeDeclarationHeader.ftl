<#-- @ftlvariable name="type" type="com.github.alex.zuy.boilerplate.sourcemodel.TypeDescription" -->

<#if type.packageName??>
    package ${type.packageName};
</#if>

<#list typesToImport as impotedType>
    import ${impotedType.asString()};
</#list>
