<#ftl output_format="HTML" strip_whitespace=true>
<ul class="list-unstyled" style="list-style:none;padding-left:0;">
    <li class="list-unstyled">
        <div class="treeview hover p-l-10">
            <input type="checkbox" id="${expression.id}" />
            <label for="${expression.id}" class="list-header"><strong>${expression.name}</strong></label>
            <ul class="code" style="margin-right: 20%; opacity: 1;">
                <li class="list-unstyled">
                    <div>
                        <ul style="padding-left: 0px;">
                            <li style="padding-left: 0px;" class="list-unstyled">
                                <div>
                                    <pre class="cql-definition-body">${expression.logic}</pre>
                                </div>
                            </li>
                        </ul>&nbsp;
                    </div>
                </li>
            </ul>
        </div>
    </li>
</ul>