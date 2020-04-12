<div class="custom-card">
    <#if qdmCqlConversionErrors?has_content>
        <#list qdmCqlConversionErrors as key, value>
            <div class="card-header">QDM CQL Conversion Errors For "${key}"</div>
            <#include "default_cql_conversion_errors.ftl" />
        </#list>
    <#else>
        <div class="card-header">QDM CQL Conversion Errors</div>
        <#include "no_conversion_errors.ftl" />
    </#if>
</div>