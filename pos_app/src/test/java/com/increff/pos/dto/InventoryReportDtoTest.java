package com.increff.pos.dto;

import com.increff.pos.model.datas.InventoryReportData;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class InventoryReportDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryReportDto inventoryReportDto;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testGetAllInventoryReports() throws ApiException, IllegalAccessException {
        createAndPlaceOrder();
        List<InventoryReportData> inventoryReportDataList = inventoryReportDto.getAll();
        assertEquals(3, inventoryReportDataList.size());
        for (int i = 0; i < 3; i++) {
            assertEquals("brand" + i, inventoryReportDataList.get(i).getBrand());
            assertEquals("category" + i, inventoryReportDataList.get(i).getCategory());
            assertEquals((Integer) 400, inventoryReportDataList.get(i).getQuantity());
        }
    }

//    @Test
//    public void testDownloadInventoryReportCSV() throws ApiException, IllegalAccessException, IOException {
//        createAndPlaceOrder();
//        assertNotEquals(null, inventoryReportDto.downloadInventoryReportCSV());
//    }


}
