package com.backend.sevenX.service;

import com.backend.sevenX.data.dto.requestDto.EmailReqDto;
import com.backend.sevenX.data.dto.requestDto.LoginDto;
import com.backend.sevenX.data.dto.requestDto.SignUpDto;
import com.backend.sevenX.data.dto.requestDto.TokenDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<?> login(LoginDto loginDto);

	ResponseEntity<?> signUp(SignUpDto signUpDto, String role);

	ResponseEntity<?> logout(Integer userId);

	ResponseEntity<?> getAllFAQ();

	ResponseEntity<?> getFAQById(Integer id);

	ResponseEntity<?> getProfileByUser(Integer id);

	ResponseEntity<?> editProfileByUser(Integer userId, SignUpDto signUpDto);

	ResponseEntity<?> forgotPassword(EmailReqDto emailReqDto);

	ResponseEntity<?> resetPassword(TokenDto tokenDto);
}
