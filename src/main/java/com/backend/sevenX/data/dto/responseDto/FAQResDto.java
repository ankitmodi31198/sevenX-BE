package com.backend.sevenX.data.dto.responseDto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FAQResDto {

	private Integer id;

	@NotNull
	private String question;

	@NotNull
	private String answer;
}
