package com.increff.pos.controller;

import com.increff.pos.dto.*;
import com.increff.pos.model.datas.*;
import com.increff.pos.model.forms.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private BrandReportDto brandReportDto;
    @Autowired
    private InventoryReportDto inventoryReportDto;
    @Autowired
    private DailyReportDto dailyReportDto;
    @Autowired
    private SalesReportDto salesReportDto;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "/brands", method = RequestMethod.POST)
    public void addBrand(@RequestBody BrandForm brandForm) throws ApiException, IllegalAccessException {
        brandDto.addBrand(brandForm);
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "brands/{brandId}", method = RequestMethod.PUT)
    public void updateBrand(@PathVariable Integer brandId, @RequestBody BrandForm brandForm) throws ApiException, IllegalAccessException {
        brandDto.updateBrand(brandId, brandForm);
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
        userDto.addUser(userForm);
    }

    @ApiOperation(value = "Updates a user")
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.PUT)
    public void updateUser(@PathVariable Integer userId, @RequestBody UserForm userForm) throws ApiException, IllegalAccessException {
        userDto.updateUser(userId, userForm);
    }

    @ApiOperation(value = "Deletes a user")
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Integer userId) throws ApiException {
        userDto.delete(userId);
    }

    @ApiOperation(value = "Gets list of all users")
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.GET)
    public UserData getUser(@PathVariable Integer userId) throws ApiException {
        return userDto.get(userId);
    }

    @ApiOperation(value = "Gets list of all users")
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UserData> getAllUser() throws ApiException {
        return userDto.getAll();
    }

    @ApiOperation(value = "Gets brand report")
    @RequestMapping(path = "/brand-report", method = RequestMethod.GET)
    public List<BrandData> getBrandReport() throws ApiException {
        return brandReportDto.getAllBrands();
    }

    @ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/inventory-report", method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws ApiException {
        return inventoryReportDto.getAll();
    }

    @ApiOperation(value = "Gets daily report")
    @RequestMapping(path = "/daily-report", method = RequestMethod.GET)
    public List<DailyReportData> getDailyReport() throws ApiException {
        return dailyReportDto.getAll();
    }

    @ApiOperation(value = "Gets daily report with filter")
    @RequestMapping(path = "/daily-report", method = RequestMethod.POST)
    public List<DailyReportData> getFilteredDailyReport(@RequestBody DailyReportForm dailyReportForm) throws ApiException, IllegalAccessException {
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
        return salesReportDto.getFilteredReport(salesReportForm);
    }
}