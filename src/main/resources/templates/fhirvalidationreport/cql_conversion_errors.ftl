<div class="custom-card">
    <div class="card-header">CQL Conversion Errors</div>
    <#if cqlConversionErrors?has_content>
        <div class="card-body">
            <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th scope="col">Error</th>
                        <th scope="col">Start line</th>
                        <th scope="col">End line</th>
                        <th scope="col">Error Severity</th>
                        <th scope="col">Message</th>
                        <th scope="col">Start char</th>
                        <th scope="col">End char</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list cqlConversionErrors as cqlError>
                        <tr>
                            <td>${cqlError?counter}</td>
                            <td>${cqlError.startLine}</td>
                            <td>${cqlError.endLine}</td>
                            <td>${cqlError.errorSeverity}</td>
                            <td>${cqlError.message}</td>
                            <td>${cqlError.startChar}</td>
                            <td>${cqlError.endChar}</td>
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