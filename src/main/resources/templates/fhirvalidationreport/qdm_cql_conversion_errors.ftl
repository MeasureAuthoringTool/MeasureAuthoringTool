<div class="custom-card">
    <div class="card-header">QDM CQL Conversion Errors</div>
    <#if qdmCqlConversionErrors?has_content || qdmMatCqlConversionErrors?has_content >
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
                    <#if qdmCqlConversionErrors?has_content>
                        <#list qdmCqlConversionErrors as cqlError>
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
                    </#if>
                    <#if qdmMatCqlConversionErrors?has_content>
                        <#list qdmMatCqlConversionErrors as cqlError>
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
                    </#if>
                    </tbody>
            </table>
        </div>
    <#else>
        <#include "no_conversion_errors.ftl" />
    </#if>
</div>