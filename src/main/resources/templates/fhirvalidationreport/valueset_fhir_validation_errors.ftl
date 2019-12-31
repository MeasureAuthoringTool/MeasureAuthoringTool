<div class="font-smaller">ValueSet Validation Errors</div>
<div class="card-body">
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th scope="col">Severity</th>
            <th scope="col">Location Field</th>
            <th scope="col">Error Description</th>
        </tr>
        </thead>
        <tbody>
        <#list valueSetFhirValidationErrors as valueSetValidationError>
            <tr>
                <td>${valueSetValidationError.severity}</td>
                <td>${valueSetValidationError.locationField}</td>
                <td>${valueSetValidationError.errorDescription}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<br/>
