package com.backend.sevenX.data.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StartupIdeaFormReqDto {

    private String startupName;

    private String contactNo;

    private String email;

    private String stage;

    private String idea;

    private String industry;

    private String remarks;
}
