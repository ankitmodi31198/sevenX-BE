package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDetailsResDto {

    private Integer id;

    private Integer userId;

    private String username;

    private String firstName;

    private String phoneNo;

    private String address;

    private String state;

    private String gstNumber;

    private String panNumber;

    private Double subTotal;

   // private Double gstAmount;

    private Double orderTotal;

    private String createdAt;

    private String updatedAt;

    private List<PackagesResDto> packagesList;

    private Double finalOrderTotal;

    private String orderStatus;

    private String transactionStatus;

    private String transactionId;
}
