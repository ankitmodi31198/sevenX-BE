package com.backend.sevenX.data.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SignUpDto {

    @NotNull
    private String username;

    private String password;

    private String newPassword;

    private String firstName;

    private String lastName;

    private String phoneNo;

    private String address;

    private String state;

    private String gstNumber;

    private String panNumber;

    private String socialId;

    @NotNull
    private String loginType;
}
