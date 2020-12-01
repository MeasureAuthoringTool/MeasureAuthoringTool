<#ftl output_format="HTML" strip_whitespace=true>
<html>
    <head>
        <title>${population.name}</title>
        <#include "human_readable_css.ftl" />
    </head>
    <body>
        <li class="list-unstyled" style="list-style:none;padding-left:0px;">
            <div class="treeview hover p-l-10">
                <ul class="list-unstyled">
                    <li class="list-unstyled"><input type="checkbox" id="${population.id}"/>
                        <label for="${population.id}" class="list-header"><strong>${population.name}</strong></label>
                        <ul class="code">
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
                        </ul>
                    </li>
                </ul>
            </div>
        </li>
    </body>
</html>