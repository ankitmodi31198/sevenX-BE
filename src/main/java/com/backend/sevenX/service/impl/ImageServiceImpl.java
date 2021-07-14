package com.backend.sevenX.service.impl;

import com.backend.sevenX.service.ImageService;
import com.backend.sevenX.utills.Constant;
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

@Service
public class ImageServiceImpl implements ImageService {

	public String storeUploadedFile(MultipartFile file) throws IOException {

		String uploadDir = Constant.ImageFolders.Images;
		File dir = new File(uploadDir);
		boolean isCreated = dir.mkdirs();
		Path rootLocation = Paths.get(dir.getAbsolutePath());

		System.out.println("rootLocation  ==  " + rootLocation);

		String nameExtension[] = file.getContentType().split("/");
		String fileName = "IMAGE" + System.currentTimeMillis() + "." + nameExtension[1];

		System.out.println("fileName  :: " + fileName);

		long copy = Files.copy(file.getInputStream(), rootLocation.resolve(fileName));
		File csvFile = new File(dir, fileName);
		System.out.println(">> is file copied... " + csvFile.isFile());
		return getImageUrl(fileName);
	}

	public String getImageUrl(String fileName){
		String currentDirectory = System.getProperty("user.dir") ;
		String path = currentDirectory.concat("/").concat(Constant.ImageFolders.Images + "/" + fileName);
		return path;
	}

	public ResponseEntity<?> getImage(String image) {
		try {
			if (image != null) {
				File img = new File(Constant.ImageFolders.Images + "/" + image);
				return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
			}
			File img = new File("https://resize.indiatvnews.com/en/resize/newbucket/620_-/2020/07/bihar-floods-pti-final-1595349363.jpg");
			return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.ok("");
		}
	}
}

