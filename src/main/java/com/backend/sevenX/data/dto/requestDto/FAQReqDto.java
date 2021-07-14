package com.backend.sevenX.data.dto.requestDto;


import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FAQReqDto {

	@NotNull
	private String question;

	@NotNull
	private String answer;
}
