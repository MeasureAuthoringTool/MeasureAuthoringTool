<html>
    <head>
        <title>MAT | FHIR Conversion Report</title>
        <#include "fhir_validation_report_css.ftl" />
    </head>
    <body>
        <div class="report-container">
            <h1 class="color-sky-blue">FHIR Library Conversion Report</h1>
            <div class="report-header">
                Measure Authoring Tool ${matVersion}
            </div>
            <#if cqlLibraryNotFoundError??>
                <div class="error-msg">${cqlLibraryNotFoundError}</div>
            <#elseif conversionServiceError??>
                <div class="error-msg">${conversionServiceError}</div>
            <#else>
                <div class="report-body">
                    <#include "fhir_library_validation_report_header.ftl" />
                    <#include "library_fhir_validation_errors.ftl" />
                </div>
            </#if>
        </div>
    </body>
</html>
