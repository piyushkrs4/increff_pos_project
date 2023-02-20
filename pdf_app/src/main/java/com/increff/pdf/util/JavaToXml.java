package com.increff.pdf.util;

import com.increff.pdf.model.InvoiceData;
import com.increff.pdf.model.InvoiceLineItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

@Service
public class JavaToXml {

    @Value("${xmlFilePath}")
    private String xmlFilePath;

    public void javaToXmlConverter(InvoiceData invoiceData){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element invoice = doc.createElement("invoice");
            doc.appendChild(invoice);

            Element orderNumber = doc.createElement("orderNumber");
            orderNumber.appendChild(doc.createTextNode("" + invoiceData.getOrderCode()));
            invoice.appendChild(orderNumber);

            Element invoiceNumber = doc.createElement("invoiceNumber");
            invoiceNumber.appendChild(doc.createTextNode("" + invoiceData.getInvoiceNumber()));
            invoice.appendChild(invoiceNumber);

            Element invoiceDate = doc.createElement("invoiceDate");
            invoiceDate.appendChild(doc.createTextNode("" + invoiceData.getInvoiceDate()));
            invoice.appendChild(invoiceDate);

            Element invoiceTime = doc.createElement("invoiceTime");
            invoiceTime.appendChild(doc.createTextNode("" + invoiceData.getInvoiceTime()));
            invoice.appendChild(invoiceTime);

            Element companyName = doc.createElement("companyName");
            companyName.appendChild(doc.createTextNode("Increff"));
            invoice.appendChild(companyName);

            Element building = doc.createElement("building");
            building.appendChild(doc.createTextNode("2nd floor, Enzyme Tech Park,"));
            invoice.appendChild(building);

            Element street = doc.createElement("street");
            street.appendChild(doc.createTextNode("Sector 6, HSR Layout,"));
            invoice.appendChild(street);

            Element city = doc.createElement("city");
            city.appendChild(doc.createTextNode("Bengaluru, Karnataka 560102"));
            invoice.appendChild(city);

            Element total = doc.createElement("total");
            total.appendChild(doc.createTextNode("Rs. " + invoiceData.getTotal()));
            invoice.appendChild(total);

            // supercars element
            Element lineitems = doc.createElement("lineitems");
            invoice.appendChild(lineitems);

            for(InvoiceLineItem invoiceLineItem: invoiceData.getLineItems()){
                Element lineitem = doc.createElement("lineitem");
                lineitems.appendChild(lineitem);
                Element sno = doc.createElement("sno");
                sno.appendChild(doc.createTextNode("" + invoiceLineItem.getSno()));
                lineitem.appendChild(sno);

                Element productName = doc.createElement("productName");
                productName.appendChild(doc.createTextNode("" + invoiceLineItem.getProductName()));
                lineitem.appendChild(productName);

                Element barcode = doc.createElement("barcode");
                barcode.appendChild(doc.createTextNode("" + invoiceLineItem.getBarcode()));
                lineitem.appendChild(barcode);

                Element quantity = doc.createElement("quantity");
                quantity.appendChild(doc.createTextNode("" + invoiceLineItem.getQuantity()));
                lineitem.appendChild(quantity);

                Element unitPrice = doc.createElement("unitPrice");
                unitPrice.appendChild(doc.createTextNode("Rs. " + invoiceLineItem.getUnitPrice()));
                lineitem.appendChild(unitPrice);

                Element totalAmount = doc.createElement("total");
                totalAmount.appendChild(doc.createTextNode("Rs. " + invoiceLineItem.getTotal()));
                lineitem.appendChild(totalAmount);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlFilePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
