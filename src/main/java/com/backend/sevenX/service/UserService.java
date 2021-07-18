package com.backend.sevenX.service;

import com.backend.sevenX.data.dto.requestDto.LoginDto;
import com.backend.sevenX.data.dto.requestDto.SignUpDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<?> login(LoginDto loginDto);

	ResponseEntity<?> signUp(SignUpDto signUpDto, String role);

	ResponseEntity<?> logout(Integer userId);

	ResponseEntity<?> getAllFAQ();

	ResponseEntity<?> getFAQById(Integer id);

	ResponseEntity<?> getProfileByUser(Integer id);

}
