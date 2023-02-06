package com.increff.pos.controller;

import com.increff.pos.dto.*;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import com.opencsv.CSVWriter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/supervisor")
public class SupervisorApiController {

    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private UserDto userDto;
    @Autowired
    private InventoryReportDto inventoryReportDto;
    @Autowired
    private DailyReportDto dailyReportDto;
    @Autowired
    private SalesReportDto salesReportDto;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "/brands", method = RequestMethod.POST)
    public void addBrand(@RequestBody BrandForm brandForm) throws ApiException, IllegalAccessException {
        brandDto.add(brandForm);
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "brands/{brandId}", method = RequestMethod.PUT)
    public void updateBrand(@PathVariable Integer brandId, @RequestBody BrandForm brandForm) throws ApiException, IllegalAccessException {
        brandDto.update(brandId, brandForm);
    }

    @ApiOperation(value = "Adds a Product")
    @RequestMapping(path = "/products", method = RequestMethod.POST)
    public void addProduct(@RequestBody ProductForm productForm) throws ApiException, IllegalAccessException {
        productDto.add(productForm);
    }

    @ApiOperation(value = "Updates a product")
    @RequestMapping(path = "products/{productId}", method = RequestMethod.PUT)
    public void updateProduct(@PathVariable Integer productId, @RequestBody EditProductForm editProductForm) throws ApiException, IllegalAccessException {
        productDto.update(productId, editProductForm);
    }

    @ApiOperation(value = "Adds an Inventory")
    @RequestMapping(path = "/inventories", method = RequestMethod.POST)
    public void addInventory(@RequestBody InventoryForm inventoryForm) throws ApiException, IllegalAccessException {
        inventoryDto.add(inventoryForm);
    }

    @ApiOperation(value = "Updates a inventory")
    @RequestMapping(path = "/inventories/{inventoryId}", method = RequestMethod.PUT)
    public void updateInventory(@PathVariable Integer inventoryId, @RequestBody InventoryForm inventoryForm) throws ApiException, IllegalAccessException {
        inventoryDto.update(inventoryId, inventoryForm);
    }

    @ApiOperation(value = "Adds a user")
    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public void addUser(@RequestBody UserForm userForm) throws ApiException, IllegalAccessException {
        userDto.add(userForm);
    }

    @ApiOperation(value = "Deletes a user")
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Integer userId) {
        userDto.delete(userId);
    }

    @ApiOperation(value = "Gets list of all users")
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UserData> getAllUser() throws ApiException {
        return userDto.getAll();
    }

    @ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/inventory-report", method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws ApiException {
        return inventoryReportDto.getAll();
    }

//    @ApiOperation(value = "Downloads inventory report")
//    @RequestMapping(value = "/inventory-report/download", method = RequestMethod.GET)
//    public void downloadInventoryReportCSV(HttpServletResponse response) throws IOException, ApiException {
//        List<InventoryReportData> inventoryReportDataList = inventoryReportDto.getAll(); //get the objects you want to convert to CSV
//
//        String csvFileName = "objects.csv";
//        response.setContentType("text/csv");
//        response.setHeader("Content-Disposition", "attachment; filename=" + csvFileName);
//
//        //use OpenCSV library to write the objects to a CSV file
//        Writer writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
//        CSVWriter csvWriter = new CSVWriter(writer,
//                CSVWriter.DEFAULT_SEPARATOR,
//                CSVWriter.NO_QUOTE_CHARACTER,
//                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//                CSVWriter.DEFAULT_LINE_END);
//        String[] headerRecord = {"S.No.", "Brand", "Category", "Quantity"};
//        csvWriter.writeNext(headerRecord);
//        Integer sno = 1;
//        for (InventoryReportData inventoryReportData : inventoryReportDataList) {
//            String[] dataRecord = {String.valueOf(sno++), inventoryReportData.getBrand(),
//                                        inventoryReportData.getCategory(), String.valueOf(inventoryReportData.getQuantity())};
//            csvWriter.writeNext(dataRecord);
//        }
//
//        csvWriter.close();
//    }

    @ApiOperation(value = "Downloads inventory report")
    @RequestMapping(value = "/inventory-report/download", method = RequestMethod.GET, produces = "text/csv")
    public String downloadInventoryReportCSV(HttpServletResponse response) throws IOException, ApiException {
        List<InventoryReportData> inventoryReportDataList = inventoryReportDto.getAll(); //get the objects you want to convert to CSV
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //use OpenCSV library to write the objects to a CSV string
        CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(byteArrayOutputStream, Charset.forName("UTF-8")),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        String[] headerRecord = {"S.No.", "Brand", "Category", "Quantity"};
        csvWriter.writeNext(headerRecord);
        Integer sno = 1;
        for (InventoryReportData inventoryReportData : inventoryReportDataList) {
            String[] dataRecord = {String.valueOf(sno++), inventoryReportData.getBrand(),
                    inventoryReportData.getCategory(), String.valueOf(inventoryReportData.getQuantity())};
            csvWriter.writeNext(dataRecord);
        }

        csvWriter.close();

        response.setHeader("Content-Disposition", "attachment; filename=inventory-report.csv");
        response.setContentType("text/csv");

        return byteArrayOutputStream.toString("UTF-8");
    }



    @ApiOperation(value = "Gets daily report")
    @RequestMapping(path = "/daily-report", method = RequestMethod.GET)
    public List<DailyReportData> getDailyReport() throws ApiException {
        return dailyReportDto.getAll();
    }

    @ApiOperation(value = "Gets daily report with filter")
    @RequestMapping(path = "/daily-report", method = RequestMethod.POST)
    public List<DailyReportData> getFilteredDailyReport(@RequestBody DailyReportForm dailyReportForm) throws ApiException {
        return dailyReportDto.getFilteredReport(dailyReportForm);
    }

    @ApiOperation(value = "Gets sales report")
    @RequestMapping(path = "/sales-report", method = RequestMethod.GET)
    public List<SalesReportData> getSalesReport() throws ApiException {
        return salesReportDto.getAll();
    }

    @ApiOperation(value = "Gets sales report with filter")
    @RequestMapping(path = "/sales-report", method = RequestMethod.POST)
    public List<SalesReportData> getFilteredSalesReport(@RequestBody SalesReportForm salesReportForm) throws ApiException, IllegalAccessException {
        System.out.println(salesReportForm.getStartDate());
        return salesReportDto.getFilteredReport(salesReportForm);
    }
}

// Todo -> have 2 controllers for admin and standard
// ToDo -> add method with request
// ToDo -> remove getUniqueBrands