<div class="custom-card">
    <div class="card-header">Value Set</div>
    <#if valueSetFhirValidationErrors?has_content>
        <div class="card-body">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th scope="col">Error</th>
                        <th scope="col">Location Field</th>
                        <th scope="col">Severity</th>
                        <th scope="col">Error Description</th>
                    </tr>
                </thead>
                <tbody>
                    <#list valueSetFhirValidationErrors as valueSetValidationError>
                        <tr>
                            <td>${valueSetValidationError?counter}</td>
                            <td>${valueSetValidationError.locationField}</td>
                            <td>${valueSetValidationError.severity}</td>
                            <td>${valueSetValidationError.errorDescription}</td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    <#else>
        <div class="card-body card-no-errors">
            <div class="container">No Errors Found</div>
        </div>
    </#if>
</div>