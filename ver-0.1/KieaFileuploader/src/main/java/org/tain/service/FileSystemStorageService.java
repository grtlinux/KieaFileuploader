package org.tain.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation = Paths.get("/Users/kangmac/upload-dir");
	
	@Override
	public void init() throws Exception {
		Files.createDirectories(this.rootLocation);
	}

	@Override
	public void store(MultipartFile file) throws Exception {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		
		if (file.isEmpty()) {
			throw new Exception("File not found ...");
		}
		if (filename.contains("..")) {
			throw new Exception("Cannot store file ..." + filename);
		}
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	@Override
	public Stream<Path> loadAll() throws Exception {
		return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
	}

	@Override
	public Resource loadAsResource(String filename) throws Exception {
		Path file = load(filename);
		Resource resource = new UrlResource(file.toUri());
		if (resource.exists() || resource.isReadable()) {
			return resource;
		}
		return null;
	}

	@Override
	public Path load(String filename) {
		return this.rootLocation.resolve(filename);
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(this.rootLocation.toFile());
	}
}
