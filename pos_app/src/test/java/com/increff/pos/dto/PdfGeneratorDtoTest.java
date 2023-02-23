//package com.increff.pos.dto;
//
//import com.increff.pos.model.datas.InvoiceData;
//import com.increff.pos.model.datas.OrderData;
//import com.increff.pos.service.AbstractUnitTest;
//import com.increff.pos.service.ApiException;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.IOException;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//
//public class PdfGeneratorDtoTest extends AbstractUnitTest {
//    @Autowired
//    private OrderDto orderDto;
//    @Autowired
//    private PdfGeneratorDto pdfGeneratorDto;
//
//    @Test(expected = ApiException.class)
//    public void testGeneratePdfForIncorrectOrderId() throws ApiException, IllegalAccessException, IOException {
//        pdfGeneratorDto.generatePdf(2);
//    }
//
//
//    @Test
//    public void testShowPlacedOrder() throws ApiException, IllegalAccessException {
//        createAndPlaceOrder();
//        OrderData orderData = orderDto.getAllOrders().get(0);
//        InvoiceData invoiceData = pdfGeneratorDto.showPlacedOrder(orderData.getId());
//        assertEquals(orderData.getOrderCode(), invoiceData.getOrderCode());
//    }
//
//    @Test(expected = ApiException.class)
//    public void testShowPlacedOrderForIncorrectOrderId() throws ApiException {
//        pdfGeneratorDto.showPlacedOrder(2);
//    }
//
//    @Test
//    public void testDownloadInvoice() throws IOException {
//        assertNotEquals(null, pdfGeneratorDto.downloadInvoice(1));
//    }
//
//
//}
