<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
<xsl:template match="Invoice">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
		<!--
          <fo:block font-size="16pt" font-weight="bold" space-after="5mm">CELERA</fo:block>
  		  <fo:block><fo:external-graphic src="url('./logo.png')" /></fo:block>
		  <fo:block>
			<fo:external-graphic src="images/logo.png"  content-height="scale-to-fit" height="2.00in"  content-width="2.00in" scaling="non-uniform"/>
		  </fo:block>-->
         <fo:table table-layout="fixed" width="100%" border-collapse="separate">    
            <fo:table-column column-width="10cm"/>
			<fo:table-column column-width="8cm"/>
            <fo:table-body>
			    <fo:table-row>
					<fo:table-cell number-rows-spanned="7">
						<fo:block>
							<fo:external-graphic src="images/logo.png"  content-height="scale-to-fit" height="2.00cm"  content-width="5.00cm" scaling="non-uniform" display-align="before"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell >
						<xsl:attribute name="font-weight">bold</xsl:attribute>
						<fo:block>CELERA MARKETS LIMITED</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell font-size="10pt"><fo:block>11G-1, 51 Man Yue Street,</fo:block></fo:table-cell>
				</fo:table-row>
				<fo:table-row>  
					<fo:table-cell font-size="10pt"><fo:block>Hunghom, kowloon, Hong Kong</fo:block></fo:table-cell>
				</fo:table-row>
				<fo:table-row>  
					<fo:table-cell font-size="10pt"><fo:block>Tel: +852 3746 3801 / 3746 3898</fo:block></fo:table-cell>
				</fo:table-row>
				<fo:table-row>  
					<fo:table-cell font-size="10pt"><fo:block>Email: accounting@celera-markets.com</fo:block></fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell>
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>
            </fo:table-body>
        </fo:table>
		  
		  <fo:table table-layout="fixed" margin="2pt 1pt 1pt 1pt" width="100%" border-collapse="separate" border-separation="3pt">    
            <fo:table-column column-width="5cm"/>
			<fo:table-column column-width="5cm"/>
			<fo:table-column column-width="8cm"/>
            <fo:table-body>
				<fo:table-row  height="2cm">  
					<fo:table-cell number-columns-spanned="3">
						<!--<fo:block>CELERA</fo:block>-->
						<fo:block font-weight="bold" font-size="16pt" text-decoration="underline" text-align="center" display-align="bottom">INVOICE</fo:block>
					</fo:table-cell>
				</fo:table-row>
			    <fo:table-row>  
					<fo:table-cell border="inherit" background-color="#D9D9D9"  >
						<!--<fo:block>CELERA</fo:block>-->
						<fo:block  margin="0mm" padding-left="2mm">Bill To</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
					<fo:table-cell >
						<fo:block>Invoice # <xsl:value-of select="invoice_number"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>  
					<fo:table-cell number-columns-spanned="2">
						<fo:block margin="0mm" padding-left="2mm"><xsl:value-of select="company"/></fo:block>
					</fo:table-cell>
					<fo:table-cell >
						<fo:block>Invoice Date: <xsl:value-of select="invoice_date"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-rows-spanned="4" number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm"><xsl:value-of select="address"/></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>Account number: <xsl:value-of select="account_number"/></fo:block>
					</fo:table-cell>
				</fo:table-row>

				<fo:table-row>
					<fo:table-cell>
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>

				
				<fo:table-row>
					<fo:table-cell >
						<fo:block>Due date: <xsl:value-of select="due_date"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell >
						<fo:block>Amount Due: <xsl:value-of select="amount_due"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">Attn: <xsl:value-of select="attn"/></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<fo:table-row>
					<fo:table-cell border="inherit" background-color="#D9D9D9">
						<fo:block  margin="0mm" padding-left="2mm">Sent To</fo:block>
					</fo:table-cell>
					<fo:table-cell number-rows-spanned="3">
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>	
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm"><xsl:value-of select="sent_to"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell >
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>
			  </fo:table-body>
          </fo:table>	
				
		  <fo:table table-layout="fixed" margin="2pt 1pt 1pt 1pt" width="100%" border-collapse="separate"  border-separation="3pt">    
			<fo:table-column column-width="10cm" />
			<fo:table-column column-width="8cm"/>
            <fo:table-body vertical-align="center">
				<fo:table-row>
					<fo:table-cell border="inherit" background-color="#D9D9D9">
						<fo:block margin="0mm" padding-left="2mm">Description</fo:block>
					</fo:table-cell>
					<fo:table-cell border="inherit" background-color="#D9D9D9">
						<fo:block margin="0mm" padding-left="2mm">Amount</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell >
						<fo:block  margin="0mm" padding-left="2mm"><xsl:value-of select="description"/></fo:block>
					</fo:table-cell>
					<fo:table-cell >
						<fo:block><xsl:value-of select="amount"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row height="3cm">
					<fo:table-cell >
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
					<fo:table-cell number-rows-spanned="3">
						<fo:block >
							<fo:external-graphic src="images/chop.png"  content-height="scale-to-fit" height="1.00cm"  content-width="1.00cm" scaling="non-uniform" padding="2cm" margin="0mm" />
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell >
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell >
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				

				<fo:table-row>
					<fo:table-cell border="inherit" background-color="#D9D9D9" number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">Please remit to</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">Bank Name: <xsl:value-of select="payment_bank_name"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
				<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">Bank Address: <xsl:value-of select="payment_bank_address"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">Bank Code: <xsl:value-of select="payment_bank_code"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">Branch Code: <xsl:value-of select="payment_branch_code"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">Account Number: <xsl:value-of select="payment_account_number"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">Account Beneficiary: <xsl:value-of select="payment_account_beneficiary"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="2">
						<fo:block  margin="0mm" padding-left="2mm">SWIFT: <xsl:value-of select="payment_swift"/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell >
						<fo:block><fo:leader/></fo:block>
					</fo:table-cell>
				</fo:table-row>
				
            </fo:table-body>
          </fo:table>
        </fo:flow>
      </fo:page-sequence>
     </fo:root>
</xsl:template>
</xsl:stylesheet>
