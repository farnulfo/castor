<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Document Stylesheet                            -->
<!-- Ismael Ghalimi ghalimi@exoffice.com            -->
<!-- Copyright (c) Exoffice Technologies, Inc. 1999 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/XSL/Transform/1.0">

  <xsl:template match="document">
    <xsl:apply-templates/>
  </xsl:template>


  <xsl:template match="document/properties">
    <table border="0" cellpadding="4" cellspacing="2">
      <tr>
        <td valign="top"><b>Author:</b></td>
        <td valign="top">
          <xsl:for-each select="authors/author">
              <xsl:value-of select="firstname"/><xsl:value-of select="$nbsp"/>
              <xsl:if test="initials">
                <xsl:value-of select="initials"/><xsl:value-of select="$nbsp"/>
              </xsl:if>
              <xsl:value-of select="lastname"/><xsl:value-of select="$nbsp"/><xsl:value-of select="$nbsp"/>
              <a href="mailto:{@email}"><xsl:value-of select="@email"/></a><br/>
          </xsl:for-each>
        </td>
      </tr>
      <tr>
        <td valign="top"><b>Abstract:</b></td>
        <td valign="top"><xsl:value-of select="abstract"/><br/><xsl:value-of select="$nbsp"/><xsl:value-of select="$nbsp"/></td>
      </tr>
      <tr>
        <td valign="top"><b>Status:</b></td>
        <td valign="top"><xsl:value-of select="status"/></td>
      </tr>
    </table><br/>
  </xsl:template>


  <xsl:template match="document/body">
    <xsl:if test="/document/properties/title">
      <br/>
      <h1><xsl:value-of select="/document/properties/title"/></h1>
      <br/>
    </xsl:if>
    <xsl:if test="@title">
      <br/>
      <h1><xsl:value-of select="@title"/></h1>
      <br/>
    </xsl:if>
    <blockquote>
      <xsl:apply-templates/>
    </blockquote>
  </xsl:template>


  <xsl:template match="document//section">
    <xsl:variable name="level" select="count(ancestor::*)"/>
    <xsl:choose>
      <xsl:when test='$level=2'>
        <a name="{@title}"><h2>
          <xsl:number level="multiple" count="section" format="1.1"/>. <xsl:value-of select="$nbsp"/><xsl:value-of select="$nbsp"/><xsl:value-of select="@title"/>
        </h2></a>
      </xsl:when>
      <xsl:when test='$level=3'>
        <a name="{@title}"><h3>
          <xsl:number level="multiple" count="section" format="1.1"/>. <xsl:value-of select="$nbsp"/><xsl:value-of select="$nbsp"/><xsl:value-of select="@title"/>
        </h3></a>
      </xsl:when>
      <xsl:when test='$level=4'>
        <a name="{@title}"><h4>
          <xsl:number level="multiple" count="section" format="1.1"/>. <xsl:value-of select="$nbsp"/><xsl:value-of select="$nbsp"/><xsl:value-of select="@title"/>
        </h4></a>
      </xsl:when>
      <xsl:when test='$level>=5'>
        <h5><xsl:copy-of select="@title"/></h5>
      </xsl:when>
    </xsl:choose>
    <xsl:apply-templates/>
  </xsl:template>


  <xsl:template match="p">
    <p><xsl:apply-templates/><br/></p>
  </xsl:template>


  <xsl:template match="ul">
    <table border="0" cellpadding="2" cellspacing="2">
      <tr><td colspan="2" height="5"></td></tr>
      <xsl:apply-templates/>
    </table>
  </xsl:template>


  <xsl:template match="ul/li">
    <tr>
      <td align="left" valign="top" width="15"><img src="images/bullets/blue.gif" height="22" width="15" alt="*"/></td>
      <td align="left" valign="top"><xsl:apply-templates/></td>
    </tr>
  </xsl:template>


  <xsl:template match="project-name">
    <xsl:value-of select="$project/@name"/>
  </xsl:template>


  <xsl:template match="project-repository">
    <xsl:value-of select="$project/@repository"/>
  </xsl:template>

  <xsl:template match="project-title">
    <xsl:value-of select="$project/title"/>
  </xsl:template>

  <xsl:template match="*|@*">
    <xsl:copy>
      <xsl:apply-templates select="*|@*|text()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>








































