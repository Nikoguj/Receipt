package com.back.receipt.domain.mapper;

import com.back.receipt.domain.UserPartPriceProduct;
import com.back.receipt.domain.UserPartPriceReceipt;
import com.back.receipt.domain.dto.UserPartPriceDto;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class MapperUserPartPriceTest {

    @Autowired
    private MapperUserPartPrice mapperUserPartPrice;

    @Test
    void mapToUserPartReceiptDtoList() {
        //Given
        List<UserPartPriceReceipt> userPartPriceReceiptList = new ArrayList<>();
        userPartPriceReceiptList.add(new UserPartPriceReceipt("user1", 1.5));
        userPartPriceReceiptList.add(new UserPartPriceReceipt("user2", 4.7));
        userPartPriceReceiptList.add(new UserPartPriceReceipt("user3", 11.3));

        List<UserPartPriceDto> exceptedUserPartPriceDtoList = new ArrayList<>();
        exceptedUserPartPriceDtoList.add(new UserPartPriceDto("user1", 1.5));
        exceptedUserPartPriceDtoList.add(new UserPartPriceDto("user2", 4.7));
        exceptedUserPartPriceDtoList.add(new UserPartPriceDto("user3", 11.3));

        //When
        List<UserPartPriceDto> returnUserPartPriceDtoList = mapperUserPartPrice.mapToUserPartReceiptDtoList(userPartPriceReceiptList);

        //Then
        Assert.assertEquals(exceptedUserPartPriceDtoList, returnUserPartPriceDtoList);
        Assert.assertEquals(exceptedUserPartPriceDtoList.get(0).getUsername(), returnUserPartPriceDtoList.get(0).getUsername());
        Assert.assertEquals(exceptedUserPartPriceDtoList.get(0).getPartPrice(), returnUserPartPriceDtoList.get(0).getPartPrice(), 0.02);
    }

    @Test
    void mapToUserPartProductDtoList() {
        //Given
        List<UserPartPriceProduct> userPartPriceProductList = new ArrayList<>();
        userPartPriceProductList.add(new UserPartPriceProduct("user1", 1.5));
        userPartPriceProductList.add(new UserPartPriceProduct("user2", 4.7));
        userPartPriceProductList.add(new UserPartPriceProduct("user3", 11.3));

        List<UserPartPriceDto> exceptedUserPartPriceDtoList = new ArrayList<>();
        exceptedUserPartPriceDtoList.add(new UserPartPriceDto("user1", 1.5));
        exceptedUserPartPriceDtoList.add(new UserPartPriceDto("user2", 4.7));
        exceptedUserPartPriceDtoList.add(new UserPartPriceDto("user3", 11.3));

        //When
        List<UserPartPriceDto> returnUserPartPriceDtoList = mapperUserPartPrice.mapToUserPartProductDtoList(userPartPriceProductList);

        //Then
        Assert.assertEquals(exceptedUserPartPriceDtoList, returnUserPartPriceDtoList);
        Assert.assertEquals(exceptedUserPartPriceDtoList.get(0).getUsername(), returnUserPartPriceDtoList.get(0).getUsername());
        Assert.assertEquals(exceptedUserPartPriceDtoList.get(0).getPartPrice(), returnUserPartPriceDtoList.get(0).getPartPrice(), 0.02);
    }

    @Test
    void mapToUserPartPriceProductSet() {
        //Given
        Set<UserPartPriceDto> userPartPriceDtoHashSet = new HashSet<>();
        userPartPriceDtoHashSet.add(new UserPartPriceDto("user1", 1.5));
        userPartPriceDtoHashSet.add(new UserPartPriceDto("user2", 4.7));
        userPartPriceDtoHashSet.add(new UserPartPriceDto("user3", 11.3));

        Set<UserPartPriceProduct> exceptedSet = new HashSet<>();
        exceptedSet.add(new UserPartPriceProduct("user1", 1.5));
        exceptedSet.add(new UserPartPriceProduct("user2", 4.7));
        exceptedSet.add(new UserPartPriceProduct("user3", 11.3));

        //When
        Set<UserPartPriceProduct> returnSet = mapperUserPartPrice.mapToUserPartPriceProductSet(userPartPriceDtoHashSet);

        //Then
        Assert.assertEquals(exceptedSet, returnSet);
    }
}