package com.backend.sevenX.data.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class EmailReqDto {

    @NotEmpty(message = "Email Can't be null or empty")
    String username;

    @NotEmpty(message = "Reset URL Can't be null or empty")
    String resetUrl;
}
