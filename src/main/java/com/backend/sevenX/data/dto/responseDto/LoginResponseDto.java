package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String username;

    private String jwt;
}
