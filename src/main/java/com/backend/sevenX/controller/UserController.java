package com.backend.sevenX.controller;

import com.backend.sevenX.config.CommonResponse;
import com.backend.sevenX.data.dto.requestDto.ContactFormReqDto;
import com.backend.sevenX.data.dto.requestDto.EmailReqDto;
import com.backend.sevenX.data.dto.requestDto.LoginDto;
import com.backend.sevenX.data.dto.requestDto.SignUpDto;
import com.backend.sevenX.data.dto.requestDto.TokenDto;
import com.backend.sevenX.data.model.Document;
import com.backend.sevenX.security.JwtTokenUtil;
import com.backend.sevenX.service.ImageService;
import com.backend.sevenX.service.UserService;
import com.backend.sevenX.utills.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	private ImageService imageService;

	@GetMapping("/hello-world")
	public Map helloWorld(){
		Map demoMap = new HashMap();
		demoMap.put("name","sevenX");
		demoMap.put("key2","startUp");
		return demoMap;
	}

	@PostMapping(Constant.EndPoints.LOGIN)
	public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) throws Exception {
		try {
			return userService.login(loginDto);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CommonResponse().getResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				Constant.Messages.SOMETHING_WENT_WRONG, null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(Constant.EndPoints.SIGNUP)
	public ResponseEntity<?> userSignUp(@RequestBody @Valid SignUpDto signUpDto) throws Exception {
		return userService.signUp(signUpDto,"User");
	}

	@PostMapping(Constant.EndPoints.AdminSignUp)
	public ResponseEntity<?> adminSignUp(@RequestBody @Valid SignUpDto signUpDto) throws Exception {

		return userService.signUp(signUpDto,"Admin");
	}

	@GetMapping(Constant.EndPoints.LOGOUT)
	public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) throws Exception {
		String token = httpServletRequest.getHeader(Constant.JwtConst.KEY_AUTHORIZATION);
		Integer userId = jwtTokenUtil.getUserIdFromToken(token);
		if(userId == null){
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.UNAUTHORIZED.value(),
				Constant.Messages.INVALID_TOKEN, Constant.Messages.PLEASE_LOGIN_AGAIN), HttpStatus.UNAUTHORIZED);
		}
		return userService.logout(userId);
	}

	@GetMapping(Constant.EndPoints.ALLFAQ)
	public ResponseEntity<?> getAllFAQ() throws Exception {
		return userService.getAllFAQ();
	}

	@GetMapping(Constant.EndPoints.FAQ)
	public ResponseEntity<?> getFAQById(@PathVariable(required = true) Integer id) throws Exception {
		return userService.getFAQById(id);
	}

	@GetMapping(Constant.EndPoints.PROFILE)
	public ResponseEntity<?> getProfileByUser(@RequestAttribute("userId") Integer userId) throws Exception {
		return userService.getProfileByUser(userId);
	}

	@PutMapping(Constant.EndPoints.PROFILE)
	public ResponseEntity<?> editProfileByUser(@RequestAttribute("userId") Integer userId, @RequestBody SignUpDto signUpDto) {
		return userService.editProfileByUser(userId,signUpDto);
	}

	@PostMapping(Constant.EndPoints.FORGOT_PASSWORD)
	public ResponseEntity<?> forgotPassword(
											@RequestBody @Valid EmailReqDto emailReqDto
	) {
		return userService.forgotPassword(emailReqDto);
	}

	@PostMapping(Constant.EndPoints.RESET_PASSWORD)
	public ResponseEntity<?> resetPassword(
			@RequestBody @Valid TokenDto tokenDto
	) {
		return userService.resetPassword(tokenDto);
	}

	@PostMapping(Constant.EndPoints.DOCUMENT_UPLOAD)
	public ResponseEntity<?> addDocuments(@RequestParam(value = "document") MultipartFile document,
									 @RequestParam(value = "documentTitle", required = false) String documentTitle,
									 @RequestParam(value = "documentFor", required = false) String documentFor,
									@RequestParam(value = "screenName", required = false) String screenName,
									 @RequestAttribute("userId") Integer userId) throws ParseException {

		if (document != null) {
			try {
				String documentPath = imageService.storeUploadedFile(document);
				Document documentObj = new Document();
				documentObj.setDocumentURL(documentPath);
				documentObj.setDocumentTitle(documentTitle);
				documentObj.setDocumentFor(documentFor);
				documentObj.setScreenName(screenName);
				documentObj.setUserId(userId);
				return imageService.saveDocumentByUserId(documentObj);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return new ResponseEntity<>("Error Occured in saving documents", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else{
			return new ResponseEntity<>("document is not found", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(Constant.EndPoints.CONTACT_FORM)
	public ResponseEntity<?> saveContactForm(@RequestBody @Valid ContactFormReqDto contactFormReqDto) throws Exception {
		return userService.saveContactForm(contactFormReqDto);
	}

	@GetMapping(Constant.EndPoints.Documents)
	public ResponseEntity<?> getImage(@RequestParam String name) {
		try {
			return imageService.getImage(name);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@GetMapping(Constant.EndPoints.DocumentsBYUSER)
	public ResponseEntity<?> getDocumentByUserId(@RequestAttribute("userId") Integer userId) {
		try {
			return imageService.getDocumentByUserId(userId);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
