package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class TokenDto {
    @NotEmpty(message = "Token can't be null or empty")
    String token;

    @NotEmpty(message = "Password can't be null or empty")
    String password;
}
