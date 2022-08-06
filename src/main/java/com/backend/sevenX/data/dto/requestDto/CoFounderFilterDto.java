package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoFounderFilterDto {

    private String startupName;

    private String contactNo;

    private String email;

    private String state;

    private String startupYear;

    private String industry;

    private Long fromDate;

    private Long toDate;

    private Integer offset;

    private Integer limit;

    private String stage;

    private String sortField;

    private String sortOrder;
}
