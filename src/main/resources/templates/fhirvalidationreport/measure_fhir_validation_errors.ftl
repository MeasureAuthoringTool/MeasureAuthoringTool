<div class="custom-card">
    <div class="card-header">Measure</div>
    <#if measureFhirValidationErrors?has_content>
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
                    <#list measureFhirValidationErrors as measureValidationError>
                        <tr>
                            <td>${measureValidationError?counter}</td>
                            <td>${measureValidationError.locationField!""}</td>
                            <td>${measureValidationError.severity?lower_case?capitalize!""}</td>
                            <td>${measureValidationError.errorDescription!""}</td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    <#else>
        <#include "no_conversion_errors.ftl" />
    </#if>
</div>
