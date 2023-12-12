package com.fastshop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DocumentController {

	@Value("${krishoop.img.logo}")
	private String uploadDirShoplogo;

	@Value("${krishoop.addproduct.image}")
	private String uploadDirAddProuduct;

	@Value("${krishoop.shopverify.doc}")
	private String uploadDirShopVerify;

	@Value("${krishoop.addAmtByUser.screenshot}")
	private String uploadDirAddAmtByUser;

	@GetMapping("/shoplogo/{imageName}")
	public ResponseEntity<byte[]> getShopLogoImage(@PathVariable String imageName) throws IOException {
		uploadDirShoplogo = uploadDirShoplogo.trim().replaceAll("\\s", "");
		Path imagePath = Paths.get(uploadDirShoplogo, imageName);
		byte[] imageBytes = Files.readAllBytes(imagePath);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
	}

	@GetMapping("/addproductimg/{imageName}")
	public ResponseEntity<byte[]> getProductImage(@PathVariable String imageName) throws IOException {
		uploadDirAddProuduct = uploadDirAddProuduct.trim().replaceAll("\\s", "");
		Path imagePath = Paths.get(uploadDirAddProuduct, imageName);
		byte[] imageBytes = Files.readAllBytes(imagePath);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
	}

	@GetMapping("/shopverify/{imageName}")
	public ResponseEntity<byte[]> getShopVerifyDocs(@PathVariable String imageName) throws IOException {
		uploadDirShopVerify = uploadDirShopVerify.trim().replaceAll("\\s", "");
		Path imagePath = Paths.get(uploadDirShopVerify, imageName);
		byte[] imageBytes = Files.readAllBytes(imagePath);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
	}

	@GetMapping("/addAmtByUser/{imageName}")
	public ResponseEntity<byte[]> getWalletScreenshot(@PathVariable String imageName) throws IOException {
		uploadDirAddAmtByUser = uploadDirAddAmtByUser.trim().replaceAll("\\s", "");
		Path imagePath = Paths.get(uploadDirAddAmtByUser, imageName);
		byte[] imageBytes = Files.readAllBytes(imagePath);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
	}
}
