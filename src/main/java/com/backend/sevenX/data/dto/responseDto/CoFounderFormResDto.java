package com.backend.sevenX.data.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CoFounderFormResDto {

    private Integer id;

    private Integer userId;

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

    //private List<String> documents;
}
