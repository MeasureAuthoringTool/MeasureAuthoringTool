<h3><a name="d1e405" href="#toc">Population Criteria</a></h3>
<div>
	<#-- If the size of the poulation criterias list is one, then we do not want to create an individual population criteria section and just want to display
		the population information in a flat structure. So assign the first element in the list to the population critiera, loop through its populations, and
		display the populations by including the population template. 
  	-->
	<#if model.numberOfGroups == 1>
	    <#assign pc = model.populationCriterias?first>
        <!-- Score Unit -->
        <#if pc.scoreUnit?length != 0>
            <ul>
                <li class="list-unstyled" style="list-style:none;padding-left:0px;">
                    <div class="treeview hover p-l-10">
                        <ul class="list-unstyled">
                            <li class="list-unstyled"><input type="checkbox" id="${pc.id}-scoreUnit"/>
                                <label for="${pc.id}-scoreUnit" class="list-header"><strong>Score Unit</strong></label>
                                <ul class="code">
                                    <li class="list-unstyled">
                                        <div class="treeview hover p-l-10">
                                            <ul>
                                                <li style="padding-left: 0px;">
                                                    <div>
                                                        <pre class="cql-definition-body">${pc.scoreUnit}</pre>
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
            </ul>
        </#if>
        <#list pc.populations as population>
            <ul>
            <#include "population.ftl">
            </ul>
        </#list>
        <#-- If the size of the poulation criterias list is greater than one, then we want to create indiviual population criteria sections, so loop through
            all of the population criterias and create a collapsable section for them.
          -->
    <#else>
        <#if model.populationCriterias?size != 0>
            <ul style="list-style:none;">
                <#list model.populationCriterias as populationCriteria>
                    <#include "population_criteria.ftl">
                </#list>
            </ul>
        </#if>
	</#if>
</div>