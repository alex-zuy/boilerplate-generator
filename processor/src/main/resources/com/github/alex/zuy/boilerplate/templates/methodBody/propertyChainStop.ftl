<#-- @ftlvariable name="data.propertiesClassType" type="com.github.alex.zuy.boilerplate.domain.types.Type" -->
<#-- @ftlvariable name="data.propertyNameConstantName" type="java.lang.String" -->

<#import "../utility.ftl" as util/>

return buildRelativePath(${util.formatType(data.propertiesClassType)}.${data.propertyNameConstantName});
