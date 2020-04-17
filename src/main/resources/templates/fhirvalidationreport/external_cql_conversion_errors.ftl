<div class="custom-card">
    <#if externalErrorsMap?has_content>
        <#list externalErrorsMap as key, value>
            <div class="card-header">CQL Conversion Errors for "${key}"</div>
            <#include "default_cql_conversion_errors.ftl" />
        </#list>
    <#else>
        <div class="card-header">Included Libraries CQL Conversion Errors</div>
        <#include "no_conversion_errors.ftl" />
    </#if>
</div>