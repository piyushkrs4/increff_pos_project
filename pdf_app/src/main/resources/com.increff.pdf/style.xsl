<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21.0cm" margin-top="1cm" margin-left="2cm" margin-right="2cm" margin-bottom="1cm">
                    <!-- Page template goes here -->
                    <fo:region-body />
                    <fo:region-before region-name="xsl-region-before" extent="3cm"/>
                    <fo:region-after region-name="xsl-region-after" extent="4cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <!-- Page content goes here -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block>
                        <fo:table table-layout="fixed" width = "100%">
                            <fo:table-column column-width="8.5cm"/>
                            <fo:table-column column-width="8.5cm"/>
                            <fo:table-body>
                                <fo:table-row font-size="18pt" line-height="30px" background-color="#0e7a77" color="white">
                                    <fo:table-cell padding-left="5pt">
                                    <fo:block>
                                        <fo:external-graphic  src="url(file:///D:/WorkSpace/IncreffProjects/pdf_generator/src/main/resources/logo.png)" content-height="scale-to-fit" height="50px"  content-width="2.00in" scaling="non-uniform"/>
<!--                                        D:\WorkSpace\IncreffProjects\pdf_generator\src\main\resources\name.xml-->
                                    </fo:block>
                                    </fo:table-cell>
<!--                                    <fo:table-cell padding-left="5pt">-->
<!--                                        <fo:block>-->
<!--                                            Increff-->
<!--                                        </fo:block>-->
<!--                                    </fo:table-cell>-->
                                    <fo:table-cell padding-top="20pt" padding-right="10pt" font-weight = "bold">
                                        <fo:block text-align="right">
                                            INVOICE
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
<!--                                <fo:table-row>-->
<!--&lt;!&ndash;                                    <fo:table-cell padding-left="5pt" padding-top="5pt">&ndash;&gt;-->
<!--&lt;!&ndash;                                        <fo:block>&ndash;&gt;-->
<!--&lt;!&ndash;                                            c/o Looney tunes&#x2028;&ndash;&gt;-->
<!--&lt;!&ndash;                                            Toontown&ndash;&gt;-->
<!--&lt;!&ndash;                                        </fo:block>&ndash;&gt;-->
<!--&lt;!&ndash;                                    </fo:table-cell>&ndash;&gt;-->
<!--                                </fo:table-row>-->
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block line-height="20pt">
                        <fo:block font-weight="bold" text-align = "center">
                            Have a nice Day!!
                        </fo:block>
                    </fo:block>
                </fo:static-content>
<!--                <fo:flow flow-name="xsl-region-body" line-height="20pt">-->
<!--                    <xsl:apply-templates />-->
<!--                </fo:flow>-->
                <fo:flow flow-name="xsl-region-body" line-height="20pt">
                    <fo:block><xsl:apply-templates /></fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="invoice">
        <fo:block />
        <fo:block space-before="120pt" width="17cm" >
            <fo:table>
                <fo:table-column column-width="11.5cm"/>
<!--                <fo:table-column column-width="5cm"/>-->
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block>
                                <fo:inline font-weight="bold">From</fo:inline>&#x2028;
                                <xsl:value-of select="./companyName"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./building"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./street"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./city"></xsl:value-of>&#x2028;
                            </fo:block>
                        </fo:table-cell>
<!--                        <fo:table-cell>-->
<!--                            <fo:block>-->
<!--                                <fo:inline font-weight="bold">Ship To</fo:inline>-->
<!--                                <xsl:call-template name="address">-->
<!--                                    <xsl:with-param name="address" select="./address[@type='shipto']"></xsl:with-param>-->
<!--                                </xsl:call-template>-->
<!--                            </fo:block>-->
<!--                        </fo:table-cell>-->
                        <fo:table-cell>
                            <fo:block text-align="left">
                                <fo:inline font-weight="bold">Order No.</fo:inline>&#x2028;
                                <fo:inline font-weight="bold">Invoice No.</fo:inline>&#x2028;
                                <fo:inline font-weight="bold">Invoice Date</fo:inline>&#x2028;
                                <fo:inline font-weight="bold">Invoice Time</fo:inline>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align="left">
                                <xsl:value-of select="./orderNumber"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./invoiceNumber"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./invoiceDate"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./invoiceTime"></xsl:value-of>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
        <fo:block space-before="35pt">
            <fo:table line-height="30px">
                <fo:table-column column-width="1.5cm"/>
                <fo:table-column column-width="5cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="2cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-header>
                    <fo:table-row background-color="#f5f5f5" text-align="center" font-weight="bold">
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>S.No.</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Product Name</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Barcode</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Quantity</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Unit Price</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Total</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>
                <fo:table-body>
                    <xsl:apply-templates select="lineitems/lineitem"></xsl:apply-templates>
<!--                    <fo:table-row>-->
<!--                        <fo:table-cell number-columns-spanned="5" text-align="right" padding-right="3pt">-->
<!--                            <fo:block>Subtotal</fo:block>-->
<!--                        </fo:table-cell>-->
<!--                        <fo:table-cell  text-align="right" padding-right="3pt" border-left="1px solid #b8b6b6" border-right="1px solid #b8b6b6" >-->
<!--                            <fo:block>-->
<!--                                <xsl:value-of select="subtotal" />-->
<!--                            </fo:block>-->
<!--                        </fo:table-cell>-->
<!--                    </fo:table-row>-->
<!--                    <fo:table-row>-->
<!--                        <fo:table-cell number-columns-spanned="5" text-align="right" padding-right="3pt">-->
<!--                            <fo:block>Sales tax 5%</fo:block>-->
<!--                        </fo:table-cell>-->
<!--                        <fo:table-cell  text-align="right" padding-right="3pt" border-left="1px solid #b8b6b6" border-right="1px solid #b8b6b6" >-->
<!--                            <fo:block>-->
<!--                                <xsl:value-of select="tax" />-->
<!--                            </fo:block>-->
<!--                        </fo:table-cell>-->
<!--                    </fo:table-row>-->
                    <fo:table-row font-weight="bold">
                        <fo:table-cell number-columns-spanned="5" text-align="right" padding-right="3pt">
                            <fo:block>Total</fo:block>
                        </fo:table-cell>
                        <fo:table-cell  text-align="right" padding-right="3pt" background-color="#f5f5f5" border="1px solid #b8b6b6" >
                            <fo:block>
                                <xsl:value-of select="total" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>
    <xsl:template name="address">
        <xsl:param name="address"></xsl:param>
        <fo:block>
            <xsl:value-of select="$address/name" />&#x2028;
            <xsl:value-of select="$address/street" />&#x2028;
            <xsl:value-of select="$address/zipcode" />&#160;<xsl:value-of select="$address/city" />
        </fo:block>
    </xsl:template>
    <xsl:template match="lineitem">
        <fo:table-row>
            <fo:table-cell border="1px solid #b8b6b6" text-align="center">
                <fo:block>
                    <xsl:value-of select="sno"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="center" padding-left="3pt">
                <fo:block>
                    <xsl:value-of select="productName"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="center" padding-right="3pt">
                <fo:block>
                    <xsl:value-of select="barcode"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
                <fo:block>
                    <xsl:value-of select="quantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
                <fo:block>
                    <xsl:value-of select="unitPrice"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
                <fo:block>
                    <xsl:value-of select="total"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>

    </xsl:template>
</xsl:stylesheet>