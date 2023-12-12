package com.fastshop.controller;

import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastshop.Repo.CartItemRepository;
import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.Repo.ShopVerifcationRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.helper.FileService;
import com.fastshop.model.CartItem;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.ShopVerification;
import com.fastshop.model.User;
import com.fastshop.service.StoreVerificationService;

@Controller
public class StoreVerificationController {
	@Autowired
	StoreVerificationService storeVerificationService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ShopDetailRepo shopDetailRepo;
	@Autowired
	ShopVerifcationRepo shopVerifcationRepo;
	@Autowired
	CartItemRepository cartItemRepo;

	@Value("${krishoop.shopverify.doc}")
	private String uploadDirShopVerify;

	@ModelAttribute
	public void commonMethod(Model model, Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		if (!model.containsAttribute("countCart")) {
			List<CartItem> cartItemList = cartItemRepo.findByUser(user);
			int count = 0;
			for (CartItem cart : cartItemList) {
				count += cart.getQuantity();
			}
			model.addAttribute("countCart", count);
		}

		if (!model.containsAttribute("logoImg")) {
			ShopDetail logoImg = shopDetailRepo.findByUser(user);
			model.addAttribute("logoImg", logoImg);
		}
		if (!model.containsAttribute("username")) {
			model.addAttribute("username", principal.getName());
		}
	}

	@GetMapping("/storeverification")
	public String viewStoreVerificationForm(Model model, Principal principal, RedirectAttributes redirectAttributes) {
		model.addAttribute("username", principal.getName());

		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);

		if (shopDetail != null) {
			ShopVerification shopVerification = shopVerifcationRepo.findByUserAndShopDetail(user, shopDetail);
			if (shopVerification == null) {
				shopVerification = new ShopVerification();
			}

			model.addAttribute("storeverification", shopVerification);
			model.addAttribute("payemtingsettingCreated", true);
			return "sidebar/storeverification";
		} else {
			model.addAttribute("payemtingsettingCreated", false);
			redirectAttributes.addFlashAttribute("message11", "Kindly establish your store first.!");
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			return "redirect:/shopsetting";
//			return "sidebar/storeverification";
		}

//		if (shopVerification == null) {
//			shopVerification = new ShopVerification();
//		}
//		model.addAttribute("storeverification", shopVerification);
//		return "sidebar/storeverification";
	}

	@PostMapping("/storeverification")
	private String addStoreVerificationData(@ModelAttribute("shopVerification") ShopVerification shopVerification,
			RedirectAttributes redirectAttributes, Principal principal, Model model,
			@RequestParam("myFile") MultipartFile file) {
		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);

		if (shopDetail != null) {
			if (file.isEmpty()) {
				shopVerification.setValidIdDoc("default");
			} else {
				try {
					String filename = file.getOriginalFilename();
					File saveFileFolder = new ClassPathResource("static/").getFile();
//					for local
//					String uploadDirShopVerify = saveFileFolder.getAbsolutePath();
					uploadDirShopVerify = uploadDirShopVerify.trim().replaceAll("\\s", "");

					System.out.println("upload path ======> " + saveFileFolder.getAbsolutePath());
					FileService.saveFile(uploadDirShopVerify, filename, file);
					shopVerification.setValidIdDoc(filename);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			shopVerification.setShopDetail(shopDetail);
			shopVerification.setUser(user);
			shopVerification.setDate(new Date());

			storeVerificationService.addShopVerificatonData(shopVerification);
			redirectAttributes.addFlashAttribute("message", "Record has been added successfully!");
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		} else {
			System.out.println("data not save:add alert message");
		}
		return "redirect:/storeverification";
	}
}
