package com.back.receipt.domain.mapper;

import com.back.receipt.domain.Product;
import com.back.receipt.domain.dto.ProductDto;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    Product product = new Product();

    @BeforeEach
    public void init() {
        product.setId(1L);
        product.setName("product");
        product.setQuantity(10);
        product.setPrice(10);
        product.setPriceForOne(1);
        product.setPriceWithoutDiscount(0);
    }


    @Test
    void mapToReceiptDto() {
        //Given


        //When
        ProductDto returnProductDto = productMapper.mapToProductDto(product);

        //Then
        Assert.assertEquals(java.util.Optional.of(product.getId()), java.util.Optional.of(returnProductDto.getId()));
        Assert.assertEquals(product.getName(), returnProductDto.getName());
        Assert.assertEquals(product.getPrice(), returnProductDto.getPrice(), 0.02);
        Assert.assertEquals(product.getQuantity(), returnProductDto.getQuantity(), 0.02);
        Assert.assertEquals(product.getPriceWithoutDiscount(), returnProductDto.getPriceWithoutDiscount(), 0.02);
        Assert.assertEquals(product.getPriceForOne(), returnProductDto.getPriceForOne(), 0.02);

    }

    @Test
    void mapToReceiptDtoList() {
        //Given
        Product product2 = new Product();
        product2.setId(1L);
        product2.setName("product");
        product2.setQuantity(10);
        product2.setPrice(10);
        product2.setPriceForOne(1);
        product2.setPriceWithoutDiscount(0);

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        //When
        List<ProductDto> returnProductDtoList = productMapper.mapToProductDtoList(productList);

        //Then
        Assert.assertEquals(2, returnProductDtoList.size());
    }
}