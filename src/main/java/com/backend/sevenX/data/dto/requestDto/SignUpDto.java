package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SignUpDto {

    @NotNull
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNo;

    private String address;

    private String socialValue;

    @NotNull
    private String loginType;
}
