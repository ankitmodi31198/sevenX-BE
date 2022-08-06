package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartUpIdeaFilterDto {

    private String startupName;

    private String contactNo;

    private String email;

    private String industry;

    private Long fromDate;

    private Long toDate;

    private String stage;

    private Integer offset;

    private Integer limit;

    private String sortField;

    private String sortOrder;
}
