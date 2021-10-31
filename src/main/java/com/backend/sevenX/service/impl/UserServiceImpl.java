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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
            if (General.nonNullNonEmpty(signUpDto.getNewPassword()) &&
                    General.checkPassword(signUpDto.getPassword(), users.getPassword())) {
                users.setPassword(General.hashPassword(signUpDto.getNewPassword()));
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Password is not same , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
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

        if (Strings.isNotEmpty(signUpDto.getState())) {
            users.setState(signUpDto.getState());
        }

        if (Strings.isNotEmpty(signUpDto.getPanNumber())) {
            users.setPanNumber(signUpDto.getPanNumber());
        }

        if (Strings.isNotEmpty(signUpDto.getGstNumber())) {
            users.setGstNumber(signUpDto.getGstNumber());
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
    public ResponseEntity<?> getAllPackagesByScreenNameList(PackagesListReqDto packagesListReqDto) {
        try {
            List<Packages> packagesList = packagesRepo.findByScreenNameList(packagesListReqDto.getScreenNameList());
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
            CartDetails existCartDetails = cartDetailsRepo.findByUserId(userId);
            CartDetails cartDetails = new CartDetails();
            if (Objects.nonNull(cartDetailsReqDto) && Objects.nonNull(cartDetailsReqDto.getPackageId())) {
                CartPackages cartPackages = new CartPackages();
                if (Objects.nonNull(existCartDetails)) {
                    CartPackages existPackage = cartPackagesRepo.findByCartDetailsIdAndPackageId(existCartDetails.getId(), cartDetailsReqDto.getPackageId());
                    if (existPackage != null) {
                        cartPackages = existPackage;
                        cartPackages.setCartDetailsId(null);
                        cartPackages.setQty(existPackage.getQty() + 1);
                        cartPackages.setCartDetailsId(existCartDetails.getId());
                        cartPackagesRepo.save(cartPackages);
                        cartDetails = existCartDetails;
                        cartDetails = cartDetailsRepo.save(cartDetails);
                    } else {
                        cartDetails = existCartDetails;
                        cartPackages.setPackageId(cartDetailsReqDto.getPackageId());
                        cartPackages.setCartDetailsId(existCartDetails.getId());
                        cartPackagesList.add(cartPackages);
                        cartDetails.getCartPackagesList().add(cartPackages);
                        cartDetails.setCartPackagesList(cartDetails.getCartPackagesList());
                        cartDetails = cartDetailsRepo.save(cartDetails);
                    }
                } else {
                    cartPackages.setPackageId(cartDetailsReqDto.getPackageId());
                    cartPackagesList.add(cartPackages);
                    cartDetails.setCartPackagesList(cartPackagesList);
                    cartDetails.setUserId(userId);
                    cartDetails = cartDetailsRepo.save(cartDetails);
                    CartDetails finalCartDetails = cartDetails;
                    cartPackagesList.stream().forEach(c -> {
                        c.setCartDetailsId(finalCartDetails.getId());
                        cartPackagesRepo.save(c);
                    });
                }
                return new ResponseEntity<>(new CommonResponse().getResponse(
                        HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, cartDetails.getId()), HttpStatus.OK);
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
    public ResponseEntity<?> getCartDetailsByUserId(Integer userId) {
        try {
            CartDetails existingCartDetails = cartDetailsRepo.findByUserId(userId);
            if (existingCartDetails != null) {
                CartDetails cartDetails = reCalculateOrderTotal(userId);
                CartDetailsResDto cartDetailsResDto = new CartDetailsResDto();
                // cartDetailsResDto.setGstAmount(cartDetails.getGstAmount());
                cartDetailsResDto.setOrderTotal(cartDetails.getOrderTotal());
                cartDetailsResDto.setUserId(cartDetails.getUserId());
                cartDetailsResDto.setSubTotal(cartDetails.getSubTotal());
                List<PackagesResDto> packageList = new ArrayList<>();
                for (CartPackages cartPackages : existingCartDetails.getCartPackagesList()) {
                    Packages packages = packagesRepo.findById(cartPackages.getPackageId()).orElse(null);
                    PackagesResDto packagesResDto = mapper.map(packages, PackagesResDto.class);
                    packagesResDto.setQty(cartPackages.getQty());
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

    public CartDetails reCalculateOrderTotal(Integer userId) {
        CartDetails existingCartDetails = cartDetailsRepo.findByUserId(userId);
        AtomicReference<Double> subTotal = new AtomicReference<>(0.0);
        existingCartDetails.getCartPackagesList().stream().forEach(c -> {
            Packages packages = packagesRepo.findById(c.getPackageId()).orElse(null);
            subTotal.set(subTotal.get() + packages.getAmount() * c.getQty());
        });
        existingCartDetails.setSubTotal(subTotal.get());
        //  existingCartDetails.setGstAmount(existingCartDetails.getSubTotal() * 0.05);
        existingCartDetails.setOrderTotal(existingCartDetails.getSubTotal());
        existingCartDetails.setId(existingCartDetails.getId());
        CartDetails cartDetails = cartDetailsRepo.save(existingCartDetails);
        return cartDetails;
    }

    @Override
    public ResponseEntity<?> saveOrders(Integer userId, SaveOrderDetailsReqDto saveOrderDetailsReqDto) {
        try {
            CartDetails existCartDetails = cartDetailsRepo.findByUserId(userId);
            if (Objects.nonNull(existCartDetails)) {
                List<OrderPackages> orderPackagesList = new ArrayList<>();
                existCartDetails.getCartPackagesList().stream().forEach(c -> {
                    Packages packages = packagesRepo.findById(c.getPackageId()).orElse(null);
                    if (packages != null) {
                        OrderPackages orderPackages = new OrderPackages();
                        orderPackages.setPackageAmount(packages.getAmount());
                        orderPackages.setFinalPackageAmount(c.getQty() * packages.getAmount());
                        orderPackages.setPackageId(c.getPackageId());
                        orderPackages.setQty(c.getQty());
                        orderPackagesList.add(orderPackages);
                    }
                });
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setUserId(userId);
                //  orderDetails.setGstAmount(existCartDetails.getGstAmount());
                orderDetails.setSubTotal(existCartDetails.getSubTotal());
                orderDetails.setOrderTotal(existCartDetails.getOrderTotal());
                orderDetails.setFinalOrderTotal(existCartDetails.getOrderTotal());
                orderDetails.setOrderPackagesList(orderPackagesList);
                orderDetails = orderDetailsRepo.save(orderDetails);
                OrderDetails finalCartDetails = orderDetails;
                orderDetails.getOrderPackagesList().stream().forEach(orderPackages -> {
                    orderPackages.setOrderDetailsId(finalCartDetails.getId());
                    orderPackagesRepo.save(orderPackages);
                });
                orderDetails.setUsername(saveOrderDetailsReqDto.getUsername());
                orderDetails.setFirstName(saveOrderDetailsReqDto.getFirstName());
                orderDetails.setAddress(saveOrderDetailsReqDto.getAddress());
                orderDetails.setState(saveOrderDetailsReqDto.getState());
                orderDetails.setGstNumber(saveOrderDetailsReqDto.getGstNumber());
                orderDetails.setPanNumber(saveOrderDetailsReqDto.getPanNumber());
                orderDetails.setPhoneNo(saveOrderDetailsReqDto.getPhoneNo());
                if (orderDetails.getId() != null) {
                    cartDetailsRepo.delete(existCartDetails);
                    OrderDetailsResDto orderDetailsResDto = mapper.map(orderDetails, OrderDetailsResDto.class);
                    List<PackagesResDto> packageList = new ArrayList<>();
                    for (OrderPackages orderPackages : orderDetails.getOrderPackagesList()) {
                        Packages packages = packagesRepo.findById(orderPackages.getPackageId()).orElse(null);
                        PackagesResDto packagesResDto = mapper.map(packages, PackagesResDto.class);
                        packagesResDto.setQty(orderPackages.getQty());
                        packagesResDto.setFinalPackageAmount(orderPackages.getFinalPackageAmount());
                        packageList.add(packagesResDto);
                    }
                    orderDetailsResDto.setPackagesList(packageList);
                    return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                            Constant.Messages.SUCCESS, orderDetailsResDto), HttpStatus.OK);
                }
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        Constant.Messages.ERROR, "order not saved"), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Empty Cart"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateOrders(OrderDetailsReqDto orderDetailsReqDto) {
        try {
            OrderDetails existOrderDetails = orderDetailsRepo.findById(orderDetailsReqDto.getOrderId()).orElse(null);
            if (Objects.nonNull(existOrderDetails)) {
              /*  existOrderDetails.setAdditionalOrderCost(orderDetailsReqDto.getAdditionalCost());
                if (Objects.nonNull(orderDetailsReqDto.getOrderPackageList()) && orderDetailsReqDto.getOrderPackageList().size() > 0) {
                    for (OrderPackageReqDto orderPackageReqDto : orderDetailsReqDto.getOrderPackageList()) {
                        OrderPackages existPackage = existOrderDetails.getOrderPackagesList().stream().filter(
                                p -> p.getId().equals(orderPackageReqDto.getOrderPackageId())
                        ).findAny().orElse(null);
                        if (existPackage != null) {
                            existPackage.setAdditionalCost(orderPackageReqDto.getAdditionalCost());
                            existPackage.setFinalPackageAmount(existPackage.getFinalPackageAmount() + existPackage.getAdditionalCost());
                            orderPackagesRepo.save(existPackage);
                        }
                    }
                }
                AtomicReference<Double> finalOrderTotalCal = new AtomicReference<>(existOrderDetails.getFinalOrderTotal());
                existOrderDetails.getOrderPackagesList().stream().forEach(orderPackages -> {
                    finalOrderTotalCal.set(finalOrderTotalCal.get() + orderPackages.getFinalPackageAmount());
                });*/
                existOrderDetails.setFinalOrderTotal(orderDetailsReqDto.getFinalOrderTotalAmount());
                existOrderDetails.setOrderStatus(Constant.Status.Approved);
                existOrderDetails.setNote(orderDetailsReqDto.getNote());
                orderDetailsRepo.save(existOrderDetails);
            }
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                    Constant.Messages.SUCCESS, "Saved"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public OrderDetails reCalculateOrderTotalForOrder(Integer orderId) {
        OrderDetails existOrderDetails = orderDetailsRepo.findById(orderId).orElse(null);
        AtomicReference<Double> subTotal = new AtomicReference<>(0.0);
        existOrderDetails.getOrderPackagesList().stream().forEach(c -> {
            subTotal.set(subTotal.get() + c.getPackageAmount() * c.getQty());
        });
        existOrderDetails.setSubTotal(subTotal.get());
        // existOrderDetails.setGstAmount(existOrderDetails.getSubTotal() * 0.05);
        existOrderDetails.setOrderTotal(existOrderDetails.getSubTotal());
        existOrderDetails.setId(existOrderDetails.getId());
        OrderDetails orderDetails = orderDetailsRepo.save(existOrderDetails);
        return orderDetails;
    }

    @Override
    public ResponseEntity<?> getOrderDetailsByUserId(Integer userId) {
        try {
            List<OrderDetails> existingOrderDetailsList = orderDetailsRepo.findByUserId(userId);
            if (existingOrderDetailsList != null && existingOrderDetailsList.size() > 0) {
                List<OrderDetailsResDto> orderDetailsResDtoList = new ArrayList<>();
                for (int i = 0; i < existingOrderDetailsList.size(); i++) {
                    OrderDetails existingOrderDetails = existingOrderDetailsList.get(i);
                    OrderDetails orderDetails = reCalculateOrderTotalForOrder(existingOrderDetails.getId());
                    OrderDetailsResDto orderDetailsResDto = new OrderDetailsResDto();
                    //  orderDetailsResDto.setGstAmount(orderDetails.getGstAmount());
                    orderDetailsResDto.setOrderTotal(existingOrderDetails.getOrderTotal());
                    orderDetailsResDto.setUserId(orderDetails.getUserId());
                    orderDetailsResDto.setSubTotal(orderDetails.getSubTotal());
                    orderDetailsResDto.setId(existingOrderDetails.getId());
                    orderDetailsResDto.setCreatedAt(existingOrderDetails.getCreatedAt().toString());
                    orderDetailsResDto.setUpdatedAt(existingOrderDetails.getUpdatedAt().toString());
                    orderDetailsResDto.setUsername(existingOrderDetails.getUsername());
                    orderDetailsResDto.setFirstName(existingOrderDetails.getFirstName());
                    orderDetailsResDto.setAddress(existingOrderDetails.getAddress());
                    orderDetailsResDto.setState(existingOrderDetails.getState());
                    orderDetailsResDto.setGstNumber(existingOrderDetails.getGstNumber());
                    orderDetailsResDto.setPanNumber(existingOrderDetails.getPanNumber());
                    orderDetailsResDto.setPhoneNo(existingOrderDetails.getPhoneNo());
                    List<PackagesResDto> packageList = new ArrayList<>();
                    for (OrderPackages orderPackages : existingOrderDetails.getOrderPackagesList()) {
                        Packages packages = packagesRepo.findById(orderPackages.getPackageId()).orElse(null);
                        PackagesResDto packagesResDto = mapper.map(packages, PackagesResDto.class);
                        packagesResDto.setQty(orderPackages.getQty());
                        packagesResDto.setFinalPackageAmount(orderPackages.getFinalPackageAmount());
                        packageList.add(packagesResDto);
                    }
                    orderDetailsResDto.setPackagesList(packageList);
                    orderDetailsResDtoList.add(orderDetailsResDto);
                }
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, orderDetailsResDtoList), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "No Existing Order"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> removePackageCart(PackageIdReqDto packageIdReqDto, Integer userId) {
        try {
            CartDetails existCart = cartDetailsRepo.findByUserId(userId);
            if (Objects.nonNull(existCart)) {
                CartPackages cartPackages = existCart.getCartPackagesList().stream().filter(
                        p -> p.getPackageId().equals(packageIdReqDto.getPackageId())
                ).findAny().orElse(null);
                if (cartPackages == null) {
                    return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                            Constant.Messages.ERROR, "Invalid Package Id"), HttpStatus.NOT_FOUND);
                } else {
                    cartPackages.setDeletedAt(LocalDateTime.now());
                    cartPackagesRepo.save(cartPackages);
                    List<CartPackages> cartPackagesList = existCart.getCartPackagesList().stream().filter(
                            p -> !p.getPackageId().equals(packageIdReqDto.getPackageId())
                    ).collect(Collectors.toList());
                    if (cartPackagesList.size() == 0) {
                        cartDetailsRepo.delete(existCart);
                    }
                    return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                            Constant.Messages.SUCCESS, "Remove Package"), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Invalid Package Id"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> inDePackage(PackageIdReqDto packageIdReqDto, Integer userId) {
        try {
            CartDetails existCart = cartDetailsRepo.findByUserId(userId);
            if (Objects.nonNull(existCart)) {
                CartPackages cartPackages = existCart.getCartPackagesList().stream().filter(
                        p -> p.getPackageId().equals(packageIdReqDto.getPackageId())
                ).findAny().orElse(null);
                if (cartPackages == null) {
                    return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                            Constant.Messages.ERROR, "Invalid Package Id"), HttpStatus.NOT_FOUND);
                } else {
                    if (packageIdReqDto.isIncrease()) {
                        cartPackages.setQty(cartPackages.getQty() + 1);
                        cartPackagesRepo.save(cartPackages);
                    } else {
                        if (cartPackages.getQty() > 1) {
                            cartPackages.setQty(cartPackages.getQty() - 1);
                            cartPackagesRepo.save(cartPackages);
                        } else {
                            cartPackagesRepo.delete(cartPackages);
                            List<CartPackages> cartPackagesList = existCart.getCartPackagesList().stream().filter(
                                    p -> !p.getPackageId().equals(packageIdReqDto.getPackageId())
                            ).collect(Collectors.toList());
                            if (cartPackagesList.size() == 0) {
                                cartDetailsRepo.delete(existCart);
                            }
                        }
                    }
                }
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, "Plus Minus Qty"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Invalid Package Id"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllOrderByFilter(OrderFilterDto orderFilterDto) {
        try {
            Map<String, Object> param = new HashMap<>();
            StringBuilder queryBuilder = new StringBuilder("SELECT * from order_details od ");
            queryBuilder.append("WHERE deleted_at IS NULL");
            if (General.nonNullNonEmpty(orderFilterDto.getUsername())) {
                queryBuilder.append(" AND od.user_name in (:user_name)");
                param.put("user_name", orderFilterDto.getUsername());
            }
            if (General.nonNullNonEmpty(orderFilterDto.getFirstName())) {
                queryBuilder.append(" AND od.first_name in (:first_name)");
                param.put("first_name", orderFilterDto.getFirstName());
            }
            if (General.nonNullNonEmpty(orderFilterDto.getPhoneNo())) {
                queryBuilder.append(" AND od.phone_no in (:phone_no)");
                param.put("phone_no", orderFilterDto.getPhoneNo());
            }
            if (General.nonNullNonEmpty(orderFilterDto.getOrderStatus())) {
                queryBuilder.append(" AND od.order_status in (:order_status)");
                param.put("order_status", orderFilterDto.getOrderStatus());
            }
            if (General.nonNullNonEmpty(orderFilterDto.getTransactionStatus())) {
                queryBuilder.append(" AND od.transaction_status in (:transaction_status)");
                param.put("transaction_status", orderFilterDto.getTransactionStatus());
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Objects.nonNull(orderFilterDto.getFromDate())) {
                Date from = new Date(orderFilterDto.getFromDate());
                queryBuilder.append(" AND od.created_at > '");
                queryBuilder.append(df.format(from));
                queryBuilder.append("'");
            }
            if(Objects.nonNull(orderFilterDto.getToDate())){
                Date to = new Date(orderFilterDto.getToDate());
                queryBuilder.append(" AND od.created_at < '");
                queryBuilder.append(df.format(to));
                queryBuilder.append("'");
            }
            if (General.nonNullNonEmpty(orderFilterDto.getSortField()) && General.nonNullNonEmpty(orderFilterDto.getSortOrder())) {
                queryBuilder.append(" ORDER BY ");
                queryBuilder.append(orderFilterDto.getSortField() + " " + orderFilterDto.getSortOrder());
            }
            if (Objects.nonNull(orderFilterDto.getLimit())) {
                queryBuilder.append(String.format(" LIMIT %s ", orderFilterDto.getLimit()));
            }
            if (Objects.nonNull(orderFilterDto.getOffset())) {
                queryBuilder.append(String.format(" OFFSET %s", orderFilterDto.getOffset()));
            }
            try {
                Query query = entityManager.createNativeQuery(queryBuilder.toString(), OrderDetails.class);
                setParameters(query, param);
                List<OrderDetails> orderDetailsList = query.getResultList();
                Type targetListType = new TypeToken<List<OrderDetailsResDto>>() {
                }.getType();
                List<OrderDetailsResDto> orderDetailsResDtoList = mapper.map(orderDetailsList, targetListType);
                ListOrderDetailsResDto listOrderDetailsResDto = new ListOrderDetailsResDto();
                listOrderDetailsResDto.setOrderList(orderDetailsResDtoList);
                listOrderDetailsResDto.setTotalOrdersCount(orderDetailsRepo.countRecords());
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, listOrderDetailsResDto), HttpStatus.OK);
            } catch (Exception e) {
                throw new Exception("Error in query");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setParameters(Query query, Map<String, Object> params) {
        if (!params.isEmpty()) {
            params.entrySet().stream().forEach(entrySet -> {
                query.setParameter(entrySet.getKey(), entrySet.getValue());
            });
        }
    }

    @Override
    public ResponseEntity<?> getOrderDetailsByOrderId(Integer orderId) {
        try {
            Optional<OrderDetails> existingOrderDetailsList = orderDetailsRepo.findById(orderId);
            if (existingOrderDetailsList.isPresent()) {
                OrderDetails existingOrderDetails = existingOrderDetailsList.get();
                OrderDetailsResDto orderDetailsResDto = mapper.map(existingOrderDetails, OrderDetailsResDto.class);
                List<PackagesResDto> packageList = new ArrayList<>();
                for (OrderPackages orderPackages : existingOrderDetails.getOrderPackagesList()) {
                    Packages packages = packagesRepo.findById(orderPackages.getPackageId()).orElse(null);
                    PackagesResDto packagesResDto = mapper.map(packages, PackagesResDto.class);
                    packagesResDto.setQty(orderPackages.getQty());
                    packagesResDto.setFinalPackageAmount(orderPackages.getFinalPackageAmount());
                    packageList.add(packagesResDto);
                }
                orderDetailsResDto.setPackagesList(packageList);
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
                        Constant.Messages.SUCCESS, orderDetailsResDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
                        Constant.Messages.ERROR, "Invalid Order Id"), HttpStatus.NOT_FOUND);
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
