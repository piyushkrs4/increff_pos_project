package com.increff.pos.dto;

import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class SalesReportDto {
    @Autowired
    private SalesReportService salesReportService;

    public List<SalesReportData> getAll() throws ApiException {
        return salesReportService.getAll();
    }


    public List<SalesReportData> getFilteredReport(SalesReportForm salesReportForm) throws ApiException, IllegalAccessException {
        validate(salesReportForm);
        normalize(salesReportForm);
        return salesReportService.getFilteredReport(salesReportForm);
    }

}
