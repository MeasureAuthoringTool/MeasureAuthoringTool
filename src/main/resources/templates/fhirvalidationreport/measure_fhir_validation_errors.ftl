<div class="font-smaller">Measure Validation Errors</div>
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
        <#list measureFhirValidationErrors as measureValidationError>
            <tr>
                <td>${measureValidationError.severity}</td>
                <td>${measureValidationError.locationField}</td>
                <td>${measureValidationError.errorDescription}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<br/>
