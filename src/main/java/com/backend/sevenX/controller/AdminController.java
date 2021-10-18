package com.backend.sevenX.controller;

import com.backend.sevenX.data.dto.requestDto.FAQReqDto;
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

	@GetMapping(Constant.EndPoints.CONTACT_FORM)
	public ResponseEntity<?> getAllContactFormDetails(@PageableDefault Pageable pageable) throws Exception {
		return adminService.getAllContactFormDetails(pageable);
	}

}
