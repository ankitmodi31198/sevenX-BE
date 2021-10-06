package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PackagesReqDto {

    @NotNull
    private String planName;

    private String heading;

    @Lob
    private String description;

    @NotNull
    private String screenName;

    private Double amount;
}
