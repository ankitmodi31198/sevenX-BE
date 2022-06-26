package com.backend.sevenX.controller;

import com.backend.sevenX.data.dto.requestDto.CoFounderFilterDto;
import com.backend.sevenX.data.dto.requestDto.ContactFilterDto;
import com.backend.sevenX.data.dto.requestDto.FAQReqDto;
import com.backend.sevenX.data.dto.requestDto.StartUpIdeaFilterDto;
import com.backend.sevenX.service.AdminService;
import com.backend.sevenX.utills.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping(Constant.EndPoints.ADDFAQ)
	public ResponseEntity<?> addFaq(@RequestBody FAQReqDto faqReqDto) throws Exception {
		return adminService.addFaq(faqReqDto);
	}

	@PutMapping(Constant.EndPoints.FAQ)
	public ResponseEntity<?> updateFAQ(@RequestBody FAQReqDto faqReqDto, @PathVariable(required = true) Integer id) throws Exception {
		return adminService.updateFAQById(faqReqDto, id);
	}

	@DeleteMapping(Constant.EndPoints.FAQ)
	public ResponseEntity<?> deleteFAQById(@PathVariable(required = true) Integer id) {
		return adminService.deleteFAQById(id);
	}

	@PostMapping(Constant.EndPoints.ALLCONTACT)
	public ResponseEntity<?> getAllContactFormDetails(@RequestBody ContactFilterDto contactFilterDto) throws Exception {
		return adminService.getAllContactFormDetails(contactFilterDto);
	}

	@PostMapping(Constant.EndPoints.STARTUP_IDEA_LIST)
	public ResponseEntity<?> getAllStartupIdeaList(@RequestBody StartUpIdeaFilterDto startUpIdeaFilterDto) throws Exception {
		return adminService.getAllStartupIdeaList(startUpIdeaFilterDto);
	}

	@PostMapping(Constant.EndPoints.CO_FOUNDER_LIST)
	public ResponseEntity<?> getAllCoFounderList(@RequestBody CoFounderFilterDto coFounderFilterDto) throws Exception {
		return adminService.getAllCoFounderList(coFounderFilterDto);
	}

}
