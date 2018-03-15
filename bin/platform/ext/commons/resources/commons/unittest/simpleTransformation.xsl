<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-function">
<xsl:output method="text" omit-xml-declaration="yes" />
<xsl:preserve-space elements="text"/>

<xsl:template match="b"><xsl:text disable-output-escaping="yes"><![CDATA[<cTypeface:Bold>]]></xsl:text><xsl:apply-templates mode="output"/><xsl:text disable-output-escaping="yes"><![CDATA[<cTypeface:>]]></xsl:text></xsl:template>

</xsl:stylesheet>