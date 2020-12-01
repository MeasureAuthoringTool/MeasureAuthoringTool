<#ftl output_format="HTML" strip_whitespace=true>
<#include "divider.ftl">
<table class="header_table" role="presentation">
    <tbody>
        <tr>
            <th scope="row" class="row-header"><span class="td_label">Measure Set</span></th>
            <td style="width:80%" colspan="3">
                <div style="width:660px;">
                    <pre>${model.measureInformation.measureSet!""}</pre>
                </div>
            </td>
        </tr>
    </tbody>
</table>

