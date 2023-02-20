package com.increff.pos.service;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private OrderDto orderDto;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    protected void createAndPlaceOrder() throws ApiException, IllegalAccessException {
        for (int i = 0; i < 3; i++) {
            List<OrderItemForm> orderItemFormList = createOrder(i);
            Integer orderId = orderDto.addOrder(orderItemFormList);
            orderService.placeOrder(orderId);
        }
    }
    protected List<OrderItemForm> createOrder(Integer id) throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand" + id);
        brandForm.setCategory("category" + id);
        brandDto.addBrand(brandForm);

        //productI
        ProductForm productForm1 = new ProductForm();
        productForm1.setBarcode("barcode" + id);
        productForm1.setBrand("brand" + id);
        productForm1.setCategory("category" + id);
        productForm1.setName("product" + id);
        productForm1.setMrp(100.0);
        productDto.add(productForm1);

        //Iproduct
        ProductForm productForm2 = new ProductForm();
        productForm2.setBarcode(id + "barcode");
        productForm2.setBrand("brand" + id);
        productForm2.setCategory("category" + id);
        productForm2.setName(id + "product");
        productForm2.setMrp(200.0);
        productDto.add(productForm2);


        //productI add inventory
        InventoryForm inventoryForm1 = new InventoryForm();
        inventoryForm1.setBarcode("barcode" + id);
        inventoryForm1.setQuantity(400);
        inventoryDto.add(inventoryForm1);

        //Iproduct add inventory
        InventoryForm inventoryForm2 = new InventoryForm();
        inventoryForm2.setBarcode(id + "barcode");
        inventoryForm2.setQuantity(400);
        inventoryDto.add(inventoryForm2);


        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        OrderItemForm orderItemForm1 = new OrderItemForm();
        orderItemForm1.setBarcode("barcode" + id);
        orderItemForm1.setQuantity(200);
        orderItemForm1.setSellingPrice(98.5);
        orderItemFormList.add(orderItemForm1);

        OrderItemForm orderItemForm2 = new OrderItemForm();
        orderItemForm2.setBarcode(id + "barcode");
        orderItemForm2.setQuantity(200);
        orderItemForm2.setSellingPrice(198.5);
        orderItemFormList.add(orderItemForm2);
        return orderItemFormList;
    }

    public List<OrderItemPojo> createOrderItemPojoList(Integer id) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + id);
        brandPojo.setCategory("category" + id);
        Integer brandId = brandService.addBrand(brandPojo);

        ProductPojo productPojo1 = new ProductPojo();
        productPojo1.setBarcode("barcode" + id);
        productPojo1.setBrandId(brandId);
        productPojo1.setName("product" + id);
        productPojo1.setMrp(100.0);
        Integer productId1 = productService.add(productPojo1);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setBarcode(id + "barcode");
        productPojo2.setBrandId(brandId);
        productPojo2.setName(id + "product");
        productPojo2.setMrp(100.0);
        Integer productId2 = productService.add(productPojo2);

        InventoryPojo inventoryPojo1 = new InventoryPojo();
        inventoryPojo1.setProductId(productId1);
        inventoryPojo1.setQuantity(400);
        inventoryService.add(inventoryPojo1);

        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setProductId(productId2);
        inventoryPojo2.setQuantity(400);
        inventoryService.add(inventoryPojo2);

        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();

        OrderItemPojo orderItemPojo1 = new OrderItemPojo();
        orderItemPojo1.setProductId(productId1);
        orderItemPojo1.setQuantity(10);
        orderItemPojo1.setSellingPrice(20.0);
        orderItemPojoList.add(orderItemPojo1);

        OrderItemPojo orderItemPojo2 = new OrderItemPojo();
        orderItemPojo2.setProductId(productId2);
        orderItemPojo2.setQuantity(10);
        orderItemPojo2.setSellingPrice(20.0);
        orderItemPojoList.add(orderItemPojo2);

        return orderItemPojoList;
    }
}
