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
        <#list value as cqlError>
            <tr>
                <td>${cqlError?counter}</td>
                <td>${cqlError.startLine!""}</td>
                <td>${cqlError.endLine!""}</td>
                <td>${cqlError.errorSeverity?lower_case?capitalize!""}</td>
                <td>${cqlError.message!""}</td>
                <td>${cqlError.startChar!""}</td>
                <td>${cqlError.endChar!""}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>