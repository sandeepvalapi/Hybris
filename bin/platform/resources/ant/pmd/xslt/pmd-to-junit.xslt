<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:functx="http://www.xsltfunctions.com/xsl/functx-1.0-doc-2007-01.xsl">

	<xsl:output indent="yes" omit-xml-declaration="yes" />

	<!-- Key used to filter violations by class and rule name -->
	<xsl:key name="classAndRule" match="violation" use="concat(@package, '|', @class, '|', @rule)" />
	<xsl:key name="class" match="violation" use="concat(@package, '|', @class)" />
	<xsl:key name="rule" match="violation" use="@rule" />

	<!-- Parameter for extension (folder) name currently being processed. -->
	<xsl:param name="extension">hybris</xsl:param>

	<!-- Root template, generates test suite XML -->
	<xsl:template match="/">
		<!--  Number of tests is total number of violations, number of failures is number of priority 1 violations  -->
		<testsuites name="PMD Version {pmd/@version} report for {$extension}" failures="{count(//violation[@priority = 1])}" errors="0" tests="{count(//violation)}">
			<xsl:apply-templates select="//file" />
			
			<system-out>
<!-- Report total number of violations per rule -->
<!-- Not indented to produce output without leading whitespace -->
Total violations count per rule:
Priority 1:
<xsl:apply-templates select="//violation[generate-id(.) = generate-id(key('rule', @rule)[1]) and @priority = 1]" mode="rule">
	<xsl:sort select="@rule" />
</xsl:apply-templates>
Priority 2:
<xsl:apply-templates select="//violation[generate-id(.) = generate-id(key('rule', @rule)[1]) and @priority = 2]" mode="rule">
	<xsl:sort select="@rule" />
</xsl:apply-templates>
			</system-out>
		</testsuites> 
	</xsl:template>

	<!-- Process each file separately to reduce grouping calculations. -->
	<xsl:template match="file">
		<!-- Group by class and rule name -->
		<xsl:apply-templates select="violation[generate-id(.) = generate-id(key('class', concat(@package, '|', @class))[1])]" mode="class"/>
	</xsl:template>
	
	<!-- Sum up totals per rule -->
	<xsl:template match="violation" mode="rule">
		<xsl:variable name="ruleName" select="@rule" />
		<xsl:value-of select="$ruleName" />
		<xsl:text>: </xsl:text>
		<xsl:value-of select="count(//violation[@rule = $ruleName])" /> 
		<xsl:text><![CDATA[
]]></xsl:text>
	</xsl:template>
	
	<!-- Map classes to test suites -->
	<xsl:template match="violation" mode="class">
		<!-- Get current package and class for use in expressions -->
		<xsl:variable name="class" select="@class" />
		<xsl:variable name="package" select="@package" />
		<!--  Calculate qualified class name -->
		<xsl:variable name="classname" select="concat(@package, '.', @class)" />

  		<testsuite failures="{count(../violation[@priority = 1 and @package = $package and @class = $class])}" errors="0" name="{$classname}" tests="{count(../violation[@package = $package and @class = $class])}" timestamp="{/pmd/@timestamp}">
			<xsl:apply-templates select="../violation[@package = $package and @class = $class and generate-id(.) = generate-id(key('classAndRule', concat(@package, '|', @class, '|', @rule))[1])]" mode="classRule" />
		</testsuite>
	</xsl:template>
	
	<!-- Map each rule to a test case -->
	<xsl:template match="violation" mode="classRule">
		
		<!-- One test case per class and finding -->
		<xsl:variable name="classname" select="concat(@package, '.', @class)" />
	    
		<testcase name="{@rule}" classname="{$classname}" status="Priority {@priority}">
			<xsl:for-each select="key('classAndRule', concat(@package, '|', @class, '|', @rule))">
				<failure message="{normalize-space(text())}" type="{@ruleset}">
					<xsl:if test="@variable">
						<xsl:text>Variable: </xsl:text>
						<xsl:value-of select="@variable" />
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:if test="@method">
						<xsl:text>Method: </xsl:text>
						<xsl:value-of select="@method" />
						<xsl:text>(), </xsl:text>
					</xsl:if>
					<xsl:text>Location: line </xsl:text>
					<xsl:value-of select="@beginline" />
					<xsl:text>, column </xsl:text>
					<xsl:value-of select="@begincolumn" />
					<xsl:text> - line </xsl:text>
					<xsl:value-of select="@endline" />
					<xsl:text>, column </xsl:text>
					<xsl:value-of select="@endcolumn" />
					<xsl:if test="@externalInfoUrl">
						<xsl:text>&#xa;</xsl:text>
						<xsl:value-of select="@externalInfoUrl" />
					</xsl:if>
				</failure>
			</xsl:for-each>
		</testcase>
	</xsl:template>
</xsl:stylesheet>