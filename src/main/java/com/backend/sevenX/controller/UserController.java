package com.backend.sevenX.controller;

import com.backend.sevenX.config.CommonResponse;
import com.backend.sevenX.data.dto.requestDto.LoginDto;
import com.backend.sevenX.data.dto.requestDto.SignUpDto;
import com.backend.sevenX.security.JwtTokenUtil;
import com.backend.sevenX.service.UserService;
import com.backend.sevenX.utills.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@GetMapping("/hello-world")
	public Map helloWorld(){
		Map demoMap = new HashMap();
		demoMap.put("name","sevenX");
		demoMap.put("key2","startUp");
		return demoMap;
	}

	@PostMapping(Constant.EndPoints.LOGIN)
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws Exception {
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
	public ResponseEntity<?> userSignUp(@RequestBody SignUpDto signUpDto) throws Exception {
		try {
			return userService.signUp(signUpDto,"User");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				Constant.Messages.SOMETHING_WENT_WRONG, null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(Constant.EndPoints.AdminSignUp)
	public ResponseEntity<?> adminSignUp(@RequestBody SignUpDto signUpDto) throws Exception {
		try {
			return userService.signUp(signUpDto,"Admin");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				Constant.Messages.SOMETHING_WENT_WRONG, null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
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

}
