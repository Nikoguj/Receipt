package com.back.receipt.domain.mapper;

import com.back.receipt.domain.UserPartPriceProduct;
import com.back.receipt.domain.UserPartPriceReceipt;
import com.back.receipt.domain.dto.UserPartPriceDto;
import com.back.receipt.security.domain.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MapperUserPartPrice {

    public List<UserPartPriceDto> mapToUserPartReceiptDtoList(final List<UserPartPriceReceipt> userPartPriceReceiptList) {
        List<UserPartPriceDto> userPartPriceDtoList = new ArrayList<>();
        for(UserPartPriceReceipt userPartPriceReceipt: userPartPriceReceiptList) {
            userPartPriceDtoList.add(new UserPartPriceDto(userPartPriceReceipt.getName(), userPartPriceReceipt.getPartPrice()));
        }
        return userPartPriceDtoList;
    }

    public List<UserPartPriceDto> mapToUserPartProductDtoList(final List<UserPartPriceProduct> userPartPriceReceiptList) {
        List<UserPartPriceDto> userPartPriceDtoList = new ArrayList<>();
        for(UserPartPriceProduct userPartPriceProduct: userPartPriceReceiptList) {
            userPartPriceDtoList.add(new UserPartPriceDto(userPartPriceProduct.getName(), userPartPriceProduct.getPartPrice()));
        }
        return userPartPriceDtoList;
    }

    public Set<UserPartPriceProduct> mapToUserPartPriceProductSet(final Set<UserPartPriceDto> userPartPriceDtos) {
        Set<UserPartPriceProduct> userPartPriceProducts = new HashSet<>();
        for(UserPartPriceDto userPartPriceDto: userPartPriceDtos) {
            userPartPriceProducts.add(new UserPartPriceProduct(userPartPriceDto.getUsername(),userPartPriceDto.getPartPrice()));
        }
        return userPartPriceProducts;
    }
}