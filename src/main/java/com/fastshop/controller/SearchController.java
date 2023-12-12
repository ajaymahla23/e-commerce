package com.fastshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fastshop.Repo.CategoryRepo;
import com.fastshop.Repo.MyOrderDetailRepo;
import com.fastshop.Repo.ProductDetailRepo;
import com.fastshop.Repo.ShopVerifcationRepo;

@Controller
public class SearchController {
	@Autowired
	MyOrderDetailRepo myOrderDetailRepo;
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	ShopVerifcationRepo shopVerifcationRepo;
	@Autowired
	ProductDetailRepo productDetailRepo;

	@GetMapping("/search_order")
	public String searchOrderInSearchBar(@RequestParam(required = false) String name, Model model) {
		model.addAttribute("myOrderDetail", myOrderDetailRepo.findByMyOrderDetailOrderIdContainingIgnoreCase(name));
		return "dashboard/pages/orderhistory";
	}

	@GetMapping("/search_category")
	public String searchCategoryInSearchBar(@RequestParam(required = false) String name, Model model) {
		model.addAttribute("categories", categoryRepo.findByNameContaining(name));
		return "dashboard/pages/category_management";
	}

	@GetMapping("/search_ordermanagement")
	public String searchOrderManagementInSearchBar(@RequestParam(required = false) String name, Model model) {
		model.addAttribute("myorderdetail", myOrderDetailRepo.findByMyOrderDetailOrderIdContainingIgnoreCase(name));
		return "dashboard/pages/order_management";
	}

	@GetMapping("/search_address")
	public String searchAddressInSearchBar(@RequestParam(required = false) String name, Model model) {
		model.addAttribute("shopVerificationList", shopVerifcationRepo.findByAddressContainingIgnoreCase(name));
		return "dashboard/pages/addressmanagement";
	}

	@GetMapping("/searchstore")
	public String searchStores(@RequestParam String query, Model model) {
		model.addAttribute("shopVerificationList",
				shopVerifcationRepo.findByShopDetail_ShopNameContainingIgnoreCase(query, query));
		return "dashboard/pages/storemangement";
	}

	@GetMapping("/search_product")
	public String searchProductsAndCategoryInSearchBar(@RequestParam(required = false) String name, Model model) {
		model.addAttribute("productsDetailList",
				productDetailRepo.findByProductNameOrCategoryNameContaining(name, name));
		return "dashboard/pages/productMangement";
	}

	@GetMapping("/search_hotproduct")
	public String searchHotProductInSearchBar(@RequestParam(required = false) String name, Model model) {
		model.addAttribute("hotproduct", productDetailRepo.findByProductNameOrCategoryNameContaining(name, name));
		return "dashboard/pages/hotproduct_management";
	}
}
