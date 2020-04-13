<html>
    <head>
        <title>MAT | FHIR Conversion Report</title>
        <#include "fhir_validation_report_css.ftl" />
    </head>
    <body>
        <div class="report-container">
            <h1 class="color-sky-blue">FHIR Conversion Report</h1>
            <div class="report-header">
                Measure Authoring Tool ${matVersion}
            </div>
            <#if noMeasureFoundError??>
                <div class="error-msg">${noMeasureFoundError}</div>
            <#elseif conversionServiceError??>
                <div class="error-msg">${conversionServiceError}</div>
            <#else>
                <div class="report-body">
                    <#include "validation_report_header.ftl" />
<#--                    <#include "fields_to_reconsile.ftl" />-->
                    <#include "valueset_fhir_validation_errors.ftl" />
                    <#include "library_fhir_validation_errors.ftl" />
                    <#include "measure_fhir_validation_errors.ftl" />
                    <#include "qdm_cql_conversion_errors.ftl" />
                    <#include "fhir_cql_conversion_errors.ftl" />
                    <#include "external_cql_conversion_errors.ftl" />
                </div>
            </#if>
        </div>
    </body>
</html>
