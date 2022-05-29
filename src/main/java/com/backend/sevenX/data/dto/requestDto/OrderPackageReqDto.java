package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPackageReqDto {

   private Integer orderPackageId;

   private String note;

   private Double additionalCost;

   private Double finalPackageAmount;
}
