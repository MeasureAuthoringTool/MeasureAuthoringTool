 <#ftl output_format="HTML" strip_whitespace=true>
 <table class="header_table">
	<tbody>
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">eCQM Title</span></td>
			<td style="width:80%" colspan="3"><h1 style="font-size:10px">${model.measureInformation.ecqmTitle}</h1></td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">eCQM Identifier (Measure Authoring Tool)</span></td>
			<td style="width:30%">${model.measureInformation.ecqmIdentifier!""}</td>
						
			<td style="background-color:#656565; width:20%"><span class="td_label">eCQM Version Number</span></td>
			<td style="width:30%">${model.measureInformation.ecqmVersionNumber}</td>			
		</tr>	
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">NQF Number</span></td>
			<#-- Default to "Not Applicable" if there is no value -->
			<td style="width:30%">${model.measureInformation.nqfNumber!"Not Applicable"}</td>
						
			<td style="background-color:#656565; width:20%"><span class="td_label">GUID</span></td>
			<td style="width:30%">${model.measureInformation.guid}</td>			
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Measurement Period</span></td>
			<td style="width:80%" colspan="3">${model.measureInformation.measurementPeriod}</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Measure Steward</span></td>
			<td style="width:80%" colspan="3">${model.measureInformation.measureSteward!""}</td>
		</tr>
		
		<#-- Loop through all measure developers if the measureDeveloeprs list exists, otherwise display a blank row -->
		<#if model.measureInformation.measureDevelopers??>
			<#list model.measureInformation.measureDevelopers as measureDeveloper>
				<tr>
					<td style="background-color:#656565; width:20%"><span class="td_label">Measure Developer</span></td>
					<td style="width:80%" colspan="3">${measureDeveloper!""}</td>
				</tr>
			</#list>
			
			<#else>
				<tr>
					<td style="background-color:#656565; width:20%"><span class="td_label">Measure Developer</span></td>
					<td style="width:80%" colspan="3">${""}</td>
				</tr>
		</#if>
			
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Endorsed By</span></td>
			<#-- Default to "None" if no value -->
			<td style="width:80%" colspan="3">${model.measureInformation.endorsedBy!"None"}</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Description</span></td>
			<td style="width:80%" colspan="3">
				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	     		  <pre>${model.measureInformation.description!""}</pre>
	     		 </div>
     		 </td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Copyright</span></td>
			<td style="width:80%" colspan="3">
				 <div style="width:660px;overflow-x:hidden;overflow-y:auto">
	  				 <pre>${model.measureInformation.copyright!""}</pre>
      			</div>
      		</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Disclaimer</span></td>
		  	<td style="width:80%" colspan="3">
      			<div style="width:660px;overflow-x:hidden;overflow-y:auto">
     			  <pre>${model.measureInformation.disclaimer!""}</pre>
 		 		</div>
 		 	</td>
		</tr>

		<#if model.measureInformation.componentMeasures??>
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Composite Scoring Method</span></td>
				<td style="width:80%" colspan="3">${model.measureInformation.compositeScoringMethod!""}</td>
			</tr>
		</#if>
				
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Measure Scoring</span></td>
			<td style="width:80%" colspan="3">${model.measureInformation.measureScoring!""}</td>
		</tr>
		
		<#-- Loop through all measures types if the measures types list exists, otherwise display a blank row -->
		<#if model.measureInformation.measureTypes??>
			<#list model.measureInformation.measureTypes as measureType>
				<tr>
					<td style="background-color:#656565; width:20%"><span class="td_label">Measure Type</span></td>
					<td style="width:80%" colspan="3">${measureType!""}</td>
				</tr>
			</#list>
			
			<#else>
				<tr>
					<td style="background-color:#656565; width:20%"><span class="td_label">Measure Type</span></td>
					<td style="width:80%" colspan="3">${""}</td>
				</tr>
		</#if>
		
		<#if model.measureInformation.componentMeasures??>
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Component Measures List</span></td>
				<td style="width:80%" colspan="3">
					<table class="inner_table">
						<tBody>
					        <tr>
						         <th style="background-color:#656565; width:60%"><span class="td_label">Measure Name</span></th>
						         <th style="background-color:#656565; width:10%"><span class="td_label">Version Number</span></th>
						         <th style="background-color:#656565; width:30%"><span class="td_label">GUID</span></th>
					        </tr>
							<#list model.measureInformation.componentMeasures as componentMeasure>
								<tr>
									<td style="width:60%">${componentMeasure.name}</td>
									<td style="width:10%"><div class="ver">${componentMeasure.version}</div></td>
									<td style="width:30%">${componentMeasure.measureSetId}</div><td>
								</tr>
							</#list>
						</tBody>		
					</table>
				</td>
			</tr>
		</#if>
		<#if model.measureInformation.measureScoring?lower_case != "ratio" && model.measureInformation.qdmVersion < 5.5 >
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Stratification</span></td>
				<td style="width:80%" colspan="3">
	      			<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.stratification!""}</pre>
	       			</div>
	  			</td>
			</tr>
		</#if>
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Risk Adjustment</span></td>
	     	<td style="width:80%" colspan="3">
      			<div style="width:660px;overflow-x:hidden;overflow-y:auto">
       				<pre>${model.measureInformation.riskAdjustment!""}</pre>
      			</div>
  			</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Rate Aggregation</span></td>
		 	<td style="width:80%" colspan="3">
		 		<div style="width:660px;overflow-x:hidden;overflow-y:auto">
       				<pre>${model.measureInformation.rateAggregation!""}</pre>
  				</div>
			</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Rationale</span></td>
	     	<td style="width:80%" colspan="3">
 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
       				<pre>${model.measureInformation.rationale!""}</pre>
      			</div>
  			</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Clinical Recommendation Statement</span></td>
    		<td style="width:80%" colspan="3">
      			<div style="width:660px;overflow-x:hidden;overflow-y:auto">
       				<pre>${model.measureInformation.clinicalRecommendationStatement!""}</pre>
  				</div>
			</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Improvement Notation</span></td>
		    <td style="width:80%" colspan="3">
	      		<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       			<pre>${model.measureInformation.improvementNotation!""}</pre>
	      		</div>
      		</td>
		</tr>
		
		
		<#-- Loop through all references if the references list exists, otherwise display a blank row -->
		<#if model.measureInformation.references??>
			<#list model.measureInformation.references as reference>
				<tr>
					<td style="background-color:#656565; width:20%"><span class="td_label">Reference</span></td>
					<td style="width:80%" colspan="3">
			      		<div style="width:660px;overflow-x:hidden;overflow-y:auto">
			       			<pre>${reference!""}</pre>
			      		</div>
		      		</td>
				</tr>
			</#list>
			
			<#else>
				<tr>
					<td style="background-color:#656565; width:20%"><span class="td_label">Reference</span></td>
					<td style="width:80%" colspan="3">${""}</td>
				</tr>
		</#if>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Definition</span></td>
			<td style="width:80%" colspan="3">
	      		<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       			<pre>${model.measureInformation.definition!""}</pre>
	      		</div>
      		</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Guidance</span></td>
	     	<td style="width:80%" colspan="3">
 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
       				<pre>${model.measureInformation.guidance!""}</pre>
      			</div>	
  			</td>
		</tr>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Transmission Format</span></td>
	     	<td style="width:80%" colspan="3">
 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
       				<pre>${model.measureInformation.transmissionFormat!""}</pre>
      			</div>	
  			</td>
		</tr>
		
		<#-- Initial Populations should show up for all measure types, thus we will not wrap this in a conditional -->
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Initial Population</span></td>
	     	<td style="width:80%" colspan="3">
 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
       				<pre>${model.measureInformation.initialPopulation!""}</pre>
      			</div>	
  			</td>
		</tr>
		
		<#if model.measureInformation.measureScoring?lower_case == "proportion" || model.measureInformation.measureScoring?lower_case == "ratio">
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Denominator</span></td>
		     	<td style="width:80%" colspan="3">
	 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.denominator!""}</pre>
	      			</div>	
  				</td>
			</tr>
		</#if>
		
		<#if model.measureInformation.measureScoring?lower_case == "proportion" || model.measureInformation.measureScoring?lower_case == "ratio">
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Denominator Exclusions</span></td>
		     	<td style="width:80%" colspan="3">
	 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.denominatorExclusions!""}</pre>
	      			</div>	
  				</td>
			</tr>
		</#if>

		<#if model.measureInformation.measureScoring?lower_case == "continuous variable">
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Measure Population</span></td>
		     	<td style="width:80%" colspan="3">
	 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.measurePopulation!""}</pre>
	      			</div>	
  				</td>
			</tr>
		</#if>

		<#if model.measureInformation.measureScoring?lower_case == "continuous variable">
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Measure Population Exclusions</span></td>
		     	<td style="width:80%" colspan="3">
	 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.measurePopulationExclusions!""}</pre>
	      			</div>	
  				</td>
			</tr>
		</#if>
		
		<#if model.measureInformation.measureScoring?lower_case == "continuous variable" || (model.measureInformation.measureScoring?lower_case == "ratio" && model.measureInformation.patientBased == false)>
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Measure Observations</span></td>
		     	<td style="width:80%" colspan="3">
	 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.measureObservations!""}</pre>
	      			</div>	
  				</td>
			</tr>
		</#if>
		
		<#if model.measureInformation.measureScoring?lower_case == "proportion" || model.measureInformation.measureScoring?lower_case == "ratio">
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Numerator</span></td>
		     	<td style="width:80%" colspan="3">
	 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.numerator!""}</pre>
	      			</div>	
  				</td>
			</tr>		
		</#if>
		
		<#if model.measureInformation.measureScoring?lower_case == "proportion" || model.measureInformation.measureScoring?lower_case == "ratio">
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Numerator Exclusions</span></td>
		     	<td style="width:80%" colspan="3">
	 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.numeratorExclusions!""}</pre>
	      			</div>	
  				</td>
			</tr>
		</#if>

		<#if model.measureInformation.measureScoring?lower_case == "proportion">
			<tr>
				<td style="background-color:#656565; width:20%"><span class="td_label">Denominator Exceptions</span></td>
		     	<td style="width:80%" colspan="3">
	 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
	       				<pre>${model.measureInformation.denominatorExceptions!""}</pre>
	      			</div>	
  				</td>
			</tr>
		</#if>
		
		<tr>
			<td style="background-color:#656565; width:20%"><span class="td_label">Supplemental Data Elements</span></td>
	     	<td style="width:80%" colspan="3">
 				<div style="width:660px;overflow-x:hidden;overflow-y:auto">
       				<pre>${model.measureInformation.supplementalDataElements!""}</pre>
      			</div>	
			</td>
		</tr>
	</tbody>
</table>