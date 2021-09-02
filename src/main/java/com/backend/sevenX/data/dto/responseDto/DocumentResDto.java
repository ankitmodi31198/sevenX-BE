package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DocumentResDto {

	@NotNull
	private String documentURL;

	private String documentTitle;

	private String documentFor;

	private Integer userId;

}
