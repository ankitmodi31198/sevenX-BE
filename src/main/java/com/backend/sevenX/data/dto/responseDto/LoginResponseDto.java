package com.backend.sevenX.data.dto.responseDto;
import lombok.Data;

@Data
public class LoginResponseDto {

    private String username;

    private String jwtToken;
}
