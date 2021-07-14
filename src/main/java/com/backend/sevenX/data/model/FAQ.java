package com.backend.sevenX.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Data
public class FAQ extends Base {

	@Lob
	private String question;

	@Lob
	private String answer;
}
