<div class="custom-card">
    <div class="card-header">CQL Conversion Errors</div>
    <div class="card-body">
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th scope="col">Error</th>
                <th scope="col">Start line</th>
                <th scope="col">End line</th>
                <th scope="col">Error severity</th>
                <th scope="col">Message</th>
                <th scope="col">Start char</th>
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
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>
