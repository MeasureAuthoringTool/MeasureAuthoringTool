<#ftl output_format="HTML" strip_whitespace=true>
<#assign isFhir=isFhir/>
<#if isFhir>
    <h3><a name="d1e647" href="#toc">Data Criteria (FHIR Data Requirements)</a></h3>
<#else>
    <h3><a name="d1e647" href="#toc">Data Criteria (QDM Data Elements)</a></h3>
</#if>

<div>
    <#if model.valuesetAndCodeDataCriteriaList?size != 0>
        <ul style="padding-left: 50px;">
            <#list model.valuesetAndCodeDataCriteriaList as terminology>
                <li style="width:80%">${terminology.dataCriteriaDisplay}</li>
            </#list>
        </ul>
    </#if>
</div>