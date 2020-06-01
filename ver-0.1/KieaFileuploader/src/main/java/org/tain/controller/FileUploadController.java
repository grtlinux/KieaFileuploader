package org.tain.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tain.service.StorageService;

@Controller
public class FileUploadController {

	@Autowired
	private StorageService storageService;
	
	// uploadForm.html
	@GetMapping("/")
	public String list(Model model) throws Exception {
		List<String> list = this.storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(
						FileUploadController.class
						, "serveFile"
						, path.getFileName().toString()
				).build().toUri().toString()
		).collect(Collectors.toList());
		model.addAttribute("files", list);
		return "uploadForm";
	}
	
	// upload
	@PostMapping("/")
	public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
		this.storageService.store(file);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded [" + file.getOriginalFilename() + "] !");
		return "redirect:/";
	}
	
	// download
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws Exception {
		Resource file = this.storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachement;filename=\"" + file.getFilename() + "\"").body(file);
	}
}
