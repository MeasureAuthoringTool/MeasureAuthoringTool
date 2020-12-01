<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>

	<xsl:template match="attribute" mode="src_of_new">
		
		<xsl:variable name="qid">
			<xsl:value-of select="../@id"/>
		</xsl:variable>
		
		<xsl:variable name="qcode">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdm[@id=$qid]/@oid"/>
		</xsl:variable>
		
		<xsl:variable name="qdisplayName">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdm[@id=$qid]/@name"/>
		</xsl:variable>
		<!-- <xsl:variable name="qcodeSystem">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdsel[@id=$qid]/@codeSystem"/>
		</xsl:variable> -->
		<!-- <xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable> -->
		<xsl:variable name="pdisplayName">
			<xsl:value-of select="lower-case(@name)"/>
		</xsl:variable>
		
		<xsl:variable name="pcode">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=$pdisplayName]/CODE" />
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@code"/> -->
		</xsl:variable>
		
		<xsl:variable name="qdmUUID">
			<xsl:value-of select="@qdmUUID"/>
		</xsl:variable>	
		<xsl:variable name="poid">
			<xsl:value-of select="ancestor::measure/elementLookUp/qdm[@id=$qdmUUID]/@oid"/> 
		</xsl:variable>
		<xsl:variable name="pcodeSystem">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=$pdisplayName]/CODE_SYSTEM"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@codeSystem"/> -->
		</xsl:variable>	
		<xsl:variable name="pcodeSystemName">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=$pdisplayName]/CODE_SYSTEM_NAME"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@codeSystemName"/> -->
		</xsl:variable>
		<xsl:variable name="pdatatype">
			<xsl:value-of select="@name"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/qdm[@id=$qid]/@datatype"/> -->
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@datatype"/> -->
		</xsl:variable>
		<xsl:variable name="ptypeCode">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=$pdisplayName]/TYPE_CODE"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@typeCode"/> -->
		</xsl:variable>
		<xsl:variable name="pmode">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=$pdisplayName]/MODE"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@mode"/> -->
		</xsl:variable>
		<xsl:variable name="pname">
			<xsl:value-of select="@mode"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/> -->
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
				<xsl:when test="/measure/elementLookUp/*[@id=$qdmUUID]/@taxonomy='GROUPING'">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$qdmUUID]/@name"/><!-- <xsl:text> Value Set </xsl:text><xsl:value-of select="/measure/elementLookUp/*[@id=$uuidVal]/@taxonomy"/> -->
				</xsl:when>
				<xsl:when test="/measure/elementLookUp/*[@id=$qdmUUID]/@taxonomy">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$qdmUUID]/@name"/><xsl:text> </xsl:text><xsl:value-of select="/measure/elementLookUp/*[@id=$qdmUUID]/@taxonomy"/>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="rsondisplayname">
			<xsl:variable name="uuidVal"><xsl:value-of select="parent::elementRef/@id"/></xsl:variable>
			<xsl:choose>
				<xsl:when test="upper-case(/measure/elementLookUp/*[@id=$uuidVal]/@taxonomy)='GROUPING'">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$uuidVal]/@name"/><!-- <xsl:text> GROUPING</xsl:text> --><!-- <xsl:text> Value Set </xsl:text><xsl:value-of select="/measure/elementLookUp/*[@id=$uuidVal]/@taxonomy"/> -->
				</xsl:when>
				<xsl:when test="/measure/elementLookUp/*[@id=$uuidVal]/@taxonomy">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$uuidVal]/@name"/><xsl:text> </xsl:text><!-- <xsl:value-of select="/measure/elementLookUp/*[@id=$uuidVal]/@taxonomy"/> -->
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="@name='removal datetime' or @name='removal datetime id'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<templateId root="2.16.840.1.113883.3.560.1.1032.1"/>
						<code code="118292001" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="removal">
							<qualifier>
								<name code="118575009" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="datetime"/>
								<value code="397898000" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="stop datetime"/>
							</qualifier>
						</code>
						<value xsi:type="ANYNonNull"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='incision datetime' or @name='incision datetime id'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<templateId root="2.16.840.1.113883.3.560.1.1007.1"/>
						<code code="34896006" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="incision">
							<qualifier>
								<name code="118575009" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="datetime"/>
								<value code="398201009" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="start datetime"/>
							</qualifier>
						</code>
						<value xsi:type="ANYNonNull"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="$pmode='S'">
				<sourceOf typeCode="{$ptypeCode}">
					<!-- if reaction then sourceOf/@inversionInd="true" -->
					<xsl:if test="lower-case($pdatatype)='reaction'">
						<xsl:attribute name="inversionInd">true</xsl:attribute>
					</xsl:if>
					<xsl:variable name="attribute_mode">
						<xsl:choose>
							<!-- new addition for new XML architechture -->
							<xsl:when test="@qdmUUID">
								<xsl:text>2</xsl:text>
							</xsl:when>
							<xsl:when test="@mode">
								<xsl:text>3</xsl:text>
							</xsl:when>
							<!-- new addition for new XML architechture ends -->
							<xsl:when test="high or low or equal">
								<xsl:text>3</xsl:text>
							</xsl:when>
							<xsl:when test="string-length($poid)>0">
								<xsl:text>2</xsl:text>
							</xsl:when>
							<xsl:when test="contains($pname, 'is present')">
								<xsl:text>1</xsl:text>
							</xsl:when>
							
						</xsl:choose>
						
					</xsl:variable>
					
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<!-- add template id for the attribute itself -->
						<xsl:call-template name="attribute_tid">
							<xsl:with-param name="attribute_name" select="$pdatatype"/>
							<xsl:with-param name="attribute_mode" select="$attribute_mode"/>
						</xsl:call-template>
						<code code="{$pcode}" codeSystem="{$pcodeSystem}" displayName="{$pdatatype}">
							<!-- code/@codeSystemName is optional -->
							<xsl:if test="string-length($pcodeSystemName)>0">
								<xsl:attribute name="codeSystemName">
									<xsl:value-of select="$pcodeSystemName"></xsl:value-of>
								</xsl:attribute>
							</xsl:if>
						</code>
						<xsl:choose>
							<xsl:when test="@qdmUUID">
								<!-- Value Set -->
								<value xsi:type="CD" code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
							</xsl:when>
							<xsl:when test="@mode">
								<!-- value -->
								<xsl:apply-templates select="." mode="property_values_new"/>
							</xsl:when>
							<xsl:when test="high or low or equal">
								<!-- value -->
								<xsl:apply-templates select="." mode="property_values"/>
							</xsl:when>
							<xsl:when test="string-length($poid)>0">
								<!-- Value Set -->
								<value xsi:type="CD" code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
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
			<xsl:when test="$pmode='E'">
				<sourceOf typeCode="{$ptypeCode}">
					<xsl:variable name="attribute_mode">
						<xsl:choose>
							<xsl:when test="high or low or equal">
								<xsl:text>3</xsl:text>
							</xsl:when>
							<xsl:when test="string-length($poid)>0">
								<xsl:text>2</xsl:text>
							</xsl:when>
							<xsl:when test="contains($pname, 'is present')">
								<xsl:text>1</xsl:text>
							</xsl:when>
						</xsl:choose>
						
					</xsl:variable>
					<encounter classCode="ENC" moodCode="EVN" isCriterionInd="true">
						<!-- add template id for the attribute itself -->
						<xsl:call-template name="attribute_tid">
							<xsl:with-param name="attribute_name" select="$pdatatype"/>
							<xsl:with-param name="attribute_mode" select="$attribute_mode"/>
						</xsl:call-template>
						<participant typeCode="LOC">
							<roleParticipant classCode="SDLOC">
								<code>
									<xsl:if test="string-length($poid)>0">
										<xsl:attribute name="code">
											<xsl:value-of select="$poid"/>
										</xsl:attribute>
										<xsl:if test="string-length($pcodeSystem)>0">
											<xsl:attribute name="codeSystem">
												<xsl:text>2.16.840.1.113883.3.560.101.1</xsl:text>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="string-length($pdisplayName)>0">
											<xsl:attribute name="displayName">
												<xsl:value-of select="$pdisplayName"/>
											</xsl:attribute>
										</xsl:if>
									</xsl:if>
								</code>
								<xsl:if test="contains($pdatatype, 'arrival') or contains($pdatatype, 'departure')">
									<effectiveTime xsi:type="IVL_TS">
										<xsl:choose>
											<xsl:when test="contains($pdatatype, 'arrival')">
												<low/>
											</xsl:when>
											<xsl:when test="contains($pdatatype, 'departure')">
												<high/>
											</xsl:when>
										</xsl:choose>
									</effectiveTime>
								</xsl:if>
							</roleParticipant>
						</participant>
					</encounter>
				</sourceOf>
			</xsl:when>
			<!-- deprecate start QDM 2.1.1.1 -->
			<xsl:when test="@name='result value'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<templateId root="2.16.840.1.113883.3.560.1.1019.3"/>
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
						<xsl:apply-templates select="attribute" mode="src_of_new"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='polyp count id'">
				<sourceOf typeCode="OUTC">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
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
						<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}">
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
			
			<xsl:when test="@name='location id'">
				<sourceOf typeCode="REFR">
	                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="285202004" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Environment"/>
						<value xsi:type="CD" code="{$poid}"   displayName="{$pdisplayName}"/>
				 	</observation>
	            </sourceOf>
			</xsl:when>			
			<xsl:when test="@name='reason for delay' or @name='reason for delay is present' or @name='reason for delay is not present'">
				<sourceOf typeCode="REFR">
	                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
	                    <code code="373786007" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Reason For Delay"/>
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
			<xsl:when test="@name='result is present'">
				<sourceOf typeCode="OUTC">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
						<value xsi:type="ANYNonNull"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<!-- <xsl:when test="@name='negation rationale is present' or @name='negation rationale is not present' or (@name='negation rationale' and (@value='is present' or @value='is not present'))"> -->
			<xsl:when test="@name='negation rationale'">
				<sourceOf typeCode="RSON">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
						<value xsi:type="CD" code="{$poid}" displayName="{$rdisplayname}" notDoneDisplayName="{$rsondisplayname}"/>
						<xsl:if test="contains(lower-case(@mode),'is not present') or contains(lower-case(@name),'is not present')">
							<valueNegationInd/>
						</xsl:if>
					</observation>
				 </sourceOf>
			</xsl:when>
			<!-- deprecate end QDM 2.1.1.1 -->			
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="attribute" mode="property_values_new">
		
		<xsl:choose>
			<xsl:when test="lower-case(@mode) = 'less than'">
				<xsl:apply-templates select="." mode="Less_Than">
					<xsl:with-param name="inclusive">false</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="lower-case(@mode) = 'greater than'">
				<xsl:apply-templates select="." mode="Greater_Than">
					<xsl:with-param name="inclusive">false</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="lower-case(@mode) = 'equal to'">
				<xsl:apply-templates select="." mode="Equal_To"/>
			</xsl:when>
			<xsl:when test="lower-case(@mode) = 'less than or equal to'">
				<xsl:apply-templates select="." mode="Less_Than">
					<xsl:with-param name="inclusive">true</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="lower-case(@mode) = 'greater than or equal to'">
				<xsl:apply-templates select="." mode="Greater_Than">
					<xsl:with-param name="inclusive">true</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="lower-case(@mode) = 'value set'">
				
			</xsl:when>
			<xsl:when test="lower-case(@mode) = 'check if present'">
				<value xsi:type="ANYNonNull"/> 
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="attribute" mode="Equal_To">
		<value xsi:type="PQ" >
			<xsl:if test="@comparisonValue">
				<xsl:attribute name="value"><xsl:value-of select="@comparisonValue"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="@unit">
				<xsl:variable name="unit">
					<xsl:call-template name="unitvalue">
						<xsl:with-param name="uval" select="@unit"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:attribute name="unit">
					<xsl:value-of select="$unit"/>
				</xsl:attribute>
			</xsl:if>
		</value>	
	</xsl:template>
	
	<xsl:template match="attribute" mode="Less_Than">
		<xsl:param name="inclusive"/>
		<value xsi:type="IVL_PQ" >
			<high>
				<xsl:if test="@comparisonValue">
					<xsl:attribute name="value"><xsl:value-of select="@comparisonValue"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="@unit">
					<xsl:variable name="unit">
						<xsl:call-template name="unitvalue">
							<xsl:with-param name="uval" select="@unit"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:attribute name="unit">
						<xsl:value-of select="$unit"/>
					</xsl:attribute>
				</xsl:if> 
				<xsl:attribute name="inclusive"><xsl:value-of select="$inclusive"/></xsl:attribute>
			</high>
		</value>	
	</xsl:template>
	
	<xsl:template match="attribute" mode="Greater_Than">
		<xsl:param name="inclusive"/>
		<value xsi:type="IVL_PQ" >
			<low>
				<xsl:if test="@comparisonValue">
					<xsl:attribute name="value"><xsl:value-of select="@comparisonValue"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="@unit">
					<xsl:variable name="unit">
						<xsl:call-template name="unitvalue">
							<xsl:with-param name="uval" select="@unit"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:attribute name="unit">
						<xsl:value-of select="$unit"/>
					</xsl:attribute>
				</xsl:if> 
				<xsl:attribute name="inclusive"><xsl:value-of select="$inclusive"/></xsl:attribute>
			</low>
		</value>	
	</xsl:template>
	
	<xsl:template match="*" mode="property_values">
		<xsl:choose>
			<xsl:when test="@name='is present'">
				<value xsi:type="ANYNonNull"/> 
			</xsl:when>
			<xsl:when test="@name='is notpresent' or @name='is not present'">
				<value xsi:type="ANYNonNull"/>
				<valueNegationInd/>
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
			<xsl:when test="contains(@name, 'is present')">
				<value xsi:type="ANYNonNull"/>
			</xsl:when>
			<xsl:when test="contains(@name, 'is not present')">
				<value xsi:type="ANYNonNull"/>
				<valueNegationInd/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<!-- output <templateId root="{$root}"/> iff an entry matching attribute_name exists inside the tid mapping xml file -->
	<xsl:template name="attribute_tid">
		<xsl:param name="attribute_name"/>
		<xsl:param name="attribute_mode"/>
		<xsl:variable name="root">
			<xsl:if test="string-length($attribute_name)>0">
				<xsl:value-of select="$the_tidrootMapping/PatternMapping/attribute[@name=lower-case($attribute_name)]/@root"/>
				<xsl:if test="string-length($the_tidrootMapping/PatternMapping/attribute[@name=lower-case($attribute_name)]/@root)>0">
					<xsl:text>.</xsl:text>
					<xsl:value-of select="$attribute_mode"/>
				</xsl:if>
			</xsl:if>
		</xsl:variable>
		<xsl:if test="string-length($root)>0">
			<templateId root="{$root}"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="property" mode="src_of">
		<xsl:variable name="qid">
			<xsl:value-of select="../../@id"/>
		</xsl:variable>
		<xsl:variable name="qcode">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdm[@id=$qid]/@oid"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/qdsel[@id=$qid]/@oid"/> -->
		</xsl:variable>
		<xsl:variable name="qdisplayName">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdm[@id=$qid]/@name"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/qdsel[@id=$qid]/@name"/> -->
		</xsl:variable>
		<xsl:variable name="qcodeSystem">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdsel[@id=$qid]/@codeSystem"/>
		</xsl:variable>
		<xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable>
		<xsl:variable name="pcode">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@code"/>
		</xsl:variable>
		<xsl:variable name="poid">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@oid"/>
		</xsl:variable>
		<xsl:variable name="pdisplayName">
			<xsl:choose>
				<xsl:when test="string-length(ancestor::measure//elementLookUp/*[@id=$pvalue]/@displayName)>0">
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@displayName"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="pcodeSystem">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@codeSystem"/>
		</xsl:variable>	
		<xsl:variable name="pcodeSystemName">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@codeSystemName"/>
		</xsl:variable>
		<xsl:variable name="pdatatype">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@datatype"/>
		</xsl:variable>
		<xsl:variable name="ptypeCode">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@typeCode"/>
		</xsl:variable>
		<xsl:variable name="pmode">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@mode"/>
		</xsl:variable>
		<xsl:variable name="pname">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
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
				<xsl:when test="/measure/elementLookUp/*[@id=$rid]/@taxonomy='GROUPING'">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$rid]/@name"/><xsl:text> Value Set </xsl:text><xsl:value-of select="/measure/elementLookUp/*[@id=$rid]/@taxonomy"/>
				</xsl:when>
				<xsl:when test="/measure/elementLookUp/*[@id=$rid]/@taxonomy">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$rid]/@name"/><xsl:text> </xsl:text><xsl:value-of select="/measure/elementLookUp/*[@id=$rid]/@taxonomy"/><xsl:text> Value Set</xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="@name='removal datetime' or @name='removal datetime id'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<templateId root="2.16.840.1.113883.3.560.1.1032.1"/>
						<code code="118292001" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="removal">
							<qualifier>
								<name code="118575009" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="datetime"/>
								<value code="397898000" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="stop datetime"/>
							</qualifier>
						</code>
						<value xsi:type="ANYNonNull"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='incision datetime' or @name='incision datetime id'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<templateId root="2.16.840.1.113883.3.560.1.1007.1"/>
						<code code="34896006" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="incision">
							<qualifier>
								<name code="118575009" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="datetime"/>
								<value code="398201009" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="start datetime"/>
							</qualifier>
						</code>
						<value xsi:type="ANYNonNull"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="$pmode='S'">
				<sourceOf typeCode="{$ptypeCode}">
					<!-- if reaction then sourceOf/@inversionInd="true" -->
					<xsl:if test="lower-case($pdatatype)='reaction'">
						<xsl:attribute name="inversionInd">true</xsl:attribute>
					</xsl:if>
					<xsl:variable name="attribute_mode">
						<xsl:choose>
							<xsl:when test="high or low or equal">
								<xsl:text>3</xsl:text>
							</xsl:when>
							<xsl:when test="string-length($poid)>0">
								<xsl:text>2</xsl:text>
							</xsl:when>
							<xsl:when test="contains($pname, 'is present')">
								<xsl:text>1</xsl:text>
							</xsl:when>
						</xsl:choose>
						
					</xsl:variable>
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<!-- add template id for the attribute itself -->
						<xsl:call-template name="attribute_tid">
							<xsl:with-param name="attribute_name" select="$pdatatype"/>
							<xsl:with-param name="attribute_mode" select="$attribute_mode"/>
						</xsl:call-template>
						<code code="{$pcode}" codeSystem="{$pcodeSystem}" displayName="{$pdatatype}">
							<!-- code/@codeSystemName is optional -->
							<xsl:if test="string-length($pcodeSystemName)>0">
								<xsl:attribute name="codeSystemName">
									<xsl:value-of select="$pcodeSystemName"></xsl:value-of>
								</xsl:attribute>
							</xsl:if>
						</code>
						<xsl:choose>
							<xsl:when test="high or low or equal">
								<!-- value -->
								<xsl:apply-templates select="." mode="property_values"/>
							</xsl:when>
							<xsl:when test="string-length($poid)>0">
								<!-- Value Set -->
								<value xsi:type="CD" code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
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
			<xsl:when test="$pmode='E'">
				<sourceOf typeCode="{$ptypeCode}">
					<xsl:variable name="attribute_mode">
						<xsl:choose>
							<xsl:when test="high or low or equal">
								<xsl:text>3</xsl:text>
							</xsl:when>
							<xsl:when test="string-length($poid)>0">
								<xsl:text>2</xsl:text>
							</xsl:when>
							<xsl:when test="contains($pname, 'is present')">
								<xsl:text>1</xsl:text>
							</xsl:when>
						</xsl:choose>
						
					</xsl:variable>
					<encounter classCode="ENC" moodCode="EVN" isCriterionInd="true">
						<!-- add template id for the attribute itself -->
						<xsl:call-template name="attribute_tid">
							<xsl:with-param name="attribute_name" select="$pdatatype"/>
							<xsl:with-param name="attribute_mode" select="$attribute_mode"/>
						</xsl:call-template>
						<participant typeCode="LOC">
							<roleParticipant classCode="SDLOC">
								<code>
									<xsl:if test="string-length($poid)>0">
										<xsl:attribute name="code">
											<xsl:value-of select="$poid"/>
										</xsl:attribute>
										<xsl:if test="string-length($pcodeSystem)>0">
											<xsl:attribute name="codeSystem">
												<xsl:text>2.16.840.1.113883.3.560.101.1</xsl:text>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="string-length($pdisplayName)>0">
											<xsl:attribute name="displayName">
												<xsl:value-of select="$pdisplayName"/>
											</xsl:attribute>
										</xsl:if>
									</xsl:if>
								</code>
								<xsl:if test="contains($pdatatype, 'arrival') or contains($pdatatype, 'departure')">
									<effectiveTime xsi:type="IVL_TS">
										<xsl:choose>
											<xsl:when test="contains($pdatatype, 'arrival')">
												<low/>
											</xsl:when>
											<xsl:when test="contains($pdatatype, 'departure')">
												<high/>
											</xsl:when>
										</xsl:choose>
									</effectiveTime>
								</xsl:if>
							</roleParticipant>
						</participant>
					</encounter>
				</sourceOf>
			</xsl:when>
			<!-- deprecate start QDM 2.1.1.1 -->
			<xsl:when test="@name='result value'">
				<sourceOf typeCode="REFR">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<templateId root="2.16.840.1.113883.3.560.1.1019.3"/>
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
						<xsl:apply-templates select="attribute" mode="src_of"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<xsl:when test="@name='polyp count id'">
				<sourceOf typeCode="OUTC">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
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
						<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}">
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
			
			<xsl:when test="@name='location id'">
				<sourceOf typeCode="REFR">
	                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="285202004" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Environment"/>
						<value xsi:type="CD" code="{$poid}"   displayName="{$pdisplayName}"/>
				 	</observation>
	            </sourceOf>
			</xsl:when>			
			<xsl:when test="@name='reason for delay' or @name='reason for delay is present' or @name='reason for delay is not present'">
				<sourceOf typeCode="REFR">
	                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
	                    <code code="373786007" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Reason For Delay"/>
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
			<xsl:when test="@name='result is present'">
				<sourceOf typeCode="OUTC">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
						<value xsi:type="ANYNonNull"/>
					</observation>
				</sourceOf>
			</xsl:when>
			<!-- <xsl:when test="@name='negation rationale is present' or @name='negation rationale is not present' or (@name='negation rationale' and (@value='is present' or @value='is not present'))"> -->
			<xsl:when test="@name='negation rationale is present' or @name='negation rationale is not present' or (@name='negation rationale' and (lower-case(@mode)='check if present' or lower-case(@mode)='is not present'))">
				<sourceOf typeCode="RSON">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
						<value xsi:type="CD" notDoneDisplayName="{$rdisplayname}"/>
						<xsl:if test="contains(@value,'is not present') or contains(@name,'is not present')">
							<valueNegationInd/>
						</xsl:if>
					</observation>
				 </sourceOf>
			</xsl:when>
			<!-- deprecate end QDM 2.1.1.1 -->			
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>