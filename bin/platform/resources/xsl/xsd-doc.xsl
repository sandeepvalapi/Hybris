<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
<!--
  Copyright (C) DSTC Pty Ltd (ACN 052 372 577) 2002
 
  The software contained on this media is the property of the
  DSTC Pty Ltd.  Use of this software is strictly in accordance
  with the license agreement in the accompanying LICENSE file.
  If your distribution of this software does not contain a
  LICENSE file then you have no rights to use this software
  in any manner and should contact DSTC at the address below
  to determine an appropriate licensing arrangement.
 
      DSTC Pty Ltd
      Level 7, General Purpose South 
      The University of Queensland
      QLD 4072 Australia 
      Tel: +61 7 3365 4310
      Fax: +61 7 3365 4311
      Email: titanium_enquiries@dstc.edu.au
 
  This software is being provided "AS IS" without warranty of
  any kind.  In no event shall DSTC Pty Ltd be liable for
  damage of any kind arising out of or in connection with
  the use or performance of this software.
-->
<!-- 
  File:
     xs3p.xsl
  Description:
     Stylesheet that generates XHTML documentation, given an XML 
     Schema document
  Assumptions:
     -Resulting documentation will only be displayed properly with 
      the latest browsers that support XHTML and CSS. Older 
      browsers are not supported.
     -Assumed that XSD document conforms to the XSD recommendation.
      No validity checking is done.
  Constraints:
     -Local schema components cannot contain two dashes in 
      'documentation' elements within their 'annotation' element.
      This is because the contents of those 'documentation' 
      elements are displayed in a separate window using Javascript. 
      This Javascript code is enclosed in comments, which do not
      allow two dashes inside themselves.
  Notes:
     -Javascript code is placed within comments, even though in 
      strict XHTML, JavaScript code should be placed within CDATA 
      sections. This is because current browsers generate a syntax 
      error if the page contains CDATA sections. Placing Javascript 
      code within comments means that the code cannot contain two 
      dashes.
      (See 'PrintJSCode' named template.)
  Stylesheet Sections:
     -Global Parameters
          Specify parameters that can be set externally to customise 
          stylesheet
     -Constants
          Constants used by the stylesheet
     -Main Document
          Templates to generate the overall document and the top-level 
          sections within it
     -Hierarchy table
          Templates for displaying type hierarchy for simple and 
          complex types, and substitution group hierarchy for elements
     -Properties table
          Templates for displaying the properties of top-level schema 
          components
     -XML Instance Representation table
          Templates for displaying how an XML instance conforming to 
          the schema component would look like
     -XML Pretty Printer
          Templates for displaying arbitrary XML instances
     -Handling Schema Component References
          Templates for generating internal and external links
     -General Utility Templates
          General templates used by other templates in this stylesheet
  To Do List:
     -It is not clever when printing out element and attribute 
      wildcards in the XML Instance Representation tables. It prints 
      out all wildcards, rather than working out the actual wildcard 
      is from multiple wildcard instances.
     -Same as above for simple type constraints, e.g. it doesn't 
      summarise multiple pattern constraints.
-->
<xsl:stylesheet 
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
 xmlns="http://www.w3.org/1999/xhtml" 
 xmlns:html="http://www.w3.org/1999/xhtml" 
 xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
 xmlns:ppp="http://titanium.dstc.edu.au/xml/xs3p" 
 version="1.0" 
 exclude-result-prefixes="xsd ppp html">

   <xsl:output 
    method="xml" 
    encoding="ISO-8859-1"
    standalone="yes"
    version="1.0"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" 
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" 
    indent="yes"/>

   <xsl:key name="type" match="/xsd:schema/xsd:complexType | /xsd:schema/xsd:simpleType | /xsd:schema/xsd:redefine/xsd:complexType | /xsd:schema/xsd:redefine/xsd:simpleType" use="@name" />
   <xsl:key name="complexType" match="/xsd:schema/xsd:complexType | /xsd:schema/xsd:redefine/xsd:complexType" use="@name" />
   <xsl:key name="simpleType" match="/xsd:schema/xsd:simpleType | /xsd:schema/xsd:redefine/xsd:simpleType" use="@name" />
   <xsl:key name="attributeGroup" match="/xsd:schema/xsd:attributeGroup | /xsd:schema/xsd:redefine/xsd:attributeGroup" use="@name" />
   <xsl:key name="group" match="/xsd:schema/xsd:group | /xsd:schema/xsd:redefine/xsd:group" use="@name" />
   <xsl:key name="attribute" match="/xsd:schema/xsd:attribute" use="@name" />
   <xsl:key name="element" match="/xsd:schema/xsd:element" use="@name" />

   <!-- ******** Global Parameters ******** -->

   <!-- Title of XHTML document. -->
   <xsl:param name="title"></xsl:param>

   <!-- If 'true', sorts the top-level schema components by type, 
        then name. Otherwise, displays the components by the order that 
        they appear in the schema. -->
   <xsl:param name="sortByComponent">true</xsl:param>

   <!-- If 'true', XHTML document uses JavaScript for added
        functionality, such as pop-up windows and information-
        hiding.
        Otherwise, XHTML document does not use JavaScript. -->
   <xsl:param name="useJavaScript">true</xsl:param>

   <!-- If 'true', prints all super-types in the
        type hierarchy box.
        Otherwise, prints the parent type only in the
        type hierarchy box. -->
   <xsl:param name="printAllSuperTypes">false</xsl:param>

   <!-- If 'true', prints all sub-types in the
        type hierarchy box.
        Otherwise, prints the direct sub-types only in the
        type hierarchy box. -->
   <xsl:param name="printAllSubTypes">true</xsl:param>

   <!-- If 'true', prints prefix matching namespace of schema
        components in XML Instance Representation tables. -->
   <xsl:param name="printNSPrefixes">false</xsl:param>

   <!-- If 'true', searches 'included' schemas for schema components 
        when generating links and XML Instance Representation tables. -->
   <xsl:param name="searchIncludedSchemas">false</xsl:param>

   <!-- If 'true', searches 'imported' schemas for schema components 
        when generating links and XML Instance Representation tables. -->
   <xsl:param name="searchImportedSchemas">false</xsl:param>

   <!-- File containing the mapping from file locations of external 
        (e.g. included, imported, refined) schemas to file locations 
        of their XHTML documentation. -->
   <xsl:param name="linksFile"></xsl:param>
  
   <!-- Set the base URL for resolving links. -->
   <xsl:param name="baseURL"></xsl:param>
  
   <!-- Uses an external CSS stylesheet rather than using 
        internally-declared CSS properties. -->
   <xsl:param name="externalCSSURL"></xsl:param>

   <!-- Show start and end comments of templates. -->
   <xsl:param name="showComments">false</xsl:param>

   <!-- ******** Constants ******** -->

   <!-- XML Schema Namespace -->
   <xsl:variable name="XSD_NS">http://www.w3.org/2001/XMLSchema</xsl:variable>

   <!-- XML Namespace -->
   <xsl:variable name="XML_NS">http://www.w3.org/XML/1998/namespace</xsl:variable>

   <!-- Number of 'em' to indent from parent element's start tag to
        child element's start tag -->
   <xsl:variable name="ELEM_INDENT">1.5</xsl:variable>

   <!-- Number of 'em' to indent from parent element's start tag to
        attribute's tag -->
   <xsl:variable name="ATTR_INDENT">1.0</xsl:variable>

   <!-- Title to use if none provided -->
   <xsl:variable name="DEFAULT_TITLE">XML Schema Documentation</xsl:variable>

   <!-- Prefixes used for anchor names -->
      <!-- Type definitions -->
   <xsl:variable name="TYPE_PREFIX">type_</xsl:variable>
      <!-- Attribute declarations -->
   <xsl:variable name="ATTR_PREFIX">attribute_</xsl:variable>
      <!-- Attribute group definitions -->
   <xsl:variable name="ATTR_GRP_PREFIX">attributeGroup_</xsl:variable>
      <!-- Complex type definitions -->
   <xsl:variable name="CTYPE_PREFIX" select="$TYPE_PREFIX"/>
      <!-- Element declarations -->
   <xsl:variable name="ELEM_PREFIX">element_</xsl:variable>
      <!-- Key definitions -->
   <xsl:variable name="KEY_PREFIX">key_</xsl:variable>
      <!-- Group definitions -->
   <xsl:variable name="GRP_PREFIX">group_</xsl:variable>
      <!-- Notations -->
   <xsl:variable name="NOTA_PREFIX">notation_</xsl:variable>
      <!-- Namespace declarations -->
   <xsl:variable name="NS_PREFIX">ns_</xsl:variable>
      <!-- Simple type definitions -->
   <xsl:variable name="STYPE_PREFIX" select="$TYPE_PREFIX"/>

   <!-- The original schema needs to be stored because when
        calculating links for references, the links have to be
        relative to the original schema. See 'PrintCompRef'
        template. -->
   <xsl:variable name="ORIGINAL_SCHEMA" select="/xsd:schema"/>

   <!-- ******** Main Document ******** -->

   <!--
     Main template that starts the process
     -->
   <xsl:template match="/xsd:schema">
      <!-- Check that links file is provided if searching external
           schemas for components. -->
      <xsl:if test="$linksFile='' and (normalize-space(translate($searchIncludedSchemas, 'TRUE', 'true'))='true' or normalize-space(translate($searchImportedSchemas, 'TRUE', 'true'))='true')">
         <xsl:call-template name="HandleError">
            <xsl:with-param name="isTerminating">true</xsl:with-param>
            <xsl:with-param name="errorMsg">
'linksFile' variable must be provided if either 
'searchIncludedSchemas' or 'searchImportedSchemas' is true.
            </xsl:with-param>
         </xsl:call-template>
      </xsl:if>

      <!-- Get title of document -->
      <xsl:variable name="actualTitle">
         <xsl:choose>
            <xsl:when test="$title != ''">
               <xsl:value-of select="$title"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$DEFAULT_TITLE"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>

      <html>
         <head>
            <!-- Set title bar -->
            <title><xsl:value-of select="$actualTitle"/></title>

            <!-- Set content type -->
            <meta http-equiv="Content-Type" content="text/xml; charset=iso-8859-1"/>

            <!-- Set base URL to use in working out relative paths -->
            <xsl:if test="$baseURL != ''">
               <xsl:element name="base">
                  <xsl:attribute name="href"><xsl:value-of select="$baseURL"/></xsl:attribute> 
               </xsl:element>
            </xsl:if>

            <!-- Add Javascript code to make the collapseable boxes work -->
            <xsl:if test="normalize-space(translate($useJavaScript,'TRUE','true'))='true'">
               <xsl:call-template name="PrintJSCode">
                  <xsl:with-param name="code">
                     <xsl:call-template name="DocumentJSCode"/>
                  </xsl:with-param>
               </xsl:call-template>
            </xsl:if>
         </head>
         <body>

            <!-- Set CSS styles -->
            <style type="text/css">
               <xsl:choose>
                  <!-- Use external CSS stylesheet -->
                  <xsl:when test="$externalCSSURL != ''">
                     <xsl:text>
@import url(</xsl:text><xsl:value-of select="$externalCSSURL"/><xsl:text>);
</xsl:text>
                  </xsl:when>
                  <!-- Use internal CSS styles -->
                  <xsl:otherwise>
                     <xsl:call-template name="DocumentCSSStyles"/>
                  </xsl:otherwise>
               </xsl:choose>
            </style>

            <!-- Title -->
            <h1><a name="top"><xsl:value-of select="$actualTitle"/></a></h1>
            <hr/>

            <!-- Section: Table of Contents -->
            <h2>Table of Contents</h2>
            <xsl:apply-templates select="." mode="toc"/>
            <xsl:call-template name="SectionFooter"/>

            <!-- Section: Schema Document Properties -->
            <h2><a name="SchemaProperties">Schema Document Properties</a></h2>
            <!-- Sub-section: Properties table -->
            <xsl:apply-templates select="." mode="properties"/>
            <!-- Sub-section: Namespace Legend -->
            <h3>Declared Namespaces</h3>
            <xsl:apply-templates select="." mode="namespaces"/>
            <xsl:call-template name="SectionFooter"/>

            <!-- Section: Redefined Schema Components -->
            <xsl:if test="xsd:redefine">
               <h2><a name="Redefinitions">Redefined Schema Components</a></h2>
               <xsl:apply-templates select="xsd:redefine/xsd:simpleType | xsd:redefine/xsd:complexType | xsd:redefine/xsd:attributeGroup | xsd:redefine/xsd:group" mode="topSection"/>
            </xsl:if>

            <!-- Sections: Top-level Schema Components -->
            <xsl:choose>
               <!-- Sort schema components -->
               <xsl:when test="normalize-space(translate($sortByComponent,'TRUE','true'))='true'">
                  <!-- Declarations -->
                  <xsl:if test="xsd:attribute or xsd:element">
                     <h2><a name="SchemaDeclarations">Global Declarations</a></h2>
                     <xsl:apply-templates select="xsd:attribute | xsd:element" mode="topSection">
                        <xsl:sort select="local-name(.)" order="ascending"/>
                        <xsl:sort select="@name" order="ascending"/>
                     </xsl:apply-templates>
                  </xsl:if>
                  <!-- Definitions -->
                  <xsl:if test="xsd:attributeGroup or xsd:complexType or xsd:group or xsd:notation or xsd:simpleType">
                     <h2><a name="SchemaDefinitions">Global Definitions</a></h2>
                     <xsl:apply-templates select="xsd:attributeGroup | xsd:complexType | xsd:group | xsd:notation | xsd:simpleType" mode="topSection">
                        <xsl:sort select="local-name(.)" order="ascending"/>
                        <xsl:sort select="@name" order="ascending"/>
                     </xsl:apply-templates>
                  </xsl:if>
               </xsl:when>
               <!-- Display schema components as they occur -->
               <xsl:otherwise>
                  <h2><a name="SchemaComponents">Global Schema Components</a></h2>
                  <xsl:apply-templates select="xsd:attribute | xsd:attributeGroup | xsd:complexType | xsd:element | xsd:group | xsd:notation | xsd:simpleType" mode="topSection"/>
               </xsl:otherwise>
            </xsl:choose>

            <!-- Document Footer -->
					<hr/>
					<table class="footer" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td align="left">
								<xsl:text> hybris Platform - Copyright (c) 2000-2013 hybris AG - </xsl:text>
								<a href="http://www.hybris.de">www.hybris.com</a>
							</td>
							<td align="right">
								<xsl:if test="normalize-space(translate($useJavaScript,'TRUE','true'))='true'">
			                  <xsl:text> (Last modified: </xsl:text>
			                  <xsl:call-template name="PrintJSCode">
			                     <xsl:with-param name="code">document.write(document.lastModified);</xsl:with-param>
			                  </xsl:call-template>
									<xsl:text>) </xsl:text>
			               </xsl:if>
		               </td>
						</tr>	
					</table>
         </body>
      </html>
   </xsl:template>

   <!--
     Prints out the table of Declared Namespaces for the
     current schema.
     -->
   <xsl:template match="xsd:schema" mode="namespaces">
      <table class="namespaces">
         <tr>
            <th>Prefix</th>
            <th>Namespace</th>
         </tr>
         <!-- Default namespace (no prefix) -->
         <xsl:if test="namespace::*[local-name(.)='']">
            <xsl:variable name="ns" select="namespace::*[local-name(.)='']"/>
            <tr>
               <td>
                  <a name="{$NS_PREFIX}">Default namespace</a>
               </td>
               <td>
                  <xsl:choose>
                     <xsl:when test="/xsd:schema/@targetNamespace and $ns=normalize-space(/xsd:schema/@targetNamespace)">
                        <span class="targetNS">
                           <xsl:value-of select="$ns"/>
                        </span>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="$ns"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
            </tr>
         </xsl:if>
         <!-- Namespaces with prefixes -->
         <xsl:for-each select="namespace::*[local-name(.)!='']">
            <xsl:variable name="prefix" select="local-name(.)"/>
            <xsl:variable name="ns" select="."/>
            <tr>
               <td>
                  <a name="{concat($NS_PREFIX, $prefix)}">
                     <xsl:value-of select="$prefix"/>
                  </a>
               </td>
               <td>
                  <xsl:choose>
                     <xsl:when test="/xsd:schema/@targetNamespace and $ns=normalize-space(/xsd:schema/@targetNamespace)">
                        <span class="targetNS">
                           <xsl:value-of select="$ns"/>
                        </span>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="$ns"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
            </tr>
         </xsl:for-each>
      </table>
   </xsl:template>

   <!--
     Prints out the Table of Contents.
     -->
   <xsl:template match="xsd:schema" mode="toc">
      <ul>
         <!-- Section: Schema Document Properties -->
         <li>
            <a href="#SchemaProperties">Schema Document Properties</a>
         </li>

         <!-- Section: Redefined Schema Components -->
         <xsl:if test="xsd:redefine">
            <li>
               <a href="#Redefinitions">Redefined Schema Components</a>
            </li>
         </xsl:if>

         <!-- Sections: Top-level Schema Components -->
         <xsl:choose>
            <!-- Sort schema components -->
            <xsl:when test="normalize-space(translate($sortByComponent,'TRUE','true'))='true'">
               <!-- Declarations -->
               <xsl:if test="xsd:attribute or xsd:element">
                  <li><a href="#SchemaDeclarations">Global Declarations</a>
                     <ul>
                        <xsl:apply-templates select="xsd:attribute | xsd:element" mode="toc">
                           <xsl:sort select="local-name(.)" order="ascending"/>
                           <xsl:sort select="@name" order="ascending"/>
                        </xsl:apply-templates>
                     </ul>
                  </li>
               </xsl:if>
               <!-- Definitions -->
               <xsl:if test="xsd:attributeGroup or xsd:complexType or xsd:group or xsd:notation or xsd:simpleType">
                  <li><a href="#SchemaDefinitions">Global Definitions</a>
                     <ul>
                        <xsl:apply-templates select="xsd:attributeGroup | xsd:complexType | xsd:group | xsd:notation | xsd:simpleType" mode="toc">
                           <xsl:sort select="local-name(.)" order="ascending"/>
                           <xsl:sort select="@name" order="ascending"/>
                        </xsl:apply-templates>
                     </ul>
                  </li>
               </xsl:if>
            </xsl:when>
            <!-- Display schema components in order as they appear in schema -->
            <xsl:otherwise>
               <li><a href="#SchemaComponents">Global Schema Components</a>
                  <ul>
                     <xsl:apply-templates select="xsd:attribute | xsd:attributeGroup | xsd:complexType | xsd:element | xsd:group | xsd:notation | xsd:simpleType" mode="toc"/>
                  </ul>
               </li>
            </xsl:otherwise>
         </xsl:choose>
      </ul>
   </xsl:template>

   <!--
     Prints out a link to a top-level schema component section in the
     Table of Contents.
     -->
   <xsl:template match="xsd:*[@name]" mode="toc">
      <xsl:variable name="componentID">
         <xsl:call-template name="GetComponentID">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </xsl:variable>

      <li>
         <a href="#{$componentID}">
            <xsl:call-template name="GetComponentDescription">
               <xsl:with-param name="component" select="."/>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <strong><xsl:value-of select="@name"/></strong>
         </a>
      </li>
   </xsl:template>

   <!--
     Prints out the section for a top-level schema component.
     -->
   <xsl:template match="xsd:*[@name]" mode="topSection">
      <!-- Header -->
      <xsl:call-template name="ComponentSectionHeader">
         <xsl:with-param name="component" select="."/>
      </xsl:call-template>

      <!-- Hierarchy table (for types and elements) -->
      <xsl:apply-templates select="." mode="hierarchy"/>

      <!-- Properties table -->
      <xsl:apply-templates select="." mode="properties"/>

      <!-- XML Instance Representation table -->
      <xsl:call-template name="SampleInstanceTable">
         <xsl:with-param name="component" select="."/>
      </xsl:call-template>

      <!-- Footer -->
      <xsl:call-template name="SectionFooter"/>
   </xsl:template>

   <!--
     Prints out the section header of a top-level schema component.
     Param(s):
            component (Node) required
              Top-level schema component
     -->
   <xsl:template name="ComponentSectionHeader">
      <xsl:param name="component"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: ComponentSectionHeader </xsl:comment>
		</xsl:if>

      <xsl:variable name="componentID">
         <xsl:call-template name="GetComponentID">
            <xsl:with-param name="component" select="$component"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="componentDescription">
         <xsl:call-template name="GetComponentDescription">
            <xsl:with-param name="component" select="$component"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="componentTermRef">
         <xsl:call-template name="GetComponentTermRef">
            <xsl:with-param name="component" select="$component"/>
         </xsl:call-template>
      </xsl:variable>

		<!-- Anchor with horizontal line -->
      <a name="{$componentID}"/>
      <hr/>

      <h3>
         <!-- Description -->
         <xsl:choose>
            <xsl:when test="$componentTermRef != ''">
               <xsl:call-template name="PrintGlossaryTermRef">
                  <xsl:with-param name="code" select="$componentTermRef"/>
                  <xsl:with-param name="term" select="$componentDescription"/>
               </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$componentDescription"/>
            </xsl:otherwise>
         </xsl:choose>
         <xsl:text>: </xsl:text>
         <!-- Name -->
         <span class="name">
            <xsl:value-of select="$component/@name"/>
         </span>
      </h3>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: ComponentSectionHeader </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!-- 
     Prints out footer for top-level sections. 
     -->
   <xsl:template name="SectionFooter">
      <!-- Link to top of page-->
      <div style="text-align: right; clear: both;"><a href="#top">top</a></div>
   </xsl:template>

   <!-- 
     Java Script code required by the entire HTML document.
     -->
   <xsl:template name="DocumentJSCode">
      <!-- Functions -->
      <xsl:text>

/**
 * Returns an element in the current HTML document.
 * 
 * @param elementID Identifier of HTML element
 * @return               HTML element object
 */
function getElementObject(elementID) {
    var elemObj = null;
    if (document.getElementById) {
        elemObj = document.getElementById(elementID);
    }
    return elemObj;
}             


</xsl:text>
   </xsl:template>

   <!--
     CSS properties for the entire HTML document.
     -->
   <xsl:template name="DocumentCSSStyles">
      <xsl:text>
/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
/* More-configurable styles */

/******** General *****k***/

/* Document body */
body {
   color: Black;
   background-color: White;
   font-family: Arial, sans-serif;
   font-size: 10pt;
}
/* Horizontal rules */
hr {
}

/* Document title */
h1 {
   font-size: 14pt;
	color: #000077;
   padding-top: 10px;
}
/* Main section headers */
h2 {
   font-size: 12pt;
}
/* Sub-section headers */
h3, h3 a, h3 span {
   font-size: 11pt;
   font-weight: bold;
   color: black;
}
/* Table displaying the properties of the schema components or the
   schema document itself */
table.properties th, table.properties th a {
   color: black;
   background-color: #dddddd; /* middle gray */
}
table.properties td {
   background-color: #eeeeee; /* light Gray */
}

table.footer		{ width: 100%; margin: 0px; padding: 0px; }
table.footer td	{ font-size: 8pt; margin: 0px; padding: 0px; }


