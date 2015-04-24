<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
	
	<xsl:template match="property" mode="src_of">
		<xsl:variable name="qid">
			<xsl:value-of select="../../@id"/>
		</xsl:variable>
		<xsl:variable name="qcode">
			<xsl:value-of select="ancestor::measure//elementlookup/qdsel[@id=$qid]/@oid"/>
		</xsl:variable>
		<xsl:variable name="qdisplayName">
			<xsl:value-of select="ancestor::measure//elementlookup/qdsel[@id=$qid]/@name"/>
		</xsl:variable>
		<xsl:variable name="qcodeSystem">
			<xsl:value-of select="ancestor::measure//elementlookup/qdsel[@id=$qid]/@codeSystem"/>
		</xsl:variable>
		<xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable>
		<xsl:variable name="pcode">
			<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@code"/>
		</xsl:variable>
		<xsl:variable name="poid">
			<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@oid"/>
		</xsl:variable>
		<xsl:variable name="pdisplayName">
			<xsl:choose>
				<xsl:when test="string-length(ancestor::measure//elementlookup/*[@id=$pvalue]/@displayName)>0">
					<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@displayName"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="pcodeSystem">
			<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@codeSystem"/>
		</xsl:variable>	
		<xsl:variable name="pcodeSystemName">
			<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@codeSystemName"/>
		</xsl:variable>
		<xsl:variable name="pdatatype">
			<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@datatype"/>
		</xsl:variable>
		<xsl:variable name="ptypeCode">
			<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@typeCode"/>
		</xsl:variable>
		<xsl:variable name="pmode">
			<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@mode"/>
		</xsl:variable>
		<xsl:variable name="pname">
			<xsl:value-of select="ancestor::measure//elementlookup/*[@id=$pvalue]/@name"/>
		</xsl:variable>
		<xsl:variable name="conj">
			<xsl:choose>
				<xsl:when test="../../@name='result conj'">
					<xsl:value-of select="../../@value"/>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="rid">
			<xsl:value-of select="../../../..[reference]/@id"/>
		</xsl:variable>
		<xsl:variable name="rdisplayname">
			<xsl:choose>
				<xsl:when test="/measure/elementlookup/*[@id=$rid]/@taxonomy='GROUPING'">
					<xsl:value-of select="/measure/elementlookup/*[@id=$rid]/@name"/><xsl:text> Code List </xsl:text><xsl:value-of select="/measure/elementlookup/*[@id=$rid]/@taxonomy"/>
				</xsl:when>
				<xsl:when test="/measure/elementlookup/*[@id=$rid]/@taxonomy">
					<xsl:value-of select="/measure/elementlookup/*[@id=$rid]/@name"/><xsl:text> </xsl:text><xsl:value-of select="/measure/elementlookup/*[@id=$rid]/@taxonomy"/><xsl:text> Code List</xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$pmode='S'">
				<sourceOf typeCode="{$ptypeCode}">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="{$pcode}" codeSystem="{$pcodeSystem}" codeSystemName="{$pcodeSystemName}" displayName="{$pdatatype}"/>
						<xsl:choose>
							<xsl:when test="high or low or equal">
								<!-- value -->
								<xsl:choose>
									<xsl:when test="high">
										<xsl:apply-templates select="high" mode="high"/>
									</xsl:when>
									<xsl:when test="low">
										<xsl:apply-templates select="low" mode="low"/>
									</xsl:when>
									<xsl:when test="equal">
										<xsl:apply-templates select="equal" mode="equal"/>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="string-length($poid)>0">
								<!-- code list -->
								<value xsi:type="CD" code="{$poid}" displayName="{$pdisplayName}"/>
							</xsl:when>
							<xsl:when test="contains($pname, 'is present')">
								<!-- is present -->
								<value xsi:type="ANYNonNull"/>
							</xsl:when>
							<xsl:when test="contains($pname, 'is notpresent')">
								<!-- is not present -->
								<value xsi:type="ANYNonNull"/>
								<valueNegationInd/>
							</xsl:when>
						</xsl:choose>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='result value'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="385676005" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Result Value"/>
							<xsl:choose>
								<xsl:when test="high">
									<xsl:apply-templates select="high" mode="high"/>
								</xsl:when>
								<xsl:when test="low">
									<xsl:apply-templates select="low" mode="low"/>
								</xsl:when>
								<xsl:when test="equal">
									<xsl:apply-templates select="equal" mode="equal"/>
								</xsl:when>
							</xsl:choose>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='result conj'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<xsl:apply-templates select="properties/property" mode="src_of"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='date is present' or @name='date is not present'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="410672004" codeSystem="2.16.840.1.113883.6.96" displayName="Date"/>
						<value xsi:type="ANYNonNull"/>
						<xsl:if test="@name='time is not present'">
							<valueNegationInd/>
						</xsl:if>
				 	</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='time is present' or @name='time is not present'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="410669006" codeSystem="2.16.840.1.113883.6.96" displayName="Time"/>
						<value xsi:type="ANYNonNull"/>
						<xsl:if test="@name='time is not present'">
							<valueNegationInd/>
						</xsl:if>
				 	</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='polyp count id'">
				<sourceOf typeCode="OUTC">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="{$poid}"   displayName="{$pdisplayName}"/>
						<xsl:choose>
							<xsl:when test="high">
								<xsl:apply-templates select="high" mode="high"/>
							</xsl:when>
							<xsl:when test="low">
								<xsl:apply-templates select="low" mode="low"/>
							</xsl:when>
							<xsl:when test="equal">
								<xsl:apply-templates select="equal" mode="equal"/>
							</xsl:when>
						</xsl:choose>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='result id'">
				<sourceOf typeCode="OUTC">
					<xsl:if test="string-length($conj)>0">
						<xsl:attribute name="displayInd">true</xsl:attribute>
						<conjunctionCode code="{$conj}"/>
					</xsl:if>
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="{$poid}"   displayName="{$pdisplayName}">
							<xsl:for-each select="../property">
								<xsl:call-template name="qualifier">
	         						<xsl:with-param name="qname" select="@value"/>
		      					</xsl:call-template>
	      					</xsl:for-each>
						</code>
						<xsl:choose>
							<xsl:when test="high">
								<xsl:apply-templates select="high" mode="high"/>
							</xsl:when>
							<xsl:when test="low">
								<xsl:apply-templates select="low" mode="low"/>
							</xsl:when>
							<xsl:when test="equal">
								<xsl:apply-templates select="equal" mode="equal"/>
							</xsl:when>
						</xsl:choose>
					</observation>
				</sourceOf>
			</xsl:when>	
			<xsl:when test="@name='result outc'">
				<!-- output content for qdsel nested inside property -->
				<sourceOf typeCode="OUTC" displayInd="true">
					<xsl:if test="string-length($conj)>0">
						<conjunctionCode code="{$conj}"/>
					</xsl:if>
					<xsl:apply-templates select="./qdsel"/>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='status id'">
				<sourceOf typeCode="REFR">
	                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						
						<code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
						<value xsi:type="CD" code="{$poid}"   displayName="{$pdisplayName}"/>
				 	</observation>
	            </sourceOf>
			</xsl:when>
			<xsl:when test="@name='location id'">
				<sourceOf typeCode="REFR">
	                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="285202004" codeSystem="2.16.840.1.113883.6.96" displayName="Environment"/>
						<value xsi:type="CD" code="{$poid}"   displayName="{$pdisplayName}"/>
				 	</observation>
	            </sourceOf>
			</xsl:when>
			<xsl:when test="@name='severity id'">
				<sourceOf typeCode="SUBJ">
		           	<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		               	<code code="SEV" codeSystem="2.16.840.1.113883.5.4" displayName="Severity observation"/>
		               	<value xsi:type="CD" code="{$poid}"   displayName="{$pdisplayName}"/>
		           	</observation>
		       	</sourceOf>
			</xsl:when>
			<xsl:when test="contains(@name, 'severity')"><!-- 0047 -->
				<xsl:call-template name="severity">
	         		<xsl:with-param name="prop" select="."/>
		      	</xsl:call-template>
			</xsl:when>
			
			<xsl:when test="contains(@name, 'ordinal')"><!-- 0105 -->
				<xsl:call-template name="ordinal">
					<xsl:with-param name="prop" select="."/>
		      	</xsl:call-template>		 		
			</xsl:when>
			<xsl:when test="@name='reason' or @name='reason is present' or @name='reason is not present'"><!-- .reason NOTEMPTY ENDOSCOPY -->
				<sourceOf typeCode="REFR">
	                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
	                    <code code="410666004" codeSystem="2.16.840.1.113883.6.96" displayName="Reason"/>
     					<xsl:if test="contains(@name, 'is present')">
							<value xsi:type="ANYNonNull"/> 
						</xsl:if>
						<xsl:if test="contains(@name, 'is not present')">
							<value nullFlavor="NI"/> 
						</xsl:if>
	                </observation>
	            </sourceOf>
			</xsl:when>
			
			<xsl:when test="@name='reason for delay' or @name='reason for delay is present' or @name='reason for delay is not present'">
				<sourceOf typeCode="REFR">
	                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
	                    <code code="373786007" codeSystem="2.16.840.1.113883.6.96" displayName="Reason For Delay"/>
     					<xsl:if test="contains(@name, 'is present')">
							<value xsi:type="ANYNonNull"/> 
						</xsl:if>
						<xsl:if test="contains(@name, 'is not present')">
							<value xsi:type="ANYNonNull"/>
							<valueNegationInd/>
						</xsl:if>
	                </observation>
	            </sourceOf>
			</xsl:when>
			
			<xsl:when test="contains(@name,'CUMULATIVE MEDICATION DURATION')">
				<sourceOf typeCode="COMP">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="363819003" codeSystem="2.16.840.1.113883.6.96" displayName="cumulative medication duration"/>
						<xsl:choose>
							<xsl:when test="high">
								<xsl:apply-templates select="high" mode="high"/>
							</xsl:when>
							<xsl:when test="low">
								<xsl:apply-templates select="low" mode="low"/>
							</xsl:when>
							<xsl:when test="equal">
								<xsl:apply-templates select="equal" mode="equal"/>
							</xsl:when>
						</xsl:choose>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='DURATION' or @name='duration'">
				<sourceOf typeCode="COMP">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="TBD" codeSystem="2.16.840.1.113883.6.96" displayName="duration"/>
						<xsl:choose>
							<xsl:when test="high">
								<xsl:apply-templates select="high" mode="high"/>
							</xsl:when>
							<xsl:when test="low">
								<xsl:apply-templates select="low" mode="low"/>
							</xsl:when>
							<xsl:when test="equal">
								<xsl:apply-templates select="equal" mode="equal"/>
							</xsl:when>
						</xsl:choose>
					</observation>
				</sourceOf>
			</xsl:when>
			
			<xsl:when test="@name='radiation dosage'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="2.16.840.1.113883.3.526.02.761" codeSystem="2.16.840.1.113883.6.96" displayName="radiation dosage"/>
						<!-- 691000124100 2.16.840.1.113883.3.526.02.761 -->
						<xsl:choose>
							<xsl:when test="@value='is present'">
								<value xsi:type="ANYNonNull"/> 
							</xsl:when>
							<xsl:when test="high">
								<xsl:apply-templates select="high" mode="high"/>
							</xsl:when>
							<xsl:when test="low">
								<xsl:apply-templates select="low" mode="low"/>
							</xsl:when>
							<xsl:when test="equal">
								<xsl:apply-templates select="equal" mode="equal"/>
							</xsl:when>
						</xsl:choose>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='radiation duration'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="2.16.840.1.113883.3.526.02.762" codeSystem="2.16.840.1.113883.6.96" displayName="radiation duration"/>
						<!-- 306751006 2.16.840.1.113883.3.526.02.762 -->
						<xsl:choose>
							<xsl:when test="@value='is present'">
								<effectiveTime>
									<low></low>
									<high></high>
								</effectiveTime> 
							</xsl:when>
						</xsl:choose>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='incision'">
				<sourceOf typeCode="COMP">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="34896006" codeSystem="2.16.840.1.113883.6.96" displayName="incision"/>
						<xsl:if test="@value='is present'">
							<value xsi:type="ANYNonNull"/>
						</xsl:if>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='reaction id'">
				<sourceOf typeCode="MFST" inversionInd="true">
				     <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				         <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
				         <value xsi:type="CD" code="{$poid}"   displayName="{$pdisplayName}"/>
				    </observation>
				</sourceOf> 
			</xsl:when>
			<xsl:when test="@name='is present'">
				<sourceOf typeCode="OUTC">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
						<value xsi:type="ANYNonNull"/> 
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='result is present'">
				<sourceOf typeCode="OUTC">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
						<value xsi:type="ANYNonNull"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='negation rationale is present' or @name='negation rationale is not present' or (@name='negation rationale' and (@value='is present' or @value='is not present'))">
				<sourceOf typeCode="RSON">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
						<value xsi:type="ANYNonNull" notDoneDisplayName="{$rdisplayname}"/>
						<xsl:if test="contains(@value,'is not present') or contains(@name,'is not present')">
							<valueNegationInd/>
						</xsl:if>
					</observation>
				 </sourceOf>
			</xsl:when>
		</xsl:choose>
		
	</xsl:template>
	
</xsl:stylesheet>