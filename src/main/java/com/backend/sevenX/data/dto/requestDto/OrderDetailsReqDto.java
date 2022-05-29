package com.backend.sevenX.data.dto.requestDto;

import com.backend.sevenX.utills.Constant;
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

    private String orderStatus;

    private String transactionId;

    private String transactionStatus;

    private String transactionNote;

    private String orderStatus = Constant.Status.Pending;
}
