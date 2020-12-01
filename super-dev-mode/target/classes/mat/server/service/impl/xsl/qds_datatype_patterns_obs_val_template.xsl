<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
	<!-- deprecate start QDM 2.1.1.1 -->
	
	<xsl:template match="attribute" mode="obs_val_new">
		<xsl:variable name="qid">
			<xsl:value-of select="../@id"/>
		</xsl:variable>
		<xsl:variable name="qcode">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdm[@id=$qid]/@oid"/>
		</xsl:variable>
		<xsl:variable name="qdisplayName">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdm[@id=$qid]/@name"/>
		</xsl:variable>
		<xsl:variable name="pdisplayName">
			<xsl:value-of select="@name"/>
		</xsl:variable>
		<!-- <xsl:variable name="qcodeSystem">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdsel[@id=$qid]/@codeSystem"/>
		</xsl:variable> -->
		<!-- <xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable> -->
		<xsl:variable name="pcode">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME='$pdisplayName']/CODE" />
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
		
		<xsl:choose>
		
			<!-- TODO: filter content with a propel/@mode attribute set -->
			<xsl:when test="string-length($pmode)>0">
				<xsl:choose>
					<!-- handle: {hospital location, start datetime, stop datetime} -->
					 
					<xsl:when test="@name='hospital location id'">
						<participant typeCode="LOC">
							<xsl:if test="@negationInd">
								<xsl:attribute name="negationInd">true</xsl:attribute>
							</xsl:if>
							<roleParticipant classCode="SDLOC">
								<xsl:choose>
									<xsl:when test="string-length($poid)>0">
									<!-- Value Set -->
										<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pname}"/>
										<!-- TODO: handle case where arrival datetime property is being asserted 
											1) property 'arrival datetime' exists
											2) add roleParticipant/effectiveTime/@low -->
									</xsl:when>
									<xsl:when test="contains($pname, 'is present')">
										<!-- is present -->
										<code/>
									</xsl:when>
									<xsl:when test="contains($pname, 'is not present')">
										<!-- is not present -->
										<!-- TODO: not currently handled -->
									</xsl:when>
									<xsl:otherwise>
										<code/>
									</xsl:otherwise>
								</xsl:choose>
							</roleParticipant>
						</participant>
					</xsl:when>
					
					<xsl:when test="$pmode='P'">
						<xsl:variable name="attribute-tid-root">
							<xsl:value-of select="$the_tidrootMapping/PatternMapping/attribute[@name=$pdatatype]/@root"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$pdatatype='Source - Device'">
								<participant typeCode="DEV">
							       <templateId root="{$attribute-tid-root}"/>
							       <roleParticipant classCode="MANU">
							         <playingDevice classCode="DEV" determinerCode="KIND">
							          <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/> 
							         </playingDevice>
							        </roleParticipant>
								</participant>
							</xsl:when>
							<xsl:when test="$pdatatype='Source - Informant'">
								<participant typeCode="INF">
								    <templateId root="{$attribute-tid-root}"/>
								    <roleParticipant classCode="ROL">
								      <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
								    </roleParticipant>
								</participant>
							</xsl:when>
							<xsl:when test="$pdatatype='Recorder - Informant'">
							  <participant typeCode="AUT">
							    <templateId root="{$attribute-tid-root}"/>
							    <roleParticipant classCode="ROL">
							      <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
							    </roleParticipant>
							  </participant>
							</xsl:when>
							<xsl:when test="$pdatatype='Recorder - Device'">
							  <participant typeCode="AUT">
							    <templateId root="{$attribute-tid-root}"/>
							    <roleParticipant classCode="MANU">
							      <playingDevice classCode="DEV" determinerCode="KIND">
							          <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
							      </playingDevice>
							    </roleParticipant>
							  </participant>
							</xsl:when>
							<xsl:when test="$pdatatype='Setting'">
							  <participant typeCode="LOC">
							    <templateId root="{$attribute-tid-root}"/>
							    <roleParticipant classCode="SDLOC"> 
							      <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
							    </roleParticipant>
							  </participant>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			
			<xsl:when test="@name='location_hospital id'">
				<participant typeCode="LOC">
					<xsl:if test="@negationInd">
						<xsl:attribute name="negationInd">true</xsl:attribute>
					</xsl:if>
					<roleParticipant classCode="SDLOC">
						<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pname}"/>
						<!-- <xsl:if test="../property[@name='arrival']"> -->
						<xsl:if test="../attribute[@name='arrival']">
							<effectiveTime xsi:type="IVL_TS">
								<low />
							</effectiveTime>
						</xsl:if>
					</roleParticipant>
				</participant>
			</xsl:when>
			
			<xsl:when test="@name='residence id' or @name='care type id'">
				<value xsi:type="CD" code="{$poid}"   displayName="{$pname}"/>
			</xsl:when>
			<xsl:when test="@name='source id'">
				<participant typeCode="INF">
					<!-- quality data attribute "source" = informant pattern -->
					<templateId root="2.16.840.1.113883.3.560.1.101"/>
					<roleParticipant classCode="ROL">
						<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pname}"/>
					</roleParticipant>
				</participant>
			</xsl:when>
			
			<xsl:when test="@name='route' or @name='route id'">
				<routeCode code="{$poid}"  displayName="{$pname}"/>
			</xsl:when>
			<xsl:when test="@name='arrival' and not(../attribute[@name='location_hospital id'])">
				<participant typeCode="LOC">
					<xsl:if test="@negationInd">
						<xsl:attribute name="negationInd">true</xsl:attribute>
					</xsl:if>
					<roleParticipant classCode="SDLOC">
						<effectiveTime xsi:type="IVL_TS">
							<low />
						</effectiveTime>
					</roleParticipant>
				</participant>
			</xsl:when>
			
			<xsl:when test="@name='discharge status' or @name='dischargestatus' or @name='discharge status id'">
				<dischargeDispositionCode code="{$poid}"  displayName="{$pname}"/>
			</xsl:when>
			
		</xsl:choose>
	</xsl:template>
	
	
	<xsl:template match="property" mode="obs_val">
		<xsl:variable name="qid">
			<xsl:value-of select="../../@id"/>
		</xsl:variable>
		<xsl:variable name="qcode">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdsel[@id=$qid]/@oid"/>
		</xsl:variable>
		<xsl:variable name="qdisplayName">
			<xsl:value-of select="ancestor::measure//elementLookUp/qdsel[@id=$qid]/@name"/>
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
		
		<xsl:choose>
		
			<!-- TODO: filter content with a propel/@mode attribute set -->
			<xsl:when test="string-length($pmode)>0">
				<xsl:choose>
					<!-- handle: {hospital location, start datetime, stop datetime} -->
					 
					<xsl:when test="@name='hospital location id'">
						<participant typeCode="LOC">
							<xsl:if test="@negationInd">
								<xsl:attribute name="negationInd">true</xsl:attribute>
							</xsl:if>
							<roleParticipant classCode="SDLOC">
								<xsl:choose>
									<xsl:when test="string-length($poid)>0">
									<!-- Value Set -->
										<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pname}"/>
										<!-- TODO: handle case where arrival datetime property is being asserted 
											1) property 'arrival datetime' exists
											2) add roleParticipant/effectiveTime/@low -->
									</xsl:when>
									<xsl:when test="contains($pname, 'is present')">
										<!-- is present -->
										<code/>
									</xsl:when>
									<xsl:when test="contains($pname, 'is not present')">
										<!-- is not present -->
										<!-- TODO: not currently handled -->
									</xsl:when>
									<xsl:otherwise>
										<code/>
									</xsl:otherwise>
								</xsl:choose>
							</roleParticipant>
						</participant>
					</xsl:when>
					
					<xsl:when test="$pmode='P'">
						<xsl:variable name="attribute-tid-root">
							<xsl:value-of select="$the_tidrootMapping/PatternMapping/attribute[@name=$pdatatype]/@root"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$pdatatype='Source - Device'">
								<participant typeCode="DEV">
							       <templateId root="{$attribute-tid-root}"/>
							       <roleParticipant classCode="MANU">
							         <playingDevice classCode="DEV" determinerCode="KIND">
							          <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/> 
							         </playingDevice>
							        </roleParticipant>
								</participant>
							</xsl:when>
							<xsl:when test="$pdatatype='Source - Informant'">
								<participant typeCode="INF">
								    <templateId root="{$attribute-tid-root}"/>
								    <roleParticipant classCode="ROL">
								      <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
								    </roleParticipant>
								</participant>
							</xsl:when>
							<xsl:when test="$pdatatype='Recorder - Informant'">
							  <participant typeCode="AUT">
							    <templateId root="{$attribute-tid-root}"/>
							    <roleParticipant classCode="ROL">
							      <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
							    </roleParticipant>
							  </participant>
							</xsl:when>
							<xsl:when test="$pdatatype='Recorder - Device'">
							  <participant typeCode="AUT">
							    <templateId root="{$attribute-tid-root}"/>
							    <roleParticipant classCode="MANU">
							      <playingDevice classCode="DEV" determinerCode="KIND">
							          <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
							      </playingDevice>
							    </roleParticipant>
							  </participant>
							</xsl:when>
							<xsl:when test="$pdatatype='Setting'">
							  <participant typeCode="LOC">
							    <templateId root="{$attribute-tid-root}"/>
							    <roleParticipant classCode="SDLOC"> 
							      <code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pdisplayName}"/>
							    </roleParticipant>
							  </participant>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			
			<xsl:when test="@name='location_hospital id'">
				<participant typeCode="LOC">
					<xsl:if test="@negationInd">
						<xsl:attribute name="negationInd">true</xsl:attribute>
					</xsl:if>
					<roleParticipant classCode="SDLOC">
						<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pname}"/>
						<xsl:if test="../property[@name='arrival']">
							<effectiveTime xsi:type="IVL_TS">
								<low />
							</effectiveTime>
						</xsl:if>
					</roleParticipant>
				</participant>
			</xsl:when>
			
			<xsl:when test="@name='residence id' or @name='care type id'">
				<value xsi:type="CD" code="{$poid}"   displayName="{$pname}"/>
			</xsl:when>
			<xsl:when test="@name='source id'">
				<participant typeCode="INF">
					<!-- quality data attribute "source" = informant pattern -->
					<templateId root="2.16.840.1.113883.3.560.1.101"/>
					<roleParticipant classCode="ROL">
						<code code="{$poid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$pname}"/>
					</roleParticipant>
				</participant>
			</xsl:when>
			
			<xsl:when test="@name='route' or @name='route id'">
				<routeCode code="{$poid}"  displayName="{$pname}"/>
			</xsl:when>
			<xsl:when test="@name='arrival' and not(../property[@name='location_hospital id'])">
				<participant typeCode="LOC">
					<xsl:if test="@negationInd">
						<xsl:attribute name="negationInd">true</xsl:attribute>
					</xsl:if>
					<roleParticipant classCode="SDLOC">
						<effectiveTime xsi:type="IVL_TS">
							<low />
						</effectiveTime>
					</roleParticipant>
				</participant>
			</xsl:when>
			
			<xsl:when test="@name='discharge status' or @name='dischargestatus' or @name='discharge status id'">
				<dischargeDispositionCode code="{$poid}"  displayName="{$pname}"/>
			</xsl:when>
			
		</xsl:choose>
	</xsl:template>
	<!-- deprecate end -->
</xsl:stylesheet>