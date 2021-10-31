package com.backend.sevenX.data.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactFormResDto {

	private Integer id;

	private String name;

	private String email;

	private String contactNo;

	private String state;

	private String screenName;

	private String createdAt;

}
