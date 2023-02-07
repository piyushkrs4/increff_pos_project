package com.increff.pos.dto;

import com.increff.pos.model.EditProductForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
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
import static com.increff.pos.util.NormalizerUtil.normalize;
import static com.increff.pos.util.ValidatorUtil.validate;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public Integer add(ProductForm productForm) throws ApiException, IllegalAccessException {
        validate(productForm);
        normalize(productForm);
        ProductPojo productPojo = convertGeneric(productForm, ProductPojo.class);
        productPojo.setBrandId(brandService.findBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
        return productService.add(productPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            BrandPojo brandPojo = brandService.get(productPojo.getBrandId());
            productDataList.add(convertProductPojoToData(productPojo, brandPojo));
        }
        return productDataList;
    }

    public void update(Integer productId, EditProductForm editProductForm) throws ApiException, IllegalAccessException {
        validate(editProductForm);
        normalize(editProductForm);
        ProductPojo productPojo = convertGeneric(editProductForm, ProductPojo.class);
        productService.update(productId, productPojo);
    }
    // todo -> saparate package for convert
}
