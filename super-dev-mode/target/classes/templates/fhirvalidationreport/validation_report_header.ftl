<div class="custom-card">
    <div class="card-body">
        <div class="container">
            <#if conversionStatusMessage?has_content>
                <label>Conversion status message: </label>
                <div class="value">${conversionStatusMessage}</div><br/>
            </#if>
            <#if errorReason?has_content>
                <label>Error Reason: </label>
                <div class="value">${errorReason}</div><br/>
            </#if>
            <label>Based on ${modelType} Measure: </label>
            <div class="value">${measureName} v${measureVersion}</div><br/>
            <label>Run date:</label>
            <div class="value">${runDate!""}</div><br/>
            <label>Run time:</label>
            <div class="value">${runTime!""}</div>
        </div>
    </div>
</div>
