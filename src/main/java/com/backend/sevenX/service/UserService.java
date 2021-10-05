package com.backend.sevenX.service;

import com.backend.sevenX.data.dto.requestDto.*;
import org.springframework.http.ResponseEntity;

import javax.persistence.criteria.CriteriaBuilder;

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

	ResponseEntity<?> saveContactForm(ContactFormReqDto contactFormReqDto);

	ResponseEntity<?> savePackagesDetails(PackagesReqDto packagesReqDto);

	ResponseEntity<?> getAllPackagesByScreenName(String screenName);

	ResponseEntity<?> saveCartDetails(CartDetailsReqDto cartDetailsReqDto, Integer userId);

	ResponseEntity<?> getCartDetailsByUserId(Integer userId);
}