/* links */
a:link						{ color:#000077; text-decoration:underline; }
a:visited					{ color:#000077; text-decoration:underline; }
a:hover						{ color:#000077; text-decoration:underline; }
a:active						{ color:#000077; text-decoration:underline; outline: none;}
a:focus						{ color:#000077; text-decoration:underline; outline: none; }

/* debugging */
.debug	{ border: 1px solid red; }

/******** Table of Contents Section ********/



/******** Schema Document Properties Section ********/

/* Table displaying the namespaces declared in the schema */
table.namespaces th {
   background-color: #ccc;
}
table.namespaces td {
   background-color: #eee;
}
/* Target namespace of the schema */
span.targetNS {
   color: #0021c7;
   font-weight: bold;
}


/******** Schema Components' Sections ********/

/* Name of schema component */
.name {
   color: #000077; /* dark blue */
}

/* Hierarchy table */
table.hierarchy {
}

/* XML Instance Representation table */
div.sample div.contents {
   border: 2px;
}


/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
/* Printer-version styles */

@media print {

/* Ensures that controls are hidden when printing */
div#printerControls {
   visibility: hidden;
}
div#globalControls {
   visibility: hidden;
}

}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
/* Base styles */

/******** General ********/

/* Unordered lists */
ul {
   margin-left: 1.5em;
   margin-bottom: 0em;
}
/* Tables */
table {
   margin-top: 10px;
   margin-bottom: 10px;
   margin-left: 2px;
   margin-right: 2px;
}
table th, table td {
   font-size: 10pt;
   vertical-align: top;
   padding-top: 3px;
   padding-bottom: 3px;
   padding-left: 10px;
   padding-right: 10px;
}
table th {
   font-weight: bold;
   text-align: left;
}
/* Table displaying the properties of the schema components or the
   schema document itself */
table.properties {
}
table.properties th {
	padding-right: 30px;
	white-space: nowrap;
	width: 150px;
	padding-top: 2px;
	padding-bottom: 2px;
}
table.properties td {
	padding-right: 30px;
	padding-top: 2px;
	padding-bottom: 2px;
}
/* Boxes */
div.box {
	margin-top: 5px;
}
   /* Box caption */
div.box div.caption {
   font-weight: bold; 
   margin-top: 20px;
}
   /* Box contents */
div.box div.contents {
   margin-top: 3px;
   margin-left: 10px;
   border: 1px solid #aaaaaa;
}


/******** Table of Contents Section ********/

table.contents { background-color: #eeeeee; border-spacing: 0px; border-collapse: collapse; width:100%; empty-cells:show; }

table.contents th { border-right: 2px solid #ffffff; font-family: monospace; font-size: 9pt; font-weight: normal;
						  padding-bottom: 2px; padding-top: 2px; vertical-align: middle; }
table.contents td { font-size: 9pt; vertical-align: middle; }

.newFields { font-weight: bold; }

.inherited { color: #555555; }

/* Group Headers */
.group, .group a {
   color: #aaaaaa;
}

/******** Schema Document Properties Section ********/

/* Table displaying the namespaces declared in the schema */
table.namespaces th {
}
table.namespaces td {
}
/* Target namespace of the schema */
span.targetNS {
}


/******** Schema Components' Sections ********/

/* Name of schema component */
.name {
}

/* Hierarchy table */
table.hierarchy {
   width: 90%;
}
table.hierarchy th {
   font-weight: bold;
   white-space: nowrap;
}
table.hierarchy th, table.hierarchy td {
   padding: 5px;
}

/* XML Instance Representation table */
div.sample {
   width: 90%;
}
div.sample div.contents {
   padding: 5px;
   font-family: Courier New, sans-serif;
   font-size: 10pt;
}
   /* Normal elements and attributes */
div.sample div.contents, div.sample div.contents a {
   color: black;
}
   /* Group Headers */
div.sample div.contents .group, div.sample div.contents .group a {
   color: #999; /* Light gray */
}
   /* Type Information */
div.sample div.contents .type, div.sample div.contents .type a {
   color: #999; /* Light gray */
}
   /* Occurrence Information */
div.sample div.contents .occurs, div.sample div.contents .occurs a {
   color: #999; /* Light gray */
}
   /* Fixed values */
div.sample div.contents .fixed {
   color: #063; /* Green */
   font-weight: bold;
}
   /* Simple type constraints */
div.sample div.contents .constraint, div.sample div.contents .constraint a {
   color: #999; /* Light gray */
}
   /* Elements and attributes inherited from base type */
div.sample div.contents .inherited, div.sample div.contents .inherited a {
   color: #666; /* Dark gray */
}
   /* Elements and attributes added to or changed from base type */
div.sample div.contents .newFields {
   font-weight: bold;
}
   /* Other type of information */
div.sample div.contents .other, div.sample div.contents .other a {
   color: #369; /* Blue */
   font-style: italic;
}

div.schemaComponent div.contents {
   font-family: Courier New, sans-serif;
   font-size: 10pt;
   padding: 5px;
}
   /* Syntax characters */
div.schemaComponent div.contents {
   color: #00f; /* blue */
}
   /* Element and attribute tags */
div.schemaComponent div.contents .scTag {
   color: #933; /* maroon */
}
   /* Element and attribute content */
div.schemaComponent div.contents .scContent, div.schemaComponent div.contents .scContent a {
   color: black;
   font-weight: bold;
}
   /* Comments */
div.schemaComponent div.contents .comment {
   color: #999; /* Light gray */
}
</xsl:text>
   </xsl:template>


   <!-- ******** Hierarchy table ******** -->

   <!-- 
     Prints out substitution group hierarchy for
     element declarations.
     -->
   <xsl:template match="xsd:element" mode="hierarchy">
      <!--
        Find out members of substitution group that this element 
        heads.
        -->
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:element </xsl:comment>
		</xsl:if>
      <xsl:variable name="members">
         <ul>
            <xsl:call-template name="PrintSGroupMembers">
               <xsl:with-param name="element" select="."/>
            </xsl:call-template>
         </ul>
      </xsl:variable>
      <xsl:variable name="hasMembers">
         <xsl:if test="normalize-space($members)!=''">
            <xsl:text>true</xsl:text>
         </xsl:if>
      </xsl:variable>
      <!-- Print hierarchy table -->
      <xsl:if test="@substitutionGroup or normalize-space($hasMembers)='true'">
         <table class="hierarchy">
            <tr>
               <td>
                  <ul>
                     <!-- Print substitution group that this element belongs to -->
                     <xsl:if test="@substitutionGroup">
                        <li>
                           <em>This element can be used wherever the following element is referenced:</em>
                           <ul>
                              <li>
                                 <xsl:call-template name="PrintElementRef">
                                    <xsl:with-param name="ref" select="@substitutionGroup"/>
                                 </xsl:call-template>
                              </li>
                           </ul>
                        </li>
                     </xsl:if>
                     <!-- Print substitution group that this element heads -->
                     <xsl:if test="normalize-space($hasMembers)='true'">
                        <li>
                           <em>The following elements can be used wherever this element is referenced:</em>
                           <xsl:copy-of select="$members"/>
                        </li>
                     </xsl:if>
                  </ul>
               </td>
            </tr>
         </table>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:element </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!-- 
     Prints out Hierarchy table for complex type definitions.
     -->
   <xsl:template match="xsd:complexType" mode="hierarchy">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:complexType </xsl:comment>
		</xsl:if>
      <table class="properties">
         <!-- Print super types -->
         <tr>
            <th>
               <xsl:choose>
                  <xsl:when test="normalize-space(translate($printAllSuperTypes, 'TRUE', 'true'))='true'">
                     <xsl:text>Super-types:</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>Parent type:</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </th>
            <td>
               <xsl:choose>
                  <xsl:when test="xsd:simpleContent or xsd:complexContent">
                     <xsl:call-template name="PrintSupertypes">
                        <xsl:with-param name="type" select="."/>
                     </xsl:call-template>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>None</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </td>
         </tr>
         <!-- Print sub types -->
         <tr>
            <th>
               <xsl:choose>
                  <xsl:when test="normalize-space(translate($printAllSubTypes, 'TRUE', 'true'))='true'">
                     <xsl:text>Sub-types:</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>Direct sub-types:</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </th>
            <td>
               <xsl:call-template name="PrintComplexSubtypes">
                  <xsl:with-param name="type" select="."/>
               </xsl:call-template>
            </td>
         </tr>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:complexType </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!-- 
     Prints out Hierarchy table for simple type definitions.
     -->
   <xsl:template match="xsd:simpleType" mode="hierarchy">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:simplexType </xsl:comment>
		</xsl:if>
      <table class="hierarchy">
         <!-- Print super types -->
         <tr>
            <th>
               <xsl:choose>
                  <xsl:when test="normalize-space(translate($printAllSuperTypes, 'TRUE', 'true'))='true'">
                     <xsl:text>Super-types:</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>Parent type:</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </th>
            <td>
               <xsl:choose>
                  <xsl:when test="xsd:restriction">
                     <xsl:call-template name="PrintSupertypes">
                        <xsl:with-param name="type" select="."/>
                     </xsl:call-template>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>None</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </td>
         </tr>
         <!-- Print sub types -->
         <tr>
            <th>
               <xsl:choose>
                  <xsl:when test="normalize-space(translate($printAllSubTypes, 'TRUE', 'true'))='true'">
                     <xsl:text>Sub-types:</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>Direct sub-types:</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </th>
            <td>
               <xsl:call-template name="PrintSimpleSubtypes">
                  <xsl:with-param name="type" select="."/>
               </xsl:call-template>
            </td>
         </tr>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:simpleType </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Unmatched template for 'hierarchy' mode
     -->
   <xsl:template match="*" mode="hierarchy"/>

   <!-- 
     Prints out members, if any, of the substitution group that a 
     given element declaration heads.
     Assumes it will be called within XHTML <ul> tags.
     Param(s):
            element (Node) required
              Top-level element declaration
            elementList (String) optional
                List of elements in this call chain. Name of element starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSGroupMembers">
      <xsl:param name="element"/>
      <xsl:param name="elementList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSGroupMembers </xsl:comment>
		</xsl:if>

      <xsl:variable name="elemName" select="normalize-space($element/@name)"/>
      <xsl:choose>
         <xsl:when test="contains($elementList, concat('*', $elemName, '+'))">
            <!-- Circular element substitution group hierarchy -->
            <li>
               <xsl:call-template name="HandleError">
                  <xsl:with-param name="isTerminating">false</xsl:with-param>
                  <xsl:with-param name="errorMsg">
                     <xsl:text>Circular element reference to: </xsl:text>
                     <xsl:value-of select="$elemName"/>
                  </xsl:with-param>
               </xsl:call-template>
            </li>
         </xsl:when>
         <xsl:otherwise>
            <!-- Get 'block' attribute. -->
            <xsl:variable name="block">
               <xsl:call-template name="PrintBlockSet">
                  <xsl:with-param name="EBV">
                     <xsl:choose>
                        <xsl:when test="$element/@block">
                           <xsl:value-of select="$element/@block"/>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:value-of select="/xsd:schema/@blockDefault"/>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:with-param>
               </xsl:call-template>
            </xsl:variable>

            <xsl:for-each select="/xsd:schema/xsd:element[normalize-space(@substitutionGroup)=$elemName or normalize-space(substring-after(@substitutionGroup, ':'))=$elemName]">
               <li>
                  <xsl:call-template name="PrintElementRef">
                     <xsl:with-param name="name" select="@name"/>
                  </xsl:call-template>
               </li>
               <!-- Recursively find members of a substitution group that 
                    current element in list might head, since substitution 
                    groups are transitive (unless 'substitution' is 
                    blocked). 
                -->
               <xsl:if test="not(contains($block, 'substitution'))">
                  <xsl:call-template name="PrintSGroupMembers">
                     <xsl:with-param name="element" select="."/>
                     <xsl:with-param name="elementList" select="concat($elementList, '*', $elemName, '+')"/>
                  </xsl:call-template>
               </xsl:if>
            </xsl:for-each>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSGroupMembers </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the super types of a given type definition.
     Param(s):
            type (Node) required
                Type definition
            isCallingType (boolean) optional
                If true, 'type' is the type definition that starts 
                this call. Otherwise, this is a recursive call from 
                'PrintSupertypes' itself.
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSupertypes">
      <xsl:param name="type"/>
      <xsl:param name="isCallingType">true</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSupertypes </xsl:comment>
		</xsl:if>

      <xsl:variable name="typeName" select="$type/@name"/>
      <xsl:choose>
         <!-- Circular type hierarchy -->
         <xsl:when test="contains($typeList, concat('*', $typeName, '+'))">
            <!-- Note: Error message will be written out in the Sample Instance table. -->
            <xsl:value-of select="$typeName"/>
         </xsl:when>
         <xsl:otherwise>
            <!-- Get base type reference -->
            <xsl:variable name="baseTypeRef">
               <xsl:choose>
                  <!-- Complex type definition -->
                  <xsl:when test="local-name($type)='complexType'">
                     <xsl:choose>
                        <xsl:when test="$type/xsd:simpleContent/xsd:extension">
                           <xsl:value-of select="$type/xsd:simpleContent/xsd:extension/@base"/>
                        </xsl:when>
                        <xsl:when test="$type/xsd:simpleContent/xsd:restriction">
                           <xsl:value-of select="$type/xsd:simpleContent/xsd:restriction/@base"/>
                        </xsl:when>
                        <xsl:when test="$type/xsd:complexContent/xsd:extension">
                           <xsl:value-of select="$type/xsd:complexContent/xsd:extension/@base"/>
                        </xsl:when>
                        <xsl:when test="$type/xsd:complexContent/xsd:restriction">
                           <xsl:value-of select="$type/xsd:complexContent/xsd:restriction/@base"/>
                        </xsl:when>
                     </xsl:choose>
                  </xsl:when>
                  <!-- Simple type definition -->
                  <xsl:when test="local-name($type)='simpleType'">
                     <xsl:choose>
                        <xsl:when test="$type/xsd:restriction/@base">
                           <xsl:value-of select="$type/xsd:restriction/@base"/>
                        </xsl:when>
                        <xsl:when test="$type/xsd:restriction/xsd:simpleType">
                           <xsl:text>Local type definition</xsl:text>
                        </xsl:when>
                     </xsl:choose>
                  </xsl:when>
               </xsl:choose>
            </xsl:variable>

            <!-- Get derivation method -->
            <xsl:variable name="derive">
               <!-- Complex type definition -->
               <xsl:choose>
                  <xsl:when test="local-name($type)='complexType'">
                     <xsl:choose>
                        <xsl:when test="$type/xsd:simpleContent/xsd:extension or $type/xsd:complexContent/xsd:extension">
                           <xsl:text>extension</xsl:text>
                        </xsl:when>
                        <xsl:when test="$type/xsd:simpleContent/xsd:restriction or $type/xsd:complexContent/xsd:restriction">
                           <xsl:text>restriction</xsl:text>
                        </xsl:when>
                     </xsl:choose>
                  </xsl:when>
                  <!-- Simple type definition -->
                  <xsl:when test="local-name($type)='simpleType'">
                     <xsl:text>restriction</xsl:text>
                  </xsl:when>
               </xsl:choose>
            </xsl:variable>

            <xsl:choose>
               <!-- Print out entire hierarchy -->
               <xsl:when test="normalize-space(translate($printAllSuperTypes, 'TRUE', 'true'))='true'">
                  <xsl:choose>
                     <xsl:when test="normalize-space($baseTypeRef)='Local type definition'">
                        <xsl:value-of select="$baseTypeRef"/>
                        <!-- Symbol to indicate type derivation-->
                        <xsl:text> &lt; </xsl:text>
                     </xsl:when>
                     <xsl:when test="normalize-space($baseTypeRef)!=''">
                        <!-- Get base type name from reference -->
                        <xsl:variable name="baseTypeName">
                           <xsl:call-template name="GetRefName">
                              <xsl:with-param name="ref" select="$baseTypeRef"/>
                           </xsl:call-template>
                        </xsl:variable>
                        <!-- Get base type definition from schema -->
                        <xsl:variable name="baseType" select="key('type', $baseTypeName)"/>
                        <xsl:choose>
                           <!-- Base type was found in this schema -->
                           <xsl:when test="$baseType">
                              <!-- Make recursive call to print out base type and itself -->
                              <xsl:call-template name="PrintSupertypes">
                                 <xsl:with-param name="type" select="$baseType"/>
                                 <xsl:with-param name="isCallingType">false</xsl:with-param>
                                 <xsl:with-param name="typeList" select="concat($typeList, '*', $typeName, '+')"/>
                              </xsl:call-template>
                           </xsl:when>
                           <!-- Base type was not found in this schema -->
                           <xsl:otherwise>
                              <xsl:call-template name="PrintTypeRef">
                                 <xsl:with-param name="ref" select="$baseTypeRef"/>
                              </xsl:call-template>
                           </xsl:otherwise>
                        </xsl:choose>
                        <!-- Symbol to indicate type derivation -->
                        <xsl:text> &lt; </xsl:text>
                     </xsl:when>
                     <xsl:otherwise>
                        <!-- IGNORE: Base type may not be exist probably because 
                            current type does not be derived from another type.
                        -->
                     </xsl:otherwise>
                  </xsl:choose>
                  <!-- Print out current type's name -->
                  <xsl:choose>
                     <xsl:when test="$isCallingType='true'">
                        <strong><xsl:value-of select="$typeName"/></strong>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:call-template name="PrintTypeRef">
                           <xsl:with-param name="name" select="$typeName"/>
                        </xsl:call-template>
                     </xsl:otherwise>
                  </xsl:choose>
                  <!-- Print out derivation method -->
                  <xsl:if test="$derive != ''">
                     <xsl:text> (by </xsl:text>
                     <xsl:value-of select="$derive"/>
                     <xsl:text>)</xsl:text>
                  </xsl:if>
               </xsl:when>
               <!-- Print out parent type only -->
               <xsl:otherwise>
                  <!-- Print out base type reference -->
                  <xsl:choose>
                     <xsl:when test="normalize-space($baseTypeRef)='Local type definition'">
                        <xsl:value-of select="$baseTypeRef"/>
                     </xsl:when>
                     <xsl:when test="$baseTypeRef!=''">
                        <xsl:call-template name="PrintTypeRef">
                           <xsl:with-param name="ref" select="$baseTypeRef"/>
                        </xsl:call-template>
                     </xsl:when>
                     <xsl:otherwise>
                        <!-- IGNORE: Base type may not be exist probably because 
                             current type does not be derived from another type.
                        -->
                     </xsl:otherwise>
                  </xsl:choose>
                  <!-- Print out derivation method -->
                  <xsl:if test="$derive != ''">
                     <xsl:text> (derivation method: </xsl:text>
                     <xsl:value-of select="$derive"/>
                     <xsl:text>)</xsl:text>
                  </xsl:if>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSupertypes </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the sub types of a given complex type definition.
     Param(s):
            type (Node) required
                Complex type definition
            isCallingType (boolean) optional
                If true, 'type' is the type definition that starts this 
                call. Otherwise, this is a recursive call from 
                'PrintComplexSubtypes' itself.
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintComplexSubtypes">
      <xsl:param name="type"/>
      <xsl:param name="isCallingType">true</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintComplexSubtypes </xsl:comment>
		</xsl:if>

      <xsl:variable name="typeName" select="normalize-space($type/@name)"/>
      <xsl:choose>
         <!-- Circular type hierarchy -->
         <xsl:when test="contains($typeList, concat('*', $typeName, '+'))">
            <!-- Do nothing. Note: Error message will be written out in the Sample Instance table. -->
         </xsl:when>
         <xsl:otherwise>
            <!-- Find sub types -->
            <xsl:variable name="subTypes">
               <ul>
                  <xsl:for-each select="/xsd:schema/xsd:complexType/xsd:complexContent/xsd:restriction[normalize-space(@base)=$typeName or normalize-space(substring-after(@base, ':'))=$typeName] | /xsd:schema/xsd:complexType/xsd:complexContent/xsd:extension[normalize-space(@base)=$typeName or normalize-space(substring-after(@base, ':'))=$typeName]">
                     <li>
                        <xsl:variable name="subType" select="../.."/>
                        <!-- Write out type name -->
                        <xsl:call-template name="PrintTypeRef">
                           <xsl:with-param name="name" select="$subType/@name"/>
                        </xsl:call-template>
                        <!-- Write derivation method -->
                        <xsl:text> (by </xsl:text>
                        <xsl:value-of select="local-name(.)"/>
                        <xsl:text>)</xsl:text>
                        <!-- Make recursive call to write sub-types of this sub-type -->
                        <xsl:if test="normalize-space(translate($printAllSubTypes, 'TRUE', 'true'))='true'">
                           <xsl:call-template name="PrintComplexSubtypes">
                              <xsl:with-param name="type" select="$subType"/>
                              <xsl:with-param name="isCallingType">false</xsl:with-param>
                              <xsl:with-param name="typeList" select="concat($typeList, '*', $typeName, '+')"/>
                           </xsl:call-template>
                        </xsl:if>
                     </li>
                  </xsl:for-each>
               </ul>
            </xsl:variable>
            <!-- Print out sub types -->
            <xsl:choose>
               <xsl:when test="normalize-space($subTypes)!=''">
                  <xsl:copy-of select="$subTypes"/>
               </xsl:when>
               <xsl:when test="$isCallingType='true'">
                  <xsl:text>None</xsl:text>
               </xsl:when>
            </xsl:choose>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintComplexSubtypes </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
        Prints out the sub types of a given simple type definition.
        Param(s):
            type (Node) required
                Simple type definition
            isCallingType (boolean) optional
                If true, 'type' is the type definition that starts this 
                call. Otherwise, this is a recursive call from 
                'PrintSimpleSubtypes'
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSimpleSubtypes">
      <xsl:param name="type"/>
      <xsl:param name="isCallingType">true</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSimpleSubtypes </xsl:comment>
		</xsl:if>

      <xsl:variable name="typeName" select="normalize-space($type/@name)"/>
      <xsl:choose>
         <!-- Circular type hierarchy -->
         <xsl:when test="contains($typeList, concat('*', $typeName, '+'))">
            <!-- Do nothing. Note: Error message will be written out in the Sample Instance table. -->
         </xsl:when>
         <xsl:otherwise>
            <!-- Find sub-types that are simple type definitions -->
            <xsl:variable name="simpleSubTypes">
               <ul>
                  <xsl:for-each select="/xsd:schema/xsd:simpleType/xsd:restriction[normalize-space(@base)=$typeName or normalize-space(substring-after(@base, ':'))=$typeName]">
                     <li>
                        <xsl:variable name="subType" select=".."/>
                        <!-- Write out type name -->
                        <xsl:call-template name="PrintTypeRef">
                           <xsl:with-param name="name" select="$subType/@name"/>
                        </xsl:call-template>
                        <!-- Write derivation method -->
                        <xsl:text> (by restriction)</xsl:text>
                        <!-- Make recursive call to write sub-types of this sub-type -->
                        <xsl:if test="normalize-space(translate($printAllSubTypes, 'TRUE', 'true'))='true'">
                           <xsl:call-template name="PrintSimpleSubtypes">
                              <xsl:with-param name="type" select="$subType"/>
                              <xsl:with-param name="isCallingType">false</xsl:with-param>
                              <xsl:with-param name="typeList" select="concat($typeList, '*', $typeName, '+')"/>
                           </xsl:call-template>
                        </xsl:if>
                     </li>
                  </xsl:for-each>
               </ul>
            </xsl:variable>
            <!-- Find sub-types that are complex type definitions -->
            <xsl:variable name="complexSubTypes">
               <ul>
                  <xsl:for-each select="/xsd:schema/xsd:complexType/xsd:simpleContent/xsd:restriction[normalize-space(@base)=$typeName or normalize-space(substring-after(@base, ':'))=$typeName] | /xsd:schema/xsd:complexType/xsd:simpleContent/xsd:extension[normalize-space(@base)=$typeName or normalize-space(substring-after(@base, ':'))=$typeName]">
                     <li>
                        <xsl:variable name="subType" select="../.."/>
                        <!-- Write out type name -->
                        <xsl:call-template name="PrintTypeRef">
                           <xsl:with-param name="name" select="$subType/@name"/>
                        </xsl:call-template>
                        <!-- Write derivation method -->
                        <xsl:text> (by </xsl:text>
                        <xsl:value-of select="local-name(.)"/>
                        <xsl:text>)</xsl:text>
                        <!-- Make recursive call to write sub-types of this sub-type -->
                        <xsl:if test="normalize-space(translate($printAllSubTypes, 'TRUE', 'true'))='true'">
                           <xsl:call-template name="PrintComplexSubtypes">
                              <xsl:with-param name="type" select="$subType"/>
                              <xsl:with-param name="isCallingType">false</xsl:with-param>
                              <xsl:with-param name="typeList" select="concat($typeList, '*', $typeName, '+')"/>
                           </xsl:call-template>
                        </xsl:if>
                     </li>
                  </xsl:for-each>
               </ul>
            </xsl:variable>

            <xsl:variable name="hasSimpleSubTypes">
               <xsl:if test="normalize-space($simpleSubTypes)!=''">
                  <xsl:text>true</xsl:text>
               </xsl:if>
            </xsl:variable>
            <xsl:variable name="hasComplexSubTypes">
               <xsl:if test="normalize-space($complexSubTypes)!=''">
                  <xsl:text>true</xsl:text>
               </xsl:if>
            </xsl:variable>
            <!-- Print out sub types -->
            <xsl:choose>
               <xsl:when test="$hasSimpleSubTypes='true' or $hasComplexSubTypes='true'">
                  <xsl:if test="$hasSimpleSubTypes='true'">
                     <xsl:copy-of select="$simpleSubTypes"/>
                  </xsl:if>
                  <xsl:if test="$hasComplexSubTypes='true'">
                     <xsl:copy-of select="$complexSubTypes"/>
                  </xsl:if>
               </xsl:when>
               <xsl:when test="$isCallingType='true'">
                  <xsl:text>None</xsl:text>
               </xsl:when>
            </xsl:choose>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSimpleSubtypes </xsl:comment>
		</xsl:if>
   </xsl:template>


   <!-- ******** Properties table ******** -->

   <!--
     Prints out the contents of an 'appinfo' or a 'documentation'
     element in the Properties table.
     -->
   <xsl:template match="xsd:appinfo | xsd:documentation" mode="properties">
      <!-- Print out children using XML pretty printer templates -->
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:appinfo | xsd:documentation (mode: properties)</xsl:comment>
		</xsl:if>
      <xsl:choose>
         <xsl:when test="local-name(.)='appinfo'">
            <xsl:apply-templates select="* | text()" mode="xpp"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:apply-templates select="* | text()" mode="html"/>
         </xsl:otherwise>
      </xsl:choose>

      <!-- Print out URL in the source attribute. -->
      <xsl:if test="@source">
         <xsl:if test="* | text()"><br/></xsl:if>
         <xsl:text> More information at: </xsl:text>
         <xsl:call-template name="PrintURI">
            <xsl:with-param name="uri" select="@source"/>
         </xsl:call-template>
         <xsl:text>.</xsl:text>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:appinfo | xsd:documentation (mode: properties)</xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the Properties table for a top-level
     attribute declaration.
     -->
   <xsl:template match="xsd:attribute" mode="properties">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:attribute (mode: properties) </xsl:comment>
		</xsl:if>
      <table class="properties">
         <!-- Type -->
         <tr>
            <th>Type</th>
            <td>
               <xsl:choose>
                  <xsl:when test="xsd:simpleType">
                     <xsl:text>Locally-defined simple type</xsl:text>
                  </xsl:when>
                  <xsl:when test="@type">
                     <xsl:call-template name="PrintTypeRef">
                        <xsl:with-param name="ref" select="@type"/>
                     </xsl:call-template>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>anySimpleType</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </td>
         </tr>
         <!-- Default Value -->
         <xsl:if test="@default">
            <tr>
               <th>Default Value</th>
               <td><xsl:value-of select="@default"/></td>
            </tr>
         </xsl:if>
         <!-- Fixed Value -->
         <xsl:if test="@fixed">
            <tr>
               <th>Fixed Value</th>
               <td><xsl:value-of select="@fixed"/></td>
            </tr>
         </xsl:if>
         <!-- Annotation -->
         <xsl:call-template name="PrintAnnotation">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:attribute (mode: properties) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out Properties table for a top-level
     attribute group or model group definition.
     -->
   <xsl:template match="xsd:attributeGroup | xsd:group" mode="properties">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:attributeGroup | xsd:group (mode: properties) </xsl:comment>
		</xsl:if>
      <table class="properties">
         <!-- Annotation -->
         <xsl:call-template name="PrintAnnotation">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:attributeGroup | xsd:group (mode: properties) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the Properties table for a top-level
     complex type definition.
     -->
   <xsl:template match="xsd:complexType" mode="properties">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: complexType (mode: properties) </xsl:comment>
		</xsl:if>
      <table class="properties">
         <!-- Abstract -->
         <tr>
            <th>
               <xsl:call-template name="PrintGlossaryTermRef">
                  <xsl:with-param name="code">Abstract</xsl:with-param>
                  <xsl:with-param name="term">Abstract</xsl:with-param>
               </xsl:call-template>
            </th>
            <td>
               <xsl:call-template name="PrintBoolean">
                  <xsl:with-param name="boolean" select="@abstract"/>
               </xsl:call-template>
            </td>
         </tr>
         <!-- Final -->
         <xsl:variable name="final">
            <xsl:call-template name="PrintDerivationSet">
               <xsl:with-param name="EBV">
                  <xsl:choose>
                     <xsl:when test="@final">
                        <xsl:value-of select="@final"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="/xsd:schema/@finalDefault"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
            </xsl:call-template>
         </xsl:variable>
         <xsl:if test="normalize-space($final)!=''">
            <tr>
               <th>
                  <xsl:call-template name="PrintGlossaryTermRef">
                     <xsl:with-param name="code">TypeFinal</xsl:with-param>
                     <xsl:with-param name="term">Prohibited Derivations</xsl:with-param>
                  </xsl:call-template>
               </th>
               <td><xsl:value-of select="$final"/></td>
            </tr>
         </xsl:if>
         <!-- Block -->
         <xsl:variable name="block">
            <xsl:call-template name="PrintDerivationSet">
               <xsl:with-param name="EBV">
                  <xsl:choose>
                     <xsl:when test="@block">
                        <xsl:value-of select="@block"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="/xsd:schema/@blockDefault"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
            </xsl:call-template>
         </xsl:variable>
         <xsl:if test="normalize-space($block)!=''">
            <tr>
               <th>
                  <xsl:call-template name="PrintGlossaryTermRef">
                     <xsl:with-param name="code">TypeBlock</xsl:with-param>
                     <xsl:with-param name="term">Prohibited Substitutions</xsl:with-param>
                  </xsl:call-template>
               </th>
               <td><xsl:value-of select="$block"/></td>
            </tr>
         </xsl:if>
         <!-- Annotation -->
         <xsl:call-template name="PrintAnnotation">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: complexType (mode: properties) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the Properties table for a top-level
     element declaration.
     -->
   <xsl:template match="xsd:element" mode="properties">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:element (mode: properties) </xsl:comment>
		</xsl:if>
      <table class="properties">
         <!-- Type -->
         <tr>
            <th>Type</th>
            <td>
               <xsl:choose>
                  <xsl:when test="xsd:simpleType">
                     <xsl:text>Locally-defined simple type</xsl:text>
                  </xsl:when>
                  <xsl:when test="xsd:complexType">
                     <xsl:text>Locally-defined complex type</xsl:text>
                  </xsl:when>
                  <xsl:when test="@type">
                     <xsl:call-template name="PrintTypeRef">
                        <xsl:with-param name="ref" select="@type"/>
                     </xsl:call-template>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>anyType</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </td>
         </tr>
         <!-- Nillable -->
         <tr>
            <th>
               <xsl:call-template name="PrintGlossaryTermRef">
                  <xsl:with-param name="code">Nillable</xsl:with-param>
                  <xsl:with-param name="term">Nillable</xsl:with-param>
               </xsl:call-template>
            </th>
            <td>
               <xsl:call-template name="PrintBoolean">
                  <xsl:with-param name="boolean" select="@nillable"/>
               </xsl:call-template>
            </td>
         </tr>
         <!-- Abstract -->
         <tr>
            <th>
               <xsl:call-template name="PrintGlossaryTermRef">
                  <xsl:with-param name="code">Abstract</xsl:with-param>
                  <xsl:with-param name="term">Abstract</xsl:with-param>
               </xsl:call-template>
            </th>
            <td>
               <xsl:call-template name="PrintBoolean">
                  <xsl:with-param name="boolean" select="@abstract"/>
               </xsl:call-template>
            </td>
         </tr>
         <!-- Default Value -->
         <xsl:if test="@default">
            <tr>
               <th>Default Value</th>
               <td><xsl:value-of select="@default"/></td>
            </tr>
         </xsl:if>
         <!-- Fixed Value -->
         <xsl:if test="@fixed">
            <tr>
               <th>Fixed Value</th>
               <td><xsl:value-of select="@fixed"/></td>
            </tr>
         </xsl:if>
         <!-- Final -->
         <xsl:variable name="final">
            <xsl:call-template name="PrintDerivationSet">
               <xsl:with-param name="EBV">
                  <xsl:choose>
                     <xsl:when test="@final">
                        <xsl:value-of select="@final"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="/xsd:schema/@finalDefault"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
            </xsl:call-template>
         </xsl:variable>
         <xsl:if test="normalize-space($final)!=''">
            <tr>
               <th>
                  <xsl:call-template name="PrintGlossaryTermRef">
                     <xsl:with-param name="code">ElemFinal</xsl:with-param>
                     <xsl:with-param name="term">Substitution Group Exclusions</xsl:with-param>
                  </xsl:call-template>
               </th>
               <td><xsl:value-of select="$final"/></td>
            </tr>
         </xsl:if>
         <!-- Block -->
         <xsl:variable name="block">
            <xsl:call-template name="PrintBlockSet">
               <xsl:with-param name="EBV">
                  <xsl:choose>
                     <xsl:when test="@block">
                        <xsl:value-of select="@block"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="/xsd:schema/@blockDefault"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
            </xsl:call-template>
         </xsl:variable>
         <xsl:if test="normalize-space($block)!=''">
            <tr>
               <th>
                  <xsl:call-template name="PrintGlossaryTermRef">
                     <xsl:with-param name="code">ElemBlock</xsl:with-param>
                     <xsl:with-param name="term">Disallowed Substitutions</xsl:with-param>
                  </xsl:call-template>
               </th>
               <td><xsl:value-of select="$block"/></td>
            </tr>
         </xsl:if>
         <!-- Annotation -->
         <xsl:call-template name="PrintAnnotation">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:element (mode: properties) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the Properties table for a top-level
     notation declaration.
     -->
   <xsl:template match="xsd:notation" mode="properties">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:notation (mode: properties) </xsl:comment>
		</xsl:if>
      <table class="properties">
         <!-- Public Identifier -->
         <tr>
            <th>Public Identifier</th>
            <td><xsl:value-of select="@public"/></td>
         </tr>
         <!-- System Identifier -->
         <xsl:if test="@system">
            <tr>
               <th>System Identifier</th>
               <td><xsl:value-of select="@system"/></td>
            </tr>
         </xsl:if>
         <!-- Annotation -->
         <xsl:call-template name="PrintAnnotation">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:notation (mode: properties) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the Properties table for the root
     schema element.
     -->
   <xsl:template match="xsd:schema" mode="properties">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:schema (mode: properties)</xsl:comment>
		</xsl:if>
      <table class="properties">
         <!-- Target Namespace -->
         <tr>
            <th>
               <xsl:call-template name="PrintGlossaryTermRef">
                  <xsl:with-param name="code">TargetNS</xsl:with-param>
                  <xsl:with-param name="term">Target Namespace</xsl:with-param>
               </xsl:call-template>
            </th>
            <td>
               <xsl:choose>
                  <xsl:when test="@targetNamespace">
                     <span class="targetNS">
                        <xsl:value-of select="@targetNamespace"/>
                     </span>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>None</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </td>
         </tr>
         <!-- Version -->
         <xsl:if test="@version">
            <tr>
               <th>Version</th>
               <td><xsl:value-of select="@version"/></td>
            </tr>
         </xsl:if>
         <!-- Language -->
         <xsl:if test="@xml:lang">
            <tr>
               <th>Language</th>
               <td><xsl:value-of select="@xml:lang"/></td>
            </tr>
         </xsl:if>
         <!-- Element/Attribute Form Defaults -->
         <tr>
            <th>Element and Attribute Namespaces</th>
            <td>
               <ul>
                  <li>Global element and attribute declarations belong to this schema's target namespace.</li>
                  <li>
                     <xsl:choose>
                        <xsl:when test="normalize-space(@elementFormDefault)='qualified'">
                           <xsl:text>By default, local element declarations belong to this schema's target namespace.</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:text>By default, local element declarations have no namespace.</xsl:text>
                        </xsl:otherwise>
                     </xsl:choose>
                  </li>
                  <li>
                     <xsl:choose>
                        <xsl:when test="normalize-space(@attributeFormDefault)='qualified'">
                           <xsl:text>By default, local attribute declarations belong to this schema's target namespace.</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:text>By default, local attribute declarations have no namespace.</xsl:text>
                        </xsl:otherwise>
                     </xsl:choose>
                  </li>
               </ul>
            </td>
         </tr>
         <!-- Schema Composition, e.g. include, import, redefine -->
         <xsl:if test="xsd:include or xsd:import or xsd:redefine">
            <tr>
               <th>Schema Composition</th>
               <td>
                  <ul>
                     <!-- Import -->
                     <xsl:if test="xsd:import">
                        <li>
                           <xsl:text>This schema imports schema(s) from the following namespace(s):</xsl:text>
                           <ul>
                           <xsl:for-each select="xsd:import">
                              <li>
                                 <em><xsl:value-of select="@namespace"/></em>
                                 <xsl:if test="@schemaLocation">
                                    <xsl:text> (at </xsl:text>
                                    <xsl:call-template name="PrintSchemaLink">
                                       <xsl:with-param name="uri" select="@schemaLocation"/>
                                    </xsl:call-template>
                                    <xsl:text>)</xsl:text>
                                 </xsl:if>
                              </li>
                           </xsl:for-each>
                           </ul>
                        </li>
                     </xsl:if>
                     <!-- Include -->
                     <xsl:if test="xsd:include">
                        <li>
                           <xsl:text>This schema includes components from the following schema document(s):</xsl:text>
                           <ul>
                           <xsl:for-each select="xsd:include">
                              <li>
                                 <xsl:call-template name="PrintSchemaLink">
                                    <xsl:with-param name="uri" select="@schemaLocation"/>
                                 </xsl:call-template>
                              </li>
                           </xsl:for-each>
                           </ul>
                        </li>
                     </xsl:if>
                     <!-- Redefine -->
                     <xsl:if test="xsd:redefine">
                        <li>
                           <xsl:text>This schema includes components from the following schema document(s), where some of the components have been redefined:</xsl:text>
                           <ul>
                              <xsl:for-each select="xsd:redefine">
                                 <li>
                                 <xsl:call-template name="PrintSchemaLink">
                                    <xsl:with-param name="uri" select="@schemaLocation"/>
                                 </xsl:call-template>
                                 </li>
                              </xsl:for-each>
                           </ul>
                        <xsl:text>See </xsl:text><a href="#Redefinitions">Redefined Schema Components</a><xsl:text> section.</xsl:text>
                        </li>
                     </xsl:if>
                  </ul>
               </td>
            </tr>
         </xsl:if>
         <!-- Annotation -->
         <xsl:call-template name="PrintAnnotation">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:schema (mode: properties) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the Properties table for a top-level
     simple type definition.
     -->
   <xsl:template match="xsd:simpleType" mode="properties">
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:simpleType (mode:properties) </xsl:comment>
		</xsl:if>
      <table class="properties">
         <!-- Constraints -->
         <tr>
            <th>Content</th>
            <td>
               <xsl:call-template name="PrintSimpleConstraints">
                  <xsl:with-param name="simpleContent" select="."/>
               </xsl:call-template>
            </td>
         </tr>
         <!-- Final -->
         <xsl:variable name="final">
            <xsl:call-template name="PrintSimpleDerivationSet">
               <xsl:with-param name="EBV">
                  <xsl:choose>
                     <xsl:when test="@final">
                        <xsl:value-of select="@final"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="/xsd:schema/@finalDefault"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
            </xsl:call-template>
         </xsl:variable>
         <xsl:if test="normalize-space($final)!=''">
            <tr>
               <th>
                  <xsl:call-template name="PrintGlossaryTermRef">
                     <xsl:with-param name="code">TypeFinal</xsl:with-param>
                     <xsl:with-param name="term">Prohibited Derivations</xsl:with-param>
                  </xsl:call-template>
               </th>
               <td><xsl:value-of select="$final"/></td>
            </tr>
         </xsl:if>
         <!-- Annotation -->
         <xsl:call-template name="PrintAnnotation">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </table>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:simpleType (mode:properties) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Unmatched template for 'properties' mode
     -->
   <xsl:template match="*" mode="properties"/>

   <!--
     Prints out the rows to display 'annotation' elements of an 
     component in the Properties table. This template assumes it 
     will be called within an HTML 'table' element.
        Param(s):
            component (Node) required
                Schema component
     -->
   <xsl:template name="PrintAnnotation">
      <xsl:param name="component"/>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintAnnotation </xsl:comment>
		</xsl:if>

      <xsl:if test="$component/xsd:annotation/xsd:documentation">
         <tr>
            <th>Documentation</th>
            <td>
               <xsl:for-each select="$component/xsd:annotation/xsd:documentation">
                  <xsl:if test="position()!=1"><br/><br/></xsl:if>
                  <xsl:apply-templates select="." mode="properties"/>
               </xsl:for-each>
            </td>
         </tr>
      </xsl:if>
      <xsl:if test="$component/xsd:annotation/xsd:appinfo">
         <tr>
            <th>Application Data</th>
            <td>
               <xsl:for-each select="$component/xsd:annotation/xsd:appinfo">
                  <xsl:if test="position()!=1"><br/><br/></xsl:if>
                  <xsl:apply-templates select="." mode="properties"/>
               </xsl:for-each>
            </td>
         </tr>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintAnnotation </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the constraints of simple content
     to be displayed within a Properties table.
     Param(s):
            simpleContent (Node) required
                Node containing the simple content
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
   -->
   <xsl:template name="PrintSimpleConstraints">
      <xsl:param name="simpleContent"/>
      <xsl:param name="typeList"/>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSimpleConstraints </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Derivation by restriction -->
         <xsl:when test="$simpleContent/xsd:restriction">
            <xsl:call-template name="PrintSimpleRestriction">
               <xsl:with-param name="restriction" select="$simpleContent/xsd:restriction"/>
               <xsl:with-param name="typeList" select="$typeList"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Derivation by list -->
         <xsl:when test="$simpleContent/xsd:list">
            <ul><li>
               <xsl:text>List of: </xsl:text>
               <xsl:choose>
                  <!-- Globally-defined item type -->
                  <xsl:when test="$simpleContent/xsd:list/@itemType">
                     <xsl:call-template name="PrintTypeRef">
                        <xsl:with-param name="ref" select="$simpleContent/xsd:list/@itemType"/>
                     </xsl:call-template>
                  </xsl:when>
                  <!-- Locally-defined item type -->
                  <xsl:otherwise>
                     <ul>
                        <li>
                           <xsl:text>Locally defined type:</xsl:text>
                           <xsl:call-template name="PrintSimpleConstraints">
                              <xsl:with-param name="simpleContent" select="$simpleContent/xsd:list/xsd:simpleType"/>
                              <xsl:with-param name="typeList" select="$typeList"/>
                           </xsl:call-template>
                        </li>
                     </ul>
                  </xsl:otherwise>
               </xsl:choose>
            </li></ul>
         </xsl:when>
         <!-- Derivation by union -->
         <xsl:when test="$simpleContent/xsd:union">
            <ul><li>
               <xsl:text>Union of following types: </xsl:text>
               <ul>
                  <!-- Globally-defined member types -->
                  <xsl:if test="$simpleContent/xsd:union/@memberTypes">
                     <xsl:call-template name="PrintWhitespaceList">
                        <xsl:with-param name="value" select="$simpleContent/xsd:union/@memberTypes"/>
                        <xsl:with-param name="compType">type</xsl:with-param>
                        <xsl:with-param name="isInList">true</xsl:with-param>
                     </xsl:call-template>
                  </xsl:if>
                  <!-- Locally-defined member types -->
                  <xsl:for-each select="$simpleContent/xsd:union/xsd:simpleType">
                     <li>
                        <xsl:text>Locally defined type:</xsl:text>
                        <xsl:call-template name="PrintSimpleConstraints">
                           <xsl:with-param name="simpleContent" select="."/>
                           <xsl:with-param name="typeList" select="$typeList"/>
                        </xsl:call-template>
                     </li>
                  </xsl:for-each>
               </ul>
            </li></ul>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSimpleConstraints </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the constraints of simple content derived by 
     restriction, which is to be displayed in a Properties table.
     Param(s):
            restriction (Node) required
                Node containing the restriction
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
   -->
   <xsl:template name="PrintSimpleRestriction">
      <xsl:param name="restriction"/>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSimpleRestriction </xsl:comment>
		</xsl:if>

      <xsl:variable name="typeName" select="$restriction/parent::xsd:simpleType/@name"/>

      <!-- Print out base type info -->
      <xsl:choose>
         <!-- Circular type hierarchy -->
         <xsl:when test="$typeName != '' and contains($typeList, concat('*', $typeName, '+'))">
            <li>
               <xsl:call-template name="HandleError">
                  <xsl:with-param name="isTerminating">false</xsl:with-param>
                  <xsl:with-param name="errorMsg">
                     <xsl:text>Circular type reference to '</xsl:text>
                     <xsl:value-of select="$typeName"/>
                     <xsl:text>' in type hierarchy.</xsl:text>
                  </xsl:with-param>
               </xsl:call-template>
            </li>
         </xsl:when>
         <!-- Locally-defined base type -->
         <xsl:when test="$restriction/xsd:simpleType">
            <xsl:call-template name="PrintSimpleConstraints">
               <xsl:with-param name="simpleContent" select="$restriction/xsd:simpleType"/>
               <xsl:with-param name="typeList" select="$typeList"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Base type reference -->
         <xsl:when test="$restriction">
            <xsl:variable name="baseTypeRef" select="$restriction/@base"/>
            <xsl:variable name="baseTypeName">
               <xsl:call-template name="GetRefName">
                  <xsl:with-param name="ref" select="$baseTypeRef"/>
               </xsl:call-template>
            </xsl:variable>
            <xsl:variable name="baseTypeNS">
               <xsl:call-template name="GetRefNS">
                  <xsl:with-param name="ref" select="$baseTypeRef"/>
               </xsl:call-template>
            </xsl:variable>

            <xsl:choose>
               <!-- Base type is built-in XSD type -->
               <xsl:when test="$baseTypeNS=$XSD_NS">
                  <ul><li>
                     <xsl:text>Base XSD Type: </xsl:text>
                     <xsl:choose>
                        <!-- Current schema is the schema for XSDL -->
                        <xsl:when test="normalize-space(/xsd:schema/@targetNamespace)=$XSD_NS">
                           <xsl:call-template name="PrintTypeRef">
                              <xsl:with-param name="ref" select="$baseTypeRef"/>
                           </xsl:call-template>
                        </xsl:when>
                        <!-- Current schema is not XSD namespace -->
                        <xsl:otherwise>
                           <xsl:value-of select="$baseTypeName"/>
                        </xsl:otherwise>
                     </xsl:choose>
                  </li></ul>
               </xsl:when>
               <!-- Other types -->
               <xsl:otherwise>
                  <xsl:variable name="baseType" select="key('simpleType', $baseTypeName)"/>
                  <xsl:choose>
                     <!-- Base type found -->
                     <xsl:when test="$baseType">
                        <xsl:call-template name="PrintSimpleConstraints">
                           <xsl:with-param name="simpleContent" select="$baseType"/>
                           <xsl:with-param name="typeList" select="concat($typeList, '*', $typeName, '+')"/>
                        </xsl:call-template>
                     </xsl:when>
                     <!-- Base type not found -->
                     <xsl:otherwise>
                        <ul><li><strong>
                           <xsl:text>'</xsl:text>
                           <xsl:value-of select="$baseTypeName"/>
                           <xsl:text>' super type was not found in this schema. </xsl:text>
                           <xsl:text>Its facets could not be printed out.</xsl:text>
                        </strong></li></ul>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:when>
      </xsl:choose>

      <!-- Find constraints in current restriction -->
      <xsl:variable name="enumeration">
         <xsl:call-template name="PrintEnumFacets">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="pattern">
         <xsl:call-template name="PrintPatternFacet">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="range">
         <xsl:call-template name="PrintRangeFacets">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="totalDigits">
         <xsl:call-template name="PrintTotalDigitsFacet">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="fractionDigits">
         <xsl:call-template name="PrintFractionDigitsFacet">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="length">
         <xsl:call-template name="PrintLengthFacets">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="whitespace">
         <xsl:call-template name="PrintWhitespaceFacet">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Print out facets -->
      <xsl:if test="$enumeration!='' or $pattern!='' or $range!='' or $totalDigits!='' or $fractionDigits!='' or $length!='' or $whitespace!=''">
         <ul>
            <xsl:if test="$enumeration!=''">
               <li>
                  <xsl:copy-of select="$enumeration"/>
               </li>
            </xsl:if>
            <xsl:if test="$pattern!=''">
               <li>
                  <xsl:copy-of select="$pattern"/>
               </li>
            </xsl:if>
            <xsl:if test="$range!=''">
               <li>
                  <xsl:copy-of select="$range"/>
               </li>
            </xsl:if>
            <xsl:if test="$totalDigits!=''">
               <li>
                  <xsl:copy-of select="$totalDigits"/>
               </li>
            </xsl:if>
            <xsl:if test="$fractionDigits!=''">
               <li>
                  <xsl:copy-of select="$fractionDigits"/>
               </li>
            </xsl:if>
            <xsl:if test="$length!=''">
               <li>
                  <xsl:copy-of select="$length"/>
               </li>
            </xsl:if>
            <xsl:if test="$whitespace!=''">
               <li>
                  <xsl:copy-of select="$whitespace"/>
               </li>
            </xsl:if>
         </ul>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSimpleRestriction </xsl:comment>
		</xsl:if>
   </xsl:template>


   <!-- ******** XML Instance Representation table ******** -->

   <!-- 
     Prints out the XML Instance Representation table for a top-level 
     schema component. 
     Param(s):
            component (Node) required
              Top-level schema component
     -->
   <xsl:template name="SampleInstanceTable">
      <xsl:param name="component"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: SampleInstanceTable </xsl:comment>
		</xsl:if>

      <!-- Not applicable for simple type definitions and notation 
      declarations -->
      <xsl:if test="local-name($component)!='simpleType' and local-name($component)!='notation'">
         <xsl:call-template name="CollapseableBox">
            <xsl:with-param name="caption">XML Instance Representation</xsl:with-param>
            <xsl:with-param name="contents">
               <xsl:apply-templates select="$component" mode="sample"/>
            </xsl:with-param>
            <xsl:with-param name="isOpened">true</xsl:with-param>
         </xsl:call-template>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: SampleInstanceTable </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance representation of an 'all' 
     model group.
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS class.
            schemaLoc (String) optional
                Schema file containing this all model group;
                if in current schema, 'schemaLoc' is set to 'this'
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template match="xsd:all" mode="sample">
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:all (mode: sample) </xsl:comment>
		</xsl:if>

      <xsl:if test="normalize-space(@maxOccurs)!='0'">
         <!-- Header -->
         <span class="group" style="margin-left: {$margin}em">
            <xsl:text>Start </xsl:text>
            <xsl:call-template name="PrintGlossaryTermRef">
               <xsl:with-param name="code">All</xsl:with-param>
               <xsl:with-param name="term">All</xsl:with-param>
            </xsl:call-template>
            <!-- Min/max occurs information-->
            <xsl:text> </xsl:text>
            <xsl:call-template name="PrintOccurs">
               <xsl:with-param name="component" select="."/>
            </xsl:call-template>
            <!-- Documentation -->
            <xsl:call-template name="PrintSampleDocumentation">
               <xsl:with-param name="component" select="."/>
            </xsl:call-template>
         </span><br/>

         <!-- Content -->
         <xsl:apply-templates select="xsd:*" mode="sample">
            <xsl:with-param name="margin" select="number($margin) + number($ELEM_INDENT)"/>
            <xsl:with-param name="isInherited" select="$isInherited"/>
            <xsl:with-param name="isNewField" select="$isNewField"/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            <xsl:with-param name="typeList" select="$typeList"/>
         </xsl:apply-templates>

         <!-- Footer -->
         <span class="group" style="margin-left: {$margin}em">
            <xsl:text>End All</xsl:text>
         </span><br/>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:all (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance representation of an element 
     content wild card.
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
     -->
   <xsl:template match="xsd:any | xsd:anyAttribute" mode="sample">
      <xsl:param name="margin">0</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:any | xsd:anyAttribute (mode: sample) </xsl:comment>
		</xsl:if>

      <div class="other" style="margin-left: {$margin}em">
         <xsl:call-template name="PrintWildcard">
            <xsl:with-param name="componentType">
               <xsl:choose>
                  <xsl:when test="local-name(.)='anyAttribute'">attribute</xsl:when>
                  <xsl:otherwise>element</xsl:otherwise>
               </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="namespace" select="@namespace"/>
            <xsl:with-param name="processContents" select="@processContents"/>
            <xsl:with-param name="minOccurs" select="@minOccurs"/>
            <xsl:with-param name="maxOccurs" select="@maxOccurs"/>
         </xsl:call-template>
      </div>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:any | xsd:anyAttribute (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance representation 
     of an attribute declaration.
     Param(s):
            subTypeAttrs (String) optional
                List of attributes in sub-types of the type that 
                contains this attribute
            isInherited (boolean) optional
                If true, display attribute using 'inherited' CSS 
                class.
            isNewField (boolean) optional
                If true, display attribute using 'newFields' CSS 
                class.
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            addBR (boolean) optional
                If true, add <br/> before attribute.
            schemaLoc (String) optional
                Schema file containing this attribute declaration;
                if in current schema, 'schemaLoc' is set to 'this'.
     -->
   <xsl:template match="xsd:attribute[@name]" mode="sample">
      <xsl:param name="subTypeAttrs"/>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="addBR">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:attribute[@name] (mode: sample) </xsl:comment>
		</xsl:if>

      <!-- Get attribute namespace -->
      <xsl:variable name="attrNS">
         <xsl:call-template name="GetAttributeNS">
            <xsl:with-param name="attribute" select="."/>
         </xsl:call-template>
      </xsl:variable>

      <xsl:choose>
         <xsl:when test="contains($subTypeAttrs, concat('*', normalize-space($attrNS), '+', normalize-space(@name), '+'))">
            <!-- IGNORE: Sub type has attribute with same name; 
                 Sub-type's attribute declaration will override this 
                 one. -->
         </xsl:when>
         <xsl:when test="@use and normalize-space(@use)='prohibited'">
            <!-- IGNORE: Attribute is prohibited. -->
         </xsl:when>
         <xsl:otherwise>

		      <xsl:variable name="documentation">
		         <xsl:call-template name="PrintSampleDocumentation">
		            <xsl:with-param name="component" select="."/>
		         </xsl:call-template>
		      </xsl:variable>
		
		      <xsl:variable name="mouseover">
					<xsl:text>this.style.backgroundColor='#d3e3f5';</xsl:text>
		      </xsl:variable>
		
		      <xsl:variable name="mouseout">
					<xsl:text>this.style.backgroundColor='#eeeeee';</xsl:text>
		      </xsl:variable>
		
		      <xsl:element name="tr">
		         <xsl:attribute name="title">
			         <xsl:value-of select="$documentation"/>
		         </xsl:attribute>
		         <xsl:attribute name="onMouseOver">
			         <xsl:value-of select="$mouseover"/>
		         </xsl:attribute>
		         <xsl:attribute name="onMouseOut">
			         <xsl:value-of select="$mouseout"/>
		         </xsl:attribute>

               <xsl:choose>
                  <xsl:when test="$isNewField!='false'">
                     <xsl:attribute name="class">newFields</xsl:attribute>
                  </xsl:when>
                  <xsl:when test="$isInherited!='false'">
                     <xsl:attribute name="class">inherited</xsl:attribute>
                  </xsl:when>
               </xsl:choose>

               <xsl:text> </xsl:text>
               <th>
						<xsl:element name="div">
							<xsl:attribute name="style">margin-left: <xsl:value-of select="$margin"/>em</xsl:attribute>						
		               <xsl:choose>
		                  <xsl:when test="$isNewField!='false'">
		                     <xsl:attribute name="class">newFields</xsl:attribute>
		                  </xsl:when>
		                  <xsl:when test="$isInherited!='false'">
		                     <xsl:attribute name="class">inherited</xsl:attribute>
		                  </xsl:when>
		               </xsl:choose>

		               <xsl:variable name="prefix">
		                  <xsl:call-template name="GetAttributePrefix">
		                     <xsl:with-param name="attribute" select="."/>
		                  </xsl:call-template>
		               </xsl:variable>
		               <xsl:call-template name="PrintNSPrefix">
		                  <xsl:with-param name="prefix" select="$prefix"/>
		                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/> 
		               </xsl:call-template>
		               <xsl:value-of select="@name"/>
	
		               <xsl:text>="</xsl:text>
		
		               <xsl:choose>
		                  <!-- Fixed value is provided -->
		                  <xsl:when test="@fixed">
		                     <span class="fixed">
		                        <xsl:value-of select="@fixed"/>
		                     </span>
		                  </xsl:when>
		                  <!-- Type reference is provided -->
		                  <xsl:when test="@type">
		                     <xsl:call-template name="PrintTypeRef">
		                        <xsl:with-param name="ref" select="@type"/>
		                        <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
		                     </xsl:call-template>
		                  </xsl:when>
		                  <!-- Local type definition is provided -->
		                  <xsl:when test="xsd:simpleType">
		                     <xsl:apply-templates select="xsd:simpleType" mode="sample">
		                        <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
		                     </xsl:apply-templates>
		                  </xsl:when>
		                  <xsl:otherwise>
		                     <span class="type">anySimpleType</span>
		                  </xsl:otherwise>
		               </xsl:choose>
	
		               <xsl:text>"</xsl:text>
		
		               <!-- Don't print occurrence info and documentation
		                    for global attributes. -->
		               <xsl:if test="local-name(..)!='schema'">
		                  <!-- Occurrence info-->
		                  <xsl:text> </xsl:text>
		                  <xsl:call-template name="PrintOccurs">
		                     <xsl:with-param name="component" select="."/>
		                  </xsl:call-template>
		               </xsl:if>
						</xsl:element>
					</th>
					<td>
	               <xsl:if test="local-name(..)!='schema'">
	                  <!-- Documentation -->
	                  <xsl:call-template name="PrintSampleDocumentation">
	                     <xsl:with-param name="component" select="."/>
	                  </xsl:call-template>
	               </xsl:if>	
					</td>
            </xsl:element>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:attribute[@name] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance representation 
     of an attribute reference.
     Param(s):
            subTypeAttrs (String) optional
                List of attribute in sub-types of the type that 
                contains this attribute
            isInherited (boolean) optional
                If true, display attributes using 'inherited' CSS 
                class.
            isNewField (boolean) optional
                If true, display attributes using 'newFields' CSS 
                class.
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            addBR (boolean) optional
                If true, add <br/> before attribute.
            schemaLoc (String) optional
                Schema file containing this attribute reference;
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template match="xsd:attribute[@ref]" mode="sample">
      <xsl:param name="subTypeAttrs"/>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="addBR">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:attribute[@ref] (mode: sample) </xsl:comment>
		</xsl:if>

      <!-- Get attribute name -->
      <xsl:variable name="attrName">
         <xsl:call-template name="GetRefName">
            <xsl:with-param name="ref" select="@ref"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Get attribute namespace -->
      <xsl:variable name="attrNS">
         <xsl:call-template name="GetAttributeNS">
            <xsl:with-param name="attribute" select="."/>
         </xsl:call-template>
      </xsl:variable>

      <xsl:choose>
         <xsl:when test="contains($subTypeAttrs, concat('*', normalize-space($attrNS), '+', normalize-space($attrName), '+'))">
            <!-- IGNORE: Sub type has attribute with same name; 
                 Sub-type's attribute declaration will override this 
                 one. -->
         </xsl:when>
         <xsl:when test="@use and normalize-space(@use)='prohibited'">
            <!-- IGNORE: Attribute is prohibited. -->
         </xsl:when>
         <xsl:otherwise>
            <xsl:if test="$addBR!='false'"><br/></xsl:if>

            <span style="margin-left: {$margin}em">
               <xsl:choose>
                  <xsl:when test="$isNewField!='false'">
                     <xsl:attribute name="class">newFields</xsl:attribute>
                  </xsl:when>
                  <xsl:when test="$isInherited!='false'">
                     <xsl:attribute name="class">inherited</xsl:attribute>
                  </xsl:when>
               </xsl:choose>

               <xsl:text> </xsl:text>
               <xsl:call-template name="PrintAttributeRef">
                  <xsl:with-param name="ref" select="@ref"/>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
               <xsl:text>="</xsl:text>
               <!-- Fixed value is provided -->
               <xsl:if test="@fixed">
                  <span class="fixed">
                     <xsl:value-of select="@fixed"/>
                  </span>
                  <xsl:text> </xsl:text>
               </xsl:if>
               <!-- Print occurs info-->
               <xsl:call-template name="PrintOccurs">
                  <xsl:with-param name="component" select="."/>
               </xsl:call-template>
               <!-- Documentation -->
               <xsl:call-template name="PrintSampleDocumentation">
                  <xsl:with-param name="component" select="."/>
               </xsl:call-template>

               <xsl:text>"</xsl:text>
            </span>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:attribute[@ref] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance representation of an attribute 
     group definition.
     Param(s):
            schemaLoc (String) optional
                Schema file containing this attribute group 
                definition; if in current schema, 'schemaLoc' is 
                set to 'this'.
     -->
   <xsl:template match="xsd:attributeGroup[@name]" mode="sample">
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:attributeGroup[@name] (mode: sample) </xsl:comment>
		</xsl:if>

      <xsl:for-each select="xsd:attribute | xsd:attributeGroup | xsd:anyAttribute">
         <xsl:variable name="addBR">
            <xsl:choose>
               <xsl:when test="position()!=1">
                  <xsl:text>true</xsl:text>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:text>false</xsl:text>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:variable>

         <xsl:apply-templates select="." mode="sample">
            <xsl:with-param name="addBR" select="$addBR"/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
         </xsl:apply-templates>
      </xsl:for-each>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:attributeGroup[@name] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance representation of an attribute 
     group reference.
     Param(s):
            subTypeAttrs (String) optional
                List of attributes in sub-types of the type that 
                contains this attribute group
            isInherited (boolean) optional
                If true, display attributes using 'inherited' CSS 
                class.
            isNewField (boolean) optional
                If true, display attributes using 'newFields' CSS 
                class.
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            parentGroups (String) optional
                List of parent attribute group definitions that 
                contain this attribute group. Used to prevent 
                infinite loops when displaying attribute group 
                definitions. In such a case, writes out an error 
                message and stops processing.
            schemaLoc (String) optional
                Schema file containing this attribute group 
                reference if in current schema, 'schemaLoc' is 
                set to 'this'.
     -->
   <xsl:template match="xsd:attributeGroup[@ref]" mode="sample">
      <xsl:param name="subTypeAttrs"/>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="parentGroups"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:attributeGroup[@ref] (mode: sample) </xsl:comment>
		</xsl:if>

      <!-- Get attribute group name -->
      <xsl:variable name="attrGrpName">
         <xsl:call-template name="GetRefName">
            <xsl:with-param name="ref" select="@ref"/>
         </xsl:call-template>
      </xsl:variable>

      <xsl:choose>
         <xsl:when test="contains($parentGroups, concat('*', normalize-space($attrGrpName), '+'))">
            <!-- Circular attribute group definition -->
            <xsl:call-template name="HandleError">
               <xsl:with-param name="isTerminating">false</xsl:with-param>
               <xsl:with-param name="errorMsg">
                  <xsl:text>Circular attribute group reference: </xsl:text>
                  <xsl:value-of select="$attrGrpName"/>
               </xsl:with-param>
            </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>
            <!-- Look for attribute group definition -->
            <xsl:variable name="defLoc">
               <xsl:call-template name="FindComponent">
                  <xsl:with-param name="ref" select="@ref"/>
                  <xsl:with-param name="compType">attribute group</xsl:with-param>
               </xsl:call-template>
            </xsl:variable>

            <xsl:choose>
               <!-- Not found -->
               <xsl:when test="normalize-space($defLoc)='' or normalize-space($defLoc)='none' or normalize-space($defLoc)='xml' or normalize-space($defLoc)='xsd'">
                  <div class="other" style="margin-left: {$margin}em">
                     <xsl:text>Attribute group reference (not shown): </xsl:text>
                     <xsl:call-template name="PrintAttributeGroupRef">
                        <xsl:with-param name="ref" select="@ref"/>
                        <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     </xsl:call-template>
                     <!-- Documentation -->
                     <xsl:call-template name="PrintSampleDocumentation">
                        <xsl:with-param name="component" select="."/>
                     </xsl:call-template>
                  </div>
               </xsl:when>
               <!-- Found in current schema -->
               <xsl:when test="normalize-space($defLoc)='this'">
                  <xsl:variable name="attrGrpDef" select="key('attributeGroup', $attrGrpName)"/>
                  <xsl:apply-templates select="$attrGrpDef/xsd:attribute | $attrGrpDef/xsd:attributeGroup" mode="sample">
                     <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="parentGroups" select="concat($parentGroups, concat('*', normalize-space($attrGrpName), '+'))"/>
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="addBR">true</xsl:with-param>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                  </xsl:apply-templates>
               </xsl:when>
               <!-- Found in external schema -->
               <xsl:otherwise>
                  <xsl:variable name="attrGrpDef" select="document($defLoc)/xsd:schema/xsd:attributeGroup[@name=$attrGrpName]"/>
                  <xsl:apply-templates select="$attrGrpDef/xsd:attribute | $attrGrpDef/xsd:attributeGroup" mode="sample">
                     <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="parentGroups" select="concat($parentGroups, concat('*', normalize-space($attrGrpName), '+'))"/>
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="addBR">true</xsl:with-param>
                     <xsl:with-param name="schemaLoc" select="$defLoc"/>
                  </xsl:apply-templates>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:attributeGroup[@ref] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance representation of a 'choice' 
     model group.
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using'inherited' CSS class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS class.
            parentGroups (String) optional
                List of parent model group definitions that contain this 
                model group. Used to prevent infinite loops when 
                displaying model group definitions.
            schemaLoc (String) optional
                Schema file containing this choice model group;
                if in current schema, 'schemaLoc' is set to 'this'
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template match="xsd:choice" mode="sample">
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="parentGroups"/>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:choice (mode: sample) </xsl:comment>
		</xsl:if>

      <xsl:if test="normalize-space(@maxOccurs)!='0'">
         <!-- Header -->
         <tr>
         	<th class="group">
         		<span style="margin-left: {$margin}em">
         			<xsl:text>Start </xsl:text>
         		</span>
	            <xsl:call-template name="PrintGlossaryTermRef">
	               <xsl:with-param name="code">Choice</xsl:with-param>
	               <xsl:with-param name="term">Choice</xsl:with-param>
	            </xsl:call-template>
	            <!-- Min/max occurrence -->
	            <xsl:text> </xsl:text>
	            <xsl:call-template name="PrintOccurs">
	               <xsl:with-param name="component" select="."/>
	            </xsl:call-template>
         	</th>
         </tr>
         <!-- Documentation -->
         <xsl:call-template name="PrintSampleDocumentation">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>

         <!-- Content -->
         <xsl:apply-templates select="xsd:*" mode="sample">
            <xsl:with-param name="margin" select="number($margin)+number($ELEM_INDENT)"/>
            <xsl:with-param name="isInherited" select="$isInherited"/>
            <xsl:with-param name="isNewField" select="$isNewField"/>
            <xsl:with-param name="parentGroups" select="$parentGroups"/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            <xsl:with-param name="typeList" select="$typeList"/>
         </xsl:apply-templates>

         <!-- Footer -->
			<tr>
				<th class="group">
					<span style="margin-left: {$margin}em">
						<xsl:text>End Choice</xsl:text>
					</span>
				</th>
			</tr>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:choice (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance from a complex type definition.
     Param(s):
            schemaLoc (String) optional
                Schema file containing this complex type definition; 
                if in current schema, 'schemaLoc' is set to 'this'.
     -->
   <xsl:template match="xsd:complexType" mode="sample">
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:complexType (mode: sample) </xsl:comment>
		</xsl:if>

      <xsl:call-template name="PrintSampleComplexElement">
         <xsl:with-param name="type" select="."/>
         <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
      </xsl:call-template>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:complexType (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance from an element declaration.
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS class.
            schemaLoc (String) optional
                Schema file containing this element declaration; 
                if in current schema, 'schemaLoc' is set to 'this'.
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template match="xsd:element[@name]" mode="sample">
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:element[@name] (mode: sample) </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Prohibited element declaration -->
         <xsl:when test="normalize-space(@maxOccurs)='0'">
            <!-- IGNORE if max occurs is zero -->
         </xsl:when>
         <!-- Global element declaration -->
         <xsl:when test="local-name(..)='schema'">
            <xsl:choose>
               <!-- With type reference -->
               <xsl:when test="@type">
                  <xsl:variable name="elemTypeName">
                     <xsl:call-template name="GetRefName">
                        <xsl:with-param name="ref" select="@type"/>
                     </xsl:call-template>
                  </xsl:variable>

                  <!-- Look for complex type definition -->
                  <xsl:variable name="defLoc">
                     <xsl:call-template name="FindComponent">
                        <xsl:with-param name="ref" select="@type"/>
                        <xsl:with-param name="compType">complex type</xsl:with-param>
                     </xsl:call-template>
                  </xsl:variable>

                  <xsl:choose>
                     <!-- Complex type was found in current 
                          schema. -->
                     <xsl:when test="normalize-space($defLoc)='this'">
                        <xsl:variable name="ctype" select="key('complexType', $elemTypeName)"/>
                        <xsl:call-template name="PrintSampleComplexElement">
                           <xsl:with-param name="element" select="."/>
                           <xsl:with-param name="type" select="$ctype"/>
                        </xsl:call-template>
                     </xsl:when>
                     <!-- Complex type was not found. -->
                     <xsl:when test="normalize-space($defLoc)='' or normalize-space($defLoc)='none' or normalize-space($defLoc)='xml' or normalize-space($defLoc)='xsd'">
                        <xsl:call-template name="PrintSampleSimpleElement">
                           <xsl:with-param name="element" select="."/>
                           <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                        </xsl:call-template>
                     </xsl:when>
                     <!-- Complex type was found in external
                          schema. -->
                     <xsl:otherwise>
                        <xsl:variable name="ctype" select="document($defLoc)/xsd:schema/xsd:complexType[@name=$elemTypeName]"/>
                        <xsl:call-template name="PrintSampleComplexElement">
                           <xsl:with-param name="element" select="."/>
                           <xsl:with-param name="type" select="$ctype"/>
                        </xsl:call-template>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:when>
               <!-- With local complex type definition -->
               <xsl:when test="xsd:complexType">
                  <xsl:call-template name="PrintSampleComplexElement">
                     <xsl:with-param name="element" select="."/>
                     <xsl:with-param name="type" select="xsd:complexType"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                  </xsl:call-template>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:call-template name="PrintSampleSimpleElement">
                     <xsl:with-param name="element" select="."/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                  </xsl:call-template>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:when>
         <!-- Local element declaration -->
         <xsl:otherwise>
            <xsl:choose>
               <!-- With local complex type definition -->
               <xsl:when test="xsd:complexType">
                  <xsl:call-template name="PrintSampleComplexElement">
                     <xsl:with-param name="element" select="."/>
                     <xsl:with-param name="type" select="xsd:complexType"/>
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     <xsl:with-param name="typeList" select="$typeList"/>
                  </xsl:call-template>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:call-template name="PrintSampleSimpleElement">
                     <xsl:with-param name="element" select="."/>
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     <xsl:with-param name="typeList" select="$typeList"/>
                  </xsl:call-template>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:element[@name] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance from an element
     reference.
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS class.
            schemaLoc (String) optional
                Schema file containing this element reference; 
                if in current schema, 'schemaLoc' is set to 'this'.
     -->
   <xsl:template match="xsd:element[@ref]" mode="sample">
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:element[@ref] (mode: sample) </xsl:comment>
		</xsl:if>

      <xsl:if test="normalize-space(@maxOccurs)!='0'">
         <xsl:call-template name="PrintSampleSimpleElement">
            <xsl:with-param name="element" select="."/>
            <xsl:with-param name="margin" select="$margin"/>
            <xsl:with-param name="isInherited" select="$isInherited"/>
            <xsl:with-param name="isNewField" select="$isNewField"/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
         </xsl:call-template>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:element[@ref] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance from a group definition.
     Param(s):
            schemaLoc (String) optional
                Schema file containing this model group definition;
                if in current schema, 'schemaLoc' is set to 'this'
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template match="xsd:group[@name]" mode="sample">
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:group[@name] (mode: sample) </xsl:comment>
		</xsl:if>

      <xsl:apply-templates select="xsd:*" mode="sample">
         <xsl:with-param name="parentGroups" select="concat('*', normalize-space(@name), '+')"/>
         <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
         <xsl:with-param name="typeList" select="$typeList"/>
      </xsl:apply-templates>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:group[@name] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance from a group reference.
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using 'inherited' CSS 
                class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS 
                class.
            parentGroups (String) optional
                List of parent model group definitions that contain 
                this  model group. Used to prevent infinite loops 
                when displaying model group definitions.
            schemaLoc (String) optional
                Schema file containing this model group reference;
                if in current schema, 'schemaLoc' is set to 'this'
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template match="xsd:group[@ref]" mode="sample">
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="parentGroups"/>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:group[@ref] (mode: sample) </xsl:comment>
		</xsl:if>

      <!-- Get group name -->
      <xsl:variable name="grpName">
         <xsl:call-template name="GetRefName">
            <xsl:with-param name="ref" select="@ref"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Create link to the group definition -->
      <xsl:variable name="grpLink">
         <xsl:call-template name="PrintGroupRef">
            <xsl:with-param name="ref" select="@ref"/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Get occurrence info -->
      <xsl:variable name="occursInfo">
         <xsl:call-template name="PrintOccurs">
            <xsl:with-param name="component" select="."/>
         </xsl:call-template>
      </xsl:variable>

      <xsl:choose>
         <!-- Circular group definition -->
         <xsl:when test="contains($parentGroups, concat('*', normalize-space($grpName), '+'))">
            <!-- Don't show contents -->
            <div class="other" style="margin-left: {$margin}em">
               <xsl:text>Circular model group reference: </xsl:text>
               <xsl:copy-of select="$grpLink"/>
               <!-- Occurrence info -->
               <xsl:text> </xsl:text>
               <xsl:copy-of select="$occursInfo"/>
               <!-- Documentation -->
               <xsl:call-template name="PrintSampleDocumentation">
                  <xsl:with-param name="component" select="."/>
               </xsl:call-template>
            </div>
         </xsl:when>
         <xsl:otherwise>
            <!-- Look for group definition -->
            <xsl:variable name="grpDefLoc">
               <xsl:call-template name="FindComponent">
                  <xsl:with-param name="ref" select="@ref"/>
                  <xsl:with-param name="compType">group</xsl:with-param>
               </xsl:call-template>
            </xsl:variable>

            <xsl:choose>
               <!-- Not found -->
               <xsl:when test="normalize-space($grpDefLoc)='' or normalize-space($grpDefLoc)='none' or normalize-space($grpDefLoc)='xml' or normalize-space($grpDefLoc)='xsd'">
                  <div class="other" style="margin-left: {$margin}em">
                     <xsl:text>Model group reference (not shown): </xsl:text>
                     <xsl:copy-of select="$grpLink"/>
                     <!-- Occurrence info -->
                     <xsl:text> </xsl:text>
                     <xsl:copy-of select="$occursInfo"/>
                     <!-- Documentation -->
                     <xsl:call-template name="PrintSampleDocumentation">
                        <xsl:with-param name="component" select="."/>
                     </xsl:call-template>
                  </div>
               </xsl:when>
               <!-- Found in current schema -->
               <xsl:when test="normalize-space($grpDefLoc)='this'">
                  <xsl:variable name="grpDef" select="key('group', $grpName)"/>
                  <xsl:call-template name="PrintSampleGroup">
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="parentGroups" select="concat($parentGroups, concat('*', normalize-space($grpName), '+'))"/>
                     <xsl:with-param name="occursInfo" select="$occursInfo"/>
                     <xsl:with-param name="grpLink" select="$grpLink"/>
                     <xsl:with-param name="grpRef" select="."/>
                     <xsl:with-param name="grpDef" select="$grpDef"/>
                     <xsl:with-param name="grpDefLoc" select="$grpDefLoc"/>
                     <xsl:with-param name="typeList" select="$typeList"/>
                  </xsl:call-template>
               </xsl:when>
               <!-- Found in external schema -->
               <xsl:otherwise>
                  <xsl:variable name="grpDef" select="document($grpDefLoc)/xsd:schema/xsd:group[@name=$grpName]"/>
                  <xsl:call-template name="PrintSampleGroup">
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="parentGroups" select="concat($parentGroups, concat('*', normalize-space($grpName), '+'))"/>
                     <xsl:with-param name="occursInfo" select="$occursInfo"/>
                     <xsl:with-param name="grpLink" select="$grpLink"/>
                     <xsl:with-param name="grpRef" select="."/>
                     <xsl:with-param name="grpDef" select="$grpDef"/>
                     <xsl:with-param name="grpDefLoc" select="$grpDefLoc"/>
                     <xsl:with-param name="typeList" select="$typeList"/>
                  </xsl:call-template>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:group[@ref] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample XML instance from a 'sequence' model group.
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS class.
            parentGroups (String) optional
                List of parent model group definitions that contain 
                this model group. Used to prevent infinite loops when 
                displaying model group definitions.
            schemaLoc (String) optional
                Schema file containing this sequence model group;
                if in current schema, 'schemaLoc' is set to 'this'
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template match="xsd:sequence" mode="sample">
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="parentGroups"/>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:sequence (mode: sample) </xsl:comment>
		</xsl:if>

      <xsl:if test="normalize-space(@maxOccurs)!='0'">
         <!-- Get occurrence info -->
         <xsl:variable name="occursInfo">
            <xsl:call-template name="PrintOccurs">
               <xsl:with-param name="component" select="."/>
            </xsl:call-template>
         </xsl:variable>

         <!-- Header -->
         <xsl:if test="normalize-space($occursInfo)!='[1]'">
            <!-- Don't display header if min/max occurs is one. -->
            <tr>
            	<th class="group">
            		<span style="margin-left: {$margin}em">
		               <xsl:text>Start </xsl:text>
		               <xsl:call-template name="PrintGlossaryTermRef">
		                  <xsl:with-param name="code">Sequence</xsl:with-param>
		                  <xsl:with-param name="term">Sequence</xsl:with-param>
		               </xsl:call-template>
		
		               <xsl:text> </xsl:text>
		               <xsl:copy-of select="$occursInfo"/>
						</span>
					</th>
				</tr>
				<!-- Documentation -->     
            <xsl:call-template name="PrintSampleDocumentation">
               <xsl:with-param name="component" select="."/>
            </xsl:call-template>
         </xsl:if>

         <xsl:apply-templates select="xsd:*" mode="sample">
            <xsl:with-param name="margin">
               <xsl:choose>
                  <xsl:when test="normalize-space($occursInfo)='[1]'">
                     <xsl:value-of select="$margin"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:value-of select="number($margin)+number($ELEM_INDENT)"/>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="isInherited" select="$isInherited"/>
            <xsl:with-param name="isNewField" select="$isNewField"/>
            <xsl:with-param name="parentGroups" select="$parentGroups"/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            <xsl:with-param name="typeList" select="$typeList"/>
         </xsl:apply-templates>

         <!-- Footer -->
         <xsl:if test="normalize-space($occursInfo)!='[1]'">
            <!-- Don't display footer if min/max occurs is one. -->
            <tr>
            	<th class="group">
            		<span style="margin-left: {$margin}em">
	            		<xsl:text>End Sequence</xsl:text>
	            	</span>
            	</th>
            </tr>
         </xsl:if>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:sequence (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the constraints of a complex type with simple content 
     to be displayed within a sample XML instance.
     Param(s):
            schemaLoc (String) optional
                Schema file containing this simple content 
                restriction; if in current schema, 'schemaLoc' is 
                set to 'this'
     -->
   <xsl:template match="xsd:simpleContent[xsd:restriction]" mode="sample">
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:simpleContent[xsd:restriction] (mode: sample) </xsl:comment>
		</xsl:if>

      <span class="constraint">
         <xsl:call-template name="PrintSampleSimpleRestriction">
            <xsl:with-param name="restriction" select="./xsd:restriction"/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
         </xsl:call-template>
      </span>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:simpleContent[xsd:restriction] (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the constraints of a simple type definition to be 
     displayed within a sample XML instance.
     Param(s):
            schemaLoc (String) optional
                Schema file containing this simple type definition; 
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template match="xsd:simpleType" mode="sample">
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:simpleType (mode: sample) </xsl:comment>
		</xsl:if>

      <span class="constraint">
         <xsl:call-template name="PrintSampleSimpleConstraints">
            <xsl:with-param name="simpleContent" select="."/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
         </xsl:call-template>
      </span>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:simpleType (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the identity constraints of an element to be displayed 
     within a sample XML instance.
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            schemaLoc (String) optional
                Schema file containing this simple type definition; 
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template match="xsd:unique | xsd:key | xsd:keyref" mode="sample">
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: xsd:unique | xsd:key | xsd:keyref (mode: sample) </xsl:comment>
		</xsl:if>

      <div class="other" style="margin-left: {$margin}em">
         <xsl:text>&lt;!-- </xsl:text><br/>

         <xsl:choose>
            <xsl:when test="local-name(.)='unique'">
               <xsl:call-template name="PrintGlossaryTermRef">
                  <xsl:with-param name="code">Unique</xsl:with-param>
                  <xsl:with-param name="term">Uniqueness</xsl:with-param>
               </xsl:call-template>
            </xsl:when>
            <xsl:when test="local-name(.)='key'">
               <xsl:call-template name="PrintGlossaryTermRef">
                  <xsl:with-param name="code">Key</xsl:with-param>
                  <xsl:with-param name="term">Key</xsl:with-param>
               </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
               <xsl:call-template name="PrintGlossaryTermRef">
                  <xsl:with-param name="code">KeyRef</xsl:with-param>
                  <xsl:with-param name="term">Key Reference</xsl:with-param>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
         <xsl:text> Constraint - </xsl:text>
         <strong>
            <xsl:choose>
               <xsl:when test="local-name(.)='keyref'">
                  <xsl:value-of select="@name"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:variable name="componentID">
                     <xsl:call-template name="GetComponentID">
                        <xsl:with-param name="component" select="."/>
                     </xsl:call-template>
                  </xsl:variable>
                  <a name="{$componentID}"><xsl:value-of select="@name"/></a>
               </xsl:otherwise>
            </xsl:choose>
         </strong><br/>

         <xsl:text>Selector - </xsl:text>
         <strong>
            <xsl:value-of select="xsd:selector/@xpath"/>
         </strong><br/>

         <xsl:text>Field(s) - </xsl:text>
         <xsl:for-each select="xsd:field">
            <xsl:if test="position()!=1">
               <xsl:text>, </xsl:text>
            </xsl:if>
            <strong>
               <xsl:value-of select="@xpath"/>
            </strong>
         </xsl:for-each>
         <br/>

         <xsl:if test="local-name(.)='keyref'">
            <xsl:text>Refers to - </xsl:text>
            <xsl:call-template name="PrintKeyRef">
               <xsl:with-param name="ref" select="@refer"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
            <br/>
         </xsl:if>

         <xsl:text>--></xsl:text>
      </div>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: xsd:unique | xsd:key | xsd:keyref (mode: sample) </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Unmatched template for 'sample' mode
     -->
   <xsl:template match="*" mode="sample"/>

   <!--
     Prints out a 
     schema component's documentation.
     Param(s):
            component (Node) required
                Schema component
  -->
   <xsl:template name="PrintSampleDocumentation">
      <xsl:param name="component"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleDocumentation </xsl:comment>
		</xsl:if>

      <xsl:if test="$component and $component/xsd:annotation/xsd:documentation">
         <xsl:variable name="documentation">
            <xsl:for-each select="$component/xsd:annotation/xsd:documentation">
               <xsl:if test="position()!=1">
                  <xsl:text>,</xsl:text>
               </xsl:if>
               <xsl:choose>
                  <xsl:when test="@source">
                     <xsl:text>More information at: </xsl:text>
                     <xsl:call-template name="EscapeQuotes">
                        <xsl:with-param name="value" select="@source"/>
                     </xsl:call-template>
                     <xsl:text>.</xsl:text>
                  </xsl:when>
                  <xsl:when test="normalize-space(.)!=''">
                     <xsl:call-template name="EscapeQuotes">
                        <xsl:with-param name="value" select="normalize-space(.)"/>
                     </xsl:call-template>
                  </xsl:when>
               </xsl:choose>
            </xsl:for-each>
         </xsl:variable>

         <xsl:value-of select="$documentation"/>

      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleDocumentation </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Translates occurrences of single and double quotes
     in a piece of text with single and double quote
     escape characters.
     Param(s):
            value (String) required
                Text to translate
     -->
   <xsl:template name="EscapeQuotes">
      <xsl:param name="value"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: EscapeQuotes </xsl:comment>
		</xsl:if>

<!--
      <xsl:variable name="noSingleQuotes">
         <xsl:call-template name="TranslateStr">
            <xsl:with-param name="value" select="$value"/>
            <xsl:with-param name="strToReplace">'</xsl:with-param>
            <xsl:with-param name="replacementStr">\'</xsl:with-param>
         </xsl:call-template>
      </xsl:variable>
-->
      <xsl:variable name="noDoubleQuotes">
         <xsl:call-template name="TranslateStr">
            <xsl:with-param name="value" select="$value"/>
            <xsl:with-param name="strToReplace">"</xsl:with-param>
            <xsl:with-param name="replacementStr">\"</xsl:with-param>
         </xsl:call-template>
      </xsl:variable>
      <xsl:value-of select="$noDoubleQuotes"/>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: EscapeQuotes </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Helper template for template, match="xsd:group[@ref]" 
     mode="sample". Basically prints out a group reference, for 
     which we are able to look up the group definition that it
     is referring to. This template is a work-around because XSLT
     doesn't have variables (in the traditional sense of 
     programming languages) and it doesn't allow you to query
     result tree fragments. 
     Param(s):
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS class.
            parentGroups (String) optional
                List of parent model group definitions that contain this 
                model group. Used to prevent infinite loops when 
                displaying model group definitions.
            occursInfo (Result tree fragment) required
                Pre-formatted occurrence info of group reference
            grpLink (Result tree fragment) required
                Pre-formatted <a> link representing group reference
            grpRef (Node) required
                Group reference
            grpDef (Node) required
                Group definition that the reference is pointing to
            grpDefLoc (String) optional
                Schema file containing 'grpDef' group definition;
                if current schema, 'schemaLoc' is set to 'this'
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSampleGroup">
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="parentGroups"/>
      <xsl:param name="occursInfo"/>
      <xsl:param name="grpLink"/>
      <xsl:param name="grpRef"/>
      <xsl:param name="grpDef"/>
      <xsl:param name="grpDefLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleGroup </xsl:comment>
		</xsl:if>

      <!-- Header -->
      <xsl:if test="normalize-space($occursInfo)!='[1]'">
         <!-- Don't print out header if min/max occurs is one. -->
         <span class="group" style="margin-left: {$margin}em">
            <xsl:text>Start Group: </xsl:text>
            <xsl:copy-of select="$grpLink"/>
            <!-- Occurrence info -->
            <xsl:text> </xsl:text>
            <xsl:copy-of select="$occursInfo"/>
            <!-- Documentation -->
            <xsl:call-template name="PrintSampleDocumentation">
               <xsl:with-param name="component" select="$grpRef"/>
            </xsl:call-template>
         </span><br/>
      </xsl:if>

      <!-- Content -->
      <xsl:apply-templates select="$grpDef/xsd:*" mode="sample">
         <xsl:with-param name="margin">
            <xsl:choose>
               <xsl:when test="normalize-space($occursInfo)!='[1]'">
                  <xsl:value-of select="number($margin)+number($ELEM_INDENT)"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$margin"/>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:with-param>
         <xsl:with-param name="isInherited" select="$isInherited"/>
         <xsl:with-param name="isNewField" select="$isNewField"/>
         <xsl:with-param name="parentGroups" select="$parentGroups"/>
         <xsl:with-param name="schemaLoc" select="$grpDefLoc"/>
         <xsl:with-param name="typeList" select="$typeList"/>
      </xsl:apply-templates>

      <!-- Footer -->
      <xsl:if test="normalize-space($occursInfo)!='[1]'">
         <!-- Don't print out footer if min/max occurs is one. -->
         <span class="group" style="margin-left: {$margin}em">
            <xsl:text>End Group: </xsl:text>
            <xsl:copy-of select="$grpLink"/>
         </span><br/>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleGroup </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample element instance in one line.
     Param(s):
            element (Node) required
                Element declaration or reference
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display element using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display element using 'newFields' CSS class.
            schemaLoc (String) optional
                Schema file containing this element declaration
                or reference; if in current schema, 'schemaLoc' is 
                set to 'this'.
     -->
   <xsl:template name="PrintSampleSimpleElement">
      <xsl:param name="element"/>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleSimpleElement </xsl:comment>
		</xsl:if>

      <!-- Element Tag -->
      <xsl:variable name="elemTag">
         <!-- Local Name -->
         <xsl:choose>
            <!-- Element reference -->
            <xsl:when test="$element/@ref">
               <!-- Note: Prefix will be automatically written out
                    in call to 'PrintElementRef'. -->
               <xsl:call-template name="PrintElementRef">
                  <xsl:with-param name="ref" select="@ref"/>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
            </xsl:when>
            <!-- Element declaration -->
            <xsl:otherwise>
               <!-- Prefix -->
               <xsl:variable name="prefix">
                  <xsl:call-template name="GetElementPrefix">
                     <xsl:with-param name="element" select="$element"/>
                  </xsl:call-template>
               </xsl:variable>
               <xsl:call-template name="PrintNSPrefix">
                  <xsl:with-param name="prefix" select="$prefix"/>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
               <xsl:value-of select="$element/@name"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>

      <xsl:variable name="documentation">
         <xsl:call-template name="PrintSampleDocumentation">
            <xsl:with-param name="component" select="$element"/>
         </xsl:call-template>
      </xsl:variable>

      <xsl:variable name="mouseover">
			<xsl:text>this.style.backgroundColor='#d3e3f5';</xsl:text>
      </xsl:variable>

      <xsl:variable name="mouseout">
			<xsl:text>this.style.backgroundColor='#eeeeee';</xsl:text>
      </xsl:variable>

      <xsl:element name="tr">
         <xsl:attribute name="title">
	         <xsl:value-of select="$documentation"/>
         </xsl:attribute>
         <xsl:attribute name="onMouseOver">
	         <xsl:value-of select="$mouseover"/>
         </xsl:attribute>
         <xsl:attribute name="onMouseOut">
	         <xsl:value-of select="$mouseout"/>
         </xsl:attribute>
		
         <xsl:choose>
            <xsl:when test="$isNewField!='false'">
               <xsl:attribute name="class">newFields</xsl:attribute>
            </xsl:when>
            <xsl:when test="$isInherited!='false'">
               <xsl:attribute name="class">inherited</xsl:attribute>
            </xsl:when>
         </xsl:choose>

         <!-- Start Tag -->
         <th>
				<xsl:element name="div">
					<xsl:attribute name="style">margin-left: <xsl:value-of select="$margin"/>em</xsl:attribute>
		         <xsl:choose>
		            <xsl:when test="$isNewField!='false'">
		               <xsl:attribute name="class">newFields</xsl:attribute>
		            </xsl:when>
		            <xsl:when test="$isInherited!='false'">
		               <xsl:attribute name="class">inherited</xsl:attribute>
		            </xsl:when>
		         </xsl:choose>
		         <xsl:text>&lt;</xsl:text>
		         	<xsl:copy-of select="$elemTag"/>
		         <xsl:text>></xsl:text>
					
		         <!-- Contents -->
		         <xsl:text> </xsl:text>
		         <xsl:choose>
		            <!-- Fixed value is provided -->
		            <xsl:when test="$element/@fixed">
		               <span class="fixed">
		                  <xsl:value-of select="$element/@fixed"/>
		               </span>
		            </xsl:when>
		            <!-- Type reference is provided -->
		            <xsl:when test="$element/@name and $element/@type">
		               <xsl:call-template name="PrintTypeRef">
		                  <xsl:with-param name="ref" select="$element/@type"/>
		                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
		               </xsl:call-template>
		            </xsl:when>
		            <!-- Local simple type definition is provided -->
		            <xsl:when test="$element/@name and $element/xsd:simpleType">
		               <xsl:apply-templates select="$element/xsd:simpleType" mode="sample">
		                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
		               </xsl:apply-templates>
		            </xsl:when>
		            <xsl:otherwise>
		               <xsl:text>...</xsl:text>
		            </xsl:otherwise>
		         </xsl:choose>
		         <xsl:text> </xsl:text>
		
		         <!-- Identity Constraints -->
		         <xsl:if test="$element/xsd:unique or $element/xsd:key or $element/xsd:keyref">
		            <xsl:apply-templates select="$element/xsd:unique | $element/xsd:key | $element/xsd:keyref" mode="sample">
		               <xsl:with-param name="margin" select="$ELEM_INDENT"/>
		               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
		            </xsl:apply-templates>
		         </xsl:if>
		
		         <!-- End Tag -->
		         <xsl:text>&lt;/</xsl:text>
		         	<xsl:copy-of select="$elemTag"/>
		         <xsl:text>></xsl:text>
		
		         <xsl:if test="local-name($element/..)!='schema'">
		            <!-- Min/max occurs information -->
		            <xsl:text> </xsl:text>
		            <xsl:call-template name="PrintOccurs">
		               <xsl:with-param name="component" select="$element"/>
		            </xsl:call-template>
		         </xsl:if>
				</xsl:element>
			</th>
			<td>	            
            <!-- Documentation -->
	         <xsl:if test="local-name($element/..)!='schema'">
	            <xsl:call-template name="PrintSampleDocumentation">
	               <xsl:with-param name="component" select="$element"/>
	            </xsl:call-template>
	         </xsl:if>
			</td>
		</xsl:element>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleSimpleElement </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a sample element instance that has complex content.
     Param(s):
            type (Node) required
                Complex type definition
            element (Node) optional
                Element declaration
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display element using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display element using 'newFields' CSS class.
            schemaLoc (String) optional
                Schema file containing this element declaration
                or type definition; if in current schema, 'schemaLoc' 
                is set to 'this'.
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSampleComplexElement">
      <xsl:param name="type"/>
      <xsl:param name="element"/>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleComplexElement </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Circular type hierarchy -->
         <xsl:when test="$type/@name and contains($typeList, concat('*', $type/@name, '+'))"/>
         <xsl:otherwise>
            <xsl:variable name="tag">
               <xsl:choose>
                  <xsl:when test="$element">
                     <!-- Prefix -->
                     <xsl:variable name="prefix">
                        <xsl:call-template name="GetElementPrefix">
                           <xsl:with-param name="element" select="$element"/>
                        </xsl:call-template>
                     </xsl:variable>
                     <xsl:call-template name="PrintNSPrefix">
                        <xsl:with-param name="prefix" select="$prefix"/>
                        <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     </xsl:call-template>
                     <xsl:value-of select="$element/@name"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>...</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:variable>

            <xsl:variable name="fromTopCType">
               <xsl:choose>
                  <xsl:when test="not($element) and local-name($type/..)='schema'">
                     <xsl:text>true</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>false</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:variable>

            <span style="margin-left: {$margin}em">
               <xsl:choose>
                  <xsl:when test="$isNewField!='false'">
                     <xsl:attribute name="class">newFields</xsl:attribute>
                  </xsl:when>
                  <xsl:when test="$isInherited!='false'">
                     <xsl:attribute name="class">inherited</xsl:attribute>
                  </xsl:when>
               </xsl:choose>
         
               <!-- get attributes -->
					<xsl:variable name="attributes">
	               <xsl:call-template name="PrintSampleTypeAttrs">
	                  <xsl:with-param name="type" select="$type"/>
	                  <xsl:with-param name="isInherited" select="$isInherited"/>
	                  <xsl:with-param name="isNewField" select="$isNewField"/>
	                  <xsl:with-param name="margin" select="$ATTR_INDENT"/>
	                  <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
	                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
	                  <xsl:with-param name="typeList" select="$typeList"/>
	               </xsl:call-template>
					</xsl:variable>

               <!-- Get content -->
               <xsl:variable name="content">
                  <xsl:call-template name="PrintSampleTypeContent">
                     <xsl:with-param name="type" select="$type"/>
                     <xsl:with-param name="margin" select="$ELEM_INDENT"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     <xsl:with-param name="typeList" select="$typeList"/>
                  </xsl:call-template>
               </xsl:variable>

               <!-- Find out if content type is mixed -->
               <xsl:variable name="mixed">
                  <xsl:choose>
                     <xsl:when test="normalize-space(translate($type/xsd:complexContent/@mixed, 'TRUE', 'true'))='true' or normalize-space($type/xsd:complexContent/@mixed)='1'">
                        <xsl:text>true</xsl:text>
                     </xsl:when>
                     <xsl:when test="normalize-space(translate($type/@mixed, 'TRUE', 'true'))='true' or normalize-space($type/@mixed)='1'">
                        <xsl:text>true</xsl:text>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:text>false</xsl:text>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:variable>

               <!-- Find out if there are identity constraints -->
               <xsl:variable name="hasIdConstraints">
                  <xsl:if test="$element and ($element/xsd:unique or $element/xsd:key or $element/xsd:keyref)">
                     <xsl:text>true</xsl:text>
                  </xsl:if>
               </xsl:variable>

               <!-- Start Tag -->
         		<xsl:choose>
         			<!-- empty attributes -->
						<xsl:when test="normalize-space($attributes)=''">
		               <tr>
		               	<th>
		                     <!-- open start tag -->
		               		<xsl:text>&lt;</xsl:text>
		               		<xsl:copy-of select="$tag"/>
		                     <!-- Close start tag -->
		               		<xsl:choose>
		               			<!-- empty content -->
		               			<xsl:when test="$hasIdConstraints!='true' and normalize-space($content)=''">
				                 		<xsl:text>/> </xsl:text>         				
		               			</xsl:when>
		               			<xsl:otherwise>
				                 		<xsl:text>> </xsl:text>         				
		               			</xsl:otherwise>
		               		</xsl:choose>
		               	</th>
		               </tr>
                 	</xsl:when>
                 	<xsl:otherwise>
		               <tr>
		               	<th>
		                     <!-- open start tag -->
		               		<xsl:text>&lt;</xsl:text>
		               		<xsl:copy-of select="$tag"/>
		               	</th>
							</tr>						
                     <!-- attribute list -->
               		<xsl:copy-of select="$attributes"/>         		
		               <tr>
		               	<th>
		                     <!-- close start tag -->
		               		<xsl:choose>
		               			<!-- empty content -->
		               			<xsl:when test="$hasIdConstraints!='true' and normalize-space($content)=''">
				                 		<xsl:text>/> </xsl:text>         				
		               			</xsl:when>
		               			<xsl:otherwise>
				                 		<xsl:text>> </xsl:text>         				
		               			</xsl:otherwise>
		               		</xsl:choose>
		               	</th>
		               </tr>
                 	</xsl:otherwise>
					</xsl:choose>
               

               <!-- Print content -->
               <xsl:choose>
                  <!-- Empty content -->
                  <xsl:when test="$hasIdConstraints!='true' and normalize-space($content)=''">

                     <xsl:if test="$element and local-name($element/..)!='schema'">
                        <!-- Occurrence info -->
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="PrintOccurs">
                           <xsl:with-param name="component" select="$element"/>
                        </xsl:call-template>

                        <!-- Documentation -->
                        <xsl:call-template name="PrintSampleDocumentation">
                           <xsl:with-param name="component" select="$element"/>
                        </xsl:call-template>
                     </xsl:if>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:if test="$element and local-name($element/..)!='schema'">
                        <!-- Occurrence info -->
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="PrintOccurs">
                           <xsl:with-param name="component" select="$element"/>
                        </xsl:call-template>

                        <!-- Documentation -->
                        <xsl:text> </xsl:text>
                        <xsl:call-template name="PrintSampleDocumentation">
                           <xsl:with-param name="component" select="$element"/>
                        </xsl:call-template>
                     </xsl:if>

                     <!-- Identity Constraints -->
                     <xsl:if test="$element">
                        <xsl:apply-templates select="$element/xsd:unique | $element/xsd:key | $element/xsd:keyref" mode="sample">
                           <xsl:with-param name="margin" select="$ELEM_INDENT"/>
                        </xsl:apply-templates>
                     </xsl:if>

                     <!-- Print out restriction/extension information -->
                     <xsl:choose>
                        <xsl:when test="false()">
                        <!--<xsl:when test="$type/xsd:complexContent/xsd:restriction/@base">-->
                           <br/><span class="other" style="margin-left: {$ELEM_INDENT}em">
                              <xsl:text>&lt;!-- Restricts : </xsl:text>
                              <xsl:call-template name="PrintTypeRef">
                                 <xsl:with-param name="ref" select="$type/xsd:complexContent/xsd:restriction/@base"/>
                                 <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                              </xsl:call-template>
                              <xsl:text> --></xsl:text>
                           </span>
                        </xsl:when>
                        <xsl:when test="false()">
                        <!--<xsl:when test="$type/xsd:complexContent/xsd:extension/@base">-->
                           <br/><span class="other" style="margin-left: {$ELEM_INDENT}em">
                              <xsl:text>&lt;!-- Extends : </xsl:text>
                              <xsl:call-template name="PrintTypeRef">
                                 <xsl:with-param name="ref" select="$type/xsd:complexContent/xsd:extension/@base"/>
                                 <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                              </xsl:call-template>
                              <xsl:text> --></xsl:text>
                           </span>
                        </xsl:when>
                     </xsl:choose>

                     <!-- Print out message if has mixed content -->
                     <xsl:if test="$mixed='true'">
                        <br/><span class="other" style="margin-left: {$ELEM_INDENT}em">
                           <xsl:text>&lt;!-- Mixed content --></xsl:text>
                        </span>
                     </xsl:if>

                     <!-- Element Content -->
                     <xsl:copy-of select="$content"/>

                     <!-- End Tag -->
                     <tr>
                     	<th>
                     		<xsl:text>&lt;/</xsl:text>
	                     <xsl:copy-of select="$tag"/>
	                     <xsl:text>></xsl:text>
                     	</th>
                     </tr>
                  </xsl:otherwise>
               </xsl:choose>
            </span>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleComplexElement </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out attributes of a complex type definition, including 
     those inherited from a base type.
     Param(s):
            type (Node) required
                Complex type definition
            subTypeAttrs (String) optional
                List of attributes in sub-types of this current type 
                definition
            isInherited (boolean) optional
                If true, display attributes using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display attributes using 'newFields' CSS class.
            fromTopCType (boolean) optional
                Set to true if this is being displayed in the XML 
                Instance Representation table of a top-level complex 
                type definition, in which case, 'inherited' attributes 
                and elements are distinguished.
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            schemaLoc (String) optional
                Schema file containing this complex type definition; 
                if in current schema, 'schemaLoc' is set to 'this'.
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSampleTypeAttrs">
      <xsl:param name="type"/>
      <xsl:param name="subTypeAttrs"/>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="fromTopCType">false</xsl:param>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleTypeAttrs </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Circular type hierarchy -->
         <xsl:when test="$type/@name and contains($typeList, concat('*', $type/@name, '+'))">
            <!-- Do nothing. 
                 Error message will be written out by 'PrintSampleTypeContent' template. 
            -->
         </xsl:when>
         <!-- Derivation -->
         <xsl:when test="$type/xsd:complexContent or $type/xsd:simpleContent">
            <xsl:choose>
               <xsl:when test="$type/xsd:complexContent/xsd:restriction">
                  <xsl:call-template name="PrintSampleDerivedTypeAttrs">
                     <xsl:with-param name="derivationElem" select="$type/xsd:complexContent/xsd:restriction"/>
                     <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
                  </xsl:call-template>
               </xsl:when>
               <xsl:when test="$type/xsd:simpleContent/xsd:restriction">
                  <xsl:call-template name="PrintSampleDerivedTypeAttrs">
                     <xsl:with-param name="derivationElem" select="$type/xsd:simpleContent/xsd:restriction"/>
                     <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
                  </xsl:call-template>
               </xsl:when>
               <xsl:when test="$type/xsd:complexContent/xsd:extension">
                  <xsl:call-template name="PrintSampleDerivedTypeAttrs">
                     <xsl:with-param name="derivationElem" select="$type/xsd:complexContent/xsd:extension"/>
                     <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
                  </xsl:call-template>
               </xsl:when>
               <xsl:when test="$type/xsd:simpleContent/xsd:extension">
                  <xsl:call-template name="PrintSampleDerivedTypeAttrs">
                     <xsl:with-param name="derivationElem" select="$type/xsd:simpleContent/xsd:extension"/>
                     <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
                     <xsl:with-param name="isInherited" select="$isInherited"/>
                     <xsl:with-param name="isNewField" select="$isNewField"/>
                     <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
                     <xsl:with-param name="margin" select="$margin"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
                  </xsl:call-template>
               </xsl:when>
            </xsl:choose>
         </xsl:when>
         <!-- No derivation -->
         <xsl:when test="local-name($type)='complexType'">
            <xsl:call-template name="PrintSampleAttrList">
               <xsl:with-param name="list" select="$type"/>
               <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
               <xsl:with-param name="isInherited" select="$isInherited"/>
               <xsl:with-param name="isNewField" select="$isNewField"/>
               <xsl:with-param name="margin" select="$margin"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Ignore base types that are simple types -->
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleTypeAttrs </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Helper function 'PrintSampleTypeAttrs' template to
     handle case of derived types.
     Param(s):
            derivationElem (Node) required
                'restriction' or 'extension' element
            subTypeAttrs (String) optional
                List of attributes in sub-types of 
                this current type definition
            isInherited (boolean) optional
                If true, display attributes using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display attributes using 'newFields' CSS class.
            fromTopCType (boolean) optional
                Set to true if this is being displayed 
                in the XML Instance Representation table 
                of a top-level complex type definition, in
                which case, 'inherited' attributes and
                elements are distinguished.
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            schemaLoc (String) optional
                Schema file containing this derivation element; 
                if in current schema, 'schemaLoc' is set to 'this'.
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSampleDerivedTypeAttrs">
      <xsl:param name="derivationElem"/>
      <xsl:param name="subTypeAttrs"/>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="fromTopCType">false</xsl:param>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleDerivedTypeAttrs </xsl:comment>
		</xsl:if>

      <!-- Get attributes from this type to add to 
            'subTypeAttrs' list for recursive call on base type -->
      <xsl:variable name="thisAttrs">
         <xsl:call-template name="GetAttrList">
            <xsl:with-param name="list" select="$derivationElem"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Look for base type -->
      <xsl:variable name="baseTypeName">
         <xsl:call-template name="GetRefName">
            <xsl:with-param name="ref" select="$derivationElem/@base"/>
         </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="defLoc">
         <xsl:call-template name="FindComponent">
            <xsl:with-param name="ref" select="$derivationElem/@base"/>
            <xsl:with-param name="compType">complex type</xsl:with-param>
         </xsl:call-template>
      </xsl:variable>
      <xsl:choose>
         <!-- Complex type was found in current schema. -->
         <xsl:when test="normalize-space($defLoc)='this'">
            <xsl:variable name="ctype" select="key('complexType', $baseTypeName)"/>
            <xsl:call-template name="PrintSampleTypeAttrs">
               <xsl:with-param name="type" select="$ctype"/>
               <xsl:with-param name="subTypeAttrs" select="concat($subTypeAttrs, $thisAttrs)"/>
               <xsl:with-param name="isInherited">
                  <xsl:choose>
                     <xsl:when test="$fromTopCType!='false'">
                        <xsl:text>true</xsl:text>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:text>false</xsl:text>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
               <xsl:with-param name="isNewField" select="$isNewField"/>
               <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
               <xsl:with-param name="margin" select="$margin"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               <xsl:with-param name="typeList" select="$typeList"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Complex type was not found. -->
         <xsl:when test="normalize-space($defLoc)='' or normalize-space($defLoc)='none' or normalize-space($defLoc)='xml' or normalize-space($defLoc)='xsd'">
            <!-- IGNORE: Error message will be printed out be
                 'PrintSampleTypeContent' template. -->
         </xsl:when>
         <!-- Complex type was found in external schema. -->
         <xsl:otherwise>
            <xsl:variable name="ctype" select="document($defLoc)/xsd:schema/xsd:complexType[@name=$baseTypeName]"/>
            <xsl:call-template name="PrintSampleTypeAttrs">
               <xsl:with-param name="type" select="$ctype"/>
               <xsl:with-param name="subTypeAttrs" select="concat($subTypeAttrs, $thisAttrs)"/>
               <xsl:with-param name="isInherited">
                  <xsl:choose>
                     <xsl:when test="$fromTopCType!='false'">
                        <xsl:text>true</xsl:text>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:text>false</xsl:text>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:with-param>
               <xsl:with-param name="isNewField" select="$isNewField"/>
               <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
               <xsl:with-param name="margin" select="$margin"/>
               <xsl:with-param name="schemaLoc" select="$defLoc"/>
               <xsl:with-param name="typeList" select="$typeList"/>
            </xsl:call-template>
         </xsl:otherwise>
      </xsl:choose>

      <!-- Print out attributes in this type -->
      <xsl:call-template name="PrintSampleAttrList">
         <xsl:with-param name="list" select="$derivationElem"/>
         <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
         <xsl:with-param name="isInherited" select="$isInherited"/>
         <xsl:with-param name="isNewField">
            <xsl:choose>
               <xsl:when test="$fromTopCType!='false' and $isInherited='false'">
                  <xsl:text>true</xsl:text>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:text>false</xsl:text>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:with-param>
         <xsl:with-param name="margin" select="$margin"/>
         <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
      </xsl:call-template>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleDerivedTypeAttrs </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the names and namespaces of attributes
     in a list of attributes and attribute groups.
     Param(s):
            list (Node) required
                Node containing list of attributes and attribute groups
     -->
   <xsl:template name="GetAttrList">
      <xsl:param name="list"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetAttrList </xsl:comment>
		</xsl:if>

      <xsl:if test="$list">
         <xsl:for-each select="$list/xsd:attribute | $list/xsd:attributeGroup | $list/xsd:anyAttribute">
            <xsl:choose>
               <!-- Attribute declaration -->
               <xsl:when test="local-name(.)='attribute' and @name">
                  <!-- Get attribute name -->
                  <xsl:variable name="attrName" select="@name"/>
                  <!-- Get attribute namespace -->
                  <xsl:variable name="attrNS">
                     <xsl:call-template name="GetAttributeNS">
                        <xsl:with-param name="attribute" select="."/>
                     </xsl:call-template>
                  </xsl:variable>

                  <xsl:value-of select="concat('*', normalize-space($attrNS), '+', normalize-space($attrName), '+')"/>
               </xsl:when>
               <!-- Attribute reference -->
               <xsl:when test="local-name(.)='attribute' and @ref">
                  <!-- Get attribute name -->
                  <xsl:variable name="attrName">
                     <xsl:call-template name="GetRefName">
                        <xsl:with-param name="ref" select="@ref"/>
                     </xsl:call-template>
                  </xsl:variable>
                  <!-- Get attribute namespace -->
                  <xsl:variable name="attrNS">
                     <xsl:call-template name="GetAttributeNS">
                        <xsl:with-param name="attribute" select="."/>
                     </xsl:call-template>
                  </xsl:variable>

                  <xsl:value-of select="concat('*', normalize-space($attrNS), '+', normalize-space($attrName), '+')"/>
               </xsl:when>
               <!-- Attribute Group reference -->
               <xsl:when test="local-name(.)='attributeGroup' and @ref">
                  <xsl:variable name="attrGrpName">
                     <xsl:call-template name="GetRefName">
                        <xsl:with-param name="ref" select="@ref"/>
                     </xsl:call-template>
                  </xsl:variable>
                  <xsl:call-template name="GetAttrList">
                     <xsl:with-param name="list" select="key('attributeGroup', $attrGrpName)"/>
                  </xsl:call-template>
               </xsl:when>
               <!-- Attribute wildcard -->
               <xsl:when test="local-name(.)='anyAttribute'">
               </xsl:when>
            </xsl:choose>
         </xsl:for-each>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetAttrList </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out sample XML instances from a list of attributes and 
     attribute groups.
     Param(s):
            list (Node) required
                Node containing list of attributes and attribute groups
            subTypeAttrs (String) optional
                List of attributes in sub-types of 
                the type definition containing this list
            isInherited (boolean) optional
                If true, display attributes using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display attributes using 'newFields' CSS class.
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            schemaLoc (String) optional
                Schema file containing this attribute list; 
                if in current schema, 'schemaLoc' is set to 'this'.
     -->
   <xsl:template name="PrintSampleAttrList">
      <xsl:param name="list"/>
      <xsl:param name="subTypeAttrs"/>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleAttrList </xsl:comment>
		</xsl:if>

      <xsl:apply-templates select="$list/xsd:attribute | $list/xsd:attributeGroup | $list/xsd:anyAttribute" mode="sample">
         <xsl:with-param name="subTypeAttrs" select="$subTypeAttrs"/>
         <xsl:with-param name="isInherited" select="$isInherited"/>
         <xsl:with-param name="isNewField" select="$isNewField"/>
         <xsl:with-param name="margin" select="$margin"/>
         <xsl:with-param name="addBR">true</xsl:with-param>
         <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
      </xsl:apply-templates>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleAttrList </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the element content of a complex type definition,
     including those inherited from a base type.
     Param(s):
            type (Node) required
                Complex type definition
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS class.
            fromTopCType (boolean) optional
                Set to true if this is being displayed in the XML 
                Instance Representation table of a top-level complex 
                type definition, in which case, 'inherited' attributes 
                and elements are distinguished.
            addBR (boolean) optional
                If true, can add <br/> before element content.
                Applicable only if displaying complex content.
            schemaLoc (String) optional
                Schema file containing this type definition; 
                if in current schema, 'schemaLoc' is set to 'this'.
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSampleTypeContent">
      <xsl:param name="type"/>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="fromTopCType">false</xsl:param>
      <xsl:param name="addBR">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleTypeContent </xsl:comment>
		</xsl:if>

      <xsl:if test="$addBR='true'"><br/></xsl:if>

      <xsl:choose>
         <!-- Circular type hierarchy -->
         <xsl:when test="$type/@name and contains($typeList, concat('*', $type/@name, '+'))"/>
         <!-- Derivation by restriction on complex content -->
         <xsl:when test="$type/xsd:complexContent/xsd:restriction">
            <xsl:variable name="restriction" select="$type/xsd:complexContent/xsd:restriction"/>

            <!-- Test if base type is in schema to print out warning comment-->
            <xsl:variable name="baseTypeName">
               <xsl:call-template name="GetRefName">
                  <xsl:with-param name="ref" select="$restriction/@base"/>
               </xsl:call-template>
            </xsl:variable>
            <!-- Look for base type -->
            <xsl:variable name="defLoc">
               <xsl:call-template name="FindComponent">
                  <xsl:with-param name="ref" select="$restriction/@base"/>
                  <xsl:with-param name="compType">complex type</xsl:with-param>
               </xsl:call-template>
            </xsl:variable>

            <xsl:choose>
               <!-- Complex type was not found. -->
               <xsl:when test="normalize-space($defLoc)='' or normalize-space($defLoc)='none' or normalize-space($defLoc)='xml' or normalize-space($defLoc)='xsd'">
                  <div class="other" style="margin-left: {$margin}em;">
                     <xsl:text>&lt;!-- '</xsl:text>
                     <xsl:call-template name="PrintTypeRef">
                        <xsl:with-param name="ref" select="$restriction/@base"/>
                        <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     </xsl:call-template>
                     <xsl:text>' super type was not found in this schema. Some elements and attributes may be missing. --></xsl:text>
                  </div>
               </xsl:when>
               <!-- Complex type was found. -->
               <xsl:otherwise>
                  <!-- IGNORE element content of base type if by restriction,
                       since current content will override restricted
                       base type's content. -->
               </xsl:otherwise>
            </xsl:choose>

            <!-- Print out content from this type -->
            <xsl:if test="$restriction/xsd:*[local-name(.)!='annotation']">
               <xsl:call-template name="PrintSampleParticleList">
                  <xsl:with-param name="list" select="$restriction"/>
                  <xsl:with-param name="margin" select="$margin"/>
                  <xsl:with-param name="isInherited" select="$isInherited"/>
                  <xsl:with-param name="isNewField">
                     <xsl:choose>
                        <xsl:when test="$fromTopCType!='false' and $isInherited='false'">
                           <xsl:text>true</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:text>false</xsl:text>
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:with-param>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                  <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
               </xsl:call-template>
            </xsl:if>
         </xsl:when>
         <!-- Derivation by extension on complex content -->
         <xsl:when test="$type/xsd:complexContent/xsd:extension">
            <xsl:variable name="extension" select="$type/xsd:complexContent/xsd:extension"/>

            <xsl:variable name="baseTypeName">
               <xsl:call-template name="GetRefName">
                  <xsl:with-param name="ref" select="$extension/@base"/>
               </xsl:call-template>
            </xsl:variable>

            <xsl:choose>
               <xsl:when test="contains($typeList, concat('*', $baseTypeName, '+'))">
                  <div class="other" style="margin-left: {$margin}em">
                     <xsl:text>&lt;-- Extends: </xsl:text>
                     <xsl:call-template name="PrintTypeRef">
                        <xsl:with-param name="ref" select="$extension/@base"/>
                        <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                     </xsl:call-template>
                     <xsl:text> (Circular type hierarchy) --&gt;</xsl:text>
                  </div>
               </xsl:when>
               <xsl:otherwise>
                  <!-- Look for base type -->
                  <xsl:variable name="defLoc">
                     <xsl:call-template name="FindComponent">
                        <xsl:with-param name="ref" select="$extension/@base"/>
                        <xsl:with-param name="compType">complex type</xsl:with-param>
                     </xsl:call-template>
                  </xsl:variable>

                  <xsl:choose>
                     <!-- Complex type was found in current schema. -->
                     <xsl:when test="normalize-space($defLoc)='this'">
                        <xsl:variable name="ctype" select="key('complexType', $baseTypeName)"/>
                        <xsl:call-template name="PrintSampleTypeContent">
                           <xsl:with-param name="type" select="$ctype"/>
                           <xsl:with-param name="margin" select="$margin"/>
                           <xsl:with-param name="isInherited">
                              <xsl:choose>
                                 <xsl:when test="$fromTopCType!='false'">
                                    <xsl:text>true</xsl:text>
                                 </xsl:when>
                                 <xsl:otherwise>
                                    <xsl:text>false</xsl:text>
                                 </xsl:otherwise>
                              </xsl:choose>
                           </xsl:with-param>
                           <xsl:with-param name="isNewField" select="$isNewField"/>
                           <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
                           <xsl:with-param name="addBR">false</xsl:with-param>
                           <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                           <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
                        </xsl:call-template>
                     </xsl:when>
                     <!-- Complex type was not found. -->
                     <xsl:when test="normalize-space($defLoc)='' or normalize-space($defLoc)='none' or normalize-space($defLoc)='xml' or normalize-space($defLoc)='xsd'">
                        <div class="other" style="margin-left: {$margin}em;">
                           <xsl:text>&lt;!-- '</xsl:text>
                           <xsl:call-template name="PrintTypeRef">
                              <xsl:with-param name="ref" select="$extension/@base"/>
                              <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                           </xsl:call-template>
                           <xsl:text>' super type was not found in this schema. Some elements and attributes may be missing. --></xsl:text>
                        </div>
                     </xsl:when>
                     <!-- Complex type was found in external schema. -->
                     <xsl:otherwise>
                        <xsl:variable name="ctype" select="document($defLoc)/xsd:schema/xsd:complexType[@name=$baseTypeName]"/>
                        <xsl:call-template name="PrintSampleTypeContent">
                           <xsl:with-param name="type" select="$ctype"/>
                           <xsl:with-param name="margin" select="$margin"/>
                           <xsl:with-param name="isInherited">
                              <xsl:choose>
                                 <xsl:when test="$fromTopCType!='false'">
                                    <xsl:text>true</xsl:text>
                                 </xsl:when>
                                 <xsl:otherwise>
                                    <xsl:text>false</xsl:text>
                                 </xsl:otherwise>
                              </xsl:choose>
                           </xsl:with-param>
                           <xsl:with-param name="isNewField" select="$isNewField"/>
                           <xsl:with-param name="fromTopCType" select="$fromTopCType"/>
                           <xsl:with-param name="addBR">false</xsl:with-param>
                           <xsl:with-param name="schemaLoc" select="$defLoc"/>
                           <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
                        </xsl:call-template>
                     </xsl:otherwise>
                  </xsl:choose>

                  <!-- Print out content from this type -->
                  <xsl:if test="$extension/xsd:*[local-name(.)!='annotation']">
                     <xsl:call-template name="PrintSampleParticleList">
                        <xsl:with-param name="list" select="$extension"/>
                        <xsl:with-param name="margin" select="$margin"/>
                        <xsl:with-param name="isInherited" select="$isInherited"/>
                        <xsl:with-param name="isNewField">
                           <xsl:choose>
                              <xsl:when test="$fromTopCType!='false' and $isInherited='false'">
                                 <xsl:text>true</xsl:text>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:text>false</xsl:text>
                              </xsl:otherwise>
                           </xsl:choose>
                        </xsl:with-param>
                        <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                        <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
                     </xsl:call-template>
                  </xsl:if>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:when>
         <!-- Derivation by restriction on simple content -->
         <xsl:when test="$type/xsd:simpleContent/xsd:restriction">
            <!-- Print out simple type constraints-->
            <span style="margin-left: {$margin}em">
               <xsl:text> </xsl:text>
               <xsl:apply-templates select="$type/xsd:simpleContent" mode="sample">
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:apply-templates>
               <xsl:text> </xsl:text>
            </span><br/>
         </xsl:when>
         <!-- Derivation by extension on simple content -->
         <xsl:when test="$type/xsd:simpleContent/xsd:extension">
            <!-- Print out base type name -->
            <span style="margin-left: {$margin}em">
               <xsl:text> </xsl:text>
               <xsl:call-template name="PrintTypeRef">
                  <xsl:with-param name="ref" select="$type/xsd:simpleContent/xsd:extension/@base"/>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
               <xsl:text> </xsl:text>
            </span><br/>
         </xsl:when>
         <!-- No derivation: complex type definition -->
         <xsl:when test="local-name($type)='complexType'">
            <!-- Print out content from this type -->
            <xsl:call-template name="PrintSampleParticleList">
               <xsl:with-param name="list" select="$type"/>
               <xsl:with-param name="margin" select="$margin"/>
               <xsl:with-param name="isInherited" select="$isInherited"/>
               <xsl:with-param name="isNewField" select="$isNewField"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               <xsl:with-param name="typeList" select="concat($typeList, '*', $type/@name, '+')"/>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleTypeContent </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out sample XML instances from a list of 
     element particle.
     Param(s):
            list (Node) required
                Node containing list of element particles
            margin (nonNegativeInteger) optional
                Number of 'em' to indent from left
            isInherited (boolean) optional
                If true, display elements using 'inherited' CSS class.
            isNewField (boolean) optional
                If true, display elements using 'newFields' CSS class.
            schemaLoc (String) optional
                Schema file containing this particle list; 
                if in current schema, 'schemaLoc' is set to 'this'.
            typeList (String) optional
                List of types in this call chain. Name of type starts
                with '*', and ends with '+'. (Used to prevent infinite
                recursive loop.)
     -->
   <xsl:template name="PrintSampleParticleList">
      <xsl:param name="list"/>
      <xsl:param name="margin">0</xsl:param>
      <xsl:param name="isInherited">false</xsl:param>
      <xsl:param name="isNewField">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleParticleList </xsl:comment>
		</xsl:if>

      <xsl:if test="$list">
         <xsl:apply-templates select="$list/xsd:group | $list/xsd:sequence | $list/xsd:choice | $list/xsd:all | $list/xsd:element" mode="sample">
            <xsl:with-param name="margin" select="$margin"/>
            <xsl:with-param name="isInherited" select="$isInherited"/>
            <xsl:with-param name="isNewField" select="$isNewField"/>
            <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            <xsl:with-param name="typeList" select="$typeList"/>
         </xsl:apply-templates>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleParticleList </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the constraints of simple content
     to be displayed within a sample XML instance.
     Param(s):
            simpleContent (Node) required
                Node containing with the simple content
            schemaLoc (String) optional
                Schema file containing these simple constraints; 
                if in current schema, 'schemaLoc' is set to 'this'.
     -->
   <xsl:template name="PrintSampleSimpleConstraints">
      <xsl:param name="simpleContent"/>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleSimpleConstraints </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Derivation by restriction -->
         <xsl:when test="$simpleContent/xsd:restriction">
            <xsl:call-template name="PrintSampleSimpleRestriction">
               <xsl:with-param name="restriction" select="$simpleContent/xsd:restriction"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               <xsl:with-param name="typeList" select="$typeList"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Derivation by list -->
         <xsl:when test="$simpleContent/xsd:list">
            <xsl:choose>
               <xsl:when test="$simpleContent/xsd:list/@itemType">
                  <xsl:text>list of: </xsl:text>
                  <xsl:call-template name="PrintTypeRef">
                     <xsl:with-param name="ref" select="$simpleContent/xsd:list/@itemType"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                  </xsl:call-template>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:text>list of: [ </xsl:text>
                  <xsl:call-template name="PrintSampleSimpleConstraints">
                     <xsl:with-param name="simpleContent" select="$simpleContent/xsd:list/xsd:simpleType"/>
                     <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
                  </xsl:call-template>
                  <xsl:text> ]</xsl:text>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:when>
         <!-- Derivation by union -->
         <xsl:when test="$simpleContent/xsd:union">
            <xsl:text>union of: [ </xsl:text>

            <xsl:variable name="hasMemberTypes">
               <xsl:if test="normalize-space($simpleContent/xsd:union/@memberTypes)!=''">
                  <xsl:text>true</xsl:text>
               </xsl:if>
            </xsl:variable>
            <xsl:if test="$hasMemberTypes='true'">
               <xsl:call-template name="PrintWhitespaceList">
                  <xsl:with-param name="value" select="$simpleContent/xsd:union/@memberTypes"/>
                  <xsl:with-param name="compType">type</xsl:with-param>
                  <xsl:with-param name="separator">,</xsl:with-param>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
            </xsl:if>
            <xsl:for-each select="$simpleContent/xsd:union/xsd:simpleType">
               <xsl:if test="position()!=1 or $hasMemberTypes='true'">
                  <xsl:text>, </xsl:text>
               </xsl:if>
               <xsl:text>[ </xsl:text>
               <xsl:call-template name="PrintSampleSimpleConstraints">
                  <xsl:with-param name="simpleContent" select="."/>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
               <xsl:text> ]</xsl:text>
            </xsl:for-each>

            <xsl:text> ]</xsl:text>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleSimpleConstraints </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the constraints of simple content
     derived by restriction, which is to be displayed 
     within a sample XML instance.
     Param(s):
            restriction (Node) required
                Node containing with the restriction
            schemaLoc (String) optional
                Schema file containing this restriction element; 
                if in current schema, 'schemaLoc' is set to 'this'.
     -->
   <xsl:template name="PrintSampleSimpleRestriction">
      <xsl:param name="restriction"/>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="typeList"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSampleSimpleRestriction </xsl:comment>
		</xsl:if>

      <xsl:variable name="typeName" select="$restriction/parent::xsd:simpleType/@name"/>

      <!-- Print out base type info -->
      <xsl:choose>
         <!-- Circular type hierarchy -->
         <xsl:when test="$typeName != '' and contains($typeList, concat('*', $typeName, '+'))">
            <xsl:call-template name="HandleError">
               <xsl:with-param name="isTerminating">false</xsl:with-param>
               <xsl:with-param name="errorMsg">
                  <xsl:text>Circular type reference to '</xsl:text>
                  <xsl:value-of select="$typeName"/>
                  <xsl:text>' in type hierarchy.</xsl:text>
               </xsl:with-param>
            </xsl:call-template>
         </xsl:when>
         <!-- Locally-defined base type -->
         <xsl:when test="$restriction/xsd:simpleType">
            <xsl:call-template name="PrintSampleSimpleConstraints">
               <xsl:with-param name="simpleContent" select="$restriction/xsd:simpleType"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               <xsl:with-param name="typeList" select="$typeList"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Base type reference -->
         <xsl:when test="$restriction">
            <xsl:variable name="baseTypeRef" select="$restriction/@base"/>
            <xsl:variable name="baseTypeName">
               <xsl:call-template name="GetRefName">
                  <xsl:with-param name="ref" select="$baseTypeRef"/>
               </xsl:call-template>
            </xsl:variable>
            <xsl:variable name="baseTypeNS">
               <xsl:call-template name="GetRefNS">
                  <xsl:with-param name="ref" select="$baseTypeRef"/>
               </xsl:call-template>
            </xsl:variable>
            <!-- Write out reference to base type -->
            <xsl:call-template name="PrintTypeRef">
               <xsl:with-param name="ref" select="$baseTypeRef"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>

      <!-- Regular Expression Pattern -->
      <xsl:variable name="pattern">
         <xsl:call-template name="PrintPatternFacet">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>
      <!-- Range -->
      <xsl:variable name="range">
         <xsl:call-template name="PrintRangeFacets">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>
      <!-- Length -->
      <xsl:variable name="length">
         <xsl:call-template name="PrintLengthFacets">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Print out facets -->
      <xsl:if test="$restriction/xsd:enumeration">
         <xsl:text> (</xsl:text>
         <xsl:call-template name="PrintEnumFacets">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
         <xsl:text>)</xsl:text>
      </xsl:if>
      <xsl:if test="$pattern !=''">
         <xsl:text> (</xsl:text>
         <xsl:copy-of select="$pattern"/>
         <xsl:text>)</xsl:text>
      </xsl:if>
      <xsl:if test="$range !=''">
         <xsl:text> (</xsl:text>
         <xsl:copy-of select="$range"/>
         <xsl:text>)</xsl:text>
      </xsl:if>
      <xsl:if test="$restriction/xsd:totalDigits">
         <xsl:text> (</xsl:text>
         <xsl:call-template name="PrintTotalDigitsFacet">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
         <xsl:text>)</xsl:text>
      </xsl:if>
      <xsl:if test="$restriction/xsd:fractionDigits">
         <xsl:text> (</xsl:text>
         <xsl:call-template name="PrintFractionDigitsFacet">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
         <xsl:text>)</xsl:text>
      </xsl:if>
      <xsl:if test="$length !=''">
         <xsl:text> (</xsl:text>
         <xsl:copy-of select="$length"/>
         <xsl:text>)</xsl:text>
      </xsl:if>
      <xsl:if test="$restriction/xsd:whiteSpace">
         <xsl:text> (</xsl:text>
         <xsl:call-template name="PrintWhitespaceFacet">
            <xsl:with-param name="simpleRestrict" select="$restriction"/>
         </xsl:call-template>
         <xsl:text>)</xsl:text>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSampleSimpleRestriction </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!-- ******** XML Pretty Printer ******** -->

   <!--
     Puts XHTML elements into the result.
     -->
   <xsl:template match="html:*" mode="html">
      <xsl:element name="{local-name(.)}">
         <xsl:for-each select="@*">
            <xsl:copy-of select="."/>
         </xsl:for-each>
         <xsl:apply-templates select="* | text()" mode="html"/>
      </xsl:element>
   </xsl:template>

   <!--
     Displays non-XHTML elements found within XHTML elements.
     -->
   <xsl:template match="*" mode="html">
      <xsl:call-template name="WriteElement">
         <xsl:with-param name="element" select="."/>
         <xsl:with-param name="mode">html</xsl:with-param>
      </xsl:call-template>
   </xsl:template>

   <!--
     Displays text node.
     -->
   <xsl:template match="text()" mode="html">
      <xsl:value-of select="."/>
   </xsl:template>

   <!--
     Displays an arbitrary XML element.
     -->
   <xsl:template match="*" mode="xpp">
      <code>
         <xsl:call-template name="WriteElement">
            <xsl:with-param name="element" select="."/>
            <xsl:with-param name="mode">xpp</xsl:with-param>
         </xsl:call-template>
      </code>
   </xsl:template>

   <!--
     Displays an arbitrary XML text node.
     -->
   <xsl:template match="text()" mode="xpp">
      <xsl:value-of select="."/>
   </xsl:template>

   <!--
     Displays an XML comment.
     -->
   <xsl:template match="comment()" mode="xpp">
      <div class="comment">
         <xsl:text>&lt;--</xsl:text>
         <xsl:value-of select="."/>
         <xsl:text>--&gt;</xsl:text>
      </div>
   </xsl:template>

   <!--
     Displays an XML element in the documentation, e.g.
     tags are escaped.
     Param(s):
            element (Node) required
                XML element to display
            mode (xpp|html) required
                Which mode to invoke for child elements
     -->
   <xsl:template name="WriteElement">
      <xsl:param name="element"/>
      <xsl:param name="mode">xpp</xsl:param>

      <!-- Start Tag -->
      <xsl:text>&lt;</xsl:text>
      <xsl:call-template name="PrintNSPrefix">
         <xsl:with-param name="prefix">
            <xsl:call-template name="GetRefPrefix">
               <xsl:with-param name="ref" select="name($element)"/>
            </xsl:call-template>
         </xsl:with-param>
      </xsl:call-template>
      <xsl:value-of select="local-name($element)"/>
      <!-- Attributes -->
      <xsl:for-each select="$element/@*">
         <xsl:text> </xsl:text>
         <xsl:value-of select="name(.)"/>
         <xsl:text>="</xsl:text>
         <xsl:value-of select="."/>
         <xsl:text>"</xsl:text>
      </xsl:for-each>

      <xsl:choose>
         <xsl:when test="$element/* | $element/text()">
            <!-- Close Start Tag -->
            <xsl:text>> </xsl:text>
            <!-- Content -->
            <xsl:choose>
               <xsl:when test="$mode!='xpp'">
                  <xsl:apply-templates select="$element/* | $element/text()" mode="html"/>
               </xsl:when>
               <xsl:otherwise>
                  <div style="margin-left: {$ELEM_INDENT}em">
                     <xsl:apply-templates select="$element/* | $element/text()" mode="xpp"/>
                  </div>
               </xsl:otherwise>
            </xsl:choose>
            <!-- End Tag -->
            <xsl:text>&lt;/</xsl:text>
            <xsl:call-template name="PrintNSPrefix">
               <xsl:with-param name="prefix">
                  <xsl:call-template name="GetRefPrefix">
                     <xsl:with-param name="ref" select="name($element)"/>
                  </xsl:call-template>
               </xsl:with-param>
            </xsl:call-template>
            <xsl:value-of select="local-name($element)"/>
            <xsl:text>></xsl:text>
         </xsl:when>
         <xsl:otherwise>
            <!-- Close Start Tag -->
            <xsl:text>/></xsl:text>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>


   <!-- ******** Templates for Handling References ******** -->

   <!--
     Prints out a reference to a term in the glossary section.
     Param(s):
            code (String) required
              Unique ID of glossary term
            term (String) optional
              Glossary term
     -->
   <xsl:template name="PrintGlossaryTermRef">
      <xsl:param name="code"/>
      <xsl:param name="term"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintGlossaryTermRef </xsl:comment>
		</xsl:if>

      <xsl:value-of select="$term"/>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintGlossaryTermRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a reference to a namespace in the schema.
     Param(s):
            prefix (String) required
              Namespace prefix referenced
            schemaLoc (String) optional
                Schema file containing this namespace prefix;
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintNamespaceRef">
      <xsl:param name="prefix"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintNamespaceRef </xsl:comment>
		</xsl:if>

      <xsl:if test="$prefix!=''">
         <xsl:choose>
            <xsl:when test="/xsd:schema/namespace::*[local-name(.)=normalize-space($prefix)]">
               <xsl:variable name="link">
                  <xsl:if test="normalize-space($schemaLoc)!='this'">
                     <xsl:call-template name="GetSchemaDocLocation">
                        <xsl:with-param name="uri" select="$schemaLoc"/>
                     </xsl:call-template>
                  </xsl:if>
                  <xsl:value-of select="concat('#', $NS_PREFIX, $prefix)"/>
               </xsl:variable>
               <a href="{$link}" title="Find out namespace of '{$prefix}' prefix">
                  <xsl:value-of select="$prefix"/>
               </a>
            </xsl:when>
            <xsl:otherwise>
               <xsl:variable name="title">
                  <xsl:text>Unknown namespace prefix, </xsl:text>
                  <xsl:value-of select="$prefix"/>
                  <xsl:text>.</xsl:text>
               </xsl:variable>
               <a href="javascript:void(0)" onclick="alert('{$title}')" title="{$title}">
                  <xsl:value-of select="$prefix"/>
               </a>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintNamespaceRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Generates a link to an attribute.
     Param(s):
            name (String) optional
                Name of attribute
            ref (String) optional
                Reference to attribute
            (One of 'name' and 'ref' must be provided.)
            schemaLoc (String) optional
                Schema file containing this attribute reference
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintAttributeRef">
      <xsl:param name="name"/>
      <xsl:param name="ref"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintAttributeRef </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="$name!=''">
            <xsl:call-template name="PrintCompName">
               <xsl:with-param name="name" select="$name"/>
               <xsl:with-param name="compType">attribute</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="$ref!=''">
            <xsl:call-template name="PrintCompRef">
               <xsl:with-param name="ref" select="$ref"/>
               <xsl:with-param name="compType">attribute</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintAttributeRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Generates a link to an attribute group.
     Param(s):
            name (String) optional
                Name of attribute group
            ref (String) optional
                Reference to attribute group
            (One of 'name' and 'ref' must be provided.)
            schemaLoc (String) optional
                Schema file containing this attribute group reference
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintAttributeGroupRef">
      <xsl:param name="name"/>
      <xsl:param name="ref"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintAttributeGroupRef </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="$name!=''">
            <xsl:call-template name="PrintCompName">
               <xsl:with-param name="name" select="$name"/>
               <xsl:with-param name="compType">attribute group</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="$ref!=''">
            <xsl:call-template name="PrintCompRef">
               <xsl:with-param name="ref" select="$ref"/>
               <xsl:with-param name="compType">attribute group</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintAttributeGroupRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Generates a link to an element.
     Param(s):
            name (String) optional
                Name of element
            ref (String) optional
                Reference to element
            (One of 'name' and 'ref' must be provided.)
            schemaLoc (String) optional
                Schema file containing this element reference
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintElementRef">
      <xsl:param name="name"/>
      <xsl:param name="ref"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintElementRef </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="$name!=''">
            <xsl:call-template name="PrintCompName">
               <xsl:with-param name="name" select="$name"/>
               <xsl:with-param name="compType">element</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="$ref!=''">
            <xsl:call-template name="PrintCompRef">
               <xsl:with-param name="ref" select="$ref"/>
               <xsl:with-param name="compType">element</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintElementRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Generates a link to a group.
     Param(s):
            name (String) optional
                Name of group
            ref (String) optional
                Reference to group
            (One of 'name' and 'ref' must be provided.)
            schemaLoc (String) optional
                Schema file containing this group reference
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintGroupRef">
      <xsl:param name="name"/>
      <xsl:param name="ref"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintGroupRef </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="$name!=''">
            <xsl:call-template name="PrintCompName">
               <xsl:with-param name="name" select="$name"/>
               <xsl:with-param name="compType">group</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="$ref!=''">
            <xsl:call-template name="PrintCompRef">
               <xsl:with-param name="ref" select="$ref"/>
               <xsl:with-param name="compType">group</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintGroupRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Generates a link to a key/uniqueness constraint.
     Param(s):
            name (String) optional
                Name of key/uniqueness constraint
            ref (String) optional
                Reference to key/uniqueness constraint
            (One of 'name' and 'ref' must be provided.)
            schemaLoc (String) optional
                Schema file containing this key/uniqueness constraint
                reference; if in current schema, 'schemaLoc' is set 
                to 'this'
     -->
   <xsl:template name="PrintKeyRef">
      <xsl:param name="name"/>
      <xsl:param name="ref"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintKeyRef </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="$name!=''">
            <xsl:call-template name="PrintCompName">
               <xsl:with-param name="name" select="$name"/>
               <xsl:with-param name="compType">uniqueness/key constraint</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="$ref!=''">
            <xsl:call-template name="PrintCompRef">
               <xsl:with-param name="ref" select="$ref"/>
               <xsl:with-param name="compType">uniqueness/key constraint</xsl:with-param>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintKeyRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Generates a link to a type.
     Param(s):
            name (String) optional
                Name of type
            ref (String) optional
                Reference to type
            (One of 'name' and 'ref' must be provided.)
            schemaLoc (String) optional
                Schema file containing this type reference'
                if in current schema, 'schemaLoc' is set 
                to 'this'
     -->
   <xsl:template name="PrintTypeRef">
      <xsl:param name="name"/>
      <xsl:param name="ref"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintTypeRef </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="$name!=''">
            <span class="type">
               <xsl:call-template name="PrintCompName">
                  <xsl:with-param name="name" select="$name"/>
                  <xsl:with-param name="compType">type</xsl:with-param>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
            </span>
         </xsl:when>
         <xsl:when test="$ref!=''">
            <span class="type">
               <xsl:call-template name="PrintCompRef">
                  <xsl:with-param name="ref" select="$ref"/>
                  <xsl:with-param name="compType">type</xsl:with-param>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
            </span>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintTypeRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a link to a schema component's section.
     Param(s):
            baseFile (String) optional
                Documentation file of schema containing this 
                component.
                If this component belongs to the current schema, 
                omit this variable.
                If this component is from an included or imported 
                schema, provide this variable.
            name (String) required
                Name of schema component
            compType (String) required
                Type of schema component
            errMsg (String) optional
                Sentence fragment.
                If specified, link will open up an alert box with 
                an error message. For example, if 'errMsg' was set 
                to "could not be found", 'name' was "x", and
                'compType' was "type", the error message would be:
                "x" type definition could not be found.
                The sentence fragment should not:
                -start with a capital letter.
                -have a space in front
                -end with a period.
            schemaLoc (String) optional
                Schema file containing this component;
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintCompName">
      <xsl:param name="baseFile"/>
      <xsl:param name="name"/>
      <xsl:param name="compType"/>
      <xsl:param name="schemaLoc">this</xsl:param>
      <xsl:param name="errMsg"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintCompName </xsl:comment>
		</xsl:if>

      <!-- Get correct terminology for statements -->
      <xsl:variable name="noun">
         <xsl:choose>
            <xsl:when test="$compType='element' or $compType='attribute'">
               <xsl:text>declaration</xsl:text>
            </xsl:when>
            <xsl:otherwise>
               <xsl:text>definition</xsl:text>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>

      <!-- Get prefix to use in anchor name. -->
      <xsl:variable name="compPrefix">
         <xsl:choose>
            <xsl:when test="$compType='attribute'">
               <xsl:value-of select="$ATTR_PREFIX"/>
            </xsl:when>
            <xsl:when test="$compType='attribute group'">
               <xsl:value-of select="$ATTR_GRP_PREFIX"/>
            </xsl:when>
            <xsl:when test="$compType='element'">
               <xsl:value-of select="$ELEM_PREFIX"/>
            </xsl:when>
            <xsl:when test="$compType='group'">
               <xsl:value-of select="$GRP_PREFIX"/>
            </xsl:when>
            <xsl:when test="$compType='type'">
               <xsl:value-of select="$TYPE_PREFIX"/>
            </xsl:when>
            <xsl:when test="$compType='uniqueness/key constraint'">
               <xsl:value-of select="$KEY_PREFIX"/>
            </xsl:when>
         </xsl:choose>
      </xsl:variable>

      <!-- Get base URI. -->
      <xsl:variable name="baseURI">
         <xsl:choose>
            <xsl:when test="$baseFile!=''">
               <xsl:value-of select="$baseFile"/>
            </xsl:when>
            <xsl:when test="normalize-space($schemaLoc)!='this'">
               <xsl:call-template name="GetSchemaDocLocation">
                  <xsl:with-param name="uri" select="$schemaLoc"/>
               </xsl:call-template>
            </xsl:when>
         </xsl:choose>
      </xsl:variable>

      <!-- Generate message. -->
      <xsl:variable name="title">
         <xsl:choose>
            <!-- Error message was provided. -->
            <xsl:when test="$errMsg!=''">
               <xsl:text>"</xsl:text><xsl:value-of select="$name"/><xsl:text>" </xsl:text>
               <xsl:value-of select="$compType"/><xsl:text> </xsl:text>
               <xsl:value-of select="$noun"/><xsl:text> </xsl:text>
               <xsl:value-of select="$errMsg"/>
            </xsl:when>
            <!-- There exists a link to the schema component's
                 documentation. -->
            <xsl:otherwise>
               <xsl:text>Jump to "</xsl:text>
               <xsl:value-of select="$name"/>
               <xsl:text>" </xsl:text>
               <xsl:value-of select="$compType"/>
               <xsl:text> </xsl:text>
               <xsl:value-of select="$noun"/>
               <!-- External link -->
               <xsl:if test="normalize-space($baseURI)!=''">
                  <xsl:text>(located in external schema documentation)</xsl:text>
               </xsl:if>
            </xsl:otherwise>
         </xsl:choose>
         <xsl:text>.</xsl:text>
      </xsl:variable>

      <!-- Generate href link -->
      <xsl:variable name="link">
         <xsl:choose>
            <!-- Error message was provided. -->
            <xsl:when test="$errMsg!=''">
               <xsl:text>javascript:void(0)</xsl:text>
            </xsl:when>
            <!-- There exists a link to the schema component's
                 documentation. -->
            <xsl:otherwise>
               <!-- Base URI -->
               <xsl:value-of select="normalize-space($baseURI)"/>
               <!-- Anchor within URI -->
               <xsl:value-of select="concat('#',normalize-space($compPrefix),normalize-space($name))"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>

      <a title="{$title}" href="{$link}">
         <!-- External link -->
         <xsl:if test="normalize-space($baseURI)!=''">
            <xsl:attribute name="class">externalLink</xsl:attribute>
         </xsl:if>

         <!-- Error message was provided. -->
         <xsl:if test="$errMsg!=''">
            <xsl:attribute name="onclick">
               <xsl:text>alert('</xsl:text>
               <xsl:value-of select="$title"/>
               <xsl:text>');</xsl:text>
            </xsl:attribute>
         </xsl:if>

         <xsl:value-of select="$name"/>
      </a>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintCompName </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a reference to a schema component.
     This template will try to work out which schema that this
     component belongs to and print out the appropriate link.
     component.
     It will also print out the namespace prefix given
     in the reference.
     Param(s):
            ref (String) required
                Reference to schema component
            compType (String) required
                Type of schema component
            schemaLoc (String) optional
                Schema file containing this component reference;
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintCompRef">
      <xsl:param name="ref"/>
      <xsl:param name="compType"/>
      <xsl:param name="schemaLoc">this</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintCompRef </xsl:comment>
		</xsl:if>

      <!-- Get correct terminology for statements -->
      <xsl:variable name="noun">
         <xsl:choose>
            <xsl:when test="$compType='element' or $compType='attribute'">
               <xsl:text>declaration</xsl:text>
            </xsl:when>
            <xsl:otherwise>
               <xsl:text>definition</xsl:text>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>

      <!-- Get local name -->
      <xsl:variable name="refName">
         <xsl:call-template name="GetRefName">
            <xsl:with-param name="ref" select="$ref"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Get prefix -->
      <xsl:variable name="refPrefix">
         <xsl:call-template name="GetRefPrefix">
            <xsl:with-param name="ref" select="$ref"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Get prefix of this schema's target namespace -->
      <xsl:variable name="tnPrefix">
         <xsl:call-template name="GetThisPrefix"/>
      </xsl:variable>

      <!-- Get file location of the schema component that is
           being referenced. -->
      <xsl:variable name="compLoc">
         <xsl:call-template name="FindComponent">
            <xsl:with-param name="ref" select="$ref"/>
            <xsl:with-param name="compType" select="$compType"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Print prefix -->
      <xsl:call-template name="PrintNSPrefix">
         <xsl:with-param name="prefix" select="$refPrefix"/>
         <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
      </xsl:call-template>

      <!-- Print local name -->
      <xsl:choose>
         <!-- Component from XML Schema's or XML's namespace -->
         <xsl:when test="normalize-space($compLoc)='xsd' or normalize-space($compLoc)='xml'">
            <xsl:value-of select="$refName"/>
         </xsl:when>
         <!-- Component found in this schema. -->
         <xsl:when test="normalize-space($compLoc)='this'">
            <xsl:call-template name="PrintCompName">
               <xsl:with-param name="name" select="$refName"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Component not found. -->
         <xsl:when test="normalize-space($compLoc)='' or normalize-space($compLoc)='none'">
            <xsl:call-template name="PrintCompName">
               <xsl:with-param name="name" select="$refName"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="errMsg">could not be found</xsl:with-param>
            </xsl:call-template>
         </xsl:when>
         <!-- Component found in an external schema. -->
         <xsl:otherwise>
            <!-- Get documentation file for included schema. -->
            <xsl:variable name="docFile">
               <xsl:call-template name="GetSchemaDocLocation">
                  <xsl:with-param name="uri" select="$compLoc"/>
               </xsl:call-template>
            </xsl:variable>

            <xsl:call-template name="PrintCompName">
               <xsl:with-param name="baseFile" select="$docFile"/>
               <xsl:with-param name="name" select="$refName"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintCompRef </xsl:comment>
		</xsl:if>
   </xsl:template>


   <!-- ******** Templates for Finding Components in Different
             Schema documents ******** -->

   <!-- Special key words: xml, xsd, this, none -->
   <xsl:template name="FindComponent">
      <xsl:param name="ref"/>
      <xsl:param name="compType"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: FindComponent </xsl:comment>
		</xsl:if>

      <!-- Get local name -->
      <xsl:variable name="refName">
         <xsl:call-template name="GetRefName">
            <xsl:with-param name="ref" select="$ref"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Get prefix -->
      <xsl:variable name="refPrefix">
         <xsl:call-template name="GetRefPrefix">
            <xsl:with-param name="ref" select="$ref"/>
         </xsl:call-template>
      </xsl:variable>

      <!-- Get prefix of this schema's target namespace -->
      <xsl:variable name="tnPrefix">
         <xsl:call-template name="GetThisPrefix"/>
      </xsl:variable>

      <!-- Get prefix of XML Schema -->
      <xsl:variable name="xsdPrefix">
         <xsl:call-template name="GetXSDPrefix"/>
      </xsl:variable>

      <xsl:choose>
         <!-- Schema component from XML Schema's namespace,
              unless this schema is for XML Schema -->
         <xsl:when test="$refPrefix=$xsdPrefix and $xsdPrefix!=$tnPrefix">
            <xsl:text>xsd</xsl:text>
         </xsl:when>
         <!-- Schema component from XML's namespace -->
         <xsl:when test="$refPrefix='xml'">
            <xsl:text>xml</xsl:text>
         </xsl:when>
         <!-- Schema component from current schema's namespace -->
         <xsl:when test="$refPrefix=$tnPrefix">
            <xsl:call-template name="FindComponentInSchema">
               <xsl:with-param name="name" select="$refName"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="schema" select="/xsd:schema"/>
               <xsl:with-param name="schemaFileLoc">this</xsl:with-param>
            </xsl:call-template>
         </xsl:when>
         <!-- Schema component from imported namespace -->
         <xsl:when test="normalize-space(translate($searchImportedSchemas, 'TRUE', 'true'))='true'">
            <xsl:variable name="refNS" select="/xsd:schema/namespace::*[local-name(.)=normalize-space($refPrefix)]"/>
            <xsl:call-template name="FindComponentInImportedSchemas">
               <xsl:with-param name="name" select="$refName"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="compNS" select="$refNS"/>
               <xsl:with-param name="schema" select="/xsd:schema"/>
               <xsl:with-param name="index">1</xsl:with-param>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: FindComponent </xsl:comment>
		</xsl:if>
   </xsl:template>

   <xsl:template name="FindComponentInSchema">
      <xsl:param name="name"/>
      <xsl:param name="compType"/>
      <xsl:param name="schema"/>
      <xsl:param name="schemaFileLoc"/>
      <xsl:param name="schemasSearched"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: FindComponentInSchema </xsl:comment>
		</xsl:if>

      <!-- Don't examine this schema if we've already
           searched it. Prevents infinite recursion.
           Also check if schema actually exists. -->
      <xsl:if test="$schema and not(contains($schemasSearched, concat('*', $schemaFileLoc, '+')))">
         <!-- Find out if the component is in this schema -->
         <xsl:variable name="thisResult">
            <xsl:call-template name="IsComponentInSchema">
               <xsl:with-param name="name" select="$name"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="schema" select="$schema"/>
            </xsl:call-template>
         </xsl:variable>

         <xsl:choose>
            <!-- Component found -->
            <xsl:when test="normalize-space($thisResult)='true'">
               <xsl:value-of select="$schemaFileLoc"/>
            </xsl:when>
            <!-- Component not found -->
            <xsl:when test="normalize-space(translate($searchIncludedSchemas, 'TRUE', 'true'))='true'">
               <!-- Search included schemas -->
               <xsl:variable name="includeResult">
                  <xsl:call-template name="FindComponentInIncludedSchemas">
                     <xsl:with-param name="schema" select="$schema"/>
                     <xsl:with-param name="name" select="$name"/>
                     <xsl:with-param name="compType" select="$compType"/>
                     <xsl:with-param name="index">1</xsl:with-param>
                     <xsl:with-param name="schemasSearched" select="concat($schemasSearched, '*', $schemaFileLoc, '+')"/>
                  </xsl:call-template>
               </xsl:variable>

               <xsl:choose>
                  <xsl:when test="normalize-space($includeResult)!='' and normalize-space($includeResult)!='none'">
                     <xsl:value-of select="$includeResult"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <!-- Search redefined schemas -->
                     <xsl:call-template name="FindComponentInRedefinedSchemas">
                        <xsl:with-param name="schema" select="$schema"/>
                        <xsl:with-param name="name" select="$name"/>
                        <xsl:with-param name="compType" select="$compType"/>
                        <xsl:with-param name="index">1</xsl:with-param>
                        <xsl:with-param name="schemasSearched" select="concat($schemasSearched, '*', $schemaFileLoc, '+')"/>
                     </xsl:call-template>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:when>
         </xsl:choose>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: FindComponentInSchema </xsl:comment>
		</xsl:if>
   </xsl:template>

   <xsl:template name="IsComponentInSchema">
      <xsl:param name="name"/>
      <xsl:param name="compType"/>
      <xsl:param name="schema"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: IsComponentInSchema </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Schema not found. -->
         <xsl:when test="not($schema)">
            <xsl:text>false</xsl:text>
         </xsl:when>
         <!-- Search for attribute declaration. -->
         <xsl:when test="$compType='attribute' and $schema/xsd:attribute[@name=$name]">
            <xsl:text>true</xsl:text>
         </xsl:when>
         <!-- Search for attribute group definition. -->
         <xsl:when test="$compType='attribute group' and ($schema/xsd:attributeGroup[@name=$name] or $schema/xsd:redefine/xsd:attributeGroup[@name=$name])">
            <xsl:text>true</xsl:text>
         </xsl:when>
         <!-- Search for element declaration. -->
         <xsl:when test="$compType='element' and $schema/xsd:element[@name=$name]">
            <xsl:text>true</xsl:text>
         </xsl:when>
         <!-- Search for group definition. -->
         <xsl:when test="$compType='group' and ($schema/xsd:group[@name=$name] or $schema/xsd:redefine/xsd:group[@name=$name])">
            <xsl:text>true</xsl:text>
         </xsl:when>
         <!-- Search for complex type definition. -->
         <xsl:when test="($compType='type' or $compType='complex type') and ($schema/xsd:complexType[@name=$name] or $schema/xsd:redefine/xsd:complexType[@name=$name])">
            <xsl:text>true</xsl:text>
         </xsl:when>
         <!-- Search for simple type definition. -->
         <xsl:when test="($compType='type' or $compType='simple type') and ($schema/xsd:simpleType[@name=$name] or $schema/xsd:redefine/xsd:simpleType[@name=$name])">
            <xsl:text>true</xsl:text>
         </xsl:when>
         <!-- Search for uniqueness/key constraint definition. -->
         <xsl:when test="$compType='uniqueness/key constraint' and ($schema//xsd:element/xsd:key[@name=$name] or $schema//xsd:element/xsd:unique[@name=$name])">
            <xsl:text>true</xsl:text>
         </xsl:when>
         <!-- Component not found. -->
         <xsl:otherwise>
            <xsl:text>false</xsl:text>
         </xsl:otherwise>
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: IsComponentInSchema </xsl:comment>
		</xsl:if>
   </xsl:template>

   <xsl:template name="FindComponentInIncludedSchemas">
      <xsl:param name="schema"/>
      <xsl:param name="name"/>
      <xsl:param name="compType"/>
      <xsl:param name="schemasSearched"/>
      <xsl:param name="index">1</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: FindComponentInIncludedSchemas </xsl:comment>
		</xsl:if>

      <xsl:if test="count($schema/xsd:include) &gt;= number($index)">
         <!-- Get the 'schemaLocation' attribute of the 'include'
              element in this schema at position, 'index'. -->
         <xsl:variable name="schemaLoc" select="$schema/xsd:include[position()=$index]/@schemaLocation"/>

         <xsl:variable name="thisResult">
            <!-- Search for the component in the current 
                 included schema. -->
            <xsl:call-template name="FindComponentInSchema">
               <xsl:with-param name="name" select="$name"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="schema" select="document($schemaLoc)/xsd:schema"/>
               <xsl:with-param name="schemaFileLoc" select="$schemaLoc"/>
               <xsl:with-param name="schemasSearched" select="$schemasSearched"/>
            </xsl:call-template>
         </xsl:variable>

         <xsl:choose>
            <!-- Component was found, so return result. -->
            <xsl:when test="normalize-space($thisResult)!='' and normalize-space($thisResult)!='none'">
               <xsl:value-of select="$thisResult"/>
            </xsl:when>
            <!-- Component was not found, so keep on searching. -->
            <xsl:otherwise>
               <!-- Examine other included schemas in this schema -->
               <xsl:call-template name="FindComponentInIncludedSchemas">
                  <xsl:with-param name="schema" select="$schema"/>
                  <xsl:with-param name="name" select="$name"/>
                  <xsl:with-param name="compType" select="$compType"/>
                  <xsl:with-param name="index" select="number($index)+1"/>
                  <xsl:with-param name="schemasSearched" select="concat($schemasSearched, '*', $schemaLoc, '+')"/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: FindComponentInIncludedSchemas </xsl:comment>
		</xsl:if>
   </xsl:template>

   <xsl:template name="FindComponentInRedefinedSchemas">
      <xsl:param name="schema"/>
      <xsl:param name="name"/>
      <xsl:param name="compType"/>
      <xsl:param name="schemasSearched"/>
      <xsl:param name="index">1</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: FindComponentInRedefinedSchemas </xsl:comment>
		</xsl:if>

      <xsl:if test="count($schema/xsd:redefine) &gt;= number($index)">
         <!-- Get the 'schemaLocation' attribute of the 'redefine'
              element in this schema at position, 'index'. -->
         <xsl:variable name="schemaLoc" select="$schema/xsd:redefine[position()=$index]/@schemaLocation"/>

         <xsl:variable name="thisResult">
            <!-- Search for the component in the current 
                 redefined schema. -->
            <xsl:call-template name="FindComponentInSchema">
               <xsl:with-param name="name" select="$name"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="schema" select="document($schemaLoc)/xsd:schema"/>
               <xsl:with-param name="schemaFileLoc" select="$schemaLoc"/>
               <xsl:with-param name="schemasSearched" select="$schemasSearched"/>
            </xsl:call-template>
         </xsl:variable>

         <xsl:choose>
            <!-- Component was found, so return result. -->
            <xsl:when test="normalize-space($thisResult)!='' and normalize-space($thisResult)!='none'">
               <xsl:value-of select="$thisResult"/>
            </xsl:when>
            <!-- Component was not found, so keep on searching. -->
            <xsl:otherwise>
               <!-- Examine other redefined schemas in this schema -->
               <xsl:call-template name="FindComponentInRedefinedSchemas">
                  <xsl:with-param name="schema" select="$schema"/>
                  <xsl:with-param name="name" select="$name"/>
                  <xsl:with-param name="compType" select="$compType"/>
                  <xsl:with-param name="index" select="number($index)+1"/>
                  <xsl:with-param name="schemasSearched" select="concat($schemasSearched, '*', $schemaLoc, '+')"/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: FindComponentInRedefinedSchemas </xsl:comment>
		</xsl:if>
   </xsl:template>

   <xsl:template name="FindComponentInImportedSchemas">
      <xsl:param name="schema"/>
      <xsl:param name="name"/>
      <xsl:param name="compType"/>
      <xsl:param name="compNS"/>
      <xsl:param name="schemasSearched"/>
      <xsl:param name="index">1</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: FindComponentInImportedSchemas </xsl:comment>
		</xsl:if>

      <xsl:if test="count($schema/xsd:import) &gt;= number($index)">
         <!-- Get the 'namespace' attribute of the 'import'
              element in this schema at position, 'index'. -->
         <xsl:variable name="schemaNS" select="$schema/xsd:import[position()=$index]/@namespace"/>
         <!-- Get the 'schemaLocation' attribute. -->
         <xsl:variable name="schemaLoc" select="$schema/xsd:import[position()=$index]/@schemaLocation"/>

         <xsl:variable name="thisResult">
            <!-- Check that the imported schema has the matching
                 namespace as the component that we're looking
                 for. -->
            <xsl:if test="normalize-space($compNS)=normalize-space($schemaNS)">
               <!-- Search for the component in the current 
                    imported schema. -->
               <xsl:call-template name="FindComponentInSchema">
                  <xsl:with-param name="name" select="$name"/>
                  <xsl:with-param name="compType" select="$compType"/>
                  <xsl:with-param name="schema" select="document($schemaLoc)/xsd:schema"/>
                  <xsl:with-param name="schemaFileLoc" select="$schemaLoc"/>
                  <xsl:with-param name="schemasSearched" select="$schemasSearched"/>
               </xsl:call-template>
            </xsl:if>
         </xsl:variable>

         <xsl:choose>
            <!-- Component was found, so return result. -->
            <xsl:when test="normalize-space($thisResult)!='' and normalize-space($thisResult)!='none'">
               <xsl:value-of select="$thisResult"/>
            </xsl:when>
            <!-- Component was not found, so keep on searching. -->
            <xsl:otherwise>
               <!-- Examine other included schemas in this schema -->
               <xsl:call-template name="FindComponentInImportedSchemas">
                  <xsl:with-param name="schema" select="$schema"/>
                  <xsl:with-param name="name" select="$name"/>
                  <xsl:with-param name="compType" select="$compType"/>
                  <xsl:with-param name="compNS" select="$compNS"/>
                  <xsl:with-param name="index" select="number($index)+1"/>
                  <xsl:with-param name="schemasSearched" select="concat($schemasSearched, '*', $schemaLoc, '+')"/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:if>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: FindComponentInImportedSchemas </xsl:comment>
		</xsl:if>
   </xsl:template>


   <!-- ******** General Utility Templates ******** -->

   <!-- 
     Creates a box that can be opened and closed, such
     that the contents can be hidden away until a button
     is pressed.
     Param(s):
            caption (String) required
              Text describing the contents of the box;
              it will always be shown even when the box
              is closed
            contents (String) required
              Contents of box, which may appear and disappear
              with the press of a button.
            anchor (String) optional
              Anchor, e.g. <a name="...", for this box
            isOpened (String) optional
              Set to true if initially opened, and
              false if initially closed
     -->
   <xsl:template name="CollapseableBox">
      <xsl:param name="caption"/>
      <xsl:param name="contents"/>
      <xsl:param name="anchor"/>
      <xsl:param name="isOpened">false</xsl:param>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: CollapseableBox </xsl:comment>
		</xsl:if>

      <div class="box">
         <!-- Box Title -->
         <xsl:text> </xsl:text>
         <div class="caption">
            <xsl:choose>
               <xsl:when test="$anchor != ''">
                  <a name="{$anchor}">
                     <xsl:value-of select="$caption"/>
                  </a>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$caption"/>
               </xsl:otherwise>
            </xsl:choose>
         </div>

         <!-- Box Contents -->
      	<table class="contents">
         	<xsl:copy-of select="$contents"/>
         </table>
      </div>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: CollapseableBox </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the namespace of an attribute
     declaration or reference.
     Param(s):
            attribute (Node) required
                Attribute declaration or reference
     -->
   <xsl:template name="GetAttributeNS">
      <xsl:param name="attribute"/>

		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetAttributeNS </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Qualified local attribute declaration -->
         <xsl:when test="$attribute[@name] and (normalize-space(translate($attribute/@form, 'QUALIFED', 'qualifed'))='qualified' or normalize-space(translate(/xsd:schema/@attributeFormDefault, 'QUALIFED', 'qualifed'))='qualified')">
            <xsl:value-of select="/xsd:schema/@targetNamespace"/>
         </xsl:when>
         <!-- Reference to global attribute declaration -->
         <xsl:when test="$attribute[@ref]">
            <xsl:call-template name="GetRefNS">
               <xsl:with-param name="ref" select="$attribute/@ref"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Otherwise, attribute has no namespace -->
      </xsl:choose>
		<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetAttributeNS </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the description that can be used in 
     headers for a schema component.
     Param(s):
            component (Node) required
                Schema component
     -->
   <xsl:template name="GetComponentDescription">
      <xsl:param name="component"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetComponentDescription </xsl:comment>
		</xsl:if>
	
	   <xsl:choose>
         <xsl:when test="local-name($component)='all'">
            <xsl:text>All Model Group</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='attribute'">
            <xsl:text>Attribute</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='attributeGroup'">
            <xsl:text>Attribute Group</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='choice'">
            <xsl:text>Choice Model Group</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='complexType'">
            <xsl:text>Complex Type</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='element'">
            <xsl:text>Element</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='group'">
            <xsl:text>Model Group</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='notation'">
            <xsl:text>Notation</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='sequence'">
            <xsl:text>Sequence Model Group</xsl:text>
         </xsl:when>
         <xsl:when test="local-name($component)='simpleType'">
            <xsl:text>Simple Type</xsl:text>
         </xsl:when>
         <xsl:otherwise>
            <xsl:call-template name="HandleError">
               <xsl:with-param name="isTerminating">true</xsl:with-param>
               <xsl:with-param name="errorMsg">
Unknown schema component, <xsl:value-of select="local-name($component)"/>.
               </xsl:with-param>
            </xsl:call-template>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetComponentDescription </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the unique identifier for a top-level schema
     component. Returns the string "schema" if the 'component' 
     is the root schema element.
     Param(s):
            component (Node) required
                Schema component
     -->
   <xsl:template name="GetComponentID">
      <xsl:param name="component"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetComponentID </xsl:comment>
		</xsl:if>
   	
      <xsl:choose>
         <xsl:when test="local-name($component)='schema'">
            <xsl:text>schema</xsl:text>
         </xsl:when>
         <xsl:otherwise>
            <xsl:variable name="componentPrefix">
               <xsl:call-template name="GetComponentPrefix">
                  <xsl:with-param name="component" select="$component"/>
               </xsl:call-template>
            </xsl:variable>
            <xsl:value-of select="concat($componentPrefix, $component/@name)"/>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetComponentID </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the prefix to add in front of a schema component
     name when generating anchor names.
     Param(s):
            component (Node) required
                Schema component
     -->
   <xsl:template name="GetComponentPrefix">
      <xsl:param name="component"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetComponentPrefix </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="local-name($component)='attribute'">
            <xsl:value-of select="$ATTR_PREFIX"/>
         </xsl:when>
         <xsl:when test="local-name($component)='attributeGroup'">
            <xsl:value-of select="$ATTR_GRP_PREFIX"/>
         </xsl:when>
         <xsl:when test="local-name($component)='complexType'">
            <xsl:value-of select="$CTYPE_PREFIX"/>
         </xsl:when>
         <xsl:when test="local-name($component)='element'">
            <xsl:value-of select="$ELEM_PREFIX"/>
         </xsl:when>
         <xsl:when test="local-name($component)='group'">
            <xsl:value-of select="$GRP_PREFIX"/>
         </xsl:when>
         <xsl:when test="local-name($component)='notation'">
            <xsl:value-of select="$NOTA_PREFIX"/>
         </xsl:when>
         <xsl:when test="local-name($component)='simpleType'">
            <xsl:value-of select="$STYPE_PREFIX"/>
         </xsl:when>
         <xsl:when test="local-name($component)='key' or local-name($component)='unique'">
            <xsl:value-of select="$KEY_PREFIX"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:call-template name="HandleError">
               <xsl:with-param name="isTerminating">true</xsl:with-param>
               <xsl:with-param name="errorMsg">
Unknown schema component, <xsl:value-of select="local-name($component)"/>.
               </xsl:with-param>
            </xsl:call-template>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetComponentPrefix </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns a glossary term reference for the 
     schema component type, if applicable.
     Param(s):
            component (Node) required
                Schema component
     -->
   <xsl:template name="GetComponentTermRef">
      <xsl:param name="component"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetComponentTermRef </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="local-name($component)='notation'">
            <xsl:text>Notation</xsl:text>
         </xsl:when>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetComponentTermRef </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the namespace prefix of an attribute.
     Param(s):
            attribute (Node) required
                Attribute declaration or reference
     -->
   <xsl:template name="GetAttributePrefix">
      <xsl:param name="attribute"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetAttributePrefix </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Element reference -->
         <xsl:when test="$attribute/@ref">
            <xsl:call-template name="GetRefPrefix">
               <xsl:with-param name="ref" select="$attribute/@ref"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Global element declaration -->
         <xsl:when test="local-name($attribute/..)='schema'">
            <xsl:call-template name="GetThisPrefix"/>
         </xsl:when>
         <!-- Local element declaration -->
         <xsl:otherwise>
            <xsl:if test="($attribute/@form and normalize-space($attribute/@form)='qualified') or (/xsd:schema/@attributeFormDefault and normalize-space(/xsd:schema/@attributeFormDefault)='qualified')">
               <xsl:call-template name="GetThisPrefix"/>
            </xsl:if>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetAttributePrefix </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the namespace prefix of an element.
     Param(s):
            element (Node) required
                Element declaration or reference
     -->
   <xsl:template name="GetElementPrefix">
      <xsl:param name="element"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetElementPrefix </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <!-- Element reference -->
         <xsl:when test="$element/@ref">
            <xsl:call-template name="GetRefPrefix">
               <xsl:with-param name="ref" select="$element/@ref"/>
            </xsl:call-template>
         </xsl:when>
         <!-- Global element declaration -->
         <xsl:when test="local-name($element/..)='schema'">
            <xsl:call-template name="GetThisPrefix"/>
         </xsl:when>
         <!-- Local element declaration -->
         <xsl:otherwise>
            <xsl:if test="($element/@form and normalize-space($element/@form)='qualified') or (/xsd:schema/@elementFormDefault and normalize-space(/xsd:schema/@elementFormDefault)='qualified')">
               <xsl:call-template name="GetThisPrefix"/>
            </xsl:if>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetElementPrefix </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the local name of a reference.
     Param(s):
            ref (String) required
                Reference
     -->
   <xsl:template name="GetRefName">
      <xsl:param name="ref"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetRefName </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="contains($ref, ':')">
            <!-- Ref has namespace prefix -->
            <xsl:value-of select="substring-after($ref, ':')"/>
         </xsl:when>
         <xsl:otherwise>
            <!-- Ref has no namespace prefix -->
            <xsl:value-of select="$ref"/>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetRefName </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the namespace prefix of a reference.
     Param(s):
            ref (String) required
                Reference
     -->
   <xsl:template name="GetRefPrefix">
      <xsl:param name="ref"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetRefPrefix </xsl:comment>
		</xsl:if>

      <!-- Get namespace prefix -->
      <xsl:value-of select="substring-before($ref, ':')"/>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetRefPrefix </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the namespace of a reference.
     Param(s):
            ref (String) required
                Reference
     -->
   <xsl:template name="GetRefNS">
      <xsl:param name="ref"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetRefNS </xsl:comment>
		</xsl:if>

      <!-- Get namespace prefix -->
      <xsl:variable name="refPrefix" select="substring-before($ref, ':')"/>
      <!-- Get namespace -->
      <xsl:value-of select="/xsd:schema/namespace::*[local-name(.)=normalize-space($refPrefix)]"/>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetRefNS </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the declared prefix of this schema's target namespace.
     -->
   <xsl:template name="GetThisPrefix">
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetThisPrefix </xsl:comment>
		</xsl:if>
      <xsl:if test="/xsd:schema/@targetNamespace">
         <xsl:value-of select="local-name(/xsd:schema/namespace::*[normalize-space(.)=normalize-space(/xsd:schema/@targetNamespace)])"/>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetThisPrefix </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the declared prefix of XML Schema namespace.
     -->
   <xsl:template name="GetXSDPrefix">
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetXSDPrefix </xsl:comment>
		</xsl:if>
      <xsl:value-of select="local-name(/xsd:schema/namespace::*[normalize-space(.)=$XSD_NS])"/>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetXSDPrefix </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Returns the schema documentation file location for a
     given URI for a schema, done by looking up the file
     specified in 'linksFile' variable.
     It'll throw a fatal error if a value for 'linksFile' was 
     provided and the documentation file for 'uri' could not be
     found.
     Param(s):
            uri (String) required
              Location of schema file
     -->
   <xsl:template name="GetSchemaDocLocation">
      <xsl:param name="uri"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: GetSchemaDocLocation </xsl:comment>
		</xsl:if>

      <xsl:if test="$linksFile!=''">
         <xsl:variable name="schemaDocFile" select="document($linksFile)/ppp:links/ppp:schema[@file-location=$uri]/@docfile-location"/>
         <xsl:if test="$schemaDocFile=''">
            <xsl:call-template name="HandleError">
               <xsl:with-param name="isTerminating">true</xsl:with-param>
               <xsl:with-param name="errorMsg">
Documentation file for the schema at, <xsl:value-of select="$uri"/>,
was not specified in the links file, <xsl:value-of select="$linksFile"/>.
               </xsl:with-param>
            </xsl:call-template>
         </xsl:if>
         <xsl:value-of select="$schemaDocFile"/>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: GetSchemaDocLocation </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a boolean value.
     Param(s):
            boolean (String) required
                Boolean value
     -->
   <xsl:template name="PrintBoolean">
      <xsl:param name="boolean"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintBoolean </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="normalize-space(translate($boolean,'TRUE', 'true'))='true' or normalize-space($boolean)='1'">
            <xsl:text>yes</xsl:text>
         </xsl:when>
         <xsl:otherwise>
            <xsl:text>no</xsl:text>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintBoolean </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out a link to the namespace of a prefix,
     and tacks on a colon in the end.
     Param(s):
            prefix (String) required
                Namespace prefix
            nolink (boolean) optional
                If true, doesn't provide a link to the
                namespace in the namespaces table.
            schemaLoc (String) optional
                Schema file containing this namespace prefix;
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintNSPrefix">
      <xsl:param name="prefix"/>
      <xsl:param name="nolink">false</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintNSPrefix </xsl:comment>
		</xsl:if>

      <xsl:if test="$prefix!='' and normalize-space(translate($printNSPrefixes,'TRUE','true'))='true'">
         <xsl:choose>
            <xsl:when test="$nolink='false'">
               <xsl:call-template name="PrintNamespaceRef">
                  <xsl:with-param name="prefix" select="$prefix"/>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$prefix"/>
            </xsl:otherwise>
         </xsl:choose>
         <xsl:text>:</xsl:text>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintNSPrefix </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out the min/max occurrences of a schema component.
     Param(s):
            component (Node) optional
                Schema component
            minOccurs (String) optional
                Minimum occurrences
            maxOccurs (String) optional
                Maximum occurrences
     -->
   <xsl:template name="PrintOccurs">
      <xsl:param name="component"/>
      <xsl:param name="minOccurs"/>
      <xsl:param name="maxOccurs"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintOccurs </xsl:comment>
		</xsl:if>

      <!-- Get min occurs -->
      <xsl:variable name="min">
         <xsl:choose>
            <xsl:when test="$component and local-name($component)='attribute'">
               <xsl:choose>
                  <xsl:when test="normalize-space($component/@use)='required'">
                     <xsl:text>1</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>0</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
               <xsl:choose>
                  <xsl:when test="$component and $component/@minOccurs">
                     <xsl:value-of select="$component/@minOccurs"/>
                  </xsl:when>
                  <xsl:when test="$minOccurs != ''">
                     <xsl:value-of select="$minOccurs"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>1</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      <!-- Get max occurs -->
      <xsl:variable name="max">
         <xsl:choose>
            <xsl:when test="$component and local-name($component)='attribute'">
               <xsl:choose>
                  <xsl:when test="normalize-space($component/@use)='prohibited'">
                     <xsl:text>0</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>1</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
               <xsl:choose>
                  <xsl:when test="($component and normalize-space($component/@maxOccurs)='unbounded') or $maxOccurs='unbounded'">
                     <xsl:text>*</xsl:text>
                  </xsl:when>
                  <xsl:when test="$component and $component/@maxOccurs">
                     <xsl:value-of select="$component/@maxOccurs"/>
                  </xsl:when>
                  <xsl:when test="$maxOccurs != ''">
                     <xsl:value-of select="$maxOccurs"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text>1</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>

      <span class="occurs">
         <xsl:choose>
            <xsl:when test="number($min)=1 and number($max)=1">
               <xsl:text>[1]</xsl:text>
            </xsl:when>
            <xsl:otherwise>
               <xsl:text>[</xsl:text>
               <xsl:value-of select="$min"/>
               <xsl:text>..</xsl:text>
               <xsl:value-of select="$max"/>
               <xsl:text>]</xsl:text>
            </xsl:otherwise>
         </xsl:choose>
      </span>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintOccurs </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Translates occurrence of '#all' in 'block' value
     of element declarations.
     Param(s):
            EBV (String) required
                Value
     -->
   <xsl:template name="PrintBlockSet">
      <xsl:param name="EBV"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintBlockSet </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="normalize-space($EBV)='#all'">
            <xsl:text>restriction, extension, substitution</xsl:text>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$EBV"/>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintBlockSet </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Translates occurrence of '#all' in 'final' value
     of element declarations, and 'block' and 'final' values 
     in complex type definitions.
     Param(s):
            EBV (String) required
                Value
     -->
   <xsl:template name="PrintDerivationSet">
      <xsl:param name="EBV"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintDerivationSet </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="normalize-space($EBV)='#all'">
            <xsl:text>restriction, extension</xsl:text>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$EBV"/>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintDerivationSet </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Translates occurrence of '#all' in 'final' value
     of simple type definitions.
     Param(s):
            EBV (String) required
                Value
     -->
   <xsl:template name="PrintSimpleDerivationSet">
      <xsl:param name="EBV"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSimpleDerivationSet </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="normalize-space($EBV)='#all'">
            <xsl:text>restriction, list, union</xsl:text>
         </xsl:when>
         <xsl:when test="normalize-space($EBV)!=''">
            <xsl:value-of select="$EBV"/>
         </xsl:when>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSimpleDerivationSet </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out a URI. If it starts with 'http', a link is provided.
     Param(s):
            uri (String) required
              URI to be printed
     -->
   <xsl:template name="PrintURI">
      <xsl:param name="uri"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintURI </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="starts-with($uri, 'http')">
            <a title="{$uri}" href="{$uri}">
               <xsl:value-of select="$uri"/>
            </a>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$uri"/>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintURI </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out a link to the documentation of schema document.
     For this happen, the 'linksFile' variable must be provided,
     it must point to an actual file, and in that file, there
     must be a mapping from the schema file location to the
     schema documentation file location.
     Param(s):
            uri (String) required
              Location of schema file
     -->
   <xsl:template name="PrintSchemaLink">
      <xsl:param name="uri"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintSchemaLink </xsl:comment>
		</xsl:if>

      <xsl:variable name="docFileLoc">
         <xsl:call-template name="GetSchemaDocLocation">
            <xsl:with-param name="uri" select="$uri"/>
         </xsl:call-template>
      </xsl:variable>

      <xsl:choose>
         <xsl:when test="$docFileLoc!=''">
            <a title="Jump to schema documentation for '{$uri}'." href="{$docFileLoc}">
               <xsl:value-of select="$uri"/>
            </a>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$uri"/>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintSchemaLink </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Tokenises a whitespace-separated list of values, and
     displays them appropriately.
     Param(s):
            value (String) required
              Whitespace-separated list
            compType (String) optional
              Specify schema component type if values in list are
              schema components, so appropriate links can be provided.
            isInList (boolean) optional
              If true, place each value within 'li' tags.
              Assumes that this template is called within an HTML
              list element.
            separator (String) optional
              Character(s) to use to separate resulting values in list.
              Only used if 'isInList' is false.
              If none is provided, uses a space character, ' '.
            isFirst (boolean) optional
              If false, it's a recursive call from 'PrintWhitespaceList'.
            schemaLoc (String) optional
                Schema file containing this all model group;
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintWhitespaceList">
      <xsl:param name="value"/>
      <xsl:param name="compType"/>
      <xsl:param name="isInList">false</xsl:param>
      <xsl:param name="separator"/>
      <xsl:param name="isFirst">true</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintWhitespaceList </xsl:comment>
		</xsl:if>

      <xsl:variable name="token" select="normalize-space(substring-before($value, ' '))"/>
      <xsl:choose>
         <xsl:when test="$token!=''">
            <!-- Whitespace found in value -->
            <!-- Print out token -->
            <xsl:call-template name="PrintWhitespaceListToken">
               <xsl:with-param name="token" select="$token"/>
               <xsl:with-param name="compType" select="$compType"/>
               <xsl:with-param name="isInList" select="$isInList"/>
               <xsl:with-param name="separator" select="$separator"/>
               <xsl:with-param name="isFirst" select="$isFirst"/>
               <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
            </xsl:call-template>
            <!-- Continue parsing -->
            <xsl:variable name="rest" select="normalize-space(substring-after($value, $token))"/>
            <xsl:if test="$rest!=''">
               <xsl:call-template name="PrintWhitespaceList">
                  <xsl:with-param name="value" select="$rest"/>
                  <xsl:with-param name="compType" select="$compType"/>
                  <xsl:with-param name="isInList" select="$isInList"/>
                  <xsl:with-param name="separator" select="$separator"/>
                  <xsl:with-param name="isFirst">false</xsl:with-param>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
            </xsl:if>
         </xsl:when>
         <xsl:otherwise>
            <!-- No more whitespaces  -->
            <!-- Print out one last token -->
            <xsl:if test="normalize-space($value)!=''">
               <xsl:call-template name="PrintWhitespaceListToken">
                  <xsl:with-param name="token" select="$value"/>
                  <xsl:with-param name="compType" select="$compType"/>
                  <xsl:with-param name="isInList" select="$isInList"/>
                  <xsl:with-param name="separator" select="$separator"/>
                  <xsl:with-param name="isFirst" select="$isFirst"/>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
            </xsl:if>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintWhitespaceList </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Helper template for 'PrintWhitespaceList' template,
     which prints out one token in the list.
     Param(s):
            token (String) required
              Token to be printed
            compType (String) optional
              Schema component type of token, if applicable.
            isInList (boolean) optional
              If true, place token within 'li' tags.
            separator (String) optional
              Character(s) use to separate one token from another.
              Only used if 'isInList' is false.
              If none is provided, uses a space character, ' '.
            isFirst (boolean) optional
              If true, token is the first value in the list.
            schemaLoc (String) optional
                Schema file containing this all model group;
                if in current schema, 'schemaLoc' is set to 'this'
     -->
   <xsl:template name="PrintWhitespaceListToken">
      <xsl:param name="token"/>
      <xsl:param name="compType"/>
      <xsl:param name="isInList">false</xsl:param>
      <xsl:param name="separator"/>
      <xsl:param name="isFirst">true</xsl:param>
      <xsl:param name="schemaLoc">this</xsl:param>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintWhitespaceListToken </xsl:comment>
		</xsl:if>

      <xsl:variable name="displayValue">
         <xsl:choose>
            <xsl:when test="$compType!=''">
               <xsl:call-template name="PrintCompRef">
                  <xsl:with-param name="ref" select="normalize-space($token)"/>
                  <xsl:with-param name="compType" select="$compType"/>
                  <xsl:with-param name="schemaLoc" select="$schemaLoc"/>
               </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="normalize-space($token)"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>

      <xsl:choose>
         <xsl:when test="$isInList!='false'">
            <li>
               <xsl:copy-of select="$displayValue"/>
            </li>
         </xsl:when>
         <xsl:when test="$isFirst!='true'">
            <xsl:choose>
               <xsl:when test="$separator!=''">
                  <xsl:value-of select="$separator"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:text> </xsl:text>
               </xsl:otherwise>
            </xsl:choose>
            <xsl:copy-of select="$displayValue"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:copy-of select="$displayValue"/>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintWhitespaceListToken </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out a wildcard.
     Param(s):
            componentType (attribute|element) required
              XML Schema component type
            namespaces (String) required
              Namespace attribute of wildcard
            processContents (String) required
              Process contents attribute of wildcard
            namespaces (String) required
              Namespace attribute of wildcard
     -->
   <xsl:template name="PrintWildcard">
      <xsl:param name="componentType">element</xsl:param>
      <xsl:param name="namespace"/>
      <xsl:param name="processContents"/>
      <xsl:param name="minOccurs"/>
      <xsl:param name="maxOccurs"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintWildcard </xsl:comment>
		</xsl:if>

      <xsl:text>Allow any </xsl:text>
      <xsl:value-of select="$componentType"/>
      <xsl:text>s from </xsl:text>

      <xsl:choose>
         <!-- ##any -->
         <xsl:when test="not($namespace) or normalize-space($namespace)='##any'">
            <xsl:text>any namespace</xsl:text>
         </xsl:when>
         <!-- ##other -->
         <xsl:when test="normalize-space($namespace)='##other'">
            <xsl:text>a namespace other than this schema's namespace</xsl:text>
         </xsl:when>
         <!-- ##targetNamespace, ##local, specific namespaces -->
         <xsl:otherwise>
            <!-- ##targetNamespace -->
            <xsl:variable name="hasTargetNS">
               <xsl:if test="contains($namespace, '##targetNamespace')">
                  <xsl:text>true</xsl:text>
               </xsl:if>
            </xsl:variable>
            <!-- ##local -->
            <xsl:variable name="hasLocalNS">
               <xsl:if test="contains($namespace, '##local')">
                  <xsl:text>true</xsl:text>
               </xsl:if>
            </xsl:variable>
            <!-- Specific namespaces -->
            <!-- Remove '##targetNamespace' string if any-->
            <xsl:variable name="temp">
               <xsl:choose>
                  <xsl:when test="$hasTargetNS='true'">
                     <xsl:value-of select="concat(substring-before($namespace, '##targetNamespace'), substring-after($namespace, '##targetNamespace'))"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:value-of select="$namespace"/>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:variable>
            <!-- Remove '##local' string if any -->
            <xsl:variable name="specificNS">
               <xsl:choose>
                  <xsl:when test="$hasLocalNS='true'">
                     <xsl:value-of select="concat(substring-before($temp, '##local'), substring-after($temp, '##local'))"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:value-of select="$temp"/>
                  </xsl:otherwise>
               </xsl:choose>
            </xsl:variable>
            <xsl:variable name="hasSpecificNS">
               <xsl:if test="normalize-space($specificNS)!=''">
                  <xsl:text>true</xsl:text>
               </xsl:if>
            </xsl:variable>

            <xsl:if test="$hasLocalNS='true'">
               <xsl:text>no namespace</xsl:text>
            </xsl:if>

            <xsl:if test="$hasTargetNS='true'">
               <xsl:choose>
                  <xsl:when test="$hasLocalNS='true' and $hasSpecificNS!='true'">
                     <xsl:text> and </xsl:text>
                  </xsl:when>
                  <xsl:when test="$hasLocalNS='true'">
                     <xsl:text>, </xsl:text>
                  </xsl:when>
               </xsl:choose>
               <xsl:text>this schema's namespace</xsl:text>
            </xsl:if>

            <xsl:if test="$hasSpecificNS='true'">
               <xsl:choose>
                  <xsl:when test="$hasTargetNS='true' and $hasLocalNS='true'">
                     <xsl:text>, and </xsl:text>
                  </xsl:when>
                  <xsl:when test="$hasTargetNS='true' or $hasLocalNS='true'">
                     <xsl:text> and </xsl:text>
                  </xsl:when>
               </xsl:choose>
               <xsl:text>the following namespace(s): </xsl:text>
               <xsl:call-template name="PrintWhitespaceList">
                  <xsl:with-param name="value" select="normalize-space($specificNS)"/>
                  <xsl:with-param name="separator">,</xsl:with-param>
               </xsl:call-template>
            </xsl:if>
         </xsl:otherwise>
      </xsl:choose>
      <!-- Process contents -->
      <xsl:text> (</xsl:text>
      <xsl:choose>
         <xsl:when test="$processContents">
            <xsl:value-of select="normalize-space($processContents)"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:text>strict</xsl:text>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:text> validation)</xsl:text>
      <xsl:text>.</xsl:text>

      <!-- Print min/max occurs -->
      <xsl:if test="$componentType='element'">
         <xsl:text> </xsl:text>
         <xsl:call-template name="PrintOccurs">
            <xsl:with-param name="minOccurs" select="$minOccurs"/>
            <xsl:with-param name="maxOccurs" select="$maxOccurs"/>
         </xsl:call-template>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintWildcard </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out the pattern property for derivations by
     restriction on simple content.
     Param(s):
            simpleRestrict (Node) required
              'restriction' element
     -->
   <xsl:template name="PrintPatternFacet">
      <xsl:param name="simpleRestrict"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintPatternFacet </xsl:comment>
		</xsl:if>

      <xsl:if test="$simpleRestrict/xsd:pattern">
         <em>pattern</em>
         <xsl:text> = </xsl:text>
         <xsl:value-of select="$simpleRestrict/xsd:pattern/@value"/>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintPatternFacet </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out the total digits property for derivations by
     restriction on simple content.
     Param(s):
            simpleRestrict (Node) required
              'restriction' element
     -->
   <xsl:template name="PrintTotalDigitsFacet">
      <xsl:param name="simpleRestrict"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintTotalDigitsFacet </xsl:comment>
		</xsl:if>

      <xsl:if test="$simpleRestrict/xsd:totalDigits">
         <em>total no. of digits</em>
         <xsl:text> = </xsl:text>
         <xsl:value-of select="$simpleRestrict/xsd:totalDigits/@value"/>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintTotalDigitsFacet </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out the fraction digits property for derivations by
     restriction on simple content.
     Param(s):
            simpleRestrict (Node) required
              'restriction' element
     -->
   <xsl:template name="PrintFractionDigitsFacet">
      <xsl:param name="simpleRestrict"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintFractionDigitsFacet </xsl:comment>
		</xsl:if>

      <xsl:if test="$simpleRestrict/xsd:fractionDigits">
         <em>
            <xsl:text>no. of fraction digits</xsl:text>
         </em>
         <xsl:text> = </xsl:text>
         <xsl:value-of select="$simpleRestrict/xsd:fractionDigits/@value"/>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintFractionDigitsFacet </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out the enumeration list for derivations by
     restriction on simple content.
     Param(s):
            simpleRestrict (Node) required
              'restriction' element
     -->
   <xsl:template name="PrintEnumFacets">
      <xsl:param name="simpleRestrict"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintEnumFacets </xsl:comment>
		</xsl:if>

      <xsl:if test="$simpleRestrict/xsd:enumeration">
         <em>value</em>
         <xsl:text> comes from list: {</xsl:text>

         <xsl:for-each select="$simpleRestrict/xsd:enumeration">
            <xsl:if test="position()!=1">
               <xsl:text>|</xsl:text>
            </xsl:if>
            <xsl:text>'</xsl:text>
            <xsl:value-of select="@value"/>
            <xsl:text>'</xsl:text>
         </xsl:for-each>

         <xsl:text>}</xsl:text>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintEnumFacets </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out the length property for derivations by
     restriction on simple content.
     Param(s):
            simpleRestrict (Node) required
              'restriction' element
     -->
   <xsl:template name="PrintLengthFacets">
      <xsl:param name="simpleRestrict"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintLengthFacets </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="$simpleRestrict/xsd:length">
            <em>
               <xsl:text>length</xsl:text>
            </em>
            <xsl:text> = </xsl:text>
            <xsl:value-of select="$simpleRestrict/xsd:length/@value"/>
         </xsl:when>
         <xsl:when test="$simpleRestrict/xsd:minLength">
            <em>
               <xsl:text>length</xsl:text>
            </em>
            <xsl:text> >= </xsl:text>
            <xsl:value-of select="$simpleRestrict/xsd:minLength/@value"/>
         </xsl:when>
         <xsl:when test="$simpleRestrict/xsd:maxLength">
            <em>
               <xsl:text>length</xsl:text>
            </em>
            <xsl:text> &lt;= </xsl:text>
            <xsl:value-of select="$simpleRestrict/xsd:maxLength/@value"/>
         </xsl:when>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintLengthFacets </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out the whitespace property for derivations by
     restriction on simple content.
     Param(s):
            simpleRestrict (Node) required
              'restriction' element
     -->
   <xsl:template name="PrintWhitespaceFacet">
      <xsl:param name="simpleRestrict"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintWhitespaceFacet </xsl:comment>
		</xsl:if>

      <xsl:variable name="facetValue" select="normalize-space(translate($simpleRestrict/xsd:whiteSpace/@value, 'ACELOPRSV', 'aceloprsv'))"/>

      <xsl:choose>
         <xsl:when test="$facetValue='preserve'">
            <em>Whitespace policy: </em>
            <xsl:call-template name="PrintGlossaryTermRef">
               <xsl:with-param name="code">PreserveWS</xsl:with-param>
               <xsl:with-param name="term">preserve</xsl:with-param>
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="$facetValue='replace'">
            <em>Whitespace policy: </em>
            <xsl:call-template name="PrintGlossaryTermRef">
               <xsl:with-param name="code">ReplaceWS</xsl:with-param>
               <xsl:with-param name="term">replace</xsl:with-param>
            </xsl:call-template>
         </xsl:when>
         <xsl:when test="$facetValue='collapse'">
            <em>Whitespace policy: </em>
            <xsl:call-template name="PrintGlossaryTermRef">
               <xsl:with-param name="code">CollapseWS</xsl:with-param>
               <xsl:with-param name="term">collapse</xsl:with-param>
            </xsl:call-template>
         </xsl:when>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintWhitespaceFacet </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Print out the value ranges for derivations by
     restriction on simple content.
     Param(s):
            simpleRestrict (Node) required
              'restriction' element
     -->
   <xsl:template name="PrintRangeFacets">
      <xsl:param name="simpleRestrict"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintRangeFacets </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="($simpleRestrict/xsd:minInclusive or $simpleRestrict/xsd:minExclusive) and ($simpleRestrict/xsd:maxInclusive or $simpleRestrict/xsd:maxExclusive)">
            <xsl:choose>
               <xsl:when test="$simpleRestrict/xsd:minInclusive">
                  <xsl:value-of select="$simpleRestrict/xsd:minInclusive/@value"/>
                  <xsl:text> &lt;= </xsl:text>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$simpleRestrict/xsd:minExclusive/@value"/>
                  <xsl:text> &lt; </xsl:text>
               </xsl:otherwise>
            </xsl:choose>
            <em>
               <xsl:text>value</xsl:text>
            </em>
            <xsl:choose>
               <xsl:when test="$simpleRestrict/xsd:maxInclusive">
                  <xsl:text> &lt;= </xsl:text>
                  <xsl:value-of select="$simpleRestrict/xsd:maxInclusive/@value"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:text> &lt; </xsl:text>
                  <xsl:value-of select="$simpleRestrict/xsd:maxExclusive/@value"/>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:when>
         <xsl:when test="$simpleRestrict/xsd:minInclusive">
            <em>
               <xsl:text>value</xsl:text>
            </em>
            <xsl:text> >= </xsl:text>
            <xsl:value-of select="$simpleRestrict/xsd:minInclusive/@value"/>
         </xsl:when>
         <xsl:when test="$simpleRestrict/xsd:minExclusive">
            <em>
               <xsl:text>value</xsl:text>
            </em>
            <xsl:text> > </xsl:text>
            <xsl:value-of select="$simpleRestrict/xsd:minExclusive/@value"/>
         </xsl:when>
         <xsl:when test="$simpleRestrict/xsd:maxInclusive">
            <em>
               <xsl:text>value</xsl:text>
            </em>
            <xsl:text> &lt;= </xsl:text>
            <xsl:value-of select="$simpleRestrict/xsd:maxInclusive/@value"/>
         </xsl:when>
         <xsl:when test="$simpleRestrict/xsd:maxExclusive">
            <em>
               <xsl:text>value</xsl:text>
            </em>
            <xsl:text> &lt; </xsl:text>
            <xsl:value-of select="$simpleRestrict/xsd:maxExclusive/@value"/>
         </xsl:when>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintRangeFacets </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Prints out JavaScript code.
     NOTE: Javascript code is placed within comments to make it
     work with current browsers. In strict XHTML, JavaScript code 
     should be placed within CDATA sections. However, most
     browsers generate a syntax error if the page contains 
     CDATA sections. Placing Javascript code within comments
     means that the code cannot contain two dashes.
     Param(s):
            code (Result Tree Fragment) required
                Javascript code
  -->
   <xsl:template name="PrintJSCode">
      <xsl:param name="code"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: PrintJSCode </xsl:comment>
		</xsl:if>

      <script type="text/javascript">
         <!-- If browsers start supporting CDATA sections, 
              uncomment the following piece of code. -->
         <!-- <xsl:text disable-output-escaping="yes">
&lt;![CDATA[
</xsl:text> -->
         <!-- If browsers start supporting CDATA sections, 
              remove the following piece of code. -->
         <xsl:text disable-output-escaping="yes">
&lt;!--
</xsl:text>

         <xsl:value-of select="$code" disable-output-escaping="yes"/>
         <!-- If browsers start supporting CDATA sections, 
              remove the following piece of code. -->
         <xsl:text disable-output-escaping="yes">
// --&gt;
</xsl:text>
         <!-- If browsers start supporting CDATA sections, 
              uncomment the following piece of code. -->
         <!-- <xsl:text disable-output-escaping="yes">
]]&gt;
</xsl:text> -->
      </script>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: PrintJSCode </xsl:comment>
		</xsl:if>
   </xsl:template>

   <!--
     Translates occurrences of a string
     in a piece of text with another string.
     Param(s):
            value (String) required
                Text to translate
            strToReplace (String) required
                String to be replaced
            replacementStr (String) required
                Replacement text
     -->
   <xsl:template name="TranslateStr">
      <xsl:param name="value"/>
      <xsl:param name="strToReplace"/>
      <xsl:param name="replacementStr"/>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: TranslateStr </xsl:comment>
		</xsl:if>

      <xsl:if test="$value != ''">
         <xsl:variable name="beforeText" select="substring-before($value, $strToReplace)"/>
         <xsl:choose>
            <xsl:when test="$beforeText != ''">
               <xsl:value-of select="$beforeText"/>
               <xsl:value-of select="$replacementStr"/>
               <xsl:call-template name="TranslateStr">
                  <xsl:with-param name="value" select="substring-after($value, $strToReplace)"/>
                  <xsl:with-param name="strToReplace" select="$strToReplace"/>
                  <xsl:with-param name="replacementStr" select="$replacementStr"/>
               </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$value"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:if>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: TranslateStr </xsl:comment>
		</xsl:if>
   </xsl:template>

   <xsl:template name="HandleError">
      <xsl:param name="errorMsg"/>
      <xsl:param name="isTerminating">false</xsl:param>

   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-start: HandleError </xsl:comment>
		</xsl:if>

      <xsl:choose>
         <xsl:when test="$isTerminating='true'">
            <xsl:message terminate="yes">
               <xsl:text>XS3P ERROR: </xsl:text>
               <xsl:value-of select="$errorMsg"/>
            </xsl:message>
         </xsl:when>
         <xsl:otherwise>
            <span style="font-weight: bold; color: red">
               <xsl:text>ERROR: </xsl:text>
               <xsl:value-of select="$errorMsg"/>
            </span>
         </xsl:otherwise>
      </xsl:choose>
   	<xsl:if test="normalize-space(translate($showComments, 'TRUE', 'true'))='true'">
			<xsl:comment> template-end: HandleError </xsl:comment>
		</xsl:if>
   </xsl:template>
</xsl:stylesheet>
