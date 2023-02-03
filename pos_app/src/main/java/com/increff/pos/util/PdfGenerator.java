package com.increff.pos.util;

import com.increff.pos.model.InvoiceData;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PdfGenerator {
    @Autowired
    JavaToXml javaToXml;
    public void xmlToPdfConverter(InvoiceData invoiceData){
        try {
            javaToXml.javaToXmlConverter(invoiceData);
            File xmlfile = new File("src/main/resources/com.increff.pdf\\hello.xml");
            File xsltfile = new File("src/main/resources/com.increff.pdf\\style.xsl");
            File pdfDir = new File("src/main/resources/com.increff.pdf./PdfFiles");
            pdfDir.mkdirs();
            String pdfFileName = "invoice_" + invoiceData.getInvoiceNumber() + ".pdf";
            File pdfFile = new File(pdfDir, pdfFileName);

            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
            // Setup output
            OutputStream out = new FileOutputStream(pdfFile);
            out = new java.io.BufferedOutputStream(out);
            try {
                // Construct fop with desired output format
                Fop fop;
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);

                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());

                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }catch(IOException exp){
            exp.printStackTrace();
        }
    }
}
