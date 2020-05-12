<#ftl output_format="HTML" strip_whitespace=true>
<h3><a name="d1e647" href="#toc">Data Criteria (QDM Data Elements)</a></h3>
<div>
    <ul style="padding-left: 50px;">
        <#list model.valuesetAndCodeDataCriteriaList as terminology>
            <li style="width:80%">${terminology.dataCriteriaDisplay}</li>
        </#list>
    </ul>
</div>