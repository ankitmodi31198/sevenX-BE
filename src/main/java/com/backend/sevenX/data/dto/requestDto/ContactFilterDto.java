package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactFilterDto {

    private String name;

    private String email;

    private String contactNo;

    private String state;

    private String screenName;

    private Long fromDate;

    private Long toDate;

    private Integer offset;

    private Integer limit;

    private String sortField;

    private String sortOrder;
}
