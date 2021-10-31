package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFilterDto {

    private String username;

    private String firstName;

    private String phoneNo;

    private String orderStatus;

    private String transactionStatus;

    private Long fromDate;

    private Long toDate;

    private Integer offset;

    private Integer limit;

    private String sortField;

    private String sortOrder;

}
