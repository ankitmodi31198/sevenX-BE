package com.backend.sevenX.data.dto.requestDto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String socialId;

    @NotNull
    private String loginType;
}
