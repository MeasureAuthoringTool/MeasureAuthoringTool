<#ftl output_format="HTML" strip_whitespace=true>
<h3><a name="d1e767" href="#toc">Supplemental Data Elements</a></h3>
<ul style="list-style:none;padding-left: 25px;">
	<#if model.supplementalDataElements?has_content>
		<#list model.supplementalDataElements as expression>
			<#include "expression.ftl" />
		</#list>
    <#else>
		<li>None</li>
	</#if>
</ul>