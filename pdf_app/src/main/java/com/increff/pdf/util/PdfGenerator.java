package com.increff.pdf.util;

import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

@Service
public class PdfGenerator {
    @Value("${xmlFilePath}")
    private String xmlFilePath;

    @Value("${xslFilePath}")
    private String xslFilePath;

    public String xmlToPdfConverter() {
        try {
            File xmlfile = new File(xmlFilePath);
            File xsltfile = new File(xslFilePath);

            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                Fop fop;
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                Source src = new StreamSource(xmlfile);

                Result res = new SAXResult(fop.getDefaultHandler());

                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                e.printStackTrace();
            } finally {
                byte[] pdf = out.toByteArray();
                return Base64.getEncoder().encodeToString(pdf);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return "";
    }
}
