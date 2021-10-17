package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDetailsResDto {

    private Integer orderId;

    private Integer userId;

    private Double subTotal;

   // private Double gstAmount;

    private Double orderTotal;

    private String createdAt;

    private String updatedAt;

    private List<PackagesResDto> packagesList;
}
