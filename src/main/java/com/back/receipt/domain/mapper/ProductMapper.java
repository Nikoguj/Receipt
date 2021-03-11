package com.back.receipt.domain.mapper;

import com.back.receipt.domain.Product;
import com.back.receipt.domain.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto mapToProductDto(final Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setQuantity(product.getQuantity());
        productDto.setPrice(product.getPrice());
        productDto.setPriceForOne(product.getPriceForOne());
        productDto.setPriceWithoutDiscount(product.getPriceWithoutDiscount());
        productDto.setDiscount(product.getDiscount());
        return productDto;
    }

    public List<ProductDto> mapToProductDtoList(final List<Product> products) {
        return products.stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
    }
}