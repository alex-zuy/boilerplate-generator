<#-- @ftlvariable name="data.propertiesClassType" type="com.github.alex.zuy.boilerplate.domain.types.Type" -->
<#-- @ftlvariable name="data.relationshipClassType" type="com.github.alex.zuy.boilerplate.domain.types.Type" -->
<#-- @ftlvariable name="data.propertyNameConstantName" type="java.lang.String" -->

<#import "../utility.ftl" as util/>

return new ${util.formatType(data.relationshipClassType)}(this, ${util.formatType(data.propertiesClassType)}.${data.propertyNameConstantName});
