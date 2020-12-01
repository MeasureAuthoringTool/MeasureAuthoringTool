<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.1" xmlns="urn:hl7-org:v3" xmlns:emsr="urn:hl7-org:v3" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:voc="http://www.alschulerassociates.com/voc" exclude-result-prefixes="emsr voc">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
	<!-- read vocabulary xml into a lookup table -->
	<xsl:variable name="the-valuesets" select="document('mat_narrVoc.xml')"/>
	<!-- ============================================================== -->
	<!--                    MAIN Entry Point                                                                                 -->
	<!-- ============================================================== -->
	<xsl:strip-space elements="
            emsr:name emsr:author emsr:assignedPerson
            emsr:custodian emsr:representedOrganization  
            emsr:verifier emsr:effectiveTime emsr:addr
            emsr:act emsr:procedure emsr:observation emsr:encounter
            emsr:substanceAdministration
            emsr:entry emsr:sourceOf emsr:component emsr:targetOf"/>
	<!-- copy all attributes and nodes to output xml. -->
	<xsl:template match="node()|@*">
		<xsl:if test="name()!='propel'">
			<xsl:copy>
				<xsl:apply-templates select="node()|@*[not(name()='displayInd')and not(name()='instanceInd') and not(name()='derivationExprInd') and not(name()='showArgsInd') and not(name()='notDoneDisplayName') and not(name()='notDoneInd')]"/>
			</xsl:copy>
		</xsl:if>
	</xsl:template>
	<!-- set/change entry typeCode to 'DRIV' 
	<xsl:template match="@typeCode[parent::emsr:entry]">
		<xsl:attribute name="typeCode"><xsl:text>DRIV</xsl:text></xsl:attribute>
	</xsl:template>
	-->
	<xsl:template match="/emsr:QualityMeasureDocument">
		<xsl:copy>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<!-- process data criteria section-->
	<xsl:template match="/emsr:QualityMeasureDocument/emsr:component/emsr:section[emsr:code[@code='57025-9'][@codeSystem='2.16.840.1.113883.6.1']]">
		<xsl:copy>
			<xsl:call-template name="start-section"/>
			<text>
				<xsl:if test="count(emsr:entry) &gt; 0">
					<list>
						<xsl:apply-templates select="emsr:entry[not(@instanceInd='true') and not(@derivationExprInd='true') and not(@notDoneDisplayName)]" mode="narrgen">
							<xsl:sort select="emsr:*//emsr:title"/>
						</xsl:apply-templates>
						<xsl:apply-templates select="emsr:propel[string-length(@oid)>0]" mode="propel"></xsl:apply-templates>
					</list>
				</xsl:if>
			</text>
			<xsl:apply-templates select="*[not(self::emsr:title | self::emsr:code | self::emsr:templateId | self::emsr:text)]|comment()"/>
		</xsl:copy>
	</xsl:template>
	<!-- process supplemental data elements section-->
	<xsl:template match="/emsr:QualityMeasureDocument/emsr:component/emsr:section[emsr:code[@code='69670-8'][@codeSystem='2.16.840.1.113883.6.1']]">
		<xsl:copy>
			<xsl:call-template name="start-section"/>
			<text>
				<xsl:choose>
					<xsl:when test="count(emsr:entry) &gt; 0">
						<list>
							<xsl:apply-templates select="emsr:entry[not(@instanceInd='true') and not(@derivationExprInd='true') and not(@notDoneDisplayName)]" mode="narrgen">
								<xsl:sort select="emsr:*//emsr:title"/>
							</xsl:apply-templates>
						</list>
					</xsl:when>
					<xsl:otherwise>
						<list>
							<item>None</item>
						</list>
					</xsl:otherwise>
				</xsl:choose>
			</text>
			<xsl:apply-templates select="*[not(self::emsr:title | self::emsr:code | self::emsr:templateId | self::emsr:text)]|comment()"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="emsr:propel" mode="propel">
		<item>
			<content>
				<xsl:text>Attribute: "</xsl:text>
				<xsl:call-template name="firstCharCaseUp">
					<xsl:with-param name="data" select="normalize-space(@datatype)"/>
				</xsl:call-template>
				<xsl:text>: </xsl:text>
				<xsl:choose>
					<xsl:when test="@displayName and string-length(@displayName)>0">
						<xsl:call-template name="firstCharCaseUp">
							<xsl:with-param name="data" select="normalize-space(@displayName)"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="firstCharCaseUp">
							<xsl:with-param name="data" select="normalize-space(@name)"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>"</xsl:text>
			</content>
			<xsl:text> using "</xsl:text>
			<xsl:call-template name="firstCharCaseUp">
				<xsl:with-param name="data" select="normalize-space(@name)"/>
			</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:if test="@taxonomy != 'GROUPING'">
				<xsl:value-of select="@taxonomy"/>
			</xsl:if>
			<xsl:text> Value Set </xsl:text>
			<xsl:if test="@taxonomy = 'GROUPING'">
				<xsl:value-of select="@taxonomy"/>
			</xsl:if>
			<xsl:text> (</xsl:text>
			<xsl:value-of select="@oid"/>
			<xsl:text>)"</xsl:text>
			
		</item>
	</xsl:template>
	<!-- process population section-->
	<xsl:template match="/emsr:QualityMeasureDocument/emsr:component/emsr:section[emsr:code[@code='57026-7'][@codeSystem='2.16.840.1.113883.6.1']]">
		<xsl:copy>
			<xsl:call-template name="start-section"/>
			<text>
				<xsl:if test="count(emsr:entry) &gt; 0">
					<xsl:call-template name="process-allPopEntries">
						<xsl:with-param name="pSec" select="current()"/>
					</xsl:call-template>
				</xsl:if>
			</text>
			<xsl:apply-templates select="*[not(self::emsr:title | self::emsr:code | self::emsr:templateId | self::emsr:text)]|comment()"/>
		</xsl:copy>
	</xsl:template>
	
	<!-- process measure observation section -->
	<xsl:template match="/emsr:QualityMeasureDocument/emsr:component/emsr:section[emsr:code[@code='57027-5'][@codeSystem='2.16.840.1.113883.6.1']]">
		<xsl:copy>
			<xsl:call-template name="start-section"/>
			<text>
				<xsl:choose>
					<xsl:when test="count(emsr:entry) &gt; 0">
						<xsl:call-template name="process-allPopEntries">
							<xsl:with-param name="pSec" select="current()"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<list>
							<item>None</item>
						</list>
					</xsl:otherwise>
				</xsl:choose>
			</text>
			<xsl:apply-templates select="*[not(self::emsr:title | self::emsr:code | self::emsr:templateId | self::emsr:text)]|comment()"/>
		</xsl:copy>
	</xsl:template>
	
	<!-- process stratification section -->
	<xsl:template match="/emsr:QualityMeasureDocument/emsr:component/emsr:section[emsr:code[@code='69669-0'][@codeSystem='2.16.840.1.113883.6.1']]">
		<xsl:copy>
			<xsl:call-template name="start-section"/>
			<text>
				<xsl:choose>
					<xsl:when test="count(emsr:entry) &gt; 0">
						<xsl:call-template name="process-allPopEntries">
							<xsl:with-param name="pSec" select="current()"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<list>
							<item>None</item>
						</list>
					</xsl:otherwise>
				</xsl:choose>
			</text>
			<xsl:apply-templates select="*[not(self::emsr:title | self::emsr:code | self::emsr:templateId | self::emsr:text)]|comment()"/>
		</xsl:copy>
	</xsl:template>
	
	<!-- create summary calculation -->
	<xsl:template name="create-summCalc">
		<component typeCode="COMP">
			<section>
				<title>Summary Calculation</title>
				<text>
					<content>Calculation is generic to all measures:</content>
					<list>
						<item>Calculate the final denominator by adding all that meet denominator criteria.</item>
						<item>Subtract from the final denominator all that do not meet numerator criteria yet also meet exclusion criteria.  Note some measures do not have exclusion criteria.</item>
				        <!--item>The performance calculation is the number meeting numerator criteria divided by the final denominator.</item-->
				        <item>The performance calculation is based on the "Measure scoring" from header information above:
				        	<list>
								<item>For "Proportion" measures, the calculation is the number meeting numerator criteria divided by the final denominator.</item>
				                <item>For "Ratio" and "Continuous Variable" measures, follow the calculation instructions in the Data Aggregation header information above, if present.</item>
							</list>
						</item>                                                                                     
						<item>For measures with multiple denominators, repeat this process for each denominator and report each result separately.</item>
						<item>For measures with multiple patient populations, repeat this process for each patient population and report each result separately.</item>
				        <item>For measures with multiple numerators, calculate each numerator separately within each population using the paired exclusion.</item>
					</list>
				</text>
			</section>
		</component>
	</xsl:template>
	
	<!-- start-section -->
	<xsl:template name="start-section">
		<xsl:copy-of select="emsr:templateId"/>
		<xsl:copy-of select="emsr:code"/>
		<title>
			<xsl:value-of select="emsr:title"/>
		</title>
	</xsl:template>
	<!-- ==================================  -->
	<!-- Process data criteria entries                             -->
	<!-- ==================================  -->
	<xsl:template match="emsr:entry[not(@instanceInd='true')][parent::emsr:section[emsr:code[@code='57025-9'][@codeSystem='2.16.840.1.113883.6.1']]][not(emsr:observation/emsr:code/emsr:originalText)]" mode="narrgen">
		
		<item>
			<xsl:choose>
				<xsl:when test="emsr:act">
					<xsl:call-template name="dataCrit-process-actInGeneral">
						<xsl:with-param name="act" select="emsr:act"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="emsr:observation">
					<xsl:call-template name="dataCrit-process-observation">
						<xsl:with-param name="obs" select="emsr:observation"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="emsr:supply">
					<xsl:text>supply: not supported</xsl:text>
				</xsl:when>
				<xsl:when test="emsr:account">
					<xsl:text>account: not supported</xsl:text>
				</xsl:when>
				<xsl:when test="emsr:publicHealthCase">
					<xsl:text>publicHealthCase: not supported</xsl:text>
				</xsl:when>
				<xsl:when test="emsr:diagnosticImage">
					<xsl:text>diagnosticImage: not supported</xsl:text>
				</xsl:when>
				<xsl:when test="emsr:document">
					<xsl:text>document: not supported</xsl:text>
				</xsl:when>
				<xsl:when test="emsr:exposure">
					<xsl:text>exposure: not supported</xsl:text>
				</xsl:when>
				<xsl:when test="emsr:financialContract">
					<xsl:text>financialContract: not supported</xsl:text>
				</xsl:when>
				<xsl:when test="emsr:financialTransaction">
					<xsl:text>financialTransaction: not supported</xsl:text>
				</xsl:when>
				<xsl:when test="emsr:invoiceElement">
					<xsl:text>invoiceElement: not supported</xsl:text>
				</xsl:when>
			</xsl:choose>
		</item>
	</xsl:template>
	<!-- ==================================  -->
	<!-- Process Supplemental Data Element entries                             -->
	<!-- ==================================  -->
	<xsl:template match="emsr:entry[not(@instanceInd='true')][parent::emsr:section[emsr:code[@code='69670-8'][@codeSystem='2.16.840.1.113883.6.1']]]" mode="narrgen">
		<item>
			<xsl:choose>
				<xsl:when test="emsr:act">
					<xsl:call-template name="dataCrit-process-actInGeneral">
						<xsl:with-param name="act" select="emsr:act"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="emsr:observation">
					<xsl:call-template name="dataCrit-process-observation">
						<xsl:with-param name="obs" select="emsr:observation"/>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</item>
	</xsl:template>
	<!-- process data criterion  -->
	<xsl:template name="dataCrit-process-actInGeneral">
		<xsl:param name="act"/>
		<xsl:variable name="isNotDonePattern">
			<!-- check actionNegationInd -->
			<!-- check title (should have nod done) -->
			<!-- check nested RSON type sourceOf -->
			<xsl:if test="$act/emsr:sourceOf[@typeCode='COMP']/emsr:*[@actionNegationInd='true']">
				<xsl:if test="contains($act/emsr:sourceOf[@typeCode='COMP']/emsr:*[@actionNegationInd='true']/emsr:title,'not done')">
					<xsl:if test="$act/emsr:sourceOf[@typeCode='COMP']/emsr:*[@actionNegationInd='true']/emsr:sourceOf[@typeCode='RSON']">
						<xsl:value-of select="'true'"/>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="ngtion">
			<xsl:choose>
				<xsl:when test="$act[@actionNegationInd='true'] and not($isNotDonePattern='true')">
					<xsl:text>Not </xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$act/emsr:templateId">
				<xsl:variable name="title_prefix">
					<xsl:call-template name="get_titlePrefix">
						<xsl:with-param name="templateRootId" select="$act/emsr:templateId/@root"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="title_suffix">
					<xsl:call-template name="get_titleSuffix">
						<xsl:with-param name="templateRootId" select="$act/emsr:templateId/@root"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="text_prefix">
					<xsl:text> using </xsl:text>
				</xsl:variable>
				<xsl:variable name="text_suffix">
					<xsl:call-template name="get_textSuffix">
						<xsl:with-param name="templateRootId" select="$act/emsr:templateId/@root"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="the_title">
					<xsl:text>"</xsl:text>
					<xsl:value-of select="$ngtion"/>
					<xsl:value-of select="$title_prefix"/>
					<xsl:choose>
						<xsl:when test="$act/emsr:sourceOf[@typeCode='COMP']/emsr:*/emsr:title">
							<xsl:value-of select="$act/emsr:sourceOf[@typeCode='COMP']/emsr:*/emsr:title"/>
						</xsl:when>
						<xsl:when test="$act//emsr:title">
							<xsl:value-of select="$act//emsr:title"/>
						</xsl:when>
					</xsl:choose>
					<xsl:text>"</xsl:text>
					<xsl:value-of select="$title_suffix"/>
				</xsl:variable>
				<xsl:variable name="the_text">
					<xsl:choose>
						<xsl:when test="$isNotDonePattern='true'">
							<xsl:if test="contains($act/emsr:sourceOf/emsr:*/emsr:sourceOf[@typeCode='RSON']/emsr:*/emsr:value/@displayName, 'Value Set')">
								<xsl:value-of select="$text_prefix"/>
								<xsl:text> "</xsl:text>
								<xsl:call-template name="show-valueSet">
									<xsl:with-param name="valueset" select="$act/emsr:sourceOf/emsr:*/emsr:sourceOf[@typeCode='RSON']/emsr:*/emsr:value"/>
								</xsl:call-template>
								<xsl:text>"</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="$act//emsr:value[contains(@displayName, 'Value Set')]">
									<xsl:for-each select="$act//emsr:value[contains(@displayName, 'Value Set')]">
										<xsl:value-of select="$text_prefix"/>
										<xsl:text> "</xsl:text>
										<xsl:value-of select="@displayName"/>
										<xsl:text> (</xsl:text><xsl:value-of select="@code"/>
										<xsl:text>)</xsl:text>
										<xsl:text>"</xsl:text>
										
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$act//emsr:code[contains(@displayName, 'Value Set')]">
									<xsl:for-each select="$act//emsr:code[contains(@displayName, 'Value Set')]">
										<xsl:value-of select="$text_prefix"/>
										<xsl:text> "</xsl:text>
										<xsl:value-of select="@displayName"/>
										<xsl:text> (</xsl:text>
										<xsl:value-of select="@code"/>
										<xsl:text>)</xsl:text>
										<xsl:text>"</xsl:text>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$act//emsr:value[contains(@displayName, 'Value Set')]">
									<xsl:for-each select="$act//emsr:value[contains(@displayName, 'Value Set')]">
										<xsl:value-of select="$text_prefix"/>
										<xsl:text> "</xsl:text>
										<xsl:value-of select="@displayName"/>
										<xsl:text>"</xsl:text>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$act//emsr:code[contains(@displayName, 'Value Set')]">
									<xsl:for-each select="$act//emsr:code[contains(@displayName, 'Value Set')]">
										<xsl:value-of select="$text_prefix"/>
										<xsl:text> "</xsl:text>
										<xsl:value-of select="@displayName"/>
										<xsl:text>"</xsl:text>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="$act/emsr:sourceOf/emsr:*[emsr:code][not(emsr:value)]">
									<xsl:if test="$act/emsr:sourceOf/emsr:*/emsr:code/@displayName">
										<xsl:choose>
											<xsl:when test="contains($act/emsr:sourceOf/emsr:*/emsr:code/@displayName, 'Value Set')">
												<xsl:value-of select="$text_prefix"/>
												<xsl:text> "</xsl:text>
												<xsl:call-template name="show-valueSet">
													<xsl:with-param name="valueset" select="$act/emsr:sourceOf/emsr:*/emsr:code"/>
												</xsl:call-template>
												<xsl:text>"</xsl:text>
											</xsl:when>
											<xsl:when test="$act/emsr:sourceOf/emsr:*/emsr:participant//emsr:code[@displayName]">
												<xsl:if test="contains($act/emsr:sourceOf/emsr:*/emsr:participant//emsr:code/@displayName, 'Value Set')">
													<xsl:value-of select="$text_prefix"/>
													<xsl:text> "</xsl:text>
													<xsl:call-template name="show-valueSet">
														<xsl:with-param name="valueset" select="$act/emsr:sourceOf/emsr:*/emsr:participant//emsr:code"/>
													</xsl:call-template>
													<xsl:text>"</xsl:text>
												</xsl:if>
											</xsl:when>
										</xsl:choose>
									</xsl:if>
								</xsl:when>
								<xsl:when test="$act/emsr:sourceOf//emsr:routeCode[@displayName]">
									<xsl:if test="contains($act/emsr:sourceOf//emsr:routeCode/@displayName, 'Value Set')">
										<xsl:for-each select="$act//emsr:code[contains(@displayName, 'Value Set')]">
										<xsl:value-of select="$text_prefix"/>
										<xsl:text> "</xsl:text>
										<xsl:value-of select="@displayName"/>
										<xsl:text>"</xsl:text>
									</xsl:for-each>
									</xsl:if>
								</xsl:when>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="string-length($text_suffix)">
						<xsl:value-of select="$text_suffix"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="show-criterion">
					<xsl:with-param name="the_title" select="$the_title"/>
					<xsl:with-param name="the_text" select="$the_text"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- process observation criterion -->
	<xsl:template name="dataCrit-process-observation">
		<xsl:param name="obs"/>
		<xsl:variable name="isNotDonePattern">
			<!-- check actionNegationInd -->
			<!-- check title (should have nod done) -->
			<!-- check nested RSON type sourceOf -->
			<xsl:if test="$obs[@actionNegationInd='true']">
				<xsl:if test="contains($obs[@actionNegationInd='true']/emsr:title,'not done')">
					<xsl:if test="$obs[@actionNegationInd='true']/emsr:sourceOf[@typeCode='RSON']">
						<xsl:value-of select="'true'"/>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="ngtion">
			<xsl:choose>
				<xsl:when test="$obs[@actionNegationInd='true'] and not($isNotDonePattern='true')">
					<xsl:text>Not </xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$obs/emsr:templateId">
				<xsl:variable name="title_prefix">
					<xsl:call-template name="get_titlePrefix">
						<xsl:with-param name="templateRootId" select="$obs/emsr:templateId/@root"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="title_suffix">
					<xsl:call-template name="get_titleSuffix">
						<xsl:with-param name="templateRootId" select="$obs/emsr:templateId/@root"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="text_prefix">
					<xsl:text> using </xsl:text>
				</xsl:variable>
				<xsl:variable name="text_suffix">
					<xsl:call-template name="get_textSuffix">
						<xsl:with-param name="templateRootId" select="$obs/emsr:templateId/@root"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="the_title">
					<xsl:text>"</xsl:text>
					<xsl:value-of select="$ngtion"/>
					<xsl:value-of select="$title_prefix"/>
					<xsl:choose>
						<xsl:when test="$obs/emsr:title">
							<xsl:value-of select="$obs/emsr:title"/>
						</xsl:when>
						<xsl:when test="$obs/emsr:code/emsr:originalText">
							<xsl:value-of select="$obs/emsr:code/emsr:originalText"/>
						</xsl:when>
					</xsl:choose>
					<xsl:text>"</xsl:text>
					<xsl:value-of select="$title_suffix"/>
				</xsl:variable>
				<xsl:variable name="the_text">
					<xsl:choose>
						<xsl:when test="$isNotDonePattern='true'">
							<xsl:if test="contains($obs/emsr:sourceOf[@typeCode='RSON']/emsr:*/emsr:value/@displayName, 'Value Set')">
								<xsl:value-of select="$text_prefix"/>
								<xsl:text> "</xsl:text>
								<xsl:call-template name="show-valueSet">
									<xsl:with-param name="valueset" select="$obs/emsr:sourceOf[@typeCode='RSON']/emsr:*/emsr:value"/>
								</xsl:call-template>
								<xsl:text>"</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="$obs/emsr:value/@displayName">
								<xsl:if test="contains($obs/emsr:value/@displayName, 'Value Set')">
									<xsl:value-of select="$text_prefix"/>
									<xsl:text> "</xsl:text>
									<xsl:call-template name="show-valueSet">
										<xsl:with-param name="valueset" select="$obs/emsr:value"/>
									</xsl:call-template>
									<xsl:text>"</xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="string-length($text_suffix)">
						<xsl:value-of select="$text_suffix"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="show-criterion">
					<xsl:with-param name="the_title" select="$the_title"/>
					<xsl:with-param name="the_text" select="$the_text"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- ================================== -->
	<!-- process population entries                                   -->
	<!-- ================================== -->
	<xsl:template name="process-allPopEntries">
		<xsl:param name="pSec"/>
		<xsl:variable name="scoring-type">
			<xsl:value-of select="/emsr:QualityMeasureDocument/emsr:subjectOf/emsr:measureAttribute[emsr:code/@code='MSRSCORE']/emsr:value/@code"/>
		</xsl:variable>
		<!--<xsl:value-of select="$scoring-type"/>-->
		<xsl:choose>
			<!-- start measure observations -->
			<xsl:when test="$pSec/emsr:code[@code='57027-5']">
				<list>
					<xsl:for-each select="$pSec/emsr:entry">
						<xsl:choose>
							<xsl:when test="emsr:act">
								<xsl:for-each select="emsr:act/emsr:sourceOf">
									<xsl:call-template name="observation-sourceOf"/>
								</xsl:for-each>
							</xsl:when>
							<!-- handle derivation expressions inside population criteria and measure observation sections not data criteria -->
							<xsl:when test="emsr:observation[@derivationExprInd='true']">
								<xsl:variable name="functionName">
									<xsl:value-of select="emsr:observation/emsr:code/emsr:originalText/text()"/>
								</xsl:variable>
								<item>
									<!-- if showArgs, then processActs should display <<function name>> of: <<args>>, else display original text -->
									<xsl:choose>
										<xsl:when test="emsr:observation[@showArgsInd='true']">
											<xsl:call-template name="process-acts">
												<xsl:with-param name="act" select="emsr:observation"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$functionName"/>
										</xsl:otherwise>
									</xsl:choose>
								</item>
							</xsl:when>
							<xsl:when test="emsr:observation">
								<item>
									<xsl:call-template name="process-acts">
										<xsl:with-param name="act" select="emsr:observation"/>
									</xsl:call-template>
								</item>
							</xsl:when>
						</xsl:choose>
					</xsl:for-each>
				</list>
			</xsl:when>
			<!-- end measure observations -->
			<!-- start stratification -->
			<!-- handles one or more stratification entries -->
			<xsl:when test="$pSec/emsr:code[@code='69669-0']">
				<list>
					<xsl:variable name="moreThanOneStrat">
						<xsl:choose>
							<xsl:when test="count($pSec/emsr:entry)>1">true</xsl:when>
						</xsl:choose>
						
					</xsl:variable>
					<xsl:for-each select="$pSec/emsr:entry">
						<item>
							<content styleCode="Bold">
								<xsl:text>Reporting Stratum</xsl:text>
								<xsl:if test="$moreThanOneStrat='true'">
									<xsl:text> </xsl:text>
									<xsl:value-of select="position()"/>
								</xsl:if>
								<xsl:text> =</xsl:text>
							</content>
							<xsl:choose>
								<xsl:when test="emsr:act">
									<list>
									<xsl:for-each select="emsr:act/emsr:sourceOf">
										<xsl:call-template name="observation-sourceOf"/>
									</xsl:for-each>
									</list>
								</xsl:when>
								<!-- handle derivation expressions inside population criteria and measure observation sections not data criteria -->
								<xsl:when test="emsr:observation[@derivationExprInd='true']">
									<xsl:variable name="functionName">
										<xsl:value-of select="emsr:observation/emsr:code/emsr:originalText/text()"/>
									</xsl:variable>
									<list>
									<item>
										<!-- if showArgs, then processActs should display <<function name>> of: <<args>>, else display original text -->
										<xsl:choose>
											<xsl:when test="emsr:observation[@showArgsInd='true']">
												<xsl:call-template name="process-acts">
													<xsl:with-param name="act" select="emsr:observation"/>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$functionName"/>
											</xsl:otherwise>
										</xsl:choose>
									</item>
									</list>
								</xsl:when>
								<xsl:when test="emsr:observation[count(emsr:sourceOf)=1]">
									<list>
										<item>
										<xsl:call-template name="process-acts">
											<xsl:with-param name="act" select="emsr:observation"/>
										</xsl:call-template>
										</item>
									</list>
								</xsl:when>
								<xsl:when test="emsr:observation[count(emsr:sourceOf)>1]">
										<xsl:call-template name="process-acts">
											<xsl:with-param name="act" select="emsr:observation"/>
										</xsl:call-template>
								</xsl:when>
							</xsl:choose>
						</item>
					</xsl:for-each>
				</list>
			</xsl:when>
			<!-- end stratification -->
			
			
			<!-- single ipp entry -->
			<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='IPP'][@codeSystem='2.16.840.1.113883.5.1063']])=1">
				<list>
					<!-- start Initial Patient Population -->
					<xsl:call-template name="process-popEntry">
						<xsl:with-param name="obs" select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='IPP'][@codeSystem='2.16.840.1.113883.5.1063']]"/>
					</xsl:call-template>
					<!-- end Initial Patient Population -->
					<xsl:if test="not($scoring-type = 'CONTVAR')">	<!-- execute only if not a continuous variable measure -->
						<!-- start Denominator -->
						<xsl:choose>
							<xsl:when test="count($pSec/emsr:entry/emsr:observation[not(@actionNegationInd='true')][emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']])=1">
								<xsl:variable name="obsD" select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][not (@actionNegationInd)]"/>
								<xsl:call-template name="process-popEntry">
									<xsl:with-param name="obs" select="$obsD"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][not(@actionNegationInd='true')])>1">
								<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']]">
									<xsl:call-template name="process-popEntry">
										<xsl:with-param name="obs" select="current()"/>
										<xsl:with-param name="idf" select="position()"/>
									</xsl:call-template>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<item>
									<content styleCode="Bold">
										<xsl:text>Denominator =</xsl:text>
									</content>
									<list>
										<item>
											<xsl:text>None</xsl:text>
										</item>
									</list>
								</item>
							</xsl:otherwise>
						</xsl:choose>
						<!-- end Denominator -->
						<!-- start Denominator Exclusions here -->
						<xsl:choose>
							<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true'])=1">
								<xsl:call-template name="process-popEntry">
									<xsl:with-param name="obs" select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true']"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true'])>1">
								<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true']">
									<xsl:call-template name="process-popEntry">
										<xsl:with-param name="obs" select="current()"/>
										<xsl:with-param name="idf" select="position()"/>
									</xsl:call-template>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<item>
									<content styleCode="Bold">
										<xsl:text>Denominator Exclusions =</xsl:text>
									</content>
									<list>
										<item>
											<xsl:text>None</xsl:text>
										</item>
									</list>
								</item>
							</xsl:otherwise>
						</xsl:choose>
						<!-- end Denominator Exclusions here -->
						<!-- start Numerator -->
						<xsl:choose>
							<xsl:when test="count($pSec/emsr:entry/emsr:observation[not(@actionNegationInd='true')][emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']])=1">
								<xsl:for-each select="$pSec/emsr:entry/emsr:observation[not(@actionNegationInd='true')][emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']]">
									<xsl:call-template name="process-popEntry">
										<xsl:with-param name="obs" select="current()"/>
									</xsl:call-template>
								</xsl:for-each>
							</xsl:when>
							<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][not(@actionNegationInd='true')])>1">
								<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][not(@actionNegationInd='true')]">
									<xsl:call-template name="process-popEntry">
										<xsl:with-param name="obs" select="current()"/>
										<xsl:with-param name="idf" select="position()"/>
									</xsl:call-template>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<item>
									<content styleCode="Bold">
										<xsl:text>Numerator =</xsl:text>
									</content>
									<list>
										<item>
											<xsl:text>None</xsl:text>
										</item>
									</list>
								</item>
							</xsl:otherwise>
						</xsl:choose>
						<!-- end Numerator -->
						<!-- start Numerator Exclusions -->
						<xsl:if test="$scoring-type = 'RATIO'">
							<xsl:choose>
								<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true'])=1">
									<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true']">
										<xsl:call-template name="process-popEntry">
											<xsl:with-param name="obs" select="current()"/>
										</xsl:call-template>
									</xsl:for-each>
								</xsl:when>
								<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true'])>1">
									<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true']">
										<xsl:call-template name="process-popEntry">
											<xsl:with-param name="obs" select="current()"/>
											<xsl:with-param name="idf" select="position()"/>
										</xsl:call-template>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<item>
										<content styleCode="Bold">
											<xsl:text>Numerator Exclusions =</xsl:text>
										</content>
										<list>
											<item>
												<xsl:text>None</xsl:text>
											</item>
										</list>
									</item>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<!-- end Numerator Exclusions -->
						<!-- start Denominator Exceptions here -->
						<!-- process Denominator Exceptions only if this is not a RATIO measure -->
						<xsl:if test="not($scoring-type = 'RATIO')">
							<xsl:choose>
								<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='DENEXCEP'][@codeSystem='2.16.840.1.113883.5.1063']])=1">
									<xsl:call-template name="process-popEntry">
										<xsl:with-param name="obs" select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENEXCEP'][@codeSystem='2.16.840.1.113883.5.1063']]"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='DENEXCEP'][@codeSystem='2.16.840.1.113883.5.1063']])>1">
									<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENEXCEP'][@codeSystem='2.16.840.1.113883.5.1063']]">
										<xsl:call-template name="process-popEntry">
											<xsl:with-param name="obs" select="current()"/>
											<xsl:with-param name="idf" select="position()"/>
										</xsl:call-template>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<item>
										<content styleCode="Bold">
											<xsl:text>Denominator Exceptions =</xsl:text>
										</content>
										<list>
											<item>
												<xsl:text>None</xsl:text>
											</item>
										</list>
									</item>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<!-- end Denominator Exceptions here -->
					</xsl:if>
					<!-- start Measure Population here -->
					<xsl:if test="$pSec/emsr:entry/emsr:observation[emsr:value[@code='MSRPOPL'][@codeSystem='2.16.840.1.113883.5.1063']]">
						<xsl:call-template name="process-popEntry">
							<xsl:with-param name="obs" select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='MSRPOPL'][@codeSystem='2.16.840.1.113883.5.1063']]"/>
						</xsl:call-template>
					</xsl:if>
					<!-- end Measure Population here -->
				</list>
			</xsl:when>
			<!-- Following code section handels multiple IPP based measure. -->
			<!--   Structure it can support: 
            1. A single measure with multiple IPP
            2. Each IPP has maximum one denominator, Denominator Exclusions, numerator, and exception.
         -->
			<xsl:when test="count($pSec/emsr:entry/emsr:observation[emsr:value[@code='IPP'][@codeSystem='2.16.840.1.113883.5.1063']])>1">
				<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='IPP'][@codeSystem='2.16.840.1.113883.5.1063']]">
					<xsl:variable name="ipp_id" select="current()/emsr:id/@root"/>
					<!--<xsl:variable name="denom_id" select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:id/@root=$ipp_id]/emsr:id/@root"/>-->
					<xsl:variable name="denom_id" select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]/emsr:id/@root"/>
					<xsl:variable name="idx" select="position()"/>
					<paragraph>
						<caption>Population Criteria <xsl:value-of select="$idx"/>
						</caption>
					</paragraph>
					<list>
						<!-- start Initial Patient Population  -->
						<xsl:call-template name="process-popEntry">
							<xsl:with-param name="obs" select="current()"/>
							<xsl:with-param name="idf" select="$idx"/>
						</xsl:call-template>
						<!-- end Initial Patient Population  -->
						<!-- start Denominator -->
						<xsl:choose>
							<xsl:when test="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]">
								<xsl:variable name="obsD" select="$pSec/emsr:entry/emsr:observation[not(@actionNegationInd='true')][emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]"/>
								<xsl:call-template name="process-popEntry">
									<xsl:with-param name="obs" select="$obsD"/>
									<xsl:with-param name="idf" select="$idx"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<item>
									<content styleCode="Bold">
										<xsl:text>Denominator =</xsl:text>
									</content>
									<list>
										<item>
											<xsl:text>None</xsl:text>
										</item>
									</list>
								</item>
							</xsl:otherwise>
						</xsl:choose>
						<!-- end Denominator -->
						<!-- start Denominator Exclusions here -->
						<xsl:choose>							
							<xsl:when test="$pSec/emsr:entry/emsr:observation[@actionNegationInd='true'][emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]">
								<xsl:for-each select="$pSec/emsr:entry/emsr:observation[@actionNegationInd='true'][emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]">
									<xsl:variable name="pos">
										<xsl:if test="string-length(position())>0 and count($pSec/emsr:entry/emsr:observation[@actionNegationInd='true'][emsr:value[@code='DENOM'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id])>1">
											<xsl:value-of select="concat('.',position())"/>
										</xsl:if>
									</xsl:variable>
									<xsl:call-template name="process-popEntry">
										<xsl:with-param name="obs" select="current()"/>
										<!--<xsl:with-param name="idf" select="$idx"/>-->
										<xsl:with-param name="idf" select="normalize-space(concat($idx, $pos))"/>
									</xsl:call-template>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<item>
									<content styleCode="Bold">
										<xsl:text>Denominator Exclusions </xsl:text>
										<xsl:value-of select="$idx"/>
										<xsl:text>=</xsl:text>
									</content>
									<list>
										<item>
											<xsl:text>None</xsl:text>
										</item>
									</list>
								</item>
							</xsl:otherwise>
						</xsl:choose>
						<!-- end Denominator Exclusions here -->
						<!-- start Numerator -->
						<!-- NOTE: if ratio measure, numerator will be tied to a population -->
						<xsl:choose>
							<xsl:when test="$pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id or emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]">
								<xsl:for-each select="$pSec/emsr:entry/emsr:observation[not(@actionNegationInd='true')][emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id or emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]">
									<xsl:variable name="pos">
										<xsl:if test="string-length(position())>0 and count($pSec/emsr:entry/emsr:observation[not(@actionNegationInd='true')][emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id or emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id])&gt;1">
											<xsl:value-of select="concat('.',position())"/>
										</xsl:if>
									</xsl:variable>
									<xsl:call-template name="process-popEntry">
										<xsl:with-param name="obs" select="current()"/>
										<!--<xsl:with-param name="idf" select="$idx"/>-->
										<xsl:with-param name="idf" select="normalize-space(concat($idx, $pos))"/>
									</xsl:call-template>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<item>
									<content styleCode="Bold">
										<xsl:text>Numerator </xsl:text>
										<xsl:value-of select="$idx"/>
										<xsl:text>=</xsl:text>
									</content>
									<list>
										<item>
											<xsl:text>None</xsl:text>
										</item>
									</list>
								</item>
							</xsl:otherwise>
						</xsl:choose>
						<!-- end Numerator -->
						<!-- start Numerator Exclusions -->
						<!-- NOTE: if ratio measure, numerator will be tied to a population -->
						<xsl:if test="$scoring-type = 'RATIO'">
							<xsl:choose>
								<xsl:when test="$pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true'][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id or emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]">
									<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true'][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id or emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id]">
										<xsl:variable name="pos">
											<xsl:if test="string-length(position())>0 and count($pSec/emsr:entry/emsr:observation[emsr:value[@code='NUMER'][@codeSystem='2.16.840.1.113883.5.1063']][@actionNegationInd='true'][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id or emsr:sourceOf/emsr:observation/emsr:id/@root=$ipp_id])&gt;1">
												<xsl:value-of select="concat('.',position())"/>
											</xsl:if>
										</xsl:variable>
										<xsl:call-template name="process-popEntry">
											<xsl:with-param name="obs" select="current()"/>
											<!--<xsl:with-param name="idf" select="$idx"/>-->
											<xsl:with-param name="idf" select="normalize-space(concat($idx, $pos))"/>
										</xsl:call-template>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<item>
										<content styleCode="Bold">
											<xsl:text>Numerator Exclusions </xsl:text>
											<xsl:value-of select="$idx"/>
											<xsl:text>=</xsl:text>
										</content>
										<list>
											<item>
												<xsl:text>None</xsl:text>
											</item>
										</list>
									</item>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<!-- end Numerator Exclusions -->
						<!-- start Denominator Exceptions -->
						<!-- process Denominator Exceptions only if this is not a RATIO measure -->
						<xsl:if test="not($scoring-type = 'RATIO')">
							<xsl:choose>
								<xsl:when test="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENEXCEP'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id]">
									<xsl:for-each select="$pSec/emsr:entry/emsr:observation[emsr:value[@code='DENEXCEP'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id]">
										<xsl:variable name="pos">
											<xsl:if test="string-length(position())>0 and count($pSec/emsr:entry/emsr:observation[emsr:value[@code='DENEXCEP'][@codeSystem='2.16.840.1.113883.5.1063']][emsr:sourceOf/emsr:observation/emsr:id/@root=$denom_id])&gt;1">
												<xsl:value-of select="concat('.',position())"/>
											</xsl:if>
										</xsl:variable>
										<xsl:call-template name="process-popEntry">
											<xsl:with-param name="obs" select="current()"/>
											<!--<xsl:with-param name="idf" select="$idx"/>-->
											<xsl:with-param name="idf" select="normalize-space(concat($idx, $pos))"/>
										</xsl:call-template>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<item>
										<content styleCode="Bold">
											<xsl:text>Denominator Exceptions </xsl:text>
											<xsl:value-of select="$idx"/>
											<xsl:text>=</xsl:text>
										</content>
										<list>
											<item>
												<xsl:text>None</xsl:text>
											</item>
										</list>
									</item>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<!-- end Denominator Exceptions -->
					</list>
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- ================================== -->	
	<!-- process entry/observation -->
	<xsl:template name="process-popEntry">
		<xsl:param name="obs"/>
		<xsl:param name="idf"/>
		<item>
			<content styleCode="Bold">
				<xsl:choose>
					<xsl:when test="$obs/emsr:value/@code='DENEXCEP'">
						<xsl:text>Denominator Exceptions</xsl:text>
					</xsl:when>
					<xsl:when test="$obs/emsr:value/@code='DENOM' and $obs[@actionNegationInd='true']">
						<xsl:text>Denominator Exclusions</xsl:text>
					</xsl:when>
					<xsl:when test="$obs/emsr:value/@code='NUMER' and $obs[@actionNegationInd='true']">
						<xsl:text>Numerator Exclusions</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$obs/emsr:value/@displayName"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="string-length($idf)>0">
					<xsl:text> </xsl:text>
					<xsl:value-of select="$idf"/>
				</xsl:if>
				<xsl:text> =</xsl:text>
			</content>
			<list>
				<xsl:choose>
					<xsl:when test="$obs[emsr:sourceOf]">
						<xsl:variable name="content-test">
							<xsl:for-each select="$obs/emsr:sourceOf">
								<xsl:call-template name="observation-sourceOf"/>
							</xsl:for-each>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="string-length($content-test)>0">
								<xsl:for-each select="$obs/emsr:sourceOf">
									<xsl:call-template name="observation-sourceOf"/>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<item>
									<xsl:text>None</xsl:text>
								</item>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<item>
							<xsl:text>None</xsl:text>
						</item>
					</xsl:otherwise>
				</xsl:choose>
			</list>
		</item>
	</xsl:template>
	
	<xsl:template name="observation-sourceOf">
			<xsl:variable name="denomRef">
				<xsl:if test="emsr:observation/emsr:title">
					<xsl:value-of select="emsr:observation/emsr:title"/>
				</xsl:if>
			</xsl:variable>
			<!-- suppress display when this is the IPP entry for denominator exceptions -->
			<xsl:variable name="isDenominatorExclusionsIPP">
				<xsl:choose>
					<xsl:when test="name(../..)='entry' and parent::node()[@actionNegationInd='true'] and contains($denomRef,'Initial Patient Population')">
						<xsl:text>true</xsl:text>
					</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="starts-with($denomRef, 'Denominator') or $isDenominatorExclusionsIPP='true'">
					<!-- do nothing: suppress denominator display -->
				</xsl:when>
				<xsl:otherwise>
					<item>
						
						<xsl:choose>
							<xsl:when test="emsr:conjunctionCode/@code">
								<xsl:value-of select="emsr:conjunctionCode/@code"/>
								<xsl:choose>
									<!-- suppress when this is a 'not done' assertion -->
									<xsl:when test="(emsr:act[@actionNegationInd='true' and not(@notDoneInd='true')]) or current()[@negationInd='true']">
										<xsl:text> NOT: </xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>: </xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<!--  suppress when this is a 'not done' assertion -->
							<xsl:when test="emsr:act[@actionNegationInd='true' and not(@notDoneInd='true')]">
								<xsl:text> NOT: </xsl:text>
							</xsl:when>
						</xsl:choose>
						
						<xsl:if test="emsr:subsetCode/@code">
							<xsl:variable name="subsetCd">
								<xsl:value-of select="emsr:subsetCode/@code"/>
							</xsl:variable>
							<xsl:variable name="subset">
								<xsl:value-of select="$the-valuesets/MATNarrVoc//code[@code=$subsetCd]/@displayName"/>
							</xsl:variable>
							<xsl:if test="string-length($subset)>0">
								<xsl:text> </xsl:text>
								<xsl:value-of select="$subset"/>
								<xsl:text>: </xsl:text>
							</xsl:if>
						</xsl:if>
						<xsl:variable name="seq">
							<xsl:if test="emsr:sequenceNumber">
								<xsl:call-template name="show-sequence">
									<xsl:with-param name="value" select="emsr:sequenceNumber/@value"/>
								</xsl:call-template>
							</xsl:if>
						</xsl:variable>
						<xsl:if test="string-length($seq) &gt;0">
							<xsl:value-of select="$seq"/>
							<xsl:text> </xsl:text>
						</xsl:if>
						<xsl:choose>
							<xsl:when test="emsr:observation[@derivationExprInd='true']">
								<xsl:choose>
									<xsl:when test="emsr:observation[@showArgsInd='true']">
										<xsl:for-each select="emsr:*[@classCode]">
											<xsl:call-template name="process-acts">
												<xsl:with-param name="act" select="current()"/>
											</xsl:call-template>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="functionName">
											<xsl:value-of select="emsr:observation/emsr:code/emsr:originalText/text()"/>
										</xsl:variable>
										<xsl:value-of select="$functionName"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:for-each select="emsr:*[@classCode]">
									<xsl:call-template name="process-acts">
										<xsl:with-param name="act" select="current()"/>
									</xsl:call-template>
								</xsl:for-each>
							</xsl:otherwise>
						</xsl:choose>
					</item>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
	<!-- ================================== -->
	<!-- Process any kind of act. in-parameter:entry/observation/sourceOf/emsr:*  -->
	<xsl:template name="process-acts">
		<xsl:param name="act"/>
		<xsl:variable name="tempId">
			<xsl:value-of select="$act/emsr:templateId/@root"/>
		</xsl:variable>
		<xsl:variable name="isNotDonePattern">
			<!-- check actionNegationInd -->
			<!-- check title (should have nod done) -->
			<!-- check nested RSON type sourceOf -->
			<xsl:if test="$act[@actionNegationInd='true'] or $act/..[@actionNegationInd='true'] or $act/../..[@actionNegationInd='true']">
				
					<xsl:if test="$act/emsr:sourceOf[@typeCode='RSON']">
						<xsl:value-of select="'true'"/>
					</xsl:if>
				
			</xsl:if>
		</xsl:variable>
		<!-- get function name via the subsetCode sibling of $act's first act ancestor (if it exists) -->
		<!--
		<xsl:variable name="subset">
			<xsl:value-of select="$the-valuesets/MATNarrVoc//code[@code=$act/ancestor::emsr:act[emsr:templateId][1]/../emsr:subsetCode/@code]/@displayName"/>
		</xsl:variable>
		 -->
		<xsl:variable name="repeat">
			<xsl:if test="$act/emsr:repeatNumber">
				<xsl:if test="$act/emsr:repeatNumber/emsr:low/@value">
					<xsl:choose>
						<xsl:when test="$act/emsr:repeatNumber/emsr:low/@inclusive='false'">
							<xsl:text> &gt; </xsl:text>
							<xsl:value-of select="$act/emsr:repeatNumber/emsr:low/@value"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> &gt;= </xsl:text>
							<xsl:value-of select="$act/emsr:repeatNumber/emsr:low/@value"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
				<xsl:if test="$act/emsr:repeatNumber/emsr:high/@value">
					<xsl:choose>
						<xsl:when test="$act/emsr:repeatNumber/emsr:high/@inclusive='false'">
							<xsl:text> &lt; </xsl:text>
							<xsl:value-of select="$act/emsr:repeatNumber/emsr:high/@value"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text> &lt;= </xsl:text>
							<xsl:value-of select="$act/emsr:repeatNumber/emsr:high/@value"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:if>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="name($act)='act' and ($act[emsr:title])">
				
				
				<xsl:choose>
					<xsl:when test="string-length($repeat)>0">
						<xsl:value-of select="$repeat"/>
						<xsl:text> count(s) of </xsl:text>
					</xsl:when>
					<!-- 
					<xsl:when test="string-length($subset)>0">
						debug6<xsl:value-of select="$subset"/>
						<xsl:text>:</xsl:text>
					</xsl:when>
					-->
				</xsl:choose>
				
				<xsl:text>"</xsl:text>
				<xsl:value-of select="$act/emsr:title"/>
				<xsl:text>"</xsl:text>
				<xsl:variable name="not_done_tail">
					<xsl:value-of select="$act//emsr:sourceOf[@typeCode='RSON']//emsr:value/@notDoneDisplayName"/>
				</xsl:variable>
				<xsl:if test="string-length($not_done_tail)>0">
						<xsl:choose>
							<xsl:when test="($isNotDonePattern='true')">
								<xsl:text> for "</xsl:text>
								<xsl:value-of select="$not_done_tail"/>
								<xsl:text>"</xsl:text>
							</xsl:when>
						</xsl:choose>
				</xsl:if>
				<xsl:variable name="act_value">
					<xsl:if test="$act/emsr:value">
						<xsl:call-template name="show-value">
							<xsl:with-param name="value" select="$act/emsr:value"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:variable>
				<xsl:if test="string-length($act_value)>0">
					<xsl:if test="not(contains($act_value,'Value Set'))">
						<xsl:text> </xsl:text>
						<xsl:value-of select="$act_value"/>
					</xsl:if>
				</xsl:if>
				<xsl:for-each select="$act/emsr:sourceOf[not(@typeCode='COMP') and not(@typeCode='PRCN') and not(@typeCode='DRIV')]">
					<xsl:call-template name="process-soruceOf-concat">
						<xsl:with-param name="srcOf" select="current()"/>
					</xsl:call-template>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="not(name($act)='act') and $act[emsr:title] and not ($tempId='2.16.840.1.113883.3.560.1.79')">
				
				
				<xsl:choose>
					<xsl:when test="string-length($repeat)>0">
						<xsl:value-of select="$repeat"/>
						<xsl:text> count(s) of </xsl:text>
					</xsl:when>
					<!-- 
					<xsl:when test="string-length($subset)>0">
						debug4<xsl:value-of select="$subset"/>
						<xsl:text>: </xsl:text>
					</xsl:when>
					-->
				</xsl:choose>
				<!--	
				<xsl:variable name="act_uuid">
					<xsl:value-of select="ancestor::emsr:act/emsr:id/@root[1]"/>
				</xsl:variable>
				
				<xsl:variable name="instance_nm">
					<xsl:value-of select="/emsr:QualityMeasureDocument/emsr:component/emsr:section[emsr:code/@code='57025-9']//emsr:entry[@instanceInd='true'][emsr:observation/emsr:id/@root=$act_uuid]//emsr:localVariableName"/>
				</xsl:variable>
				<xsl:if test="string-length($instance_nm)>0">
					<xsl:value-of select="$instance_nm"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				-->
				<xsl:text>"</xsl:text>
				<xsl:value-of select="normalize-space($act/emsr:title)"/>
				<xsl:text>"</xsl:text>
				<!--
				<xsl:variable name="act_value">
					<xsl:choose>
						<xsl:when test="$act/emsr:value">
							<xsl:call-template name="show-value">
								<xsl:with-param name="value" select="$act/emsr:value"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="not($act/emsr:value) and not($act/emsr:participant) and ($act/emsr:code) and contains($act/emsr:code/@displayName, 'Value Set')">
							<xsl:call-template name="show-code">
								<xsl:with-param name="code" select="$act/emsr:code"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:variable>
				-->
				<xsl:variable name="not_done_tail">
					<xsl:value-of select="$act//emsr:sourceOf[@typeCode='RSON']//emsr:value/@notDoneDisplayName"/>
				</xsl:variable>
				<xsl:if test="string-length($not_done_tail)>0">
						<xsl:choose>
							<xsl:when test="($isNotDonePattern='true')">
								<xsl:text> for "</xsl:text>
								<xsl:value-of select="$not_done_tail"/>
								<xsl:text>"</xsl:text>
							</xsl:when>
						</xsl:choose>
				</xsl:if>
				<!--
				<xsl:if test="string-length($act_value)>0">
					<xsl:text> </xsl:text>
					<xsl:value-of select="$act_value"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				-->

				<!-- if this is a function, then show the args -->
				<xsl:if test="$act/emsr:id/@root">
					<xsl:variable name="fxuuid">
						<xsl:value-of select="$act/emsr:id/@root"/>
					</xsl:variable>
					<xsl:if test="/*//emsr:section[emsr:code/@code='57025-9']/emsr:entry[@showArgsInd='true']/emsr:observation[emsr:id/@root=$fxuuid]/emsr:sourceOf">
						<list>
							<xsl:for-each select="/*//emsr:section[emsr:code/@code='57025-9']/emsr:entry[@showArgsInd='true']/emsr:observation[emsr:id/@root=$fxuuid]/emsr:sourceOf">
								<item>
									<xsl:text>AND: </xsl:text>
									<xsl:call-template name="process-soruceOf-concat">
										<xsl:with-param name="srcOf" select="current()"/>
									</xsl:call-template>
								</item>
							</xsl:for-each>
						</list>
					</xsl:if>
				</xsl:if>
				<xsl:for-each select="$act/emsr:sourceOf">
					<xsl:call-template name="process-soruceOf-concat">
						<xsl:with-param name="srcOf" select="current()"/>
					</xsl:call-template>
				</xsl:for-each>
			</xsl:when>
			<!-- explicitly process pattern: 2.16.840.1.113883.3.560.1.79 -->
			<xsl:when test="not(name($act)='act') and ($act[emsr:title]) and ($tempId='2.16.840.1.113883.3.560.1.79')">
				<xsl:if test="string-length($repeat)>0">
					<xsl:value-of select="$repeat"/>
					<xsl:text> count(s) of </xsl:text>
				</xsl:if>
				<xsl:text>"</xsl:text>
				<xsl:value-of select="$act/emsr:title"/>
				<xsl:text>"</xsl:text>
				<xsl:for-each select="$act/emsr:sourceOf[not(@typeCode='COMP') and not(@typeCode='PRCN') and not(@typeCode='DRIV')]">
					<xsl:variable name="childSourceOf">
					<xsl:call-template name="process-soruceOf-concat">
						<xsl:with-param name="srcOf" select="current()"/>
					</xsl:call-template>
					</xsl:variable>
					<xsl:if test="string-length(normalize-space($childSourceOf))>0">
						<list>
							<item>
								<xsl:value-of select="$childSourceOf"/>
							</item>
						</list>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<!--<xsl:when test="$act[emsr:code] and (not($act[emsr:title]))--> 
			<xsl:when test="(not($act[emsr:title]))
            and ($act/emsr:sourceOf/emsr:observation 
            or $act/emsr:sourceOf/emsr:encounter 
            or $act/emsr:sourceOf/emsr:procedure
            or $act/emsr:sourceOf/emsr:act
            or $act/emsr:sourceOf/emsr:substanceAdministration
            or $act/emsr:sourceOf/emsr:supply) 
            and (count($act/emsr:sourceOf)>0)">
				
				<xsl:choose>
					<xsl:when test="string-length($repeat)>0">
						<xsl:value-of select="$repeat"/>
						<xsl:text> count(s) of</xsl:text>
						<list>
							<xsl:for-each select="$act/emsr:sourceOf">
								<xsl:call-template name="process-sourceOf">
									<xsl:with-param name="srcOf" select="current()"/>
								</xsl:call-template>
							</xsl:for-each>
						</list>
					</xsl:when>
					
					<xsl:when test="$act[@derivationExprInd='true']">
						<xsl:variable name="functionName">
							<xsl:value-of select="$act/emsr:code/emsr:originalText/text()"/>
						</xsl:variable>
							<xsl:value-of select="$functionName"/>
							<xsl:if test="$act[@showArgsInd='true']">
								<xsl:text> of:</xsl:text>
								<xsl:choose>
									<xsl:when test="count($act/emsr:sourceOf)=1">
										<xsl:for-each select="$act/emsr:sourceOf">
											<xsl:call-template name="process-soruceOf-concat">
												<xsl:with-param name="srcOf" select="current()"/>
											</xsl:call-template>
										</xsl:for-each>
									</xsl:when>
									<xsl:when test="count($act/emsr:sourceOf) &gt;1">
										<list>
											<xsl:for-each select="$act/emsr:sourceOf">
												<xsl:call-template name="process-sourceOf">
													<xsl:with-param name="srcOf" select="current()"/>
												</xsl:call-template>
											</xsl:for-each>
										</list>
									</xsl:when>
								</xsl:choose>
							</xsl:if>
					</xsl:when>
					<xsl:when test="count($act/emsr:sourceOf)=1">
						<xsl:for-each select="$act/emsr:sourceOf">
							<xsl:call-template name="process-soruceOf-concat">
								<xsl:with-param name="srcOf" select="current()"/>
							</xsl:call-template>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="count($act/emsr:sourceOf) &gt;1">
						<list>
							<xsl:for-each select="$act/emsr:sourceOf">
								<xsl:call-template name="process-sourceOf">
									<xsl:with-param name="srcOf" select="current()"/>
								</xsl:call-template>
							</xsl:for-each>
						</list>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<!-- no code, no title, has 1 .. many sourceOf -->
			<xsl:when test="not($act[emsr:code][emsr:title]) and (count($act/emsr:sourceOf)>0)">
				<xsl:if test="string-length($repeat)>0">
					<xsl:value-of select="$repeat"/>
					<xsl:text> count(s) of</xsl:text>
				</xsl:if>
				<list>
					<xsl:for-each select="$act/emsr:sourceOf">
						<xsl:call-template name="process-sourceOf">
							<xsl:with-param name="srcOf" select="current()"/>
						</xsl:call-template>
					</xsl:for-each>
				</list>
			</xsl:when>
			<xsl:when test="not($act[emsr:code][emsr:title]) and $act[emsr:id] and (count($act/emsr:sourceOf)=0)">
				<xsl:if test="string-length($repeat)>0">
					<xsl:value-of select="$repeat"/>
					<xsl:text> count(s) of</xsl:text>
				</xsl:if>
				<!-- 
				<xsl:if test="string-length($subset)>0">
					debug2<xsl:value-of select="$subset"/>
				</xsl:if>
				-->
				<xsl:call-template name="show-targetName">
					<xsl:with-param name="oid" select="$act/emsr:id/@root"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- ================================== -->
	<!-- process nested sourceOf -->
	<xsl:template name="process-sourceOf">
		<xsl:param name="srcOf"/>
		<item>
			<!-- handle conjunction code -->
			<xsl:choose>
				<xsl:when test="$srcOf/emsr:conjunctionCode/@code">
					<xsl:value-of select="$srcOf/emsr:conjunctionCode/@code"/>
					
					<xsl:choose>
						<!-- suppress when this is a 'not done' assertion -->
						<xsl:when test="$srcOf/emsr:act[@actionNegationInd='true' and not(@notDoneInd='true')] or current()[@negationInd='true']">
							<xsl:text> NOT: </xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>: </xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<!-- suppress when this is a 'not done' assertion -->
					<xsl:if test="$srcOf/emsr:act[@actionNegationInd='true' and not(@notDoneInd='true')]">
						<xsl:text> NOT </xsl:text>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:variable name="pq">
				<xsl:choose>
					<xsl:when test="$srcOf/emsr:pauseQuantity">
						<xsl:call-template name="show-value">
							<xsl:with-param name="value" select="$srcOf/emsr:pauseQuantity"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="$srcOf/emsr:substanceAdministration/emsr:pauseQuantity">
						<xsl:call-template name="show-value">
							<xsl:with-param name="value" select="$srcOf/emsr:substanceAdministration/emsr:pauseQuantity"/>
						</xsl:call-template>
					</xsl:when>
					<!-- commenting should only show pauseQuanitty here --><!--
					<xsl:when test="$srcOf/emsr:value/emsr:high or $srcOf/../emsr:value/emsr:low">
						<xsl:call-template name="show-value">
							<xsl:with-param name="value" select="$srcOf/../emsr:value[emsr:high or emsr:low]"/>
						</xsl:call-template>
					</xsl:when>
					 -->
				</xsl:choose>
			</xsl:variable>
			<xsl:if test="string-length($pq) &gt;0">
				<xsl:text> </xsl:text>
				<xsl:value-of select="$pq"/>
			</xsl:if>
			<xsl:if test="$srcOf[@typeCode='EBS' or @typeCode='SAS' or @typeCode='SDU' or @typeCode='EDU' or @typeCode='DURING' or @typeCode='CONCURRENT' or @typeCode='ECW' or @typeCode='SCW' or @typeCode='SBS' or @typeCode='EAS' or @typeCode='EAE' or @typeCode='SAE' or @typeCode='SPRT' or (@typeCode='REFR' and @displayInd='true') or (@typeCode='OUTC' and @displayInd='true') or (@typeCode='CAUS' and @displayInd='true') or (@typeCode='GOAL' and @displayInd='true') or (@typeCode='AUTH' and @displayInd='true') or (@typeCode='DRIV' and @displayInd='true')]">
				<xsl:choose>
					<xsl:when test="$srcOf/@inversionInd='true' and @typeCode='EAS'">
						<xsl:text> starts before or during</xsl:text>
					</xsl:when>
					<xsl:when test="$srcOf/@inversionInd='true' and @typeCode='EAE'">
						<xsl:text> ends before or during</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$the-valuesets/MATNarrVoc//code[@code=$srcOf/@typeCode]/@displayName"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text> </xsl:text>
			</xsl:if>
			<xsl:if test="$srcOf[@typeCode='EBS' or @typeCode='SAS' or @typeCode='SDU' or @typeCode='EDU' or @typeCode='DURING' or @typeCode='CONCURRENT' or @typeCode='ECW' or @typeCode='SCW' or @typeCode='SBS' or @typeCode='EAS' or @typeCode='EAE' or @typeCode='SAE' or @typeCode='SPRT' or (@typeCode='REFR' and @displayInd='true') or (@typeCode='OUTC' and @displayInd='true') or (@typeCode='CAUS' and @displayInd='true') or (@typeCode='GOAL' and @displayInd='true') or (@typeCode='AUTH' and @displayInd='true') or (@typeCode='DRIV' and @displayInd='true')]">
				<xsl:if test="not($srcOf/emsr:act/emsr:sourceOf/emsr:conjunctionCode) and $srcOf//*[@displayInd='true']">
					<xsl:text> (</xsl:text>
				</xsl:if>
			</xsl:if>
			<xsl:variable name="seq">
				<xsl:if test="$srcOf/emsr:sequenceNumber">
					<xsl:call-template name="show-sequence">
						<xsl:with-param name="value" select="$srcOf/emsr:sequenceNumber/@value"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:variable>
			<xsl:if test="string-length($seq) &gt;0">
				<xsl:value-of select="$seq"/>
				<xsl:text> </xsl:text>
			</xsl:if>
			<xsl:variable name="subset">
				<xsl:if test="$srcOf/emsr:subsetCode">
					<xsl:value-of select="$the-valuesets/MATNarrVoc//code[@code=$srcOf/emsr:subsetCode/@code]/@displayName"/>
				</xsl:if>
			</xsl:variable>
			<xsl:if test="string-length($subset)>0">
				
				<xsl:value-of select="$subset"/>
				<xsl:text>:</xsl:text>
			</xsl:if>
			<xsl:for-each select="$srcOf/emsr:*[@classCode]">
				
				<xsl:call-template name="process-acts">
					<xsl:with-param name="act" select="current()"/>
				</xsl:call-template>
			</xsl:for-each>
			<xsl:if test="$srcOf[@typeCode='EBS' or @typeCode='SAS' or @typeCode='SDU' or @typeCode='EDU' or @typeCode='DURING' or @typeCode='CONCURRENT' or @typeCode='ECW' or @typeCode='SCW' or @typeCode='SBS' or @typeCode='EAS' or @typeCode='EAE' or @typeCode='SAE' or @typeCode='SPRT' or (@typeCode='REFR' and @displayInd='true') or (@typeCode='OUTC' and @displayInd='true') or (@typeCode='CAUS' and @displayInd='true') or (@typeCode='GOAL' and @displayInd='true') or (@typeCode='AUTH' and @displayInd='true') or (@typeCode='DRIV' and @displayInd='true')]">
				<xsl:if test="not($srcOf/emsr:act/emsr:sourceOf/emsr:conjunctionCode) and $srcOf//*[@displayInd='true']">
					<xsl:text>)</xsl:text>
				</xsl:if>
			</xsl:if>
			<!-- process a literal value (a number or date) -->
			<xsl:if test="$srcOf[@typeCode='DRIV'][emsr:localVariableName]/emsr:observation[@classCode='OBS'][@moodCode='DEF'][not(emsr:templateId)]/emsr:code/emsr:originalText">
				<xsl:value-of select="$srcOf[@typeCode='DRIV'][emsr:localVariableName]/emsr:observation[@classCode='OBS'][@moodCode='DEF'][not(emsr:templateId)]/emsr:code/emsr:originalText"/>
			</xsl:if>
		</item>
	</xsl:template>
	<!-- process nested sourceOf -->
	<xsl:template name="process-soruceOf-concat">
		<xsl:param name="srcOf"/>
			<xsl:variable name="isNotDonePattern">
			<xsl:if test="contains(..//emsr:title,'not done')">true</xsl:if>
			</xsl:variable>
		<xsl:choose>
			<xsl:when test="$srcOf/emsr:conjunctionCode/@code">
				<xsl:value-of select="$srcOf/emsr:conjunctionCode/@code"/>
				<xsl:choose>
					<!-- suppress when this is a 'not done' assertion -->
					<xsl:when test="(emsr:act[@actionNegationInd='true' and not(@notDoneInd='true')]) or current()[@negationInd='true']">
						<xsl:text> NOT: </xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>: </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<!-- suppress when this is a 'not done' assertion -->
				<xsl:if test="(emsr:act[@actionNegationInd='true' and not(@notDoneInd='true')]) or current()[@negationInd='true']">
					<xsl:if test="$isNotDonePattern!='true'">
						<xsl:text> NOT </xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:variable name="pq">
			<xsl:choose>
				<xsl:when test="$srcOf/emsr:pauseQuantity">
					<xsl:call-template name="show-value">
						<xsl:with-param name="value" select="$srcOf/emsr:pauseQuantity"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$srcOf/emsr:substanceAdministration/emsr:pauseQuantity">
					<xsl:call-template name="show-value">
						<xsl:with-param name="value" select="$srcOf/emsr:substanceAdministration/emsr:pauseQuantity"/>
					</xsl:call-template>
				</xsl:when>
				<!-- commenting should only show pauseQuanitty here --><!--
				<xsl:when test="$srcOf/emsr:observation/emsr:value/emsr:high or $srcOf/emsr:value/emsr:low">
					<xsl:call-template name="show-value">
						<xsl:with-param name="value" select="$srcOf/emsr:observation/emsr:value[emsr:high or emsr:low]"/>
					</xsl:call-template>
				</xsl:when>
				-->
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="string-length($pq)>0">
			<xsl:text> </xsl:text>
			<xsl:value-of select="$pq"/>
		</xsl:if>
		<xsl:variable name="typeCode">
			<xsl:if test="$srcOf[@typeCode='EBS' or @typeCode='SAS' or @typeCode='SDU' or @typeCode='EDU' or @typeCode='DURING' or @typeCode='CONCURRENT' or @typeCode='ECW' or @typeCode='SCW' or @typeCode='SBS' or @typeCode='EAS' or @typeCode='EAE' or @typeCode='SAE' or @typeCode='SPRT' or (@typeCode='REFR' and @displayInd='true') or (@typeCode='OUTC' and @displayInd='true') or (@typeCode='CAUS' and @displayInd='true') or (@typeCode='GOAL' and @displayInd='true') or (@typeCode='AUTH' and @displayInd='true') or (@typeCode='DRIV' and @displayInd='true')]">
				<xsl:choose>
					<xsl:when test="$srcOf/@inversionInd='true' and @typeCode='EAS'">
						<xsl:text> starts before or during</xsl:text>
					</xsl:when>
					<xsl:when test="$srcOf/@inversionInd='true' and @typeCode='EAE'">
							<xsl:text> ends before or during</xsl:text>
						</xsl:when>
					<xsl:otherwise>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$the-valuesets/MATNarrVoc//code[@code=$srcOf/@typeCode]/@displayName"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:variable>
		<xsl:if test="string-length($typeCode) &gt;0">
			<xsl:value-of select="$typeCode"/>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:if test="(string-length($typeCode) &gt;0) and not($srcOf/emsr:act/emsr:sourceOf/emsr:conjunctionCode) and $srcOf//*[@displayInd='true']">
			<xsl:text>(</xsl:text>
		</xsl:if>
		<xsl:variable name="seq">
			<xsl:if test="$srcOf/emsr:sequenceNumber">
				<xsl:call-template name="show-sequence">
					<xsl:with-param name="value" select="$srcOf/emsr:sequenceNumber/@value"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:variable>
		<xsl:if test="string-length($seq) &gt;0">
			<xsl:value-of select="$seq"/>
			<xsl:text> </xsl:text>
		</xsl:if>

		<xsl:variable name="subset">
			<xsl:if test="$srcOf/emsr:subsetCode">
				<xsl:value-of select="$the-valuesets/MATNarrVoc//code[@code=$srcOf/emsr:subsetCode/@code]/@displayName"/>
			</xsl:if>
		</xsl:variable>
		<xsl:if test="string-length($subset)>0">
			
			<xsl:value-of select="$subset"/>
			<xsl:text> :</xsl:text>
		</xsl:if>
		<xsl:for-each select="$srcOf/emsr:*[@classCode]">
			
			<xsl:call-template name="process-acts">
				<xsl:with-param name="act" select="current()"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:if test="(string-length($typeCode) &gt;0) and not($srcOf/emsr:act/emsr:sourceOf/emsr:conjunctionCode) and $srcOf//*[@displayInd='true']">
			<xsl:text>)</xsl:text>
		</xsl:if>
	</xsl:template>
	<!-- show data criterion name based upon root oid -->
	<xsl:template name="show-targetName">
		<xsl:param name="oid"/>
		<xsl:choose>
			<xsl:when test="count(/emsr:QualityMeasureDocument//emsr:component[emsr:section/emsr:code/@code='57025-9' and emsr:section//emsr:id/@root=$oid])>0">
				<xsl:for-each select="/emsr:QualityMeasureDocument//emsr:section[emsr:code/@code='57025-9']//emsr:id[@root=$oid]">
					<!-- got the Data Criteria entry/act/id where @root=$oid -->
					<xsl:if test="../emsr:id[@root=$oid] and (../emsr:code or ../emsr:title or ../emsr:sourceOf)">
						<xsl:choose>
							<!-- get the title descendant of the parent of id and title's code sibling -->
							<!-- commenting content here, unclear whether this is required -->
							<!-- 
							<xsl:when test="..//emsr:title and (string-length(..//emsr:title) &gt;0) and ..//emsr:title/../emsr:code/@displayName and (string-length(..//emsr:title/../emsr:code/@displayName)&gt;0)">
								<xsl:value-of select="..//emsr:title"/> using
								<xsl:text> "</xsl:text>
								<xsl:value-of select="..//emsr:title/../emsr:code/@displayName"/>
								<xsl:text>"</xsl:text>
							</xsl:when>
							 -->
							<!-- else get the title descendant of the parent of id -->
							<xsl:when test="..//emsr:title and (string-length(..//emsr:title) &gt;0)">
								<xsl:text> "</xsl:text>
								<xsl:value-of select="..//emsr:title"/>
								<xsl:text>"</xsl:text>
							</xsl:when>
							<!-- else get the code sibling of id -->
							<xsl:when test="../emsr:code/@displayName and (string-length(../emsr:code/@displayName)&gt;0)">
								<xsl:text> "</xsl:text>
								<xsl:value-of select="../emsr:code/@displayName"/>
								<xsl:text>"</xsl:text>
							</xsl:when>
							<!-- else invoke another template -->
							<xsl:when test="count(../emsr:sourceOf)&gt;1">
								<xsl:call-template name="process-acts">
									<xsl:with-param name="act" select="current()"/>
								</xsl:call-template>
							</xsl:when>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="count(/emsr:QualityMeasureDocument//emsr:subjectOf/emsr:measureAttribute/emsr:id[@root=$oid])>0">
				<xsl:for-each select="/emsr:QualityMeasureDocument//emsr:subjectOf/emsr:measureAttribute">
					<xsl:if test="emsr:id[@root=$oid] and (emsr:code or emsr:title or emsr:sourceOf)">
						<xsl:if test="emsr:code/@displayName and (string-length(emsr:code/@displayName)&gt;0)">
								<xsl:text> "</xsl:text>
								<xsl:value-of select="emsr:code/@displayName"/>
								<xsl:text>"</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show valueSet -->
	<xsl:template name="show-valueSet">
		<xsl:param name="valueset"/>
		<xsl:if test="$valueset/@displayName">
			<xsl:value-of select="$valueset/@displayName"/>
		</xsl:if>
		<xsl:if test="$valueset/@code">
			<xsl:text> (</xsl:text>
			<xsl:value-of select="$valueset/@code"/>
			<xsl:text>)</xsl:text>
		</xsl:if>
	</xsl:template>
	<!-- show code: code is a node -->
	<xsl:template name="show-code">
		<xsl:param name="code"/>
		<xsl:variable name="this-codeSystem">
			<xsl:value-of select="$code/@codeSystem"/>
		</xsl:variable>
		<xsl:variable name="this-code">
			<xsl:value-of select="$code/@code"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$code/emsr:originalText">
				<xsl:value-of select="$code/emsr:originalText"/>
			</xsl:when>
			<xsl:when test="$code/@displayName">
				<xsl:value-of select="$code/@displayName"/>
			</xsl:when>
			<xsl:when test="$the-valuesets/MATNarrVoc/code[@code=$this-code]/@displayName">
				<xsl:value-of select="$the-valuesets/MATNarrVoc/code[@code=$this-code]/@displayName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$this-code"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show code: code is a value -->
	<xsl:template name="show-code-value">
		<xsl:param name="code"/>
		<xsl:choose>
			<xsl:when test="$the-valuesets/MATNarrVoc/code[@code=$code]/@displayName">
				<xsl:value-of select="$the-valuesets/MATNarrVoc/code[@code=$code]/@displayName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$code"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show-sequence -->
	<xsl:template name="show-sequence">
		<xsl:param name="value"/>
		<xsl:choose>
			<xsl:when test="$value=1">
				<xsl:text>IMMEDIATE PRIOR</xsl:text>
			</xsl:when>
			<xsl:when test="$value=2">
				<xsl:text>CURRENT</xsl:text>
			</xsl:when>
			<xsl:when test="$value=3">
				<xsl:text>RELATIVE THIRD</xsl:text>
			</xsl:when>
			<xsl:when test="$value=4">
				<xsl:text>RELATIVE FOURTH</xsl:text>
			</xsl:when>
			<xsl:when test="$value=5">
				<xsl:text>RELATIVE FIFTH</xsl:text>
			</xsl:when>
			<xsl:when test="$value=6">
				<xsl:text>RELATIVE SIXTH</xsl:text>
			</xsl:when>
			<xsl:when test="$value=7">
				<xsl:text>RELATIVE SEVENTH</xsl:text>
			</xsl:when>
			<xsl:when test="$value=8">
				<xsl:text>RELATIVE EIGHTH</xsl:text>
			</xsl:when>
			<xsl:when test="$value=9">
				<xsl:text>RELATIVE NINTH</xsl:text>
			</xsl:when>
			<xsl:when test="$value=10">
				<xsl:text>RELATIVE TENTH</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>RELATIVE #</xsl:text><xsl:value-of select="$value"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show-value -->
	<xsl:template name="show-value">
		<xsl:param name="value"/>
		<xsl:if test="$value/emsr:high or $value/emsr:low or $value[@value]">
			<xsl:choose>
				<xsl:when test="$value/@xsi:type='CD'">
					<xsl:variable name="dispValue">
						<xsl:call-template name="show-code">
							<xsl:with-param name="code" select="$value"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:variable name="nf">
						<xsl:call-template name="show-noneFlavor">
							<xsl:with-param name="nf" select="$value/@nullFlavor"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="string-length($dispValue)&gt;0">
							<xsl:value-of select="$dispValue"/>
						</xsl:when>
						<xsl:when test="string-length($nf)&gt;0">
							<xsl:value-of select="$nf"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="$value/@xsi:type='PQ'">
					<xsl:variable name="dispUnit">
						<xsl:call-template name="show-code-value">
							<xsl:with-param name="code" select="$value/@unit"/>
						</xsl:call-template>
						<xsl:call-template name="get_unitSuffix">
							<xsl:with-param name="unit" select="$value/@unit"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:variable name="nf">
						<xsl:call-template name="show-noneFlavor">
							<xsl:with-param name="nf" select="$value/@nullFlavor"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:if test="$value/@value">
						<xsl:text> = </xsl:text>
						<xsl:value-of select="$value/@value"/>
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="string-length($dispUnit)&gt;0">
							<xsl:value-of select="$dispUnit"/>
						</xsl:when>
						<xsl:when test="string-length($nf)&gt;0">
							<xsl:value-of select="$nf"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="$value/@xsi:type='IVL_PQ' or $value/@xsi:type='URG_PQ'">
					<xsl:variable name="dispUnit">
						<xsl:choose>
							<xsl:when test="$value/emsr:low[@unit]">
								<xsl:call-template name="show-code-value">
									<xsl:with-param name="code" select="$value/emsr:*/@unit"/>
								</xsl:call-template>
								<xsl:call-template name="get_unitSuffix">
									<xsl:with-param name="unit" select="$value/emsr:low/@unit"/>
								</xsl:call-template>
								<xsl:for-each select="$value/emsr:low/emsr:translation">
									<xsl:text>, or </xsl:text>
									<xsl:value-of select="@displayName"/>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="$value/emsr:high[@unit]">
									<xsl:call-template name="show-code-value">
										<xsl:with-param name="code" select="$value/emsr:*/@unit"/>
									</xsl:call-template>
									<xsl:call-template name="get_unitSuffix">
										<xsl:with-param name="unit" select="$value/emsr:high/@unit"/>
									</xsl:call-template>
								</xsl:if>
								<xsl:for-each select="$value/emsr:high/emsr:translation">
									<xsl:text>, or </xsl:text>
									<xsl:value-of select="@displayName"/>
								</xsl:for-each>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="lowVal">
						<xsl:if test="$value/emsr:low/@value">
							<xsl:text> &gt;</xsl:text>
							<xsl:choose>
								<xsl:when test="$value/emsr:low/@inclusive='true'">
									<xsl:text>= </xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text> </xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="$value/emsr:low/@value"/>
						</xsl:if>
					</xsl:variable>
					<xsl:variable name="highVal">
						<xsl:if test="$value/emsr:high/@value">
							<xsl:text> &lt;</xsl:text>
							<xsl:choose>
								<xsl:when test="$value/emsr:high/@inclusive='true'">
									<xsl:text>= </xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text> </xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="$value/emsr:high/@value"/>
						</xsl:if>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="(string-length($lowVal)&gt;0) and (string-length($highVal)&lt;1)">
							<xsl:value-of select="$lowVal"/>
							<xsl:text> </xsl:text>
							<xsl:value-of select="$dispUnit"/>
						</xsl:when>
						<xsl:when test="(string-length($lowVal)&gt;0) and (string-length($highVal)&gt;0)">
							<xsl:value-of select="$lowVal"/>
							<xsl:text> </xsl:text>
							<xsl:value-of select="$dispUnit"/>
							<xsl:text> and</xsl:text>
							<xsl:value-of select="$highVal"/>
							<xsl:text> </xsl:text>
							<xsl:value-of select="$dispUnit"/>
						</xsl:when>
						<xsl:when test="(string-length($lowVal)&lt;1) and (string-length($highVal)&gt;0)">
							<xsl:value-of select="$highVal"/>
							<xsl:text> </xsl:text>
							<xsl:value-of select="$dispUnit"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="$value/@xsi:type='TS'">
					<xsl:variable name="timeStamp">
						<xsl:choose>
							<xsl:when test="$value/@nullFlavor">
								<xsl:call-template name="show-noneFlavor">
									<xsl:with-param name="nf" select="$value/@nullFlavor"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="show-time">
									<xsl:with-param name="datetime" select="$value"/>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:if test="string-length($timeStamp)>0">
						<xsl:value-of select="$timeStamp"/>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
		</xsl:if>
		<xsl:if test="$value/emsr:width">
			
			<xsl:text> = </xsl:text>
			<xsl:value-of select="$value/emsr:width/@value"/>
			<xsl:text> </xsl:text>
			<xsl:call-template name="show-code-value">
				<xsl:with-param name="code" select="$value/emsr:*/@unit"/>
			</xsl:call-template>
			<xsl:call-template name="get_unitSuffix">
				<xsl:with-param name="unit" select="$value/emsr:*/@unit"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!--	show-id	-->
	<xsl:template name="show-id">
		<xsl:param name="id"/>
		<xsl:choose>
			<xsl:when test="not($id)">
				<xsl:if test="not(@nullFlavor)">
					<xsl:if test="@extension">
						<xsl:value-of select="@extension"/>
					</xsl:if>
					<xsl:text> </xsl:text>
					<xsl:value-of select="@root"/>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="not($id/@nullFlavor)">
					<xsl:if test="$id/@extension">
						<xsl:value-of select="$id/@extension"/>
					</xsl:if>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$id/@root"/>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- show element full path -->
	<xsl:template name="show-fullpath">
		<xsl:for-each select="(ancestor-or-self::*)">
			<xsl:value-of select="name()"/>
			<xsl:text>[</xsl:text>
			<xsl:value-of select="1+count(preceding-sibling::*)"/>
			<xsl:text>]/</xsl:text>
		</xsl:for-each>
	</xsl:template>
	<!-- show time -->
	<xsl:template name="show-time">
		<xsl:param name="datetime"/>
		<xsl:choose>
			<xsl:when test="not($datetime)">
				<xsl:call-template name="formatDateTime">
					<xsl:with-param name="date" select="@value"/>
				</xsl:call-template>
				<xsl:text> </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="formatDateTime">
					<xsl:with-param name="date" select="$datetime/@value"/>
				</xsl:call-template>
				<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- format date time -->
	<xsl:template name="formatDateTime">
		<xsl:param name="date"/>
		<!-- month -->
		<xsl:variable name="month" select="substring ($date, 5, 2)"/>
		<xsl:choose>
			<xsl:when test="$month='01'">
				<xsl:text>January </xsl:text>
			</xsl:when>
			<xsl:when test="$month='02'">
				<xsl:text>February </xsl:text>
			</xsl:when>
			<xsl:when test="$month='03'">
				<xsl:text>March </xsl:text>
			</xsl:when>
			<xsl:when test="$month='04'">
				<xsl:text>April </xsl:text>
			</xsl:when>
			<xsl:when test="$month='05'">
				<xsl:text>May </xsl:text>
			</xsl:when>
			<xsl:when test="$month='06'">
				<xsl:text>June </xsl:text>
			</xsl:when>
			<xsl:when test="$month='07'">
				<xsl:text>July </xsl:text>
			</xsl:when>
			<xsl:when test="$month='08'">
				<xsl:text>August </xsl:text>
			</xsl:when>
			<xsl:when test="$month='09'">
				<xsl:text>September </xsl:text>
			</xsl:when>
			<xsl:when test="$month='10'">
				<xsl:text>October </xsl:text>
			</xsl:when>
			<xsl:when test="$month='11'">
				<xsl:text>November </xsl:text>
			</xsl:when>
			<xsl:when test="$month='12'">
				<xsl:text>December </xsl:text>
			</xsl:when>
		</xsl:choose>
		<!-- day -->
		<xsl:choose>
			<xsl:when test='substring ($date, 7, 1)="0"'>
				<xsl:value-of select="substring ($date, 8, 1)"/>
				<xsl:text>, </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="substring ($date, 7, 2)"/>
				<xsl:text>, </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<!-- year -->
		<xsl:choose>
			<xsl:when test="substring ($date, 1, 4)='0000'">
				<xsl:text>20xx</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="substring ($date, 1, 4)"/>
			</xsl:otherwise>
		</xsl:choose>
		<!-- time and US timezone -->
		<xsl:if test="string-length($date) > 8">
			<xsl:text>, </xsl:text>
			<!-- time -->
			<xsl:variable name="time">
				<xsl:value-of select="substring($date,9,6)"/>
			</xsl:variable>
			<xsl:variable name="hh">
				<xsl:value-of select="substring($time,1,2)"/>
			</xsl:variable>
			<xsl:variable name="mm">
				<xsl:value-of select="substring($time,3,2)"/>
			</xsl:variable>
			<xsl:variable name="ss">
				<xsl:value-of select="substring($time,5,2)"/>
			</xsl:variable>
			<xsl:if test="string-length($hh)&gt;1">
				<xsl:value-of select="$hh"/>
				<xsl:if test="string-length($mm)&gt;1 and not(contains($mm,'-')) and not (contains($mm,'+'))">
					<xsl:text>:</xsl:text>
					<xsl:value-of select="$mm"/>
					<xsl:if test="string-length($ss)&gt;1 and not(contains($ss,'-')) and not (contains($ss,'+'))">
						<xsl:text>:</xsl:text>
						<xsl:value-of select="$ss"/>
					</xsl:if>
				</xsl:if>
			</xsl:if>
			<!-- time zone -->
			<xsl:variable name="tzon">
				<xsl:choose>
					<xsl:when test="contains($date,'+')">
						<xsl:text>+</xsl:text>
						<xsl:value-of select="substring-after($date, '+')"/>
					</xsl:when>
					<xsl:when test="contains($date,'-')">
						<xsl:text>-</xsl:text>
						<xsl:value-of select="substring-after($date, '-')"/>
					</xsl:when>
				</xsl:choose>
			</xsl:variable>
			<xsl:choose>
				<!-- reference: http://www.timeanddate.com/library/abbreviations/timezones/na/ -->
				<xsl:when test="$tzon = '-0500' ">
					<xsl:text>, EST</xsl:text>
				</xsl:when>
				<xsl:when test="$tzon = '-0600' ">
					<xsl:text>, CST</xsl:text>
				</xsl:when>
				<xsl:when test="$tzon = '-0700' ">
					<xsl:text>, MST</xsl:text>
				</xsl:when>
				<xsl:when test="$tzon = '-0800' ">
					<xsl:text>, PST</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> </xsl:text>
					<xsl:value-of select="$tzon"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- show-noneFlavor -->
	<xsl:template name="show-noneFlavor">
		<xsl:param name="nf"/>
		<xsl:choose>
			<xsl:when test=" $nf = 'NI' ">
				<xsl:text>no information</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'INV' ">
				<xsl:text>invalid</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'MSK' ">
				<xsl:text>masked</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'NA' ">
				<xsl:text>not applicable</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'UNK' ">
				<xsl:text>unknown</xsl:text>
			</xsl:when>
			<xsl:when test=" $nf = 'OTH' ">
				<xsl:text>other</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- show data criterion -->
	<xsl:template name="show-criterion">
		<xsl:param name="the_title"/>
		<xsl:param name="the_text"/>
		<content>
			<xsl:call-template name="firstCharCaseUp">
				<xsl:with-param name="data" select="normalize-space($the_title)"/>
			</xsl:call-template>
		</content>
		<xsl:if test="string-length($the_text)&gt;1">
			<xsl:text> </xsl:text>
		</xsl:if>
		
		<xsl:value-of select="normalize-space($the_text)"/>
	</xsl:template>
	<!-- convert to lower case -->
	<xsl:template name="caseDown">
		<xsl:param name="data"/>
		<xsl:if test="$data">
			<xsl:value-of select="translate($data, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
		</xsl:if>
	</xsl:template>
	<!-- convert to upper case -->
	<xsl:template name="caseUp">
		<xsl:param name="data"/>
		<xsl:if test="$data">
			<xsl:value-of select="translate($data,'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
		</xsl:if>
	</xsl:template>
	<!-- convert first character to upper case -->
	<xsl:template name="firstCharCaseUp">
		<xsl:param name="data"/>
		<xsl:if test="$data">
			<xsl:call-template name="caseUp">
				<xsl:with-param name="data" select="substring($data,1,1)"/>
			</xsl:call-template>
			<xsl:value-of select="substring($data,2)"/>
		</xsl:if>
	</xsl:template>
	<!-- get pattern related title prefix -->
	<xsl:template name="get_titlePrefix">
		<xsl:param name="templateRootId"/>
		<xsl:if test="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]">
			<xsl:if test="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]/titlePrefix">
				<xsl:value-of select="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]/titlePrefix"/>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<!-- get pattern related title suffix -->
	<xsl:template name="get_titleSuffix">
		<xsl:param name="templateRootId"/>
		<xsl:for-each select="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]">
			<xsl:if test="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]/titleSuffix">
				<xsl:value-of select="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]/titleSuffix"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- get pattern related text prefix -->
	<xsl:template name="get_textPrefix">
		<xsl:param name="templateRootId"/>
		<xsl:if test="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]">
			<xsl:if test="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]/textPrefix">
				<xsl:value-of select="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]/textPrefix"/>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<!-- get pattern related title suffix -->
	<xsl:template name="get_textSuffix">
		<xsl:param name="templateRootId"/>
		<xsl:if test="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]">
			<xsl:if test="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]/textSuffix">
				<xsl:value-of select="$the-valuesets/MATNarrVoc//patternNarr[associatedTemplateId/@templateId=$templateRootId]/textSuffix"/>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<xsl:template name="get_unitSuffix">
		<xsl:param name="unit"/>
		<xsl:if test="$unit='a' or  $unit='mo' or  $unit='min' or $unit='d' or $unit='wk' or $unit='h' or $unit='[qtr]' or $unit='s'">
			<xsl:text>(s)</xsl:text>
		</xsl:if>
	</xsl:template>
</xsl:transform>