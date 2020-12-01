<h3><a name="d1e650" href="#toc">Functions</a></h3>
<div>
    <ul style="list-style:none;padding-left: 10px;">
        <li class="list-unstyled" style="list-style:none;">
            <div>
                <#if model.functions?has_content>
                    <#list model.functions as expression>
                        <#include "cql_expression.ftl" />
                    </#list>
                <#else>
                    None
                </#if>
            </div>
        </li>
    </ul>
</div>