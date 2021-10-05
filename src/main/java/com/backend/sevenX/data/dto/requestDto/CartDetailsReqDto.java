package com.backend.sevenX.data.dto.requestDto;

import com.backend.sevenX.data.model.Packages;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDetailsReqDto {

    private Double subTotal;

    private Double gstAmount;

    private Double orderTotal;

    private List<Packages> packagesList;
}
