package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDetailsReqDto {

    private Integer orderId;

    private Double finalOrderTotalAmount;

    private String note;

    private List<OrderPackageReqDto> orderPackageList;

    private Double additionalCost;

    private String transactionId;

    private String transactionStatus;
}
