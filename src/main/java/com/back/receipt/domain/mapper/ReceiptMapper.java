package com.back.receipt.domain.mapper;

import com.back.receipt.domain.Receipt;
import com.back.receipt.domain.dto.ReceiptDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReceiptMapper {

    public ReceiptDto mapToReceiptDto(final Receipt receipt) {
        return new ReceiptDto(
                receipt.getId(),
                receipt.getAddress(),
                receipt.getDate(),
                receipt.getFullPrice(),
                receipt.getProductList().size());
    }

    public List<ReceiptDto> mapToReceiptDtoList(final List<Receipt> receiptList) {
        return receiptList.stream().map(this::mapToReceiptDto).collect(Collectors.toList());
    }

}
