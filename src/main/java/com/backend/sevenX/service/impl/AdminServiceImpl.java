package com.backend.sevenX.service.impl;

import com.backend.sevenX.config.CommonResponse;
import com.backend.sevenX.data.dto.requestDto.ContactFilterDto;
import com.backend.sevenX.data.dto.requestDto.FAQReqDto;
import com.backend.sevenX.data.dto.responseDto.ContactFormResDto;
import com.backend.sevenX.data.dto.responseDto.ListContactFormResDto;
import com.backend.sevenX.data.dto.responseDto.OrderDetailsResDto;
import com.backend.sevenX.data.model.ContactForm;
import com.backend.sevenX.data.model.FAQ;
import com.backend.sevenX.data.model.OrderDetails;
import com.backend.sevenX.repository.ContactFormRepo;
import com.backend.sevenX.repository.FaqRepo;
import com.backend.sevenX.service.AdminService;
import com.backend.sevenX.utills.Constant;
import com.backend.sevenX.utills.General;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private FaqRepo faqRepository;

	private ModelMapper mapper = new ModelMapper();

	@PersistenceContext
	protected EntityManager entityManager;

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
	public ResponseEntity<?> getAllContactFormDetails(ContactFilterDto contactFilterDto) {
		try {
			Map<String, Object> param = new HashMap<>();
			StringBuilder queryBuilder = new StringBuilder("SELECT * from contact_form c ");
			queryBuilder.append("WHERE deleted_at IS NULL");
			if(General.nonNullNonEmpty(contactFilterDto.getName())) {
				queryBuilder.append(" AND c.name in (:name)");
				param.put("name", contactFilterDto.getName());
			}
			if(General.nonNullNonEmpty(contactFilterDto.getEmail())) {
				queryBuilder.append(" AND c.email in (:email)");
				param.put("email", contactFilterDto.getEmail());
			}
			if(General.nonNullNonEmpty(contactFilterDto.getContactNo())) {
				queryBuilder.append(" AND c.contact_no in (:contact_no)");
				param.put("contact_no", contactFilterDto.getContactNo());
			}
			if(General.nonNullNonEmpty(contactFilterDto.getState())) {
				queryBuilder.append(" AND c.state in (:state)");
				param.put("state", contactFilterDto.getState());
			}
			if(General.nonNullNonEmpty(contactFilterDto.getScreenName())) {
				queryBuilder.append(" AND c.screen_name in (:screen_name)");
				param.put("screen_name", contactFilterDto.getScreenName());
			}
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (Objects.nonNull(contactFilterDto.getFromDate())) {
				Date from = new Date(contactFilterDto.getFromDate());
				queryBuilder.append(" AND c.created_at > '");
				queryBuilder.append(df.format(from));
				queryBuilder.append("'");
			}
			if(Objects.nonNull(contactFilterDto.getToDate())){
				Date to = new Date(contactFilterDto.getToDate());
				queryBuilder.append(" AND c.created_at < '");
				queryBuilder.append(df.format(to));
				queryBuilder.append("'");
			}
			if(General.nonNullNonEmpty(contactFilterDto.getSortField()) && General.nonNullNonEmpty(contactFilterDto.getSortOrder())) {
				queryBuilder.append(" ORDER BY ");
				queryBuilder.append(contactFilterDto.getSortField() + " " + contactFilterDto.getSortOrder());
			}
			if(Objects.nonNull(contactFilterDto.getLimit())) {
				queryBuilder.append(String.format(" LIMIT %s ", contactFilterDto.getLimit()));
			}
			if(Objects.nonNull(contactFilterDto.getOffset())) {
				queryBuilder.append(String.format(" OFFSET %s", contactFilterDto.getOffset()));
			}

			try {
				Query query = entityManager.createNativeQuery(queryBuilder.toString(), ContactForm.class);
				setParameters(query,param);
				List<ContactForm> contactFormList = query.getResultList();
				Type targetListType = new TypeToken<List<ContactFormResDto>>() {
				}.getType();
				List<ContactFormResDto> contactFormResDtoList = mapper.map(contactFormList, targetListType);
				ListContactFormResDto listContactFormResDto = new ListContactFormResDto();
				listContactFormResDto.setContactList(contactFormResDtoList);
				listContactFormResDto.setTotalContactsCount(contactFormRepo.countRecords());
				return new ResponseEntity<>(new CommonResponse().getResponse(HttpStatus.OK.value(),
						Constant.Messages.SUCCESS, listContactFormResDto), HttpStatus.OK);
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

}
