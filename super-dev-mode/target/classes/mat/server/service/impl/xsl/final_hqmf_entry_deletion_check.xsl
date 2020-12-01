<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:exsl="http://exslt.org/common" xmlns:uuid="java:java.util.UUID"
    xmlns:math="http://exslt.org/math" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns:hl7="urn:hl7-org:v3"
    extension-element-prefixes="exsl uuid math xs" exclude-result-prefixes="exsl uuid math xs msxsl">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" />
    <xsl:preserve-space elements="content" />
   <xsl:template match="/">
       <html>
           <body>
      		<xsl:for-each select="//hl7:criteriaReference/hl7:id[@extension!='initialPopulation'][@extension!='measureperiod']
      		                         [@extension!='numerator'][@extension!='denominator']">
      		    <xsl:variable name="extension">
      		        <xsl:value-of select="@extension"/>
      		    </xsl:variable>
      		    <xsl:variable name="root">
      		        <xsl:value-of select="@root"/>
      		    </xsl:variable>
      		    
      		    <xsl:choose>
      		        <xsl:when test="count(//hl7:entry/*/hl7:id[@root=$root][@extension=$extension]) = 0">  
      		      <root>
      		          <xsl:value-of select="$root"/>
      		          -------------
      		          <xsl:value-of select="$extension"/>
      		          
      		      </root>
      		    </xsl:when>
               <!-- <xsl:otherwise>
                    <root>
                        <xsl:value-of select="$root"/>
                        <xsl:value-of select="$extension"/>
                        <xsl:value-of select="count(//hl7:dataCriteriaSection//hl7:entry/*/hl7:id[@root=$root][@extension=$extension])"/>
                    </root>
                </xsl:otherwise>  -->   		        
      		    </xsl:choose>
      		    
      		</xsl:for-each>
           </body>
       </html>
	</xsl:template>
</xsl:stylesheet>