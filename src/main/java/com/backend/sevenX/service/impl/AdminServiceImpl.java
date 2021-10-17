package com.backend.sevenX.service.impl;

import com.backend.sevenX.config.CommonResponse;
import com.backend.sevenX.data.dto.requestDto.FAQReqDto;
import com.backend.sevenX.data.dto.responseDto.ContactFormResDto;
import com.backend.sevenX.data.dto.responseDto.FAQResDto;
import com.backend.sevenX.data.model.ContactForm;
import com.backend.sevenX.data.model.FAQ;
import com.backend.sevenX.repository.ContactFormRepo;
import com.backend.sevenX.repository.FaqRepo;
import com.backend.sevenX.service.AdminService;
import com.backend.sevenX.utills.Constant;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private FaqRepo faqRepository;

	private ModelMapper mapper = new ModelMapper();

	@Autowired
	private ContactFormRepo contactFormRepo;

	public ResponseEntity<?> addFaq(FAQReqDto faqReqDto) {
		try {
			FAQ faq = mapper.map(faqReqDto, FAQ.class);
			faq = faqRepository.save(faq);
			if (faq != null)
				return new ResponseEntity<>(new CommonResponse().getResponse(
					HttpStatus.OK.value(),
					Constant.Messages.SUCCESS, "Saved successfully"), HttpStatus.OK);
			else
				return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
					Constant.Messages.ERROR, "Not saved , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				Constant.Messages.ERROR, Constant.Messages.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> updateFAQById(FAQReqDto faqReqDto, Integer id) {
		try {
			FAQ existingFaq = faqRepository.findById(id).orElse(null);
			if (existingFaq != null) {
				FAQ updatedFaqs = mapper.map(faqReqDto, FAQ.class);
				updatedFaqs.setCreatedAt(existingFaq.getCreatedAt());
				updatedFaqs.setId(id);
				updatedFaqs = faqRepository.save(updatedFaqs);
				if (updatedFaqs != null) {
					return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
						Constant.Messages.SUCCESS, "Updated successfully"), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.NOT_FOUND.value(),
						Constant.Messages.ERROR, "Not Updated , try again"), HttpStatus.INTERNAL_SERVER_ERROR);
				}
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

	public ResponseEntity<?> deleteFAQById(Integer id) {
		try {
			FAQ existingFaq = faqRepository.findById(id).orElse(null);
			if (existingFaq != null) {
				faqRepository.deleteById(id);
				return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
					Constant.Messages.SUCCESS, "deleted successfully"), HttpStatus.OK);
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
	public ResponseEntity<?> getAllContactFormDetails() {
		try {
			List<ContactForm> faqList = contactFormRepo.findAll();
			if (Objects.nonNull(faqList) && faqList.size() > 0) {
				Type targetListType = new TypeToken<List<ContactFormResDto>>() {

				}.getType();
				List<ContactFormResDto> contactFormResDtoList = mapper.map(faqList, targetListType);
				return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
						Constant.Messages.SUCCESS, contactFormResDtoList), HttpStatus.OK);
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

}
