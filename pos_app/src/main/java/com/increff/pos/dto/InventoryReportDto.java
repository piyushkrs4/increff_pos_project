package com.increff.pos.dto;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
@Component
public class InventoryReportDto {
    @Autowired
    private InventoryReportService inventoryReportService;

    public List<InventoryReportData> getAll() throws ApiException {
        return inventoryReportService.getAll();
    }

//    public downloadCSV()

}
