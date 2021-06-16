<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:exsl="http://exslt.org/common" xmlns:uuid="java:java.util.UUID"
                xmlns:math="http://exslt.org/math" xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns="urn:hl7-org:v3"
                extension-element-prefixes="exsl uuid math xs" exclude-result-prefixes="exsl uuid math xs msxsl">
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    <xsl:preserve-space elements="content"/>

    <xsl:variable name="qdmVersionNumber">
        <xsl:value-of select="/measure/measureReleaseVersion/@releaseVersion"></xsl:value-of>
    </xsl:variable>

    <xsl:template match="/measure">

		<xsl:text>

         </xsl:text>
        <QualityMeasureDocument xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                xmlns="urn:hl7-org:v3" xmlns:cql-ext="urn:hhs-cql:hqmf-n1-extensions:v1">
            <xsl:apply-templates select="measureDetails"/>
        </QualityMeasureDocument>
    </xsl:template>

    <xsl:template match="measureDetails">
		<xsl:text>

      </xsl:text>
        <xsl:comment>
            **************************************************************
            Measure Details Section
            **************************************************************
        </xsl:comment>
        <xsl:text>

      </xsl:text>
        <typeId root="2.16.840.1.113883.1.3" extension="POQM_HD000001UV02"/>
        <templateId>
            <item root="2.16.840.1.113883.10.20.28.1.2" extension="2021-02-01"/>
        </templateId>
        <id root="{normalize-space(uuid)}"/>
        <code code="57024-2" codeSystem="2.16.840.1.113883.6.1">
            <displayName value="Health Quality Measure Document"/>
        </code>
        <!-- Title -->
        <title>
            <xsl:attribute name="value">
                <xsl:call-template name="trim">
                    <xsl:with-param name="textString"
                                    select="title"/>
                </xsl:call-template>
            </xsl:attribute>
        </title>
        <!-- Description -->
        <text>
            <xsl:attribute name="value">
                <xsl:call-template name="trim">
                    <xsl:with-param name="textString"
                                    select="description"/>
                </xsl:call-template>
            </xsl:attribute>
        </text>
        <!-- Status -->
        <xsl:variable name="status">
            <xsl:choose>
                <xsl:when test="lower-case(normalize-space(status))='in progress'">
                    <xsl:text>InProgress</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="normalize-space(status)"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <statusCode code="COMPLETED"/>
        <!-- GUID -->
        <xsl:variable name="set-id">
            <xsl:choose>
                <xsl:when test="string-length(normalize-space(guid))>0">
                    <xsl:value-of select="normalize-space(guid)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>pending</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <setId root="{$set-id}"/>
        <!-- Version -->
        <versionNumber value="{normalize-space(version)}"/>
        <!-- Measure Developer -->
        <xsl:choose>
            <xsl:when test="developers/developer">
                <xsl:for-each select="developers/developer">
                    <xsl:variable name="orgName">
                        <xsl:call-template name="trim">
                            <xsl:with-param name="textString" select="."/>
                        </xsl:call-template>
                    </xsl:variable>
                    <author>
                        <responsibleParty classCode="ASSIGNED">
                            <representedResponsibleOrganization
                                    classCode="ORG" determinerCode="INSTANCE">
                                <id>
                                    <item root="{@id}"/>
                                </id>
                                <name>
                                    <item>
                                        <part value="{$orgName}"/>
                                    </item>
                                </name>
                            </representedResponsibleOrganization>
                        </responsibleParty>
                    </author>
                </xsl:for-each>
            </xsl:when>
        </xsl:choose>
        <!-- Measure Steward -->
        <custodian>
            <responsibleParty classCode="ASSIGNED">
                <representedResponsibleOrganization
                        classCode="ORG" determinerCode="INSTANCE">
                    <id>
                        <item root="{steward/@id}"/>
                    </id>
                    <name>
                        <item>
                            <xsl:variable name="stewardName">
                                <xsl:call-template name="trim">
                                    <xsl:with-param name="textString" select="steward"/>
                                </xsl:call-template>
                            </xsl:variable>
                            <part value="{$stewardName}"/>
                        </item>
                    </name>
                </representedResponsibleOrganization>
            </responsibleParty>
        </custodian>
        <!-- Endorsement -->
        <xsl:if test="endorsement">
            <verifier>
                <responsibleParty classCode="ASSIGNED">
                    <representedResponsibleOrganization
                            classCode="ORG" determinerCode="INSTANCE">
                        <id>
                            <xsl:if test="endorsement/@id">
                                <item root="{endorsement/@id}"/>
                            </xsl:if>
                        </id>
                        <name>
                            <item>
                                <xsl:variable name="endorsemnt">
                                    <xsl:choose>
                                        <xsl:when test="string-length(.)>0">
                                            <xsl:call-template name="trim">
                                                <xsl:with-param name="textString" select="endorsement"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            None
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>
                                <part value="{$endorsemnt}"/>
                            </item>
                        </name>
                    </representedResponsibleOrganization>
                </responsibleParty>
            </verifier>
        </xsl:if>

        <xsl:for-each select="//elementLookUp/qdm[@code !='true']">

            <xsl:if test="not(preceding-sibling::qdm[@name = current()/@name])">
                <xsl:comment>
                    <xsl:value-of select="@name"/>
                </xsl:comment>
                <xsl:text>
     	 </xsl:text>
                <definition>
                    <valueSet classCode="OBS" moodCode="DEF">
                        <id root="{@oid}"/> <!-- {Value Set OID} -->
                        <title value="{@name}"/> <!-- {Value Set Name} -->
                        <xsl:if
                                test="@version != '1.0' and  @version != '1' and @version != ''">
                            <cql-ext:version xmlns:cql-ext="urn:hhs-cql:hqmf-n1-extensions:v1"
                                             value="{@version}"/> <!-- {Value Set Version} -->
                        </xsl:if>

                    </valueSet>
                </definition>
            </xsl:if>

        </xsl:for-each>


        <xsl:for-each select="//elementLookUp/qdm[@code ='true']">
            <xsl:if test="not(preceding-sibling::qdm[@name = current()/@name])">
                <xsl:comment>
                    <xsl:value-of select="@name"/>-<xsl:value-of select="@codeIdentifier"/>
                </xsl:comment>
                <xsl:text>
      </xsl:text>
                <definition>
                    <cql-ext:code code="{@oid}" codeSystem="{@codeSystemOID}"
                                  codeSystemName="{@taxonomy}" xmlns:cql-ext="urn:hhs-cql:hqmf-n1-extensions:v1">
                        <xsl:if test="@isCodeSystemVersionIncluded = 'true'">
                            <xsl:attribute name="codeSystemVersion">
                                <xsl:value-of select="@codeSystemVersion"/>
                            </xsl:attribute>
                        </xsl:if>
                        <displayName value="{@name}"/>
                    </cql-ext:code>
                </definition>
            </xsl:if>

        </xsl:for-each>


        <relatedDocument typeCode="COMP">
            <expressionDocument>
                <id root="{cqlUUID}"/>
                <text mediaType="text/cql">
                    <reference
                            value="https://emeasuretool.cms.gov/libraries/{../measureDetails/guid}/{translate(../cqlLookUp/library,'_','-')}-{../cqlLookUp/version}.cql"/>
                    <translation mediaType="application/elm+xml">
                        <reference
                                value="https://emeasuretool.cms.gov/libraries/{../measureDetails/guid}/{translate(../cqlLookUp/library,'_','-')}-{../cqlLookUp/version}.xml"/>
                    </translation>
                    <translation mediaType="application/elm+json">
                        <reference
                                value="https://emeasuretool.cms.gov/libraries/{../measureDetails/guid}/{translate(../cqlLookUp/library,'_','-')}-{../cqlLookUp/version}.json"/>
                    </translation>
                </text>
                <setId root="https://emeasuretool.cms.gov/libraries" extension="{../measureDetails/guid}"
                       identifierName="{translate(../cqlLookUp/library,'_','-')}"/>
                <versionNumber value="{../cqlLookUp/version}"/>
            </expressionDocument>
        </relatedDocument>

        <xsl:for-each select="../allUsedCQLLibs/lib">
            <relatedDocument typeCode="COMP">
                <expressionDocument>
                    <id root="{@id}"/>
                    <text mediaType="text/cql">
                        <reference
                                value="https://emeasuretool.cms.gov/libraries/{@setId}/{translate(@name,'_','-')}-{@version}.cql"/>
                        <translation mediaType="application/elm+xml">
                            <reference
                                    value="https://emeasuretool.cms.gov/libraries/{@setId}/{translate(@name,'_','-')}-{@version}.xml"/>
                        </translation>
                        <translation mediaType="application/elm+json">
                            <reference
                                    value="https://emeasuretool.cms.gov/libraries/{@setId}/{translate(@name,'_','-')}-{@version}.json"/>
                        </translation>
                    </text>
                    <setId root="https://emeasuretool.cms.gov/libraries" extension="{@setId}"
                           identifierName="{translate(@name,'_','-')}"/>
                    <versionNumber value="{@version}"/>
                </expressionDocument>
            </relatedDocument>
        </xsl:for-each>

        <!-- Component Of -->
        <xsl:call-template name="componentOfTemplate"/>

        <!-- Measure Period -->
        <xsl:call-template name="measure_specific_data_elements"/>
        <xsl:if test="compositeScoring">
            <xsl:call-template name="compositeMeasureAttributeTemplate"/>
        </xsl:if>

        <subjectOf>
            <measureAttribute>
                <code nullFlavor="OTH">
                    <originalText value="eCQM Identifier (Measure Authoring Tool)"/>
                </code>
                <value xsi:type="ED" mediaType="text/plain">
                    <xsl:attribute name="value">
                        <xsl:call-template name="trim">
                            <xsl:with-param name="textString"
                                            select="emeasureid"/>
                        </xsl:call-template>
                    </xsl:attribute>
                </value>
            </measureAttribute>
        </subjectOf>
        <subjectOf>
            <measureAttribute>
                <code nullFlavor="OTH">
                    <originalText value="NQF Number"/>
                </code>
                <xsl:variable name="NQFText">
                    <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="nqfid/@extension"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$NQFText = '' ">
                        <value xsi:type="ED" mediaType="text/plain" value="Not Applicable"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <value xsi:type="ED" mediaType="text/plain" value="{$NQFText}"/>
                    </xsl:otherwise>
                </xsl:choose>
            </measureAttribute>
        </subjectOf>


        <!-- Copyright -->
        <subjectOf>
            <measureAttribute>
                <code code="COPY" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Copyright"/>
                </code>
                <xsl:variable name="copyRightTxt">
                    <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="copyright"/>
                    </xsl:call-template>
                </xsl:variable>
                <value xsi:type="ED" mediaType="text/plain" value="{$copyRightTxt}"/>
            </measureAttribute>
        </subjectOf>

        <!-- Disclaimer -->
        <subjectOf>
            <measureAttribute>
                <code code="DISC" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Disclaimer"/>
                </code>
                <xsl:variable name="disclaimerTxt">
                    <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="disclaimer"/>
                    </xsl:call-template>
                </xsl:variable>
                <value xsi:type="ED" mediaType="text/plain" value="{$disclaimerTxt}"/>
            </measureAttribute>
        </subjectOf>

        <!-- Measure Scoring -->
        <subjectOf>
            <xsl:variable name="scoring_id">
                <xsl:value-of select="scoring/@id"></xsl:value-of>
            </xsl:variable>
            <xsl:variable name="scoring_value">
                <xsl:value-of select="scoring"></xsl:value-of>
            </xsl:variable>
            <measureAttribute>
                <code code="MSRSCORE" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Measure Scoring"/>
                </code>
                <value xsi:type="CD" code="{$scoring_id}" codeSystem="2.16.840.1.113883.5.1063">
                    <displayName value="{normalize-space($scoring_value)}"/>
                </value>
            </measureAttribute>
        </subjectOf>

        <!-- Measure Type -->
        <xsl:choose>
            <xsl:when test="types/type">
                <xsl:for-each select="types/type">
                    <subjectOf>
                        <measureAttribute>
                            <code code="MSRTYPE" codeSystem="2.16.840.1.113883.5.4">
                                <displayName value="Measure Type"/>
                            </code>
                            <xsl:variable name="nameVar" select="@id"/>
                            <value xsi:type="CD" code="{$nameVar}" codeSystem="2.16.840.1.113883.5.1063">
                                <displayName value="{$nameVar}"/>
                            </value>
                        </measureAttribute>
                    </subjectOf>
                </xsl:for-each>
            </xsl:when>
        </xsl:choose>

        <!-- itemCount -->
        <xsl:call-template name="itemCountTemplate"/>

        <xsl:variable name="scoring_value">
            <xsl:value-of select="scoring"></xsl:value-of>
        </xsl:variable>

        <!-- Stratification -->
        <xsl:if test="$scoring_value != 'Ratio'">
            <subjectOf>
                <measureAttribute>
                    <code code="STRAT" codeSystem="2.16.840.1.113883.5.4">
                        <displayName value="Stratification"/>
                    </code>
                    <xsl:variable name="stratTxt">
                        <xsl:call-template name="trim">
                            <xsl:with-param name="textString" select="stratification"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <value xsi:type="ED" mediaType="text/plain" value="{$stratTxt}"/>
                </measureAttribute>
            </subjectOf>
        </xsl:if>

        <!-- Risk Adjustment -->
        <subjectOf>
            <measureAttribute>
                <code code="MSRADJ" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Risk Adjustment"/>
                </code>
                <xsl:variable name="rskAdjustTxt">
                    <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="riskAdjustment"/>
                    </xsl:call-template>
                </xsl:variable>
                <value xsi:type="ED" mediaType="text/plain" value="{$rskAdjustTxt}"/>
            </measureAttribute>
        </subjectOf>

        <!-- Rate Aggregation -->
        <subjectOf>
            <measureAttribute>
                <code code="MSRAGG" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Rate Aggregation"/>
                </code>
                <xsl:variable name="rateAggTxt">
                    <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="aggregation"/>
                    </xsl:call-template>
                </xsl:variable>
                <value xsi:type="ED" mediaType="text/plain" value="{$rateAggTxt}"/>
            </measureAttribute>
        </subjectOf>

        <!-- Rationale -->
        <subjectOf>
            <measureAttribute>
                <code code="RAT" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Rationale"/>
                </code>
                <xsl:variable name="rationalTxt">
                    <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="rationale"/>
                    </xsl:call-template>
                </xsl:variable>
                <value xsi:type="ED" mediaType="text/plain" value="{$rationalTxt}"/>
            </measureAttribute>
        </subjectOf>

        <!-- Clinical Recommendation Statement -->
        <subjectOf>
            <measureAttribute>
                <code code="CRS" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Clinical Recommendation Statement"/>
                </code>
                <xsl:variable name="recommendationsTxt">
                    <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="recommendations"/>
                    </xsl:call-template>
                </xsl:variable>
                <value xsi:type="ED" mediaType="text/plain" value="{$recommendationsTxt}"/>
            </measureAttribute>
        </subjectOf>

        <!-- Improvement Notation -->
        <subjectOf>
            <measureAttribute>
                <code code="IDUR" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Improvement Notation"/>
                </code>
                <xsl:variable name="improvementNotationsTxt">
                    <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="improvementNotations"/>
                    </xsl:call-template>
                </xsl:variable>
                <value xsi:type="ED" mediaType="text/plain" value="{$improvementNotationsTxt}"/>
            </measureAttribute>
        </subjectOf>

        <!-- Reference -->
        <xsl:choose>
            <xsl:when test="references/reference">
                <xsl:for-each select="references/reference">
                    <subjectOf>
                        <measureAttribute>
                            <code code="REF" codeSystem="2.16.840.1.113883.5.4">
                                <displayName value="Reference"/>
                            </code>
                            <xsl:variable name="refTxt">
                                <xsl:call-template name="trim">
                                    <xsl:with-param name="textString" select="."/>
                                </xsl:call-template>
                            </xsl:variable>
                            <value xsi:type="ED" mediaType="text/plain" value="{$refTxt}"/>
                        </measureAttribute>
                    </subjectOf>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <subjectOf>
                    <measureAttribute>
                        <code code="REF" codeSystem="2.16.840.1.113883.5.4">
                            <displayName value="Reference"/>
                        </code>
                        <value xsi:type="ED" mediaType="text/plain"/>
                    </measureAttribute>
                </subjectOf>
            </xsl:otherwise>
        </xsl:choose>

        <!-- cmsid -->
        <xsl:if test="cmsid">
            <subjectOf>
                <measureAttribute>
                    <code nullFlavor="OTH">
                        <originalText>CMS ID Number</originalText>
                    </code>
                    <value xsi:type="II" root="{cmsid/@root}"/>
                </measureAttribute>
            </subjectOf>
        </xsl:if>

        <!-- Definitions -->
        <xsl:choose>
            <xsl:when test="definitions">
                <xsl:for-each select="definitions">
                    <subjectOf>
                        <measureAttribute>
                            <code code="DEF" codeSystem="2.16.840.1.113883.5.4">
                                <displayName value="Definition"/>
                            </code>
                            <xsl:variable name="definitionsTxt">
                                <xsl:call-template name="trim">
                                    <xsl:with-param name="textString" select="."/>
                                </xsl:call-template>
                            </xsl:variable>
                            <value xsi:type="ED" mediaType="text/plain" value="{$definitionsTxt}"/>
                        </measureAttribute>
                    </subjectOf>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <subjectOf>
                    <measureAttribute>
                        <code code="DEF" codeSystem="2.16.840.1.113883.5.4">
                            <displayName value="Definition"/>
                        </code>
                        <value xsi:type="ED" mediaType="text/plain"/>
                    </measureAttribute>
                </subjectOf>
            </xsl:otherwise>
        </xsl:choose>

        <!-- Guidance -->
        <subjectOf>
            <measureAttribute>
                <code code="GUIDE" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Guidance"/>
                </code>

                <value xsi:type="ED" mediaType="text/plain">
                    <xsl:attribute name="value">
                        <xsl:call-template name="trim">
                            <xsl:with-param name="textString"
                                            select="guidance"/>
                        </xsl:call-template>
                    </xsl:attribute>
                </value>
            </measureAttribute>
        </subjectOf>

        <!-- transmission format -->
        <subjectOf>
            <measureAttribute>
                <code code="TRANF" codeSystem="2.16.840.1.113883.5.4">
                    <displayName value="Transmission Format"/>
                </code>
                <value xsi:type="ED" mediaType="text/plain">
                    <xsl:attribute name="value">
                        <xsl:value-of select="transmissionFormat/text()"/>
                    </xsl:attribute>
                </value>
            </measureAttribute>
        </subjectOf>


        <!-- Initial Population Description -->
        <xsl:call-template name="subjOfOrigText">
            <xsl:with-param name="origText">Initial Population</xsl:with-param>
            <xsl:with-param name="text">
                <xsl:value-of select="initialPopDescription/text()"/>
            </xsl:with-param>
            <xsl:with-param name="code">IPOP</xsl:with-param>
            <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
        </xsl:call-template>

        <xsl:variable name="patient_based_indicator">
            <xsl:value-of select="patientBasedIndicator"></xsl:value-of>
        </xsl:variable>
        <!-- Denominator Description -->
        <xsl:if test="$scoring_value = 'Ratio' or $scoring_value ='Proportion'">
            <xsl:call-template name="subjOfOrigText">

                <xsl:with-param name="origText">
                    <xsl:text>Denominator</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:value-of select="denominatorDescription/text()"/>
                </xsl:with-param>
                <xsl:with-param name="code">DENOM</xsl:with-param>
                <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
            </xsl:call-template>

            <!-- Denominator Exclusions Description -->
            <xsl:call-template name="subjOfOrigText">
                <xsl:with-param name="origText">
                    <xsl:text>Denominator Exclusions</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:value-of select="denominatorExclusionsDescription/text()"/>
                </xsl:with-param>
                <xsl:with-param name="code">DENEX</xsl:with-param>
                <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
            </xsl:call-template>

            <!-- Numerator Description -->
            <xsl:call-template name="subjOfOrigText">
                <xsl:with-param name="origText">
                    <xsl:text>Numerator</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:value-of select="numeratorDescription/text()"/>
                </xsl:with-param>
                <xsl:with-param name="code">NUMER</xsl:with-param>
                <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
            </xsl:call-template>
            <!-- Numerator Exclusions Description -->
            <xsl:call-template name="subjOfOrigText">
                <xsl:with-param name="origText">
                    <xsl:text>Numerator Exclusions</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:value-of select="numeratorExclusionsDescription/text()"/>
                </xsl:with-param>
                <xsl:with-param name="code">NUMEX</xsl:with-param>
                <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <!-- Denominator Exceptions Description -->
        <xsl:if test="$scoring_value ='Proportion'">
            <xsl:call-template name="subjOfOrigText">
                <xsl:with-param name="origText">
                    <xsl:text>Denominator Exceptions</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:value-of select="denominatorExceptionsDescription/text()"/>
                </xsl:with-param>
                <xsl:with-param name="code">DENEXCEP</xsl:with-param>
                <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$scoring_value ='Continuous Variable'">
            <!-- Measure Population Description -->
            <xsl:call-template name="subjOfOrigText">
                <xsl:with-param name="origText">
                    <xsl:text>Measure Population</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:value-of select="measurePopulationDescription/text()"/>
                </xsl:with-param>
                <xsl:with-param name="code">MSRPOPL</xsl:with-param>
                <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
            </xsl:call-template>
            <!-- Measure Population Exclusions -->
            <xsl:call-template name="subjOfOrigText">
                <xsl:with-param name="origText">
                    <xsl:text>Measure Population Exclusions</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:value-of select="measurePopulationExclusionsDescription/text()"/>
                </xsl:with-param>
                <xsl:with-param name="code">MSRPOPLEX</xsl:with-param>
                <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if
                test="($scoring_value = 'Ratio' or $scoring_value ='Continuous Variable')">
            <!-- Measure Observations Description -->
            <xsl:call-template name="subjOfOrigText">
                <xsl:with-param name="origText">
                    <xsl:text>Measure Observation</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="code">
                    <xsl:text>MSROBS</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="codeSystem">
                    <xsl:text>2.16.840.1.113883.5.4</xsl:text>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:value-of select="measureObservationsDescription/text()"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <!-- supplemental data elements -->
        <xsl:call-template name="subjOfOrigText">
            <xsl:with-param name="origText">
                <xsl:text>Supplemental Data Elements</xsl:text>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:value-of select="supplementalData/text()"/>
            </xsl:with-param>
            <xsl:with-param name="code">SDE</xsl:with-param>
            <xsl:with-param name="codeSystem">2.16.840.1.113883.5.4</xsl:with-param>
        </xsl:call-template>

        <xsl:variable name="qms_uuid">
            <xsl:value-of select="qualityMeasureSet/@uuid"/>
        </xsl:variable>
        <componentOf>
            <qualityMeasureSet classCode="ACT">
                <id root="{$qms_uuid}"/>
                <title>
                    <xsl:attribute name="value">
                        <xsl:call-template name="trim">
                            <xsl:with-param name="textString"
                                            select="qualityMeasureSet"/>
                        </xsl:call-template>
                    </xsl:attribute>
                </title>
            </qualityMeasureSet>
        </componentOf>

    </xsl:template>

    <xsl:template name="measure_specific_data_elements">

        <xsl:if test="period">
            <controlVariable>
                <measurePeriod>
                    <!-- <id root="dfa426b5-05c2-4fdf-814d-6332fb2acbd8" extension="measureperiod"/> -->
                    <id root="{normalize-space(period/@uuid)}" extension="measureperiod"/>
                    <code code="MSRTP" codeSystem="2.16.840.1.113883.5.4">
                        <originalText value="Measurement Period"/>
                    </code>
                    <xsl:choose>
                        <xsl:when test="period/@calenderYear='true'">
                            <value xsi:type="PIVL_TS">
                                <phase lowClosed="true" highClosed="true">
                                    <low value="{normalize-space(period/startDate/text())}"/>
                                    <width xsi:type="PQ" value="1" unit="a"/>
                                </phase>
                                <period value="1" unit="a"/>
                            </value>
                        </xsl:when>
                        <xsl:otherwise>
                            <value xsi:type="IVL_TS" lowClosed="true" highClosed="false">
                                <low value="{normalize-space(period/startDate/text())}"/>
                                <high value="{normalize-space(period/stopDate/text())}"/>
                            </value>
                        </xsl:otherwise>
                    </xsl:choose>
                </measurePeriod>
            </controlVariable>
        </xsl:if>
    </xsl:template>


    <xsl:template name="compositeMeasureAttributeTemplate">
        <subjectOf>
            <measureAttribute>
                <code code="CMPMSRMTH" codeSystem="2.16.840.1.113883.5.4"/>
                <value xsi:type="CD" codeSystem="2.16.840.1.113883.5.1063">
                    <xsl:attribute name="code">
                        <xsl:value-of select="compositeScoring/@id"></xsl:value-of>
                    </xsl:attribute>
                </value>
            </measureAttribute>
        </subjectOf>
    </xsl:template>

    <xsl:template name="componentOfTemplate">
        <xsl:if test="componentMeasures">
            <xsl:for-each select="componentMeasures/measure">
                <relatedDocument typeCode="XCRPT">
                    <componentQualityMeasureDocument>
                        <id>
                            <xsl:attribute name="root">
                                <xsl:call-template
                                        name="trim">
                                    <xsl:with-param
                                            name="textString" select="@id"/>
                                </xsl:call-template>
                            </xsl:attribute>
                            <xsl:attribute name="identifierName">
                                <xsl:call-template
                                        name="trim">
                                    <xsl:with-param
                                            name="textString" select="@name"/>
                                </xsl:call-template>
                            </xsl:attribute>
                        </id>
                        <setId>
                            <xsl:attribute name="root">
                                <xsl:call-template
                                        name="trim">
                                    <xsl:with-param
                                            name="textString" select="@measureSetId"/>
                                </xsl:call-template>
                            </xsl:attribute>
                        </setId>
                        <versionNumber>
                            <xsl:attribute name="value">
                                <xsl:call-template
                                        name="trim">
                                    <xsl:with-param
                                            name="textString" select="@versionNo"/>
                                </xsl:call-template>
                            </xsl:attribute>
                        </versionNumber>
                    </componentQualityMeasureDocument>
                </relatedDocument>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <xsl:template name="itemCountTemplate">
        <xsl:if test="itemCount">
            <xsl:if test="itemCount/elementRef">
                <xsl:for-each select="itemCount/elementRef">
                    <subjectOf>
                        <measureAttribute>
                            <code code="ITMCNT" codeSystem="2.16.840.1.113883.5.4">
                                <displayName value="Items to count"/>
                            </code>
                            <value xsi:type="II">
                                <xsl:attribute name="root">
                                    <xsl:value-of
                                            select="@id"/>
                                </xsl:attribute>
                                <xsl:attribute name="extension">

                                    <xsl:choose>
                                        <xsl:when test="exists(@instance)">
                                            <xsl:variable name="extVal">
                                                <xsl:value-of
                                                        select="concat(@instance,'_',@name,'_',@dataType)"/>
                                            </xsl:variable>
                                            <xsl:value-of
                                                    select="translate($extVal,' ','')"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:variable name="extVal">
                                                <xsl:value-of
                                                        select="concat(@name,'_',@dataType)"/>
                                            </xsl:variable>
                                            <xsl:value-of
                                                    select="translate($extVal,' ','')"/>
                                        </xsl:otherwise>
                                    </xsl:choose>

                                </xsl:attribute>
                            </value>
                        </measureAttribute>
                    </subjectOf>
                </xsl:for-each>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template name="subjOfOrigText">
        <xsl:param name="origText"/>
        <xsl:param name="text"/>
        <xsl:param name="xtype"/>
        <xsl:param name="code"/>
        <xsl:param name="codeSystem"/>

        <xsl:variable name="xsitype">
            <xsl:choose>
                <xsl:when test="string-length($xtype)>0">
                    <xsl:value-of select="$xtype"/>
                </xsl:when>
                <xsl:otherwise>ED</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <subjectOf>
            <measureAttribute>
                <code>
                    <xsl:choose>
                        <xsl:when test="string-length($code)=0 or string-length($codeSystem)=0">
                            <xsl:attribute name="nullFlavor">OTH</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="code">
                                <xsl:value-of select="normalize-space($code)"/>
                            </xsl:attribute>
                            <xsl:attribute name="codeSystem">
                                <xsl:value-of select="normalize-space($codeSystem)"/>
                            </xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                    <displayName>
                        <xsl:attribute name="value">
                            <xsl:value-of select="normalize-space($origText)"/>
                        </xsl:attribute>
                    </displayName>
                </code>
                <value xsi:type="{$xsitype}" mediaType="text/plain">
                    <xsl:attribute name="value">
                        <xsl:call-template name="trim">
                            <xsl:with-param name="textString"
                                            select="$text"/>
                        </xsl:call-template>
                    </xsl:attribute>
                </value>
            </measureAttribute>
        </subjectOf>
    </xsl:template>

    <xsl:template name="trim">
        <xsl:param name="textString"/>
        <xsl:value-of select="replace(replace($textString,'\s+$',''),'^\s+','')"/>
    </xsl:template>

    <xsl:template name="constructExt">

    </xsl:template>
</xsl:stylesheet>
