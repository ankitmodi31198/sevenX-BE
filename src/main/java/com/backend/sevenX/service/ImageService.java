package com.backend.sevenX.service;

import com.backend.sevenX.data.model.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

	String storeUploadedFile(MultipartFile file) throws IOException;

	String getImageUrl(String fileName);

	ResponseEntity<?> getImage(String image);

	ResponseEntity<?> saveDocumentByUserId(Document document);

}
