<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>

	<xsl:template match="attribute" mode="text_new">
		<xsl:variable name="modeVal"><xsl:value-of select="lower-case(@mode)"/></xsl:variable>
		<xsl:choose>
			<xsl:when test="$modeVal = 'less than'">
				<xsl:text> &lt;</xsl:text>
				<xsl:if test="@comparisonValue">
					<xsl:text> </xsl:text>
					<xsl:value-of select="@comparisonValue"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="@unit">
					<xsl:text> </xsl:text>
					<xsl:variable name="unit"><xsl:value-of select="@unit"/></xsl:variable>
					
					<xsl:call-template name="unit-output">
						<xsl:with-param name="uval" select="$unit"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$modeVal = 'greater than'">
				<xsl:text> &gt;</xsl:text>
				<xsl:if test="@comparisonValue">
					<xsl:text> </xsl:text>
					<xsl:value-of select="@comparisonValue"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="@unit">
					<xsl:text> </xsl:text>
					<xsl:variable name="unit"><xsl:value-of select="@unit"/></xsl:variable>
					
					<xsl:call-template name="unit-output">
						<xsl:with-param name="uval" select="$unit"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$modeVal = 'equal to'">
				<xsl:text> =</xsl:text>
				<xsl:if test="@comparisonValue">
					<xsl:text> </xsl:text>
					<xsl:value-of select="@comparisonValue"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="@unit">
					<xsl:text> </xsl:text>
					<xsl:variable name="unit"><xsl:value-of select="@unit"/></xsl:variable>
					
					<xsl:call-template name="unit-output">
						<xsl:with-param name="uval" select="$unit"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$modeVal = 'less than or equal to'">
				<xsl:text> &lt;=</xsl:text>
				<xsl:if test="@comparisonValue">
					<xsl:text> </xsl:text>
					<xsl:value-of select="@comparisonValue"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="@unit">
					<xsl:text> </xsl:text>
					<xsl:variable name="unit"><xsl:value-of select="@unit"/></xsl:variable>
					
					<xsl:call-template name="unit-output">
						<xsl:with-param name="uval" select="$unit"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$modeVal = 'greater than or equal to'">
				<xsl:text> &gt;=</xsl:text>
				<xsl:if test="@comparisonValue">
					<xsl:text> </xsl:text>
					<xsl:value-of select="@comparisonValue"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="@unit">
					<xsl:text> </xsl:text>
					<xsl:variable name="unit"><xsl:value-of select="@unit"/></xsl:variable>
					
					<xsl:call-template name="unit-output">
						<xsl:with-param name="uval" select="$unit"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$modeVal = 'value set'">
				
			</xsl:when>
			<xsl:when test="$modeVal = 'check if present'">
				 <!-- <xsl:if test="@name = 'negation rationale'">
				 	<xsl:text> is present</xsl:text> 
				 </xsl:if> -->
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
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
			<xsl:variable name="unit"><xsl:value-of select="@unit"/></xsl:variable>
			
			<xsl:call-template name="unit-output">
				<xsl:with-param name="uval" select="$unit"/>
			</xsl:call-template>
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
			<xsl:variable name="unit"><xsl:value-of select="@unit"/></xsl:variable>
			
			<xsl:call-template name="unit-output">
				<xsl:with-param name="uval" select="$unit"/>
			</xsl:call-template>
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
			<xsl:variable name="unit"><xsl:value-of select="@unit"/></xsl:variable>
			
			<xsl:call-template name="unit-output">
				<xsl:with-param name="uval" select="$unit"/>
			</xsl:call-template>
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
			<xsl:variable name="unit"><xsl:value-of select="@highunit"/></xsl:variable>
			
			<xsl:call-template name="unit-output">
				<xsl:with-param name="uval" select="$unit"/>
			</xsl:call-template>

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
			<xsl:variable name="unit"><xsl:value-of select="@lowunit"/></xsl:variable>
			
			<xsl:call-template name="unit-output">
				<xsl:with-param name="uval" select="$unit"/>
			</xsl:call-template>
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
			<xsl:variable name="unit"><xsl:value-of select="@equalunit"/></xsl:variable>
			
			<xsl:call-template name="unit-output">
				<xsl:with-param name="uval" select="$unit"/>
			</xsl:call-template>
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
			<xsl:variable name="unit"><xsl:value-of select="@aritunit"/></xsl:variable>
			
			<xsl:call-template name="unit-output">
				<xsl:with-param name="uval" select="$unit"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="*" mode="inline_equal">
		<value xsi:type="PQ" >
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
		<value xsi:type="PQ" >
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
	
	<xsl:template match="*" mode="pq_inline_equal">
		<pauseQuantity xsi:type="PQ">
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
		</pauseQuantity>
		<xsl:if test="@equalnegationind">
			<valueNegationInd/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="*" mode="pq_inline_low">
		<pauseQuantity xsi:type="IVL_PQ" >
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
		</pauseQuantity>
	</xsl:template>
	
	<xsl:template match="*" mode="pq_inline_high">
		<pauseQuantity xsi:type="IVL_PQ" >
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
		</pauseQuantity>
	</xsl:template>
	
	<xsl:template match="*" mode="pq_low">
		<pauseQuantity xsi:type="IVL_PQ" >
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
		</pauseQuantity>
	</xsl:template>
	
	<xsl:template match="*" mode="pq_equal">
		<pauseQuantity xsi:type="PQ" >
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
		</pauseQuantity>
	</xsl:template>
	
	<xsl:template match="*" mode="pq_high">
		<pauseQuantity xsi:type="IVL_PQ" >
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
		</pauseQuantity>
	</xsl:template>
	
	<xsl:template match="*" mode="pq_comparison_new">
		<xsl:variable name="operatorType"><xsl:value-of select="lower-case(@operatorType)"/></xsl:variable>
		<xsl:variable name="value"><xsl:value-of select="@quantity"/></xsl:variable>
		<xsl:variable name="unit">
			<xsl:call-template name="unitvalue">
				<xsl:with-param name="uval" select="@unit"/>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="$operatorType = 'equal to'">
				<pauseQuantity xsi:type="PQ">
					<xsl:attribute name="value"><xsl:value-of select="$value"/></xsl:attribute>
					<xsl:attribute name="unit"><xsl:value-of select="$unit"/></xsl:attribute>
				</pauseQuantity>
			</xsl:when>
			<xsl:otherwise>
				<pauseQuantity xsi:type="IVL_PQ" >
					<xsl:choose>
						<xsl:when test="$operatorType = 'greater than'">
							<low inclusive='false'>
								<xsl:attribute name="value"><xsl:value-of select="$value"/></xsl:attribute>
								<xsl:attribute name="unit"><xsl:value-of select="$unit"/></xsl:attribute>
							</low>
						</xsl:when>
						<xsl:when test="$operatorType = 'greater than or equal to'">
							<low inclusive='true'>
								<xsl:attribute name="value"><xsl:value-of select="$value"/></xsl:attribute>
								<xsl:attribute name="unit"><xsl:value-of select="$unit"/></xsl:attribute>
							</low>
						</xsl:when>
						<xsl:when test="$operatorType = 'less than'">
							<high inclusive='false'>
								<xsl:attribute name="value"><xsl:value-of select="$value"/></xsl:attribute>
								<xsl:attribute name="unit"><xsl:value-of select="$unit"/></xsl:attribute>
							</high>
						</xsl:when>
						<xsl:when test="$operatorType = 'less than or equal to'">
							<high inclusive='true'>
								<xsl:attribute name="value"><xsl:value-of select="$value"/></xsl:attribute>
								<xsl:attribute name="unit"><xsl:value-of select="$unit"/></xsl:attribute>
							</high>
						</xsl:when>
					</xsl:choose>
				</pauseQuantity>			
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- centralized logic to output comparison content for pauseQuantity -->
	<xsl:template match="*" mode="pq_comparison">
		<xsl:choose>
			<xsl:when test="current()[@lownum]">
				<xsl:apply-templates select="." mode="pq_inline_low"/>
			</xsl:when>
			<xsl:when test="current()[@highnum]">
				<xsl:apply-templates select="." mode="pq_inline_high"/>
			</xsl:when>
			<xsl:when test="current()[@equalnum]">
				<xsl:apply-templates select="." mode="pq_inline_equal"/>
			</xsl:when>
			<xsl:when test="current()/low">
				<xsl:apply-templates select="./low" mode="pq_low"/>
			</xsl:when>
			<xsl:when test="current()/high">
				<xsl:apply-templates select="./high" mode="pq_high"/>
			</xsl:when>
			<xsl:when test="current()/equal">
				<xsl:apply-templates select="./equal" mode="pq_equal"/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- centralized logic to output comparison content for title or originalText -->
	<xsl:template match="*" mode="orig_text_comparison">
		<xsl:choose>
			<xsl:when test="current()[@lownum]">
				<xsl:apply-templates select="." mode="inline_low_text"/>
			</xsl:when>
			<xsl:when test="current()[@highnum]">
				<xsl:apply-templates select="." mode="inline_high_text"/>
			</xsl:when>
			<xsl:when test="current()[@equalnum]">
				<xsl:apply-templates select="." mode="inline_equal_text"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="unit-output">
		<xsl:param name="uval"/>
		<xsl:choose>
			<xsl:when test="$uval='celsius'">&#8451;</xsl:when>
			<xsl:when test="$uval='years'">year(s)</xsl:when>
			<xsl:when test="$uval='year'">year(s)</xsl:when>
			<xsl:when test="$uval='month'">month(s)</xsl:when>
			<xsl:when test="$uval='months'">month(s)</xsl:when>
			<xsl:when test="$uval='day'">day(s)</xsl:when>
			<xsl:when test="$uval='days'">day(s)</xsl:when>
			<xsl:when test="$uval='hour'">hour(s)</xsl:when>
			<xsl:when test="$uval='hours'">hour(s)</xsl:when>
			<xsl:when test="$uval='week'">week(s)</xsl:when>
			<xsl:when test="$uval='weeks'">week(s)</xsl:when>
			<xsl:when test="$uval='minute'">minute(s)</xsl:when>
			<xsl:when test="$uval='minutes'">minute(s)</xsl:when>
			<xsl:when test="$uval='quarter'">quarter(s)</xsl:when>
			<xsl:when test="$uval='quarters'">quarter(s)</xsl:when>
			<xsl:when test="$uval='second'">second(s)</xsl:when>
			<xsl:when test="$uval='seconds'">second(s)</xsl:when>
			<xsl:otherwise><xsl:value-of select="$uval"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>