package com.backend.sevenX.service;

import com.backend.sevenX.data.dto.requestDto.ContactFilterDto;
import com.backend.sevenX.data.dto.requestDto.FAQReqDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminService {

	ResponseEntity<?> addFaq(FAQReqDto faqReqDto);

	ResponseEntity<?> updateFAQById(FAQReqDto faqReqDto, Integer id);

	ResponseEntity<?> deleteFAQById(Integer id);

	ResponseEntity<?> getAllContactFormDetails(ContactFilterDto contactFilterDto);

}
