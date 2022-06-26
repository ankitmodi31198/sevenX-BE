package com.backend.sevenX.service;

import com.backend.sevenX.data.dto.requestDto.CoFounderFilterDto;
import com.backend.sevenX.data.dto.requestDto.ContactFilterDto;
import com.backend.sevenX.data.dto.requestDto.FAQReqDto;
import com.backend.sevenX.data.dto.requestDto.StartUpIdeaFilterDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminService {

	ResponseEntity<?> addFaq(FAQReqDto faqReqDto);

	ResponseEntity<?> updateFAQById(FAQReqDto faqReqDto, Integer id);

	ResponseEntity<?> deleteFAQById(Integer id);

	ResponseEntity<?> getAllContactFormDetails(ContactFilterDto contactFilterDto);

    ResponseEntity<?> getAllStartupIdeaList(StartUpIdeaFilterDto startUpIdeaFilterDto);

	ResponseEntity<?> getAllCoFounderList(CoFounderFilterDto coFounderFilterDto);
}
