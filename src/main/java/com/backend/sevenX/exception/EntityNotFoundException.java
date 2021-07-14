package com.backend.sevenX.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class EntityNotFoundException extends RuntimeException {

	public EntityNotFoundException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 159874652L;

	public EntityNotFoundException(Class<?> entityClass, Integer id) {
		super(String.format("Invalid Id Number '%s' for class '%s'", id, entityClass.getSimpleName()));
	}
}
