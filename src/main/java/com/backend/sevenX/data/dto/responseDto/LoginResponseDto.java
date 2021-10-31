package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private Integer id;

    private String username;

    private String jwt;

    private String socialId;

    private String firstName;

    private String lastName;

    private String phoneNo;

    private String address;

    private String state;

    private String gstNumber;

    private String panNumber;

    private String role;

    private String loginType;
}
