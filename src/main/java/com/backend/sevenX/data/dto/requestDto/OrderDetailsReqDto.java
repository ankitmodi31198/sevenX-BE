package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDetailsReqDto {

    private Double subTotal;

    private Double gstAmount;

    private Double orderTotal;

    private List<Integer> packagesList;

    private String transactionId;

    private String transactionStatus;
}
