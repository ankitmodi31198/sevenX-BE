package com.backend.sevenX.service.impl;

import com.backend.sevenX.config.CommonResponse;
import com.backend.sevenX.data.dto.requestDto.LoginDto;
import com.backend.sevenX.data.dto.requestDto.SignUpDto;
import com.backend.sevenX.data.dto.responseDto.FAQResDto;
import com.backend.sevenX.data.dto.responseDto.LoginResponseDto;
import com.backend.sevenX.data.model.FAQ;
import com.backend.sevenX.data.model.Users;
import com.backend.sevenX.repository.FaqRepo;
import com.backend.sevenX.repository.UsersRepo;
import com.backend.sevenX.security.JwtTokenUtil;
import com.backend.sevenX.service.UserService;
import com.backend.sevenX.utills.Constant;
import com.backend.sevenX.utills.General;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@PersistenceContext
	public EntityManager entityManager;

	@Autowired
	private UsersRepo usersRepo;

	@Autowired
	private FaqRepo faqRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private ModelMapper mapper = new ModelMapper();

	public ResponseEntity<?> login(LoginDto loginDto) {
		Users users = usersRepo.findByUsernameAndDeletedAtIsNull(loginDto.getUsername());
		if (users != null) {
			if (General.checkPassword(loginDto.getPassword(), users.getPassword())) {
				String jwtToken = jwtTokenUtil.generateToken(users.getUsername(), users.getId(), users.getRole());
				users.setJwtToken(jwtToken);
				usersRepo.save(users);
				LoginResponseDto loginRes = mapper.map(users, LoginResponseDto.class);
				return new ResponseEntity<>(new CommonResponse().getResponse(
					HttpStatus.OK.value(),
					Constant.Messages.SUCCESS, loginRes), HttpStatus.OK);
			}
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.BAD_REQUEST.value(),
				Constant.Messages.PASSWORD_IS_WRONG, null), HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
				Constant.Messages.USER_NOT_FOUND, null), HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<?> signUp(SignUpDto signUpDto, String role) {

		Users tempUsers = usersRepo.findByUsernameAndDeletedAtIsNull(signUpDto.getUsername());
		if (tempUsers == null) {
			Users users = mapper.map(signUpDto, Users.class);
			users.setRole(role);
			users.setPassword(General.hashPassword(signUpDto.getPassword()));
			users = usersRepo.save(users);
			String jwtToken = jwtTokenUtil.generateToken(users.getUsername(), users.getId(), users.getRole());
			users.setJwtToken(jwtToken);
			usersRepo.save(users);
			LoginResponseDto loginRes = mapper.map(users, LoginResponseDto.class);
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
				Constant.Messages.SUCCESS, loginRes), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.BAD_REQUEST.value(),
				Constant.Messages.TRY_OTHER_USERNAME, null), HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<?> logout(Integer userId) {
		Users users = usersRepo.findById(userId).orElse(null);
		if (users != null) {
			users.setJwtToken("");
			usersRepo.save(users);
		}
		return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
			Constant.Messages.SUCCESS, null), HttpStatus.OK);
	}

	public ResponseEntity<?> getAllFAQ() {
		try {
			List<FAQ> faqList = faqRepo.findAll();
			if (faqList != null && faqList.size() > 0) {
				Type targetListType = new TypeToken<List<FAQResDto>>() {
				}.getType();
				List<FAQResDto> faqDtoList = mapper.map(faqList, targetListType);
				return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
					Constant.Messages.SUCCESS, faqDtoList), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
					Constant.Messages.ERROR, new ArrayList<>()), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getFAQById(Integer id) {
		try {
			FAQ faq = faqRepo.findById(id).orElse(null);
			if (faq != null) {
				Type targetType = new TypeToken<FAQResDto>() {
				}.getType();
				FAQResDto faqResDto = mapper.map(faq, targetType);
				return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
					Constant.Messages.SUCCESS, faqResDto), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
					Constant.Messages.ERROR, "Invalid FAQ ID"), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
