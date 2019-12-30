<div class="font-smaller">Library Validation Errors</div>
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
        <#list libraryFhirValidationErrors as libraryValidationError>
            <tr>
                <td>${libraryValidationError.severity}</td>
                <td>${libraryValidationError.locationField}</td>
                <td>${libraryValidationError.errorDescription}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
