package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FAQResDto {

	private Integer id;

	@NotNull
	private String question;

	@NotNull
	private String answer;
}
