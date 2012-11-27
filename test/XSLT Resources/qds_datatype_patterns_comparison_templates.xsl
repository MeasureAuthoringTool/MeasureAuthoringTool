<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>

	<xsl:template match="high" mode="high_text">
		<xsl:text> &lt;</xsl:text>
		<xsl:if test="@inclusive='true'"><xsl:text>=</xsl:text></xsl:if>
		<xsl:if test="@value">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@value"/>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:if test="@unit">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@unit"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="low" mode="low_text">
		<xsl:text> &gt;</xsl:text>
		<xsl:if test="@inclusive='true'"><xsl:text>=</xsl:text></xsl:if>
		<xsl:if test="@value">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@value"/>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:if test="@unit">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@unit"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="equal" mode="equal_text">
		<xsl:text> =</xsl:text>
		<xsl:if test="@value">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@value"/>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:if test="@unit">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@unit"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="*" mode="inline_high_text">
		<xsl:text> &lt;</xsl:text>
		<xsl:if test="@highinclusive='true'"><xsl:text>=</xsl:text></xsl:if>
		<xsl:if test="@highnum">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@highnum"/>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:if test="@highunit">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@highunit"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="*" mode="inline_low_text">
		<xsl:text> &gt;</xsl:text>
		<xsl:if test="@lowinclusive='true'"><xsl:text>=</xsl:text></xsl:if>
		<xsl:if test="@lownum">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@lownum"/>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:if test="@lowunit">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@lowunit"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="*" mode="inline_equal_text">
		<xsl:if test="@equalnegationind">
			<xsl:text> not</xsl:text>
		</xsl:if>
		<xsl:text> =</xsl:text>
		<xsl:if test="@equalnum">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@equalnum"/>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:if test="@equalunit">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@equalunit"/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="*" mode="inline_arit_text">
		<xsl:text> </xsl:text>
		<xsl:if test="@aritop">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@aritop"/>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:if test="@aritnum">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@aritnum"/>
		</xsl:if>
		<xsl:if test="@aritunit">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@aritunit"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="*" mode="inline_equal">
		<value xsi:type="IVL_PQ" >
			<xsl:if test="@equalnum">
				<xsl:attribute name="value"><xsl:value-of select="@equalnum"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="@equalunit">
				<xsl:variable name="unit">
					<xsl:call-template name="unitvalue">
						<xsl:with-param name="uval" select="@equalunit"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:attribute name="unit">
					<xsl:value-of select="$unit"/>
				</xsl:attribute>
			</xsl:if>
		</value>
		<xsl:if test="@equalnegationind">
			<valueNegationInd/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="*" mode="inline_low">
		<value xsi:type="IVL_PQ" >
			<low>
				<xsl:if test="@lownum">
					<xsl:attribute name="value"><xsl:value-of select="@lownum"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="@lowunit">
					<xsl:variable name="unit">
						<xsl:call-template name="unitvalue">
							<xsl:with-param name="uval" select="@lowunit"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:attribute name="unit">
						<xsl:value-of select="$unit"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="@lowinclusive">
					<xsl:attribute name="inclusive"><xsl:value-of select="@lowinclusive"/></xsl:attribute>
				</xsl:if>
			</low>
		</value>
	</xsl:template>
	
	<xsl:template match="*" mode="inline_high">
		<value xsi:type="IVL_PQ" >
			<high>
				<xsl:if test="@highnum">
					<xsl:attribute name="value"><xsl:value-of select="@highnum"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="@highunit">
					<xsl:variable name="unit">
						<xsl:call-template name="unitvalue">
							<xsl:with-param name="uval" select="@highunit"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:attribute name="unit">
						<xsl:value-of select="$unit"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="@highinclusive">
					<xsl:attribute name="inclusive"><xsl:value-of select="@highinclusive"/></xsl:attribute>
				</xsl:if>
			</high>
		</value>
	</xsl:template>
	
	<xsl:template match="*" mode="low">
		<value xsi:type="IVL_PQ" >
			<low>
				<xsl:if test="@value">
					<xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
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
				<xsl:if test="@inclusive">
					<xsl:attribute name="inclusive"><xsl:value-of select="@inclusive"/></xsl:attribute>
				</xsl:if>
			</low>
		</value>
	</xsl:template>
	
	<xsl:template match="*" mode="equal">
		<value xsi:type="IVL_PQ" >
			<xsl:if test="@value">
				<xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
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
	
	<xsl:template match="*" mode="high">
		<value xsi:type="IVL_PQ" >
			<high>
				<xsl:if test="@value">
					<xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
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
				<xsl:if test="@inclusive">
					<xsl:attribute name="inclusive"><xsl:value-of select="@inclusive"/></xsl:attribute>
				</xsl:if>
			</high>
		</value>
	</xsl:template>
</xsl:stylesheet>