<#ftl output_format="HTML" strip_whitespace=true>
<h3><a name="d1e649" href="#toc">Definitions</a></h3>
<div>
    <ul style="list-style:none;padding-left: 10px;">
        <li class="list-unstyled" style="list-style:none;">
            <div>
                <#if model.definitions?has_content>
                    <#list model.definitions as expression>
                    <#include "cql_expression.ftl" />
                    </#list>
                <#else>
                    None
                </#if>
            </div>
        </li>
    </ul>
</div>