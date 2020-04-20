<div class="custom-card">
    <#if fhirCqlConversionErrors?has_content>
        <#list fhirCqlConversionErrors as key, value>
            <div class="card-header">FHIR CQL Conversion Errors for "${key}"</div>
            <#include "default_cql_conversion_errors.ftl" />
        </#list>
    <#else>
        <div class="card-header">FHIR CQL Conversion Errors</div>
        <#include "no_conversion_errors.ftl" />
    </#if>
</div>