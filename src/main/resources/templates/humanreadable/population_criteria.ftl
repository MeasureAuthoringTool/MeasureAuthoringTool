<li class="list-unstyled" style="list-style:none;padding-left:0;">
    <div class="treeview hover p-l-10">
        <input type="checkbox" id="${populationCriteria.id}" />
        <label for="${populationCriteria.id}" class="list-header"><b>${populationCriteria.name}</b></label>
        <#if populationCriteria.populations?size != 0>
            <ul>
                <!-- Score Unit -->
                <#if populationCriteria.scoreUnit?length != 0>
                    <li class="list-unstyled" style="list-style:none;padding-left:0px;">
                        <div class="treeview hover p-l-10">
                            <ul class="list-unstyled">
                                <li class="list-unstyled"><input type="checkbox" id="${populationCriteria.id}-scoreUnit"/>
                                    <label for="${populationCriteria.id}-scoreUnit" class="list-header"><strong>Score Unit</strong></label>
                                    <ul class="code">
                                        <li class="list-unstyled">
                                            <div class="treeview hover p-l-10">
                                                <ul>
                                                    <li style="padding-left: 0px;">
                                                        <div>
                                                            <pre class="cql-definition-body">${populationCriteria.scoreUnit}</pre>
                                                        </div>
                                                    </li>
                                                </ul>&nbsp;
                                            </div>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </li>
                </#if>
                <#list populationCriteria.populations as population>
                    <#include "population.ftl">
                </#list>
            </ul>
        </#if>
    </div>
</li>