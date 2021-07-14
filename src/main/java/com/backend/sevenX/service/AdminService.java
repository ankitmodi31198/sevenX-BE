package com.backend.sevenX.service;

import com.backend.sevenX.data.dto.requestDto.FAQReqDto;
import org.springframework.http.ResponseEntity;

public interface AdminService {

	ResponseEntity<?> addFaq(FAQReqDto faqReqDto);

	ResponseEntity<?> updateFAQById(FAQReqDto faqReqDto, Integer id);

	ResponseEntity<?> deleteFAQById(Integer id);

}
