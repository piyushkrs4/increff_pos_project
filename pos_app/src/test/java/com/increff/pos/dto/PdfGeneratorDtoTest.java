//package com.increff.pos.dto;
//
//import com.increff.pos.model.forms.OrderItemForm;
//import com.increff.pos.service.AbstractUnitTest;
//import com.increff.pos.service.ApiException;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.IOException;
//import java.util.List;
//
//import static org.junit.Assert.assertNotEquals;
//
//public class PdfGeneratorDtoTest extends AbstractUnitTest {
//    @Autowired
//    private OrderDto orderDto;
//    @Autowired
//    private PdfGeneratorDto pdfGeneratorDto;
//
//    @Test
//    public void testGeneratePdf() throws ApiException, IllegalAccessException, IOException {
//        List<OrderItemForm> orderItemFormList = createOrder(0);
//        Integer orderId = orderDto.addOrder(orderItemFormList);
//        pdfGeneratorDto.generatePdf(orderId);
//        System.out.println(orderId);
//        assertNotEquals(null, pdfGeneratorDto.downloadInvoice(orderId));
//    }
//
//    @Test
//    public void testShowPlacedOrder()
//
////    @Test
//
//
//
//}
