package com.backend.sevenX.service.impl;

import com.backend.sevenX.config.CommonResponse;
import com.backend.sevenX.data.dto.requestDto.*;
import com.backend.sevenX.data.dto.responseDto.*;
import com.backend.sevenX.data.model.*;
import com.backend.sevenX.exception.EntityNotFoundException;
import com.backend.sevenX.repository.*;
import com.backend.sevenX.security.JwtTokenUtil;
import com.backend.sevenX.service.EmailService;
import com.backend.sevenX.service.UserService;
import com.backend.sevenX.utills.Constant;
import com.backend.sevenX.utills.General;
import com.backend.sevenX.utills.Mail;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final String LOGIN_TYPE_NORMAL = "Normal";

    private final ModelMapper mapper = General.getStrictMapper();

    @PersistenceContext
    public EntityManager entityManager;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private FaqRepo faqRepo;

    @Autowired
    private PackagesRepo packagesRepo;

    @Autowired
    private CartDetailsRepo cartDetailsRepo;

    @Autowired
    private CartPackagesRepo cartPackagesRepo;

    @Autowired
    private OrderDetailsRepo orderDetailsRepo;

    @Autowired
    private OrderPackagesRepo orderPackagesRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ContactFormRepo contactFormRepo;

    public ResponseEntity<?> login(LoginDto loginDto) {
        Users existUsers = usersRepo.findByUsername(loginDto.getUsername());
        if (existUsers == null) {
            return new ResponseEntity<>(new CommonResponse().getResponse(
                    HttpStatus.OK.value(),
                    Constant.Messages.USER_NOT_FOUND, null), HttpStatus.NOT_FOUND);
        }

        if (loginDto.getLoginType().equals(LOGIN_TYPE_NORMAL)) {
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

            if (loginDto.getSocialId().equals(existUsers.getSocialId())
                    && loginDto.getLoginType() != null
                    && loginDto.getLoginType().equals(existUsers.getLoginType())
            ) {
                String jwt = jwtTokenUtil.generateToken(existUsers.getUsername(), existUsers.getId(), existUsers.getRole());
                LoginResponseDto loginRes = mapper.map(existUsers, LoginResponseDto.class);
                loginRes.setJwt(jwt);
                return new ResponseEntity<>(new CommonResponse().getResponse(
                        HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, loginRes), HttpStatus.OK);
            } else {
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
            if (signUpDto.getLoginType().equals(LOGIN_TYPE_NORMAL)) {
                users.setPassword(General.hashPassword(signUpDto.getPassword()));
            }
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
            if (Objects.nonNull(faqList) && faqList.size() > 0) {
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

    @Override
    public ResponseEntity<?> getProfileByUser(Integer id) {
        try {
            Users existingUser = usersRepo.findById(id).orElse(null);
            if (existingUser != null) {
                LoginResponseDto responseDto = mapper.map(existingUser, LoginResponseDto.class);
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, responseDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Invalid User"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> editProfileByUser(Integer userId, SignUpDto signUpDto) {
        Users users = usersRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(Constant.Messages.USER_NOT_FOUND));

        if (signUpDto.getPassword() != null && users.getLoginType().equals(LOGIN_TYPE_NORMAL)) {
            users.setPassword(General.hashPassword(signUpDto.getPassword()));
        }

        if (Strings.isNotEmpty(signUpDto.getAddress())) {
            users.setAddress(signUpDto.getAddress());
        }

        if (Strings.isNotEmpty(signUpDto.getFirstName())) {
            users.setFirstName(signUpDto.getFirstName());
        }

        if (Strings.isNotEmpty(signUpDto.getPhoneNo())) {
            users.setPhoneNo(signUpDto.getPhoneNo());
        }

        if (Strings.isNotEmpty(signUpDto.getLastName())) {
            users.setLastName(signUpDto.getLastName());
        }

        usersRepo.save(users);

        LoginResponseDto loginRes = mapper.map(users, LoginResponseDto.class);

        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                Constant.Messages.SUCCESS, loginRes), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> forgotPassword(EmailReqDto emailReqDto) {

        Users users = usersRepo.findByUsername(emailReqDto.getUsername());

        if (users != null) {

            String token = users.getId() + General.createRandomCode();
            users.setForgotToken(token);
            LocalDateTime now = LocalDateTime.now();
            now = now.plusMinutes(10); // 10 minutes valid only
            users.setForgotTokenExpiryTime(now);
            usersRepo.save(users);

            new Thread(() -> this.sendForgotPasswordEmail(emailReqDto, token)).start();
        } else {
            throw new EntityNotFoundException(Constant.Messages.USER_NOT_FOUND);
        }

        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                Constant.Messages.RESET_LINK_EMAIL_SENT, null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPassword(TokenDto tokenDto) {
        Users users = usersRepo.findByForgotToken(tokenDto.getToken())
                .orElseThrow(() -> new EntityNotFoundException(Constant.Messages.TOKEN_EXPIRED));

        if (users.getForgotTokenExpiryTime().isAfter(LocalDateTime.now())) {
            if (users.getLoginType().equals(LOGIN_TYPE_NORMAL)) {
                users.setPassword(General.hashPassword(tokenDto.getPassword()));
            }
            usersRepo.save(users);
        } else {
            throw new EntityNotFoundException(Constant.Messages.TOKEN_EXPIRED);
        }

        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                Constant.Messages.SUCCESS, null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveContactForm(ContactFormReqDto contactFormReqDto) {
        try {
            ContactForm contactForm = mapper.map(contactFormReqDto, ContactForm.class);
            if (contactForm != null) {
                contactForm = contactFormRepo.save(contactForm);
                ContactFormResDto contactFormResDto = mapper.map(contactForm, ContactFormResDto.class);
                return new ResponseEntity<>(new CommonResponse().getResponse(
                        HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, contactFormResDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Not saved , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> savePackagesDetails(PackagesReqDto packagesReqDto) {
        try {
            Packages packages = mapper.map(packagesReqDto, Packages.class);
            if (packages != null) {
                packages = packagesRepo.save(packages);
                PackagesResDto packagesResDto = mapper.map(packages, PackagesResDto.class);
                return new ResponseEntity<>(new CommonResponse().getResponse(
                        HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, packagesResDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Not saved , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllPackagesByScreenName(String screenName) {
        try {
            List<Packages> packagesList = packagesRepo.findByScreenName(screenName);
            if (Objects.nonNull(packagesList) && packagesList.size() > 0) {
                Type targetListType = new TypeToken<List<PackagesResDto>>() {

                }.getType();
                List<PackagesResDto> packagesResDtoList = mapper.map(packagesList, targetListType);
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, packagesResDtoList), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Invalid Screen Name"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> saveUpdateCartDetails(CartDetailsReqDto cartDetailsReqDto, Integer userId) {
        try {
            List<CartPackages> cartPackagesList = new ArrayList<>();
            if (Objects.nonNull(cartDetailsReqDto)) {
               // Double totalAmount = 0.0;
                CartDetails cartDetails = mapper.map(cartDetailsReqDto, CartDetails.class);
                if (Objects.nonNull(cartDetailsReqDto.getPackagesList()) && cartDetailsReqDto.getPackagesList().size() > 0) {
                    Double subTotal = 0.0;
                    for (Integer packagesId : cartDetailsReqDto.getPackagesList()) {
                        CartPackages cartPackages = new CartPackages();
                        cartPackages.setPackageId(packagesId);
                        cartPackagesList.add(cartPackages);
                        Packages packages = packagesRepo.findById(packagesId).orElse(null);
                        subTotal = subTotal + packages.getAmount();
                    }
                    cartDetails.setCartPackagesList(cartPackagesList);
                  //  totalAmount = subTotal + cartDetailsReqDto.getGstAmount();
                  //  if (totalAmount.equals(cartDetails.getOrderTotal())) {
                        cartDetails.setUserId(userId);
                        CartDetails existCartDetails = cartDetailsRepo.findByUserId(userId);
                        if (Objects.nonNull(existCartDetails)) {
                            cartDetailsRepo.deleteById(existCartDetails.getId());
                            cartPackagesRepo.deleteByCartDetailsId(existCartDetails.getId());
                        }
                        cartDetails = cartDetailsRepo.save(cartDetails);
                        CartDetails finalCartDetails = cartDetails;
                        cartPackagesList.stream().forEach(c -> {
                            c.setCartDetailsId(finalCartDetails.getId());
                            cartPackagesRepo.save(c);
                        });
                        return new ResponseEntity<>(new CommonResponse().getResponse(
                                HttpStatus.OK.value(),
                                Constant.Messages.SUCCESS, cartDetails.getId()), HttpStatus.OK);
                   // } else {
                    //    return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    //            Constant.Messages.ERROR, "Total is not same"), HttpStatus.INTERNAL_SERVER_ERROR);
                  //  }
                }
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Not saved , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                Constant.Messages.ERROR, "Not saved , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getCartDetailsByUserId(Integer userId) {
        try {
            CartDetails existingCartDetails = cartDetailsRepo.findByUserId(userId);
            if (existingCartDetails != null) {
                CartDetailsResDto cartDetailsResDto = new CartDetailsResDto();
                cartDetailsResDto.setGstAmount(existingCartDetails.getGstAmount());
                cartDetailsResDto.setOrderTotal(existingCartDetails.getOrderTotal());
                cartDetailsResDto.setUserId(existingCartDetails.getUserId());
                cartDetailsResDto.setSubTotal(existingCartDetails.getSubTotal());
                List<PackagesResDto> packageList = new ArrayList<>();
                for (CartPackages cartPackages : existingCartDetails.getCartPackagesList()) {
                    Packages packages = packagesRepo.findById(cartPackages.getPackageId()).orElse(null);
                    PackagesResDto packagesResDto = mapper.map(packages, PackagesResDto.class);
                    packageList.add(packagesResDto);
                }
                cartDetailsResDto.setPackagesList(packageList);
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, cartDetailsResDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Empty cart"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> saveOrders(OrderDetailsReqDto orderDetailsReqDto, Integer userId) {
        try {
            List<OrderPackages> orderPackagesList = new ArrayList<>();
            if (Objects.nonNull(orderDetailsReqDto)) {
            //    Double totalAmount = 0.0;
                OrderDetails orderDetails = mapper.map(orderDetailsReqDto, OrderDetails.class);
                if (Objects.nonNull(orderDetailsReqDto.getPackagesList()) && orderDetailsReqDto.getPackagesList().size() > 0) {
                    Double subTotal = 0.0;
                    for (Integer packagesId : orderDetailsReqDto.getPackagesList()) {
                        OrderPackages orderPackages = new OrderPackages();
                        orderPackages.setPackageId(packagesId);
                        orderPackagesList.add(orderPackages);
                        Packages packages = packagesRepo.findById(packagesId).orElse(null);
                        subTotal = subTotal + packages.getAmount();
                    }
                    orderDetails.setOrderPackagesList(orderPackagesList);
               //     totalAmount = subTotal + orderDetailsReqDto.getGstAmount();
                //    if (totalAmount.equals(orderDetails.getOrderTotal())) {
                        orderDetails.setUserId(userId);
                      /*  CartDetails existCartDetails = cartDetailsRepo.findByUserId(userId);
                        if (Objects.nonNull(existCartDetails)) {
                            cartDetailsRepo.deleteById(existCartDetails.getId());
                            cartPackagesRepo.deleteByCartDetailsId(existCartDetails.getId());
                        }*/
                        orderDetails = orderDetailsRepo.save(orderDetails);
                        OrderDetails finalOrderDetails = orderDetails;
                        orderPackagesList.stream().forEach(c -> {
                            c.setOrderDetailsId(finalOrderDetails.getId());
                            orderPackagesRepo.save(c);
                        });
                        return new ResponseEntity<>(new CommonResponse().getResponse(
                                HttpStatus.OK.value(),
                                Constant.Messages.SUCCESS, orderDetails.getId()), HttpStatus.OK);
                 //   } else {
                 //       return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                 //               Constant.Messages.ERROR, "Total is not same"), HttpStatus.INTERNAL_SERVER_ERROR);
                 //   }
                }
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Not saved , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                Constant.Messages.ERROR, "Not saved , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getOrderDetailsByUserId(Integer userId) {
        try {
            OrderDetails existingOrderDetails = orderDetailsRepo.findByUserId(userId);
            if (existingOrderDetails != null) {
                CartDetailsResDto cartDetailsResDto = new CartDetailsResDto();
                cartDetailsResDto.setGstAmount(existingOrderDetails.getGstAmount());
                cartDetailsResDto.setOrderTotal(existingOrderDetails.getOrderTotal());
                cartDetailsResDto.setUserId(existingOrderDetails.getUserId());
                cartDetailsResDto.setSubTotal(existingOrderDetails.getSubTotal());
                List<PackagesResDto> packageList = new ArrayList<>();
                for (OrderPackages orderPackages : existingOrderDetails.getOrderPackagesList()) {
                    Packages packages = packagesRepo.findById(orderPackages.getPackageId()).orElse(null);
                    PackagesResDto packagesResDto = mapper.map(packages, PackagesResDto.class);
                    packageList.add(packagesResDto);
                }
                cartDetailsResDto.setPackagesList(packageList);
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, cartDetailsResDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Empty cart"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendForgotPasswordEmail(EmailReqDto emailReqDto, String token) {
        //send mail code here
        Mail mail = new Mail();
        mail.setFrom(Constant.Mail.SENDER);
        mail.setTo(emailReqDto.getUsername());
        mail.setSubject(Constant.Mail.FORGOT_PASSWORD_MAIL);

        HashMap<String, Object> data = new HashMap<>();
        data.put("resetUrl", emailReqDto.getResetUrl() + "?token=" + token);

        mail.setModel(data);

        emailService.sendForgotPasswordMail(mail);
    }

}
