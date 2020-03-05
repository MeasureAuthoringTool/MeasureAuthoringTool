<div class="custom-card">
    <div class="card-header">Library</div>
    <#if libraryFhirValidationErrors?has_content>
        <div class="card-body">
            <table class="table table-striped table-hover">
                <tbody>
                    <thead>
                    <tr>
                        <th scope="col">Error</th>
                        <th scope="col">Location Field</th>
                        <th scope="col">Severity</th>
                        <th scope="col">Error Description</th>
                    </tr>
                    </thead>
                    <#list libraryFhirValidationErrors as libraryValidationError>
                        <tr>
                            <td>${libraryValidationError?counter}</td>
                            <td>${libraryValidationError.locationField}</td>
                            <td>${libraryValidationError.severity}</td>
                            <td>${libraryValidationError.errorDescription}</td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    <#else>
        <#include "no_conversion_errors.ftl" />
    </#if>
</div>
