package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListCoFounderResDto {

    private List<CoFounderFormResDto> coFounderFormList;

    private Integer totalCoFounderCount;
}
