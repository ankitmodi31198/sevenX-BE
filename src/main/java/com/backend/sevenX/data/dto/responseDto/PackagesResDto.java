package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PackagesResDto {

    private Integer id;

    @NotNull
    private String planName;

    private String heading;

    private String description;

    @NotNull
    private String screenName;

    private Double amount;

    private Double finalAmount;

}
