package com.backend.sevenX.data.dto.requestDto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class FAQReqDto {

	@NotNull
	private String question;

	@NotNull
	private String answer;
}
