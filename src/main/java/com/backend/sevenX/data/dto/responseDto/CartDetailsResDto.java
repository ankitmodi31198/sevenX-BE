package com.backend.sevenX.data.dto.responseDto;

import com.backend.sevenX.data.model.Packages;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDetailsResDto {

    private Integer userId;

    private Double subTotal;

    private Double gstAmount;

    private Double orderTotal;

    private Double finalOrderTotal;

    private List<PackagesResDto> packagesList;
}
