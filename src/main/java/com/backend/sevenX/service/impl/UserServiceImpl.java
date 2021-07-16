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

    private static final String LoginTypeNormal = "Normal";

    @PersistenceContext
    public EntityManager entityManager;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private FaqRepo faqRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final ModelMapper mapper = new ModelMapper();

    public ResponseEntity<?> login(LoginDto loginDto) {
        Users existUsers = usersRepo.findByUsername(loginDto.getUsername());
        if (existUsers == null) {
            return new ResponseEntity<>(new CommonResponse().getResponse(
                    HttpStatus.OK.value(),
                    Constant.Messages.USER_NOT_FOUND, null), HttpStatus.NOT_FOUND);
        }

        if (loginDto.getLoginType().equals(LoginTypeNormal)) {
            if (General.checkPassword(loginDto.getPassword(), existUsers.getPassword())) {
                usersRepo.save(existUsers);
                LoginResponseDto loginRes = mapper.map(existUsers, LoginResponseDto.class);
                String jwt = jwtTokenUtil.generateToken(existUsers.getUsername(), existUsers.getId(), existUsers.getRole());
                loginRes.setJwt(jwt);
                return new ResponseEntity<>(new CommonResponse().getResponse(
                        HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, loginRes), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.BAD_REQUEST.value(),
                        Constant.Messages.PASSWORD_IS_WRONG, null), HttpStatus.BAD_REQUEST);
            }
        } else {

        	if(loginDto.getSocialId().equals(existUsers.getSocialId())
					&& loginDto.getLoginType() != null
					&& loginDto.getLoginType().equals(existUsers.getLoginType())
			) {
				String jwt = jwtTokenUtil.generateToken(existUsers.getUsername(), existUsers.getId(), existUsers.getRole());
				LoginResponseDto loginRes = mapper.map(existUsers, LoginResponseDto.class);
				loginRes.setJwt(jwt);
				return new ResponseEntity<>(new CommonResponse().getResponse(
						HttpStatus.OK.value(),
						Constant.Messages.SUCCESS, loginRes), HttpStatus.OK);
			}else {
				return new ResponseEntity<>(new CommonResponse().getResponse(
						HttpStatus.OK.value(),
						Constant.Messages.USER_NOT_FOUND, null), HttpStatus.NOT_FOUND);
			}

        }
    }

    public ResponseEntity<?> signUp(SignUpDto signUpDto, String role) {

        Users tempUsers = usersRepo.findByUsername(signUpDto.getUsername());
        if (tempUsers == null) {
            Users users = mapper.map(signUpDto, Users.class);
            users.setRole(role);
            users.setPassword(General.hashPassword(signUpDto.getPassword()));
            users = usersRepo.save(users);
            String jwt = jwtTokenUtil.generateToken(users.getUsername(), users.getId(), users.getRole());
            usersRepo.save(users);
            LoginResponseDto loginRes = mapper.map(users, LoginResponseDto.class);
            loginRes.setJwt(jwt);
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                    Constant.Messages.SUCCESS, loginRes), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.BAD_REQUEST.value(),
                    Constant.Messages.TRY_OTHER_USERNAME, null), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> logout(Integer userId) {
        //no need to do anything will be managed only from front
		/*Users users = usersRepo.findById(userId).orElse(null);
		if (users != null) {
			usersRepo.save(users);
		}*/
        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                Constant.Messages.SUCCESS, null), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllFAQ() {
        try {
            List<FAQ> faqList = faqRepo.findAll();
            if (faqList.size() > 0) {
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
