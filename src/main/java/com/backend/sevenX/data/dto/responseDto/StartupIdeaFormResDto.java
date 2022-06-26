package com.backend.sevenX.data.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StartupIdeaFormResDto {

    private Integer id;

    private Integer userId;

    private String startupName;

    private String contactNo;

    private String email;

    private String stage;

    private String idea;

    private String industry;

    private String remarks;

    private List<DocumentResDto> documents;
}
