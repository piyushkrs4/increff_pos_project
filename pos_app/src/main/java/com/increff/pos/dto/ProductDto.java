package com.increff.pos.dto;

import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.datas.ProductData;
import com.increff.pos.model.forms.EditProductForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.Converter.convertGeneric;
import static com.increff.pos.util.Converter.convertProductPojoToData;
import static com.increff.pos.util.GenericFunctionsUtil.roundUpTo2DecimalPlaces;
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class ProductDto {
    @Autowired
    private ProductFlow productFlow;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;


    public Integer add(ProductForm productForm) throws ApiException, IllegalAccessException {
        validate(productForm);
        normalize(productForm);
        if(productForm.getBarcode().length() != 8){
            throw new ApiException("Enter barcode of size 8!");
        }
        if(productForm.getBrand().equals("choose")) {
            throw new ApiException("Please choose Brand!");
        }
        if(productForm.getCategory().equals("choose")) {
            throw new ApiException("Please choose Category!");
        }
        productForm.setMrp(roundUpTo2DecimalPlaces(productForm.getMrp()));
        ProductPojo productPojo = convertGeneric(productForm, ProductPojo.class);
        productPojo.setBrandId(brandService.findBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
        return productFlow.add(productPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            BrandPojo brandPojo = brandService.getBrand(productPojo.getBrandId());
            productDataList.add(convertProductPojoToData(productPojo, brandPojo));
        }
        return productDataList;
    }

    public void update(Integer productId, EditProductForm editProductForm) throws ApiException, IllegalAccessException {
        validate(editProductForm);
        normalize(editProductForm);
        editProductForm.setMrp(roundUpTo2DecimalPlaces(editProductForm.getMrp()));
        ProductPojo productPojo = convertGeneric(editProductForm, ProductPojo.class);
        productService.update(productId, productPojo);
    }
}
