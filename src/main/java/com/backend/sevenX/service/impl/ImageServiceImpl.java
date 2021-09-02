package com.backend.sevenX.service.impl;

import com.backend.sevenX.config.CommonResponse;
import com.backend.sevenX.data.dto.responseDto.DocumentResDto;
import com.backend.sevenX.data.dto.responseDto.LoginResponseDto;
import com.backend.sevenX.data.model.Document;
import com.backend.sevenX.data.model.Users;
import com.backend.sevenX.repository.DocumentRepo;
import com.backend.sevenX.service.ImageService;
import com.backend.sevenX.utills.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	private DocumentRepo documentRepo;

	private ModelMapper mapper = new ModelMapper();

	public String storeUploadedFile(MultipartFile file) throws IOException {

		String uploadDir = Constant.ImageFolders.Documents;
		File dir = new File(uploadDir);
		boolean isCreated = dir.mkdirs();
		Path rootLocation = Paths.get(dir.getAbsolutePath());

		System.out.println("rootLocation  ==  " + rootLocation);

		String nameExtension[] = file.getContentType().split("/");

		String fileName = "Documents_" + System.currentTimeMillis() + "." + nameExtension[1];

		System.out.println("fileName  :: " + fileName);

		long copy = Files.copy(file.getInputStream(), rootLocation.resolve(fileName));
		File csvFile = new File(dir, fileName);
		System.out.println(">> is file copied... " + csvFile.isFile());
		return getImageUrl(fileName);
	}

	public String getImageUrl(String fileName){
		String currentDirectory = System.getProperty("user.dir") ;
		String path = currentDirectory.concat("/").concat(Constant.ImageFolders.Documents + "/" + fileName);
		return path;
	}

	public ResponseEntity<?> getImage(String image) {
		try {
			if (image != null) {
				File img = new File(Constant.ImageFolders.Documents + "/" + image);
				return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
			}
			File img = new File("https://resize.indiatvnews.com/en/resize/newbucket/620_-/2020/07/bihar-floods-pti-final-1595349363.jpg");
			return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.ok("");
		}
	}

	@Override
	public ResponseEntity<?> saveDocumentByUserId(Document document) {
		try {
			if (Objects.nonNull(document.getUserId())) {
				Document documentObj = documentRepo.save(document);
				DocumentResDto responseDto = mapper.map(documentObj, DocumentResDto.class);
				return new ResponseEntity<>(new CommonResponse().getResponse(
					HttpStatus.OK.value(),
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


}

