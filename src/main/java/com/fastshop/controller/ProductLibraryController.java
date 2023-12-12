package com.fastshop.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastshop.Repo.CartItemRepository;
import com.fastshop.Repo.ProductDetailRepo;
import com.fastshop.Repo.ProductsRepo;
import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.Repo.ShopVerifcationRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.model.CartItem;
import com.fastshop.model.ProductsDetail;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.ShopVerification;
import com.fastshop.model.User;
import com.fastshop.service.ProductDetailService;
import com.fastshop.service.ProductsService;

@Controller
public class ProductLibraryController {
	@Autowired
	ProductsService productsService;
	@Autowired
	ProductsRepo productsRepo;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ProductDetailService productDetailService;
	@Autowired
	ProductDetailRepo productDetailRepo;
	@Autowired
	CartItemRepository cartItemRepo;
	@Autowired
	ShopDetailRepo shopDetailRepo;
	@Autowired
	ShopVerifcationRepo shopVerifcationRepo;

	@ModelAttribute
	public void commonMethod(Model model, Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);
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
		if (!model.containsAttribute("storeverification")) {
			ShopVerification shopVerification = shopVerifcationRepo.findByUserAndShopDetail(user, shopDetail);
			model.addAttribute("storeverification", shopVerification);
		}
	}

	@GetMapping("/product")
	public String viewProduct(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		List<ProductsDetail> productDetailList = productDetailRepo.findByActiveAndUserId("Y", user.getId());
		model.addAttribute("productDetailList", productDetailList);
		return "sidebar/product";
	}

	@GetMapping("/productLibrary")
	public String viewProductLibrary(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("productList", productsService.getAllProducts());
		return "sidebar/productLibrary";
	}

	@PostMapping("/checkboxesClicked")
	public String addProductsOnUserHome(@RequestBody Map<String, Object> data, Principal principal,
			RedirectAttributes redirectAttributes) {

		String inputPercentage = data.get("percentage").toString().replaceAll("[^\\d.]", "");
		Long percentage = Long.parseLong(inputPercentage);

		List<String> idList = (List<String>) data.get("ids");
		Long[] productId = idList.stream().map(String::trim).map(Long::parseLong).toArray(Long[]::new);

		User user = userRepository.findByEmail(principal.getName());

		// Check if any of the selected products already exist for the user
		List<Long> existingProductIds = productDetailRepo.findExistingProductIds(user.getId(), productId);
		List<Long> productsToAdd = Arrays.stream(productId).filter(id -> !existingProductIds.contains(id))
				.collect(Collectors.toList());

		// Add the filtered products to the database
		productsService.updateStatusAndCalculationOfProduct("Y", productsToAdd.toArray(new Long[0]), user, percentage);

		redirectAttributes.addFlashAttribute("message", "Records have been added successfully!");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		return "sidebar/productLibrary";
	}

	@PostMapping("/published_update")
	public String productViewOrNotOnShop(@RequestBody Map<String, Object> data) {
		String productDetailId = data.get("publishedId").toString().replaceAll("[^\\d.]", "");
		Long productDetailId1 = Long.parseLong(productDetailId);
		productDetailService.productViewOrNotInShop(productDetailId1);
		return "redirect:/product";
	}

	@GetMapping("/updateProduct/{id}")
	public String viewDiscountForm(@PathVariable("id") Long productDetailId, Model model) {
		model.addAttribute("productDetail", productDetailRepo.findById(productDetailId).get());
		return "sidebar/DiscountForm";
	}

	@PostMapping("/update_discount")
	public String addDiscountOnProduct(@RequestParam("discount") Long discount, Model model,
			@RequestParam Long productDetailId, RedirectAttributes redirectAttributes) {
		ProductsDetail productsDetail = productDetailService.findProductDetailById(productDetailId).get();
		Long percentage = productsDetail.getMarkupPercentage();
		Long unitPrice = productsDetail.getUnitPrice();
		double newUnitdiscountPrice = unitPrice - discount;

		Long newUnitdiscountPrice1 = (long) newUnitdiscountPrice;
		double estimatedProfit = (newUnitdiscountPrice * percentage) / 100;
		double newestimatedProfit = estimatedProfit - discount;
		Long estProfit = (long) newestimatedProfit;
		ShopDetail shopDetail = shopDetailRepo.findShopByProductDetailId(productDetailId);
		productDetailService.updateAmountWithDiscountByProductDetailIdAndShopDetail(discount, newUnitdiscountPrice1,
				estProfit, unitPrice, productDetailId, shopDetail);
		redirectAttributes.addFlashAttribute("message", "Record has been updated successfully!");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/product";
	}
}
