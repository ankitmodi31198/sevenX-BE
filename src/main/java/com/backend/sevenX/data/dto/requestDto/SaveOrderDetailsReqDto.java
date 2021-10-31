package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveOrderDetailsReqDto {

    private String username;

    private String firstName;

    private String phoneNo;

    private String address;

    private String state;

    private String gstNumber;

    private String panNumber;

}
