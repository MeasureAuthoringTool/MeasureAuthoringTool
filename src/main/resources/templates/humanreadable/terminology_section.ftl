<#ftl output_format="HTML" strip_whitespace=true>
<h3><a name="d1e555" href="#toc">Terminology</a></h3>
<div>
    <#if model.codeTerminologyList?size != 0 ||  model.valuesetTerminologyList?size != 0>
        <ul style="padding-left: 50px;">
            <#list model.codeTerminologyList as code>
                <li style="width:80%">${code.terminologyDisplay}</li>
            </#list>
            <#list model.valuesetTerminologyList as valueset>
                <li style="width:80%">${valueset.terminologyDisplay}</li>
            </#list>
        </ul>
    </#if>
</div>