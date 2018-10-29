<h3><a name="d1e555" href="#toc">Terminology</a></h3>
	<div>
		<ul style="padding-left: 50px;">
		<#list model.valuesetDataCriteriaList as valueset>
			<li style="width:80%">${valueset.dataCriteriaDisplay}</li>
		</#list>
		
		<#list model.codeDataCriteriaList as code>
			<li style="width:80%">${code.dataCriteriaDisplay}</li>
		</#list>
		</ul>
 </div>