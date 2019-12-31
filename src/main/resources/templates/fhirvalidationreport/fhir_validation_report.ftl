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
                    <#if measureFhirValidationErrors?has_content
                        || valueSetFhirValidationErrors?has_content
                        || libraryFhirValidationErrors?has_content>
                        <div class="custom-card">
                            <div class="card-header">FHIR Resources to Reconcile</div>
                            <#if measureFhirValidationErrors?has_content>
                                <#include "measure_fhir_validation_errors.ftl" />
                            </#if>
                            <#if valueSetFhirValidationErrors?has_content>
                                <#include "valueset_fhir_validation_errors.ftl" />
                            </#if>
                            <#if libraryFhirValidationErrors?has_content>
                                <#include "library_fhir_validation_errors.ftl" />
                            </#if>
                        </div>
                    </#if>
                    <#if cqlConversionErrors?has_content>
                        <#include "cql_conversion_errors.ftl" />
                    </#if>
                </div>
            </#if>
        </div>
    </body>
</html>
