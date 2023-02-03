package com.increff.pos.dto;

import com.increff.pos.model.DailyReportData;
import com.increff.pos.model.DailyReportForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DailyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.increff.pos.util.Converter.convertDailyReportPojoToDailyReportData;

@Component
public class DailyReportDto {
    @Autowired
    private DailyReportService dailyReportService;

    public List<DailyReportData> getAll() throws ApiException {
        return convertDailyReportPojoToDailyReportData(dailyReportService.getAll());
    }

    public List<DailyReportData> getFilteredReport(DailyReportForm dailyReportForm) throws ApiException {
        return convertDailyReportPojoToDailyReportData(dailyReportService.getFilteredReport(dailyReportForm));
    }

}
