package com.back.receipt.domain.mapper;

import com.back.receipt.domain.Receipt;
import com.back.receipt.domain.dto.ReceiptDto;
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
class ReceiptMapperTest {

    @Autowired
    private ReceiptMapper receiptMapper;

    Receipt receipt = new Receipt();

    @BeforeEach
    public void init() {
        receipt.setId(1L);
        receipt.setAddress("address1");
        receipt.setDate("date1");
        receipt.setFullPrice(150.45);
    }

    @Test
    void mapToReceiptDto() {
        //Given
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setId(1L);
        receiptDto.setAddress("address1");
        receiptDto.setDate("date1");
        receiptDto.setFullPrice(150.45);

        //When
        ReceiptDto returnReceiptDto = receiptMapper.mapToReceiptDto(receipt);

        //Then
        Assert.assertEquals(receiptDto.getId(), returnReceiptDto.getId());
        Assert.assertEquals(receiptDto.getAddress(), returnReceiptDto.getAddress());
        Assert.assertEquals(receiptDto.getDate(), returnReceiptDto.getDate());
        Assert.assertEquals(receiptDto.getFullPrice(), returnReceiptDto.getFullPrice(), 0.02);
        Assert.assertEquals(0, returnReceiptDto.getProductCount());
    }

    @Test
    void mapToReceiptDtoList() {
        //Given
        Receipt receipt2 = new Receipt();
        receipt2.setId(2L);
        receipt2.setAddress("address2");
        receipt2.setDate("date2");
        receipt2.setFullPrice(450.45);

        List<Receipt> receiptList = new ArrayList<>();
        receiptList.add(receipt);
        receiptList.add(receipt2);


        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setId(1L);
        receiptDto.setAddress("address1");
        receiptDto.setDate("date1");
        receiptDto.setFullPrice(150.45);


        ReceiptDto receiptDto2 = new ReceiptDto();
        receiptDto2.setId(2L);
        receiptDto2.setAddress("address2");
        receiptDto2.setDate("date2");
        receiptDto2.setFullPrice(450.45);

        List<ReceiptDto> receiptDtoList = new ArrayList<>();
        receiptDtoList.add(receiptDto);
        receiptDtoList.add(receiptDto2);

        //When
        List<ReceiptDto> returnReceiptDtoList = receiptMapper.mapToReceiptDtoList(receiptList);

        //Then
        Assert.assertEquals(receiptDtoList, returnReceiptDtoList);
    }
}