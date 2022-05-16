package org.tain.restcontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tain.dto.UploadResultDTO;

import net.coobird.thumbnailator.Thumbnailator;

@RestController
public class UploadController {

	//spring.servlet.multipart.location: ~/FILES
	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;
	
	//@RequestMapping(value = "/uploadAjax", method = { RequestMethod.POST })
	@Deprecated
	public void _uploadFile_20220511(MultipartFile[] uploadFiles) {
		for (MultipartFile uploadFile : uploadFiles) {
			if (uploadFile.getContentType().startsWith("image") == false) {
				System.out.println(">>> this file is not image file.");
				return;
			}
			
			String originalName = uploadFile.getOriginalFilename();
			String fileName = originalName.substring(originalName.lastIndexOf('\\') + 1);
			
			System.out.println(">>> uploadPath: " + this.uploadPath);
			System.out.println(">>> originamName: " + originalName);
			System.out.println(">>> fileName: " + fileName);
			
			String folderPath = makeFolder();
			String uuid = UUID.randomUUID().toString();
			String saveName = this.uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
			
			Path savePath = Paths.get(saveName);
			
			try {
				uploadFile.transferTo(savePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value = "/uploadAjax", method = { RequestMethod.POST })
	public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
		
		List<UploadResultDTO> resultDTOList = new ArrayList<>();
		
		for (MultipartFile uploadFile : uploadFiles) {
			// skip the below
			if (Boolean.FALSE && uploadFile.getContentType().startsWith("image") == false) {
				System.out.println(">>> this file is not image file.");
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			String originalName = uploadFile.getOriginalFilename();
			String fileName = originalName.substring(originalName.lastIndexOf('\\') + 1);
			
			System.out.println(">>> uploadPath: " + this.uploadPath);
			System.out.println(">>> originamName: " + originalName);
			System.out.println(">>> fileName: " + fileName);
			
			String folderPath = makeFolder();
			String uuid = UUID.randomUUID().toString();
			String saveName = this.uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
			
			Path savePath = Paths.get(saveName);
			
			try {
				uploadFile.transferTo(savePath);
				resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
				
				FileOutputStream thumbnail = new FileOutputStream(new File(this.uploadPath + File.separator + folderPath, "s_" + uuid + "_" +  fileName));
				Thumbnailator.createThumbnail(uploadFile.getInputStream(), thumbnail, 100, 100);
				thumbnail.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
	}
	
	private String makeFolder() {
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		String folderPath = str.replace("//", File.separator);
		File uploadPathFolder = new File(this.uploadPath, folderPath);
		if (uploadPathFolder.exists() == false) {
			uploadPathFolder.mkdirs();
		}
		return folderPath;
	}

	@RequestMapping(value = "/display", method = { RequestMethod.GET })
	public ResponseEntity<byte[]> display(String fileName) {
		ResponseEntity<byte[]> result = null;
		
		try {
			String srcFileName = URLDecoder.decode(fileName, "utf-8");
			
			System.out.println(">>> uploadPath:" + uploadPath);
			System.out.println(">>> srcFileName:" + srcFileName);
			
			File file = new File(this.uploadPath + File.separator + srcFileName);
			System.out.println(">>> file:" + file);
			
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return result;
	}
}
