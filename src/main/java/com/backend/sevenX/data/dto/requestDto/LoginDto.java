package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginDto {

    @NotNull
    private String username;

    private String password;

    private String socialValue;

    @NotNull
    private String loginType;
}
