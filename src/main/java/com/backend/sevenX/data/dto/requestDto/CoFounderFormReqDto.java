package com.backend.sevenX.data.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CoFounderFormReqDto {

    private String startupName;

    private String mobileNo;

    private String emailId;

    private String startupYear;

    private String stage;

    private String idea;

    private String industry;

    private String profileSkills;

    private String state;

    private String remarks;
}
