<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
<xsl:attribute-set name="myBorder">
  <xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="myChop">
  <xsl:attribute name="src">images/chop.png</xsl:attribute>
  <xsl:attribute name="content-height">scale-to-fit</xsl:attribute>
  <xsl:attribute name="content-width">20mm</xsl:attribute>
  <xsl:attribute name="scaling">non-uniform</xsl:attribute>
  <xsl:attribute name="margin">0mm</xsl:attribute>
</xsl:attribute-set>
<xsl:template match="TradeDetails">

    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="20cm" page-width="40cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>

      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
		  <fo:block font-size="16pt" font-weight="bold" text-align="center" space-after="1cm">MONTHLY DETAILS - <xsl:value-of select="period"/>
		  </fo:block>
          <fo:block font-size="14pt" font-weight="bold" margin="0mm" padding-left="3cm" space-after="5mm">
			Client Name:            <xsl:value-of select="company"/>
          </fo:block>
          <fo:block font-size="10pt">
          <fo:table table-layout="fixed" width="100%" border-collapse="separate"  margin="2pt 1pt 1pt 1pt">    
            <fo:table-column column-width="2cm"/>
            <fo:table-column column-width="5cm"/>
            <fo:table-column column-width="15cm"/>
			<fo:table-column column-width="2cm"/>
			<fo:table-column column-width="2cm"/>
			<fo:table-column column-width="5cm"/>
			<fo:table-column column-width="4cm"/>
            <fo:table-body>
 
			 <fo:table-row font-weight="bold">   
			  <fo:table-cell xsl:use-attribute-sets="myBorder">
				<fo:block text-align="center">Trade Date</fo:block>
			  </fo:table-cell>
			  <fo:table-cell xsl:use-attribute-sets="myBorder">
				<fo:block text-align="center">Trade ID</fo:block>
			  </fo:table-cell>
			  <fo:table-cell xsl:use-attribute-sets="myBorder">
					<fo:block text-align="center">Description</fo:block>
			  </fo:table-cell>
			  <fo:table-cell xsl:use-attribute-sets="myBorder">
					<fo:block text-align="center">Size</fo:block>
			  </fo:table-cell>
			  <fo:table-cell xsl:use-attribute-sets="myBorder">
					<fo:block text-align="center">Hedge</fo:block>
			  </fo:table-cell>
			  <fo:table-cell xsl:use-attribute-sets="myBorder">
					<fo:block text-align="center">Reference</fo:block>
			  </fo:table-cell>
			  <fo:table-cell xsl:use-attribute-sets="myBorder">
					<fo:block text-align="center">Brokerage Fee</fo:block>
			  </fo:table-cell>			  
	  	     </fo:table-row>   
			
			
              <xsl:apply-templates select="TradeDetail"/>
			  
			 <fo:table-row>   
			  <fo:table-cell number-columns-spanned="7">
				<fo:block>
					<fo:block><fo:leader/></fo:block>
				</fo:block>
			  </fo:table-cell>
	  	     </fo:table-row>
			 
			 <fo:table-row  font-weight="bold" font-size="12pt">   
			  <fo:table-cell number-columns-spanned="2">
				<fo:block>
					<fo:block><fo:leader/></fo:block>
				</fo:block>
			  </fo:table-cell>   
			  <fo:table-cell>
				<fo:block>
					<fo:block><xsl:value-of select="description"/></fo:block>
				</fo:block>
			  </fo:table-cell>   
			  <fo:table-cell text-align="center">
				<fo:block>
					<fo:block><xsl:value-of select="size"/></fo:block>
				</fo:block>
			  </fo:table-cell>   
			  <fo:table-cell text-align="center">
				<fo:block>
					<fo:block><xsl:value-of select="hedge"/></fo:block>
				</fo:block>
			  </fo:table-cell>   
			  <fo:table-cell >
				<fo:block>
					<fo:block><fo:leader/></fo:block>
				</fo:block>
			  </fo:table-cell>
			  <fo:table-cell>
				<fo:block text-align="right">
					<fo:block><xsl:value-of select="total_fee"/></fo:block>
				</fo:block>
			  </fo:table-cell> 			  
	  	     </fo:table-row>   
			 
			 <fo:table-row height="3cm">
				<fo:table-cell number-columns-spanned="7">
					<fo:block text-align="right">
						<fo:external-graphic xsl:use-attribute-sets="myChop" padding="2cm" />
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
			 
            </fo:table-body>
          </fo:table>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
     </fo:root>
</xsl:template>

<xsl:template match="TradeDetail">
    <fo:table-row>   
      <fo:table-cell xsl:use-attribute-sets="myBorder">
        <fo:block  text-align="center">
          <xsl:value-of select="date"/>
        </fo:block>
      </fo:table-cell>   
      <fo:table-cell xsl:use-attribute-sets="myBorder">
        <fo:block>
          <xsl:value-of select="id"/>
        </fo:block>
      </fo:table-cell>   
      <fo:table-cell xsl:use-attribute-sets="myBorder">
        <fo:block>
			<xsl:value-of select="description"/>
        </fo:block>
      </fo:table-cell>
	        <fo:table-cell xsl:use-attribute-sets="myBorder">
	  <fo:block  text-align="center">
			<xsl:value-of select="size"/>
        </fo:block>
      </fo:table-cell>
	        <fo:table-cell xsl:use-attribute-sets="myBorder">
	    <fo:block  text-align="center">
			<xsl:value-of select="hedge"/>
        </fo:block>
      </fo:table-cell>
	        <fo:table-cell xsl:use-attribute-sets="myBorder">
	          <fo:block>
			<xsl:value-of select="reference"/>
        </fo:block>
      </fo:table-cell>
	        <fo:table-cell xsl:use-attribute-sets="myBorder">
	    <fo:block  text-align="right">
			<xsl:value-of select="fee"/>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
</xsl:stylesheet>