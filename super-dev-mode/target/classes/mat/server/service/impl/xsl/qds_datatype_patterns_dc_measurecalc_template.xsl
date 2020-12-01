<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
   
	<xsl:template match="measurecalc" mode="dc_measurecalc">
		<xsl:variable name="rid">
			<xsl:value-of select="@refid"/>
		</xsl:variable>
		<xsl:variable name="refid">
			<xsl:choose>
				<xsl:when test="/measure/elementLookUp/*[@id=$rid]/@refid">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$rid]/@refid"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@refid"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="refuuid">
			<xsl:value-of select="/measure/elementLookUp/*[@id=$rid]/@uuid"/>
		</xsl:variable>
		<xsl:variable name="refdatatype">
			<!--<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@datatype"/>-->
			<xsl:value-of select="/measure/elementLookUp/*[@id=$rid]/@datatype"/>
		</xsl:variable>
		<xsl:variable name="refname">
			<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@name"/>
		</xsl:variable>
		<xsl:variable name="tid-root">
			<xsl:value-of select="$the_tidrootMapping/PatternMapping/pattern[@dataType=lower-case($refdatatype)]/@root"/>
		</xsl:variable>
		<xsl:variable name="orig-txt">
			<xsl:call-template name="capitalize">
         			<xsl:with-param name="textString" select="@name"/>
         	</xsl:call-template>
			
			
         	<xsl:choose>
				<xsl:when test="high">
					<xsl:apply-templates select="high" mode="high_text"/>
				</xsl:when>
				<xsl:when test="low">
					<xsl:apply-templates select="low" mode="low_text"/>
				</xsl:when>
				<xsl:when test="equal">
					<xsl:apply-templates select="equal" mode="equal_text"/>
				</xsl:when>
         		<!-- suppress output of 'is present' -->
         	</xsl:choose>
			<xsl:text> of:"</xsl:text>
			<xsl:call-template name="capitalize">
				<xsl:with-param name="textString" select="$refdatatype"/>
			</xsl:call-template>
			<xsl:text>: </xsl:text>
			<xsl:value-of select="$refname"/>
			<xsl:text>"</xsl:text>
		</xsl:variable>
		<xsl:variable name="mname">
			<xsl:value-of select="@name"/>
		</xsl:variable>
		<xsl:variable name="mc-tid-root">
			<xsl:choose>
				<xsl:when test="string-length($the_tidrootMapping/PatternMapping/function[@name=lower-case($mname)]/@root)>0">
					<xsl:value-of select="$the_tidrootMapping/PatternMapping/function[@name=lower-case($mname)]/@root"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>TBD</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable>
		<xsl:variable name="poid">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@oid"/>
		</xsl:variable>
		<xsl:variable name="pname">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
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
		
		<xsl:choose>
			<xsl:when test="lower-case(@name)='duration' and string-length($refid)>0">
					<observation classCode="OBS" moodCode="DEF" derivationExprInd="true">
						<!--tid_root of X where [X].duration-->
						<templateId root="{$mc-tid-root}"/>
						<id root="{@uuid}"/><!--unique root of the der. exp.-->
						<code>
							<originalText><xsl:value-of select="$orig-txt"/></originalText><!--include in measurecalc-->
						</code>
						<derivationExpr>var.effectiveTime.high - var.effectiveTime.low</derivationExpr><!--known because property@name=duration-->
						
						<xsl:choose>
							<xsl:when test="string-length($pvalue)>0">
								<xsl:choose>
									<xsl:when test="contains($pname, 'is present')">
										<!-- is present -->
										<value xsi:type="ANYNonNull"/>
									</xsl:when>
									<xsl:when test="string-length($poid)>0">
										<value xsi:type="CD" code="{$poid}"  displayName="{$pdisplayName}"/>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="value"/>
							</xsl:otherwise>
						</xsl:choose>
						<sourceOf typeCode="DRIV">
							<localVariableName>var</localVariableName>
							<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
								<id root="{$refuuid}"/>
								<effectiveTime>
									<low />
									<high/>
								</effectiveTime>
							</observation>
						</sourceOf>
					</observation>
			</xsl:when>
			<xsl:when test="(lower-case(@name)='infusion duration' or lower-case(@name)='infusionduration') and string-length($refid)>0">
					<observation classCode="OBS" moodCode="DEF" derivationExprInd="true">
						<!--tid_root of X where [X].duration-->
						<templateId root="{$mc-tid-root}"/>
						<id root="{@uuid}"/><!--unique root of the der. exp.-->
						<code>
							<originalText><xsl:value-of select="$orig-txt"/></originalText><!--include in measurecalc-->
						</code>
						<derivationExpr>var.effectiveTime.high - var.effectiveTime.low</derivationExpr><!--known because property@name=duration-->
						
						<xsl:choose>
							<xsl:when test="string-length($pvalue)>0">
								<xsl:choose>
									<xsl:when test="contains($pname, 'is present')">
										<!-- is present -->
										<value xsi:type="ANYNonNull"/>
									</xsl:when>
									<xsl:when test="string-length($poid)>0">
										<value xsi:type="CD" code="{$poid}"  displayName="{$pdisplayName}"/>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="value"/>
							</xsl:otherwise>
						</xsl:choose>
						<sourceOf typeCode="DRIV">
							<localVariableName>var</localVariableName>
							<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
								<id root="{$refuuid}"/>
								<code code="36576007" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Infusion"/>
								<effectiveTime>
									<low />
									<high/>
								</effectiveTime>
							</observation>
						</sourceOf>
					</observation>
				<!--</entry>-->
			</xsl:when>
			
			<xsl:when test="(lower-case(@name)='radiation duration' or lower-case(@name)='radiationduration') and string-length($refid)>0">
					<observation classCode="OBS" moodCode="DEF" derivationExprInd="true">
						<!--tid_root of X where [X].duration-->
						<templateId root="{$mc-tid-root}"/>
						<id root="{@uuid}"/><!--unique root of the der. exp.-->
						<code>
							<originalText><xsl:value-of select="$orig-txt"/></originalText>
						</code>
						<derivationExpr>var.effectiveTime.high - var.effectiveTime.low</derivationExpr>
						<xsl:choose>
							<xsl:when test="string-length($pvalue)>0">
								<xsl:choose>
									<xsl:when test="contains($pname, 'is present')">
										<!-- is present -->
										<value xsi:type="ANYNonNull"/>
									</xsl:when>
									<xsl:when test="string-length($poid)>0">
										<value xsi:type="CD" code="{$poid}"  displayName="{$pdisplayName}"/>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="value"/>
							</xsl:otherwise>
						</xsl:choose>
						<sourceOf typeCode="DRIV">
							<localVariableName>var</localVariableName>
							<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
								<id root="{$refuuid}"/>
								<code code="218190002" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Radiation Exposure"/>
								<effectiveTime>
									<low />
									<high/>
								</effectiveTime>
							</observation>
						</sourceOf>
					</observation>
				<!--</entry>-->
			</xsl:when>
			
			<xsl:when test="(lower-case(@name)='duration from arrival' or lower-case(@name)='durationfromarrival') and string-length($refid)>0">
					<observation classCode="OBS" moodCode="DEF" derivationExprInd="true">
						<!--tid_root of X where [X].duration-->
						<templateId root="{$mc-tid-root}"/>
						<id root="{@uuid}"/><!--unique root of the der. exp.-->
						<code>
							<originalText><xsl:value-of select="$orig-txt"/></originalText>
						</code>
						<derivationExpr>var.effectiveTime.high - var.roleParticipant.effectiveTime.low</derivationExpr>
						<xsl:choose>
							<xsl:when test="string-length($pvalue)>0">
								<xsl:choose>
									<xsl:when test="contains($pname, 'is present')">
										<!-- is present -->
										<value xsi:type="ANYNonNull"/>
									</xsl:when>
									<xsl:when test="string-length($poid)>0">
										<value xsi:type="CD" code="{$poid}"  displayName="{$pdisplayName}"/>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="value"/>
							</xsl:otherwise>
						</xsl:choose>
						<sourceOf typeCode="DRIV">
							<localVariableName>var</localVariableName>
							<encounter classCode="OBS" moodCode="EVN" isCriterionInd="true">
								<id root="{$refuuid}"/>
								<effectiveTime>
									<high/>
								</effectiveTime>
								<participant typeCode="LOC">
									<roleParticipant classCode="SDLOC">
										<effectiveTime xsi:type="IVL_TS">
											<low />
										</effectiveTime>
									</roleParticipant>
								</participant>
							</encounter>
						</sourceOf>
					</observation>
				<!--</entry>-->
			</xsl:when>
			<xsl:when test="lower-case(@name)='days'">
					<observation classCode="OBS" moodCode="DEF" derivationExprInd="true" showArgsInd="true">
						<templateId root="{$mc-tid-root}"/>
						<id root="{@uuid}"/><!--unique root of the der. exp.-->
						<code>
							<originalText>
								<xsl:choose>
									<xsl:when test="string-length(@origText)>0">
										<xsl:value-of select="@origText"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="@name"/>
									</xsl:otherwise>
								</xsl:choose>
							</originalText>
						</code>
						<derivationExpr>var1.effectiveTime.high - var2.effectiveTime.low</derivationExpr>
							<xsl:choose>
								<xsl:when test="string-length($pvalue)>0">
									<xsl:choose>
										<xsl:when test="contains($pname, 'is present')">
											<!-- is present -->
											<value xsi:type="ANYNonNull"/>
										</xsl:when>
										<xsl:when test="string-length($poid)>0">
											<value xsi:type="CD" code="{$poid}"  displayName="{$pdisplayName}"/>
										</xsl:when>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="value"/>
								</xsl:otherwise>
							</xsl:choose>
						<xsl:apply-templates select="args" mode="args"/>
					</observation>
			</xsl:when>
			<xsl:otherwise>
				<!-- <measurecalc id="4121" refid="3121" name="duration" datatype="derivation expression" uuid="3C4D6D6D-1010-49FC-A84C-E62D7D804131"/> -->
				<xsl:variable name="derex">
					<xsl:value-of select="@name"/>
					<xsl:text>(</xsl:text>
					<xsl:for-each select="args/*">
						<xsl:variable name="var">
							<xsl:text>var</xsl:text>
							<xsl:value-of select="position()"/>
						</xsl:variable>
						<xsl:value-of select="$var"/>
						<xsl:choose>
							<xsl:when test="properties/property">
								<xsl:apply-templates select="properties/property" mode="property"/>
							</xsl:when>
							<xsl:when test="./*/properties/property">
								<xsl:apply-templates select="./*/properties/property" mode="property"/>
							</xsl:when>
						</xsl:choose>
						<!-- add {aritop, aritnum, aritunit} attribute handling here -->
						<xsl:apply-templates select="." mode="inline_arit_text"/>
						<xsl:if test="not(position()=last())">
			        		<xsl:text>, </xsl:text>
			        	</xsl:if>
					</xsl:for-each>
					<xsl:text>)</xsl:text>
				</xsl:variable>
				<observation classCode="OBS" moodCode="DEF" derivationExprInd="true" showArgsInd="true">
						<templateId root="{$mc-tid-root}"/>
						<id root="{@uuid}"/><!--unique root of the der. exp.-->
						
						<code>
							<originalText>
								<xsl:choose>
									<xsl:when test="string-length(@origText)>0">
										<xsl:value-of select="@origText"/>
										<!-- add comparison content -->
										<xsl:apply-templates select="." mode="orig_text_comparison"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="@name"/>
									</xsl:otherwise>
								</xsl:choose>
							</originalText>
						</code>
						<derivationExpr><xsl:value-of select="$derex"/></derivationExpr>
						<!-- <value xsi:type="CD" code="<<code var>>"/> -->
						<xsl:apply-templates select="." mode="value"/>
						<xsl:apply-templates select="args" mode="args"/>
					</observation>
			</xsl:otherwise>
		</xsl:choose>
   </xsl:template>
   <xsl:template match="*" mode="value">
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
		<xsl:when test="@highnum">
			<xsl:apply-templates select="." mode="inline_high"/>
		</xsl:when>
		<xsl:when test="@lownum">
			<xsl:apply-templates select="." mode="inline_low"/>
		</xsl:when>
		<xsl:when test="@equalnum">
			<xsl:apply-templates select="." mode="inline_equal"/>
		</xsl:when>
		<xsl:otherwise>
			<value xsi:type="ANYNonNull"/>
		</xsl:otherwise>
	</xsl:choose>
   </xsl:template>
	
   <xsl:template match="property" mode="property">
   	<xsl:text>.</xsl:text>
   	<xsl:choose>
   		<xsl:when test="@displayName">
   			<xsl:value-of select="@displayName"/>
   		</xsl:when>
   		<xsl:when test="@pname">
   			<xsl:value-of select="@pname"/>
   		</xsl:when>
   		<xsl:otherwise>
   			<xsl:value-of select="@name"/>
   		</xsl:otherwise>
   	</xsl:choose>
   	<xsl:apply-templates select="." mode="inline_arit_text"/>   	
   </xsl:template>
	
	<xsl:template match="args" mode="args">
		<xsl:for-each select="./*">
			<xsl:variable name="var">
				<xsl:text>var</xsl:text>
				<xsl:value-of select="position()"/>
			</xsl:variable>
			<sourceOf typeCode="DRIV">
				<localVariableName><xsl:value-of select="$var"/></localVariableName>
				<xsl:choose>
					<xsl:when test="name(.)='arg'">
						<observation classCode="OBS" moodCode="DEF">
							<code nullFlavor="OTH">
								<originalText><xsl:value-of select="@value"/></originalText>
							</code>
						</observation>
					</xsl:when>
					<xsl:otherwise>
						<!-- invoke template to output QDSEL, AND, or OR content -->
						
						<xsl:choose>
							<xsl:when test="name(.)='qdsel' or name(.)='measureel' or name(.)='and' or name(.)='or' or name(.)='calc' or name(.)='count'">
								<xsl:apply-templates select="@order" mode="sequence"/>
								<xsl:choose>
									<xsl:when test="reference/qdsel[@id]">
										<xsl:for-each select="reference/qdsel">
											<xsl:apply-templates select="."/>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="."/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="name(.)='not'">
								<xsl:apply-templates select="." mode="not"/>
							</xsl:when>
							<xsl:when test="name(.)='min' or name(.)='max' or name(.)='last' or name(.)='first' or name(.)='second' or name(.)='third' or name(.)='fourth' or name(.)='fifth'">
								<xsl:apply-templates select="." mode="subset"/>
								<xsl:apply-templates select="./*"/>
							</xsl:when>
							<xsl:when test="name(.)='value'">
								<xsl:apply-templates select=".." mode="resultvalue"/>
							</xsl:when>
						</xsl:choose>

					</xsl:otherwise>
				</xsl:choose>				
			</sourceOf>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>