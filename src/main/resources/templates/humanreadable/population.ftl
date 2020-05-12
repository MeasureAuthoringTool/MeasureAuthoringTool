<#ftl output_format="HTML" strip_whitespace=true>
<li class="list-unstyled" style="list-style:none;padding-left:0px;">
    <div class="treeview hover p-l-10">
        <ul class="list-unstyled">
            <li class="list-unstyled"><input type="checkbox" id="${population.id}">
                <label for="${population.id}" class="list-header"><strong>${population.display}</strong></label>
                <ul class="code">
                    <#if population.inGroup == true>
                    <li class="list-unstyled">
                        <div class="treeview hover p-l-10">
                            <ul>
                                <li style="padding-left: 0px;">
                                    <div>
                                        <pre class="cql-definition-body">${population.logic}</pre>
                                    </div>
                                </li>
                            </ul>&nbsp;
                        </div>
                    </li>
                    <#else>
                    <li class="list-unstyled">
                        <div class="treeview hover p-l-10">
                            <ul>
                                <li style="padding-left: 0px;">
                                    <div>
                                        <pre class="cql-definition-body cql-class">None</pre>
                                    </div>
                                </li>
                            </ul>&nbsp;
                        </div>
                    </li>
                    </#if>
                </ul>
            </li>
        </ul>
    </div>
</li>