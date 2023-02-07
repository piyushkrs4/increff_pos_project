package com.increff.pos.dto;

import com.increff.pos.service.AbstractUnitTest;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderDtoTest extends AbstractUnitTest  {
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private OrderDto orderDto;
}
