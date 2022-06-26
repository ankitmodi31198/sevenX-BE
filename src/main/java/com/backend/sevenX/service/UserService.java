package com.backend.sevenX.service;

import com.backend.sevenX.data.dto.requestDto.*;
import com.backend.sevenX.data.model.Document;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

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

	ResponseEntity<?> getAllPackagesByScreenNameList(PackagesListReqDto packagesListReqDto);

	ResponseEntity<?> saveUpdateCartDetails(CartDetailsReqDto cartDetailsReqDto, Integer userId);

	ResponseEntity<?> getCartDetailsByUserId(Integer userId);

	ResponseEntity<?> saveOrders(Integer userId, SaveOrderDetailsReqDto saveOrderDetailsReqDto);

	ResponseEntity<?> updateOrders(OrderDetailsReqDto orderDetailsReqDto);

	ResponseEntity<?> getOrderDetailsByUserId(Integer userId);

	ResponseEntity<?> removePackageCart(PackageIdReqDto packageIdReqDto, Integer userId);

	ResponseEntity<?> inDePackage(PackageIdReqDto packageIdReqDto, Integer userId);

    ResponseEntity<?> getAllOrderByFilter(OrderFilterDto orderFilterDto);

	ResponseEntity<?> getOrderDetailsByOrderId(Integer orderId);

	ResponseEntity<?> savePayment(OrderDetailsReqDto orderDetailsReqDto);

	ResponseEntity<?> saveStartupIdeaDetails(StartupIdeaFormReqDto startupIdeaFormReqDto, Integer userId);

	ResponseEntity<?> saveCoFounderDetails(CoFounderFormReqDto coFounderFormReqDto, Integer userId);

	ResponseEntity<?> saveStartupIdeaDocuments(List<Document> documentPathList, Integer userId, Integer startupDetailsId);

	ResponseEntity<?> saveCoFounderDocuments(List<Document> documentPathList, Integer userId, Integer coFounderDetailsId);
}
