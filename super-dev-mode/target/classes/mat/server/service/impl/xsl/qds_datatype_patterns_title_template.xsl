<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
	
	<xsl:template match="*" name="title">
		<xsl:param name="is_to"/>
		<xsl:param name="ref-id"/>
		
		<xsl:variable name="refid">
			<xsl:choose>
				<xsl:when test="string-length($ref-id) > 0">
					<xsl:value-of select="$ref-id"/>
				</xsl:when>
				<xsl:otherwise>NA</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
			
		
		<!-- <xsl:variable name="refid">
	      	<xsl:choose>
		      	<xsl:when test="string-length($is_to)=0 and name(..)='reference' and ../../@id"><xsl:value-of select="current()/@id"/></xsl:when>
		      	<xsl:otherwise>NA</xsl:otherwise>
	      	</xsl:choose>
		</xsl:variable> -->

		<xsl:variable name="iid">
			<xsl:choose>
				<xsl:when test="string-length($is_to)>0">
					<xsl:value-of select="$is_to"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@id"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>		

		<xsl:variable name="qdsid">
			<xsl:choose>
				<xsl:when test="string-length($is_to)>0">
					<xsl:value-of select="$is_to"/>
				</xsl:when>
				<xsl:when test="name(..)='reference' and ../../@id"><xsl:value-of select="../../@id"/></xsl:when>
				<xsl:when test="name(/measure/elementLookUp/*[@id=$iid])='iqdsel'">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$iid]/@refid"/>
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="current()/@id"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="qdsuuid">
			<xsl:choose>
				<xsl:when test="name(/measure/elementLookUp/*[@id=$iid])='iqdsel'">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$iid]/@uuid"/>
				</xsl:when>
				<!--<xsl:when test="name(..)='supplementalDataElements'">
					<xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@uuid"/>
				</xsl:when>-->
				<xsl:otherwise>
					<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@uuid"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="qdsdatatype">
			<!--<xsl:choose>
				<xsl:when test="name(..)='supplementalDataElements'">
					<xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@datatype"/>
				</xsl:when>
				<xsl:otherwise>-->
				<xsl:variable name="tmp">
					<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@datatype"/>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="name() = 'elementRef' and /measure/elementLookUp/*[@id=$qdsid][@instance]">
						<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@instance"/> of <xsl:value-of select="$tmp"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$tmp"/>
					</xsl:otherwise>
				</xsl:choose>
			<!--</xsl:otherwise>
			</xsl:choose>-->
		</xsl:variable>
		
		<xsl:variable name="qdsname">
			<!--<xsl:choose>
				<xsl:when test="name(..)='supplementalDataElements'">
					<xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@name"/>
				</xsl:when>
				<xsl:otherwise>-->
					<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@name"/>
				<!--</xsl:otherwise>
			</xsl:choose>-->
		</xsl:variable>
		
		<xsl:variable name="refname">
			<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@name"/>
		</xsl:variable>
		
		<xsl:variable name="arit_text">
			<xsl:apply-templates select="." mode="inline_arit_text"/>
		</xsl:variable>

		<xsl:choose>
		
			<xsl:when test="lower-case($qdsname)='measurement enddate' or lower-case($qdsname)='measurement end date' or lower-case($qdsname)='measurement period' or lower-case($qdsname)='measurement start date' or lower-case($qdsname)='measurement startdate'">
            	<xsl:value-of select="$qdsname"/>
            </xsl:when>
			<xsl:when test="ancestor::measure//elementLookUp/measurecalc[@uuid=$qdsuuid]">
				
				<!--  -->
				<xsl:variable name="mrefid">
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@refid"/>
				</xsl:variable>
				<xsl:variable name="mrefdatatype">
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$refid]/@datatype"/>
				</xsl:variable>
				<xsl:variable name="mrefname">
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$refid]/@name"/>
				</xsl:variable>
				<xsl:variable name="calcname">
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@name"/>
				</xsl:variable>
				<!-- evalue = measurecalc/@value -->
				<xsl:variable name="pvalue">
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@value"/>
				</xsl:variable>
				<xsl:variable name="comparison">
					<xsl:choose>
						<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/high">
							<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/high" mode="high_text"/>
						</xsl:when>
						<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/low">
							<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/low" mode="low_text"/>
						</xsl:when>
						<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/equal">
							<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/equal" mode="equal_text"/>
						</xsl:when>
						<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@highnum]">
							<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_high_text"/>
						</xsl:when>
						<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@lownum]">
							<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_low_text"/>
						</xsl:when>
						<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@equalnum]">
							<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_equal_text"/>
						</xsl:when>
						<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue]">
							<xsl:text> </xsl:text>
							<xsl:choose>
								<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue][lower-case(@name)='is present' or lower-case(@name)='is not present']">
									<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
								</xsl:when>
								<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue][string-length(@oid)>0]">
									<xsl:text>= </xsl:text>
									<xsl:call-template name="elTitle">
							      		<xsl:with-param name="datatype" select="/measure/elementLookUp/*[@id=$pvalue]/@datatype"/>
							            <xsl:with-param name="name" select="/measure/elementLookUp/*[@id=$pvalue]/@name"/>
							         </xsl:call-template>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:variable>
				<!--  -->
				<xsl:choose>
					<xsl:when test="string-length($mrefid)>0">
						<xsl:call-template name="elTitle">
							<xsl:with-param name="datatype" select="$mrefdatatype"/>
							<xsl:with-param name="name" select="$mrefname"/>
						</xsl:call-template>
						<xsl:text> (</xsl:text>
						<xsl:value-of select="$calcname"/>
						<xsl:value-of select="$comparison"/>
						<xsl:text>)</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$calcname"/>
						<xsl:value-of select="$comparison"/>
						<xsl:text> of:</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:if test="@md">
					<xsl:value-of select="@md"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				
				<xsl:choose>
					<xsl:when test="name(/measure/elementLookUp/*[@id=$iid])='iqdsel'">
						<xsl:call-template name="elTitle">
							<xsl:with-param name="datatype" select="/measure/elementLookUp/*[@id=$iid]/@datatype"/>
							<xsl:with-param name="name" select="/measure/elementLookUp/*[@id=$iid]/@name"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
					   	  <xsl:if test="$refid='NA'">
					   	  		<!-- Take care of the 'negation rationale' attribute -->
								<xsl:choose>
									<xsl:when test="attribute[@name='negation rationale']">
										<title>
											<xsl:call-template name="capitalize">
							      				<xsl:with-param name="textString" select="$qdsdatatype"/>
							      			</xsl:call-template>
											<xsl:for-each select="attribute[lower-case(@name)='negation rationale']">
												<xsl:text> not done: </xsl:text>
												<xsl:if test="@qdmUUID">
													<xsl:variable name="qdmUUID"><xsl:value-of select="@qdmUUID"/></xsl:variable>
													<xsl:value-of select="ancestor::measure/elementLookUp/qdm[@id=$qdmUUID]/@name"/>
												</xsl:if>
											</xsl:for-each>
										</title>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="elTitle">
											<xsl:with-param name="datatype" select="$qdsdatatype"/>
											<xsl:with-param name="name" select="$qdsname"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
						 </xsl:if>
					     <xsl:if test="$refid!='NA'">
					      	<title>
					      		<xsl:call-template name="capitalize">
					      			<xsl:with-param name="textString" select="$qdsdatatype"/>
					      		</xsl:call-template>
					       		<xsl:text> not done</xsl:text>
					      		<xsl:text>: </xsl:text>
					      		<xsl:value-of select="$refname"/>
					      	</title>
					     </xsl:if>
		     		</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="string-length($is_to)=0">
					<xsl:if test="attribute">
						<xsl:choose>
							<xsl:when test="count(attribute) = 1 and child::attribute[1]/lower-case(@name) = 'negation rationale' 
											and child::attribute[1]/@qdmUUID">
							<!-- Do not do anything. This condition is already handled above. -->
							</xsl:when>
							<xsl:otherwise>
								<xsl:text> (</xsl:text>
								<xsl:for-each select="attribute">
									<xsl:choose>
										<xsl:when test="lower-case(@name) = 'negation rationale' and @qdmUUID">
										<!-- Skip it. Do not do anything. -->
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="count(preceding-sibling::attribute) > 0">
												<xsl:choose>
													<xsl:when test="count(preceding-sibling::attribute) = 1 and 
																	preceding-sibling::attribute[1][lower-case(@name)='negation rationale']">
														<xsl:if test="not(preceding-sibling::attribute[1]/@qdmUUID)">
															<xsl:text>, </xsl:text>
														</xsl:if>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>, </xsl:text>
													</xsl:otherwise>										
												</xsl:choose>
											</xsl:if>
											<xsl:value-of select="@name"/>
											<xsl:choose>
												<xsl:when test="@qdmUUID">
													<xsl:text>: '</xsl:text>
													<xsl:variable name="qdmUUID"><xsl:value-of select="@qdmUUID"/></xsl:variable>
													<xsl:value-of select="ancestor::measure/elementLookUp/qdm[@id=$qdmUUID]/@name"/>
													<xsl:text>'</xsl:text>
												</xsl:when>
												<xsl:otherwise>
													<xsl:apply-templates select="current()" mode="text_new"/>		
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<xsl:text>)</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="properties/property[not(@name='result outc')]">
						<xsl:text> (</xsl:text>
						<xsl:for-each select="properties/propert[not(@name='result conj') and not(@name='result outc')]">
							<xsl:choose>
								<xsl:when test="contains(@name, ' id') or @name='_id'">
									<xsl:variable name="pvalue">
										<xsl:value-of select="@value"/>
									</xsl:variable>
									<xsl:variable name="pname">
										
										<xsl:choose>
											<xsl:when test="string-length(ancestor::measure//elementLookUp/*[@id=$pvalue]/@displayName)>0">
												<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@displayName"/>
											</xsl:when>
											<xsl:when test="string-length(ancestor::measure//elementLookUp/*[@id=$pvalue]/@name)>0">
												<xsl:choose>
													<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name='result value'">
														<xsl:text>result</xsl:text>
													</xsl:when>
													<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name='is present'">
														<xsl:value-of select="substring-before('ancestor::measure//elementLookUp/*[@id=$pvalue]/@name', 'is present')"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="substring(@name, 1, string-length(@name) - 3)"/>				
											</xsl:otherwise>
										</xsl:choose>
									</xsl:variable>
									<xsl:variable name="qid">
										<xsl:value-of select="../@id"/>
									</xsl:variable>
									<xsl:variable name="pdatatype">
										<xsl:value-of select="ancestor::measure//elementLookUp/qdm[@id=$qid]/@datatype"/>
										<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@datatype"/> -->
									</xsl:variable>
									<xsl:value-of select="$pdatatype"/>
									<xsl:choose>
										<xsl:when test="$pname='is present' or $pname='is not present'">
											<xsl:text> </xsl:text>
											<xsl:value-of select="$pname"/>
										</xsl:when>
										<xsl:when test="string-length($pname)>0">
											<xsl:if test="string-length($pdatatype)>0">
												<xsl:text>: </xsl:text>
											</xsl:if>
											<xsl:text>'</xsl:text>
											<xsl:value-of select="$pname"/>
											<xsl:text>'</xsl:text>
										</xsl:when>
									</xsl:choose>
									<xsl:if test="@aritnum">
										<xsl:apply-templates select="." mode="inline_arit_text"/>
									</xsl:if>
									<xsl:choose>
										<xsl:when test="attribute">
											<xsl:apply-templates select="attributes" mode="text_new"></xsl:apply-templates>
										</xsl:when>
										<xsl:when test="high">
											<xsl:apply-templates select="high" mode="high_text"/>
										</xsl:when>
										<xsl:when test="low">
											<xsl:apply-templates select="low" mode="low_text"/>
										</xsl:when>
										<xsl:when test="equal">
											<xsl:apply-templates select="equal" mode="equal_text"/>
										</xsl:when>
									</xsl:choose>
									<xsl:if test="not(position()=last())">
						        		<xsl:text>, </xsl:text>
						        	</xsl:if>
					      		</xsl:when>
					      		<xsl:otherwise>
						        	<xsl:if test="@name">
						        		<xsl:choose>
						        			<xsl:when test="@displayName">
						        				<xsl:value-of select="@displayName"/>
						        			</xsl:when>
						        			<xsl:when test="@name='result value'">
						        				<xsl:text>result</xsl:text>
						        			</xsl:when>
						        			<xsl:otherwise>
						        				<xsl:value-of select="@name"/>
						        			</xsl:otherwise>
						        		</xsl:choose>
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
										</xsl:choose>
						        	</xsl:if>
					      			<xsl:if test="not(high) and not(low) and not(equal)">
							        	<xsl:if test="@name and (@value or @displayValue)">
							        		<xsl:text>: </xsl:text>
							        	</xsl:if>
							        	<xsl:if test="@value or @displayValue">
							        		<xsl:text>'</xsl:text>
							        		<xsl:if test="@negationInd='true'">
							        			<xsl:text>not </xsl:text>
							        		</xsl:if>
							        		<xsl:choose>
							        			<xsl:when test="@displayValue">
							        				<xsl:value-of select="@displayValue"/>
							        			</xsl:when>
							        			<xsl:otherwise>
							        				<xsl:value-of select="@value"/>
							        			</xsl:otherwise>
							        		</xsl:choose>
							        		<xsl:text>'</xsl:text>
							        	</xsl:if>
					      			</xsl:if>
						        	<xsl:if test="not(position()=last())">
						        		<xsl:text>, </xsl:text>
						        	</xsl:if>
					        	</xsl:otherwise>
				     		</xsl:choose>
						</xsl:for-each>
						<xsl:text>)</xsl:text>
					</xsl:if>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	   	<xsl:if test="string-length(normalize-space($arit_text))>0">
	   		<xsl:text> (</xsl:text>
	   		<xsl:value-of select="normalize-space($arit_text)"/>
	   		<xsl:text>)</xsl:text>
	   	</xsl:if>
	</xsl:template>
	
	
	<xsl:template name="der_ex_to_title">
		<xsl:param name="qdsuuid"/>
		
		<xsl:variable name="refid">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@refid"/>
		</xsl:variable>
		<xsl:variable name="refdatatype">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$refid]/@datatype"/>
		</xsl:variable>
		<xsl:variable name="refname">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$refid]/@name"/>
		</xsl:variable>
		<xsl:variable name="calcname">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@name"/>
		</xsl:variable>
		<!-- evalue = measurecalc/@value -->
		<xsl:variable name="pvalue">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@value"/>
		</xsl:variable>
		<xsl:variable name="comparison">
			<xsl:choose>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/high">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/high" mode="high_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/low">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/low" mode="low_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/equal">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/equal" mode="equal_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@highnum]">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_high_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@lownum]">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_low_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@equalnum]">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_equal_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue]">
					<xsl:text> </xsl:text>
					<xsl:choose>
						<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue][lower-case(@name)='is present' or lower-case(@name)='is not present']">
							<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
						</xsl:when>
						<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue][string-length(@oid)>0]">
							<xsl:text>= </xsl:text>
							<xsl:call-template name="elTitle">
					      		<xsl:with-param name="datatype" select="/measure/elementLookUp/*[@id=$pvalue]/@datatype"/>
					            <xsl:with-param name="name" select="/measure/elementLookUp/*[@id=$pvalue]/@name"/>
					         </xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string-length($refid)>0">
				<xsl:call-template name="elTitle">
					<xsl:with-param name="datatype" select="$refdatatype"/>
					<xsl:with-param name="name" select="$refname"/>
				</xsl:call-template>
				<xsl:text> (</xsl:text>
				<xsl:value-of select="$calcname"/>
				<xsl:value-of select="$comparison"/>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$calcname"/>
				<xsl:value-of select="$comparison"/>
				<xsl:text> of:</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>