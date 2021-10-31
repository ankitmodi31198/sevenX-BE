package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ListOrderDetailsResDto {

    private List<OrderDetailsResDto> orderList;

    private Integer totalOrdersCount;
}
