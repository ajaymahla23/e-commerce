package com.fastshop.controller;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastshop.Repo.CartItemRepository;
import com.fastshop.Repo.MyOrderDetailRepo;
import com.fastshop.Repo.MyOrderRepository;
import com.fastshop.Repo.ProductDetailRepo;
import com.fastshop.Repo.ProductsRepo;
import com.fastshop.Repo.ReturnOrderRepo;
import com.fastshop.Repo.ShippingOrderRepo;
import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.dto.ProductDetailDTO;
import com.fastshop.helper.FileService;
import com.fastshop.model.CartItem;
import com.fastshop.model.Category;
import com.fastshop.model.MyOrder;
import com.fastshop.model.MyOrderDetail;
import com.fastshop.model.ProductsDetail;
import com.fastshop.model.RechargeWallet;
import com.fastshop.model.ReturnOrder;
import com.fastshop.model.ShippingOrder;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;
import com.fastshop.model.Wallet;
import com.fastshop.service.CartItemServices;
import com.fastshop.service.CategoryService;
import com.fastshop.service.MyOrderService;
import com.fastshop.service.ProductDetailService;
import com.fastshop.service.ProductsService;
import com.fastshop.service.ReturnOrderService;
import com.fastshop.service.ShippingOrderService;
import com.fastshop.service.WalletService;

import net.bytebuddy.utility.RandomString;

@Controller
public class UserHomeController {
	@Autowired
	ProductsService productsService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CartItemServices cartItemService;
	@Autowired
	CartItemRepository cartItemRepo;
	@Autowired
	CartItemServices cartItemServices;
	@Autowired
	MyOrderService myOrderService;
	@Autowired
	WalletService walletService;
	@Autowired
	ProductDetailService productDetailService;
	@Autowired
	ShopDetailRepo shopDetailRepo;
	@Autowired
	ProductDetailRepo productDetailRepo;
	@Autowired
	MyOrderRepository myOrderRepository;
	@Autowired
	MyOrderDetailRepo myOrderDetailRepo;
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductsRepo productsRepo;
	@Autowired
	ReturnOrderRepo returnOrderRepo;
	@Autowired
	ReturnOrderService returnOrderService;
	@Autowired
	ShippingOrderRepo shippingOrderRepo;
	@Autowired
	ShippingOrderService shippingOrderService;

	@Value("${krishoop.addAmtByUser.screenshot}")
	private String uploadDirAddAmtByUser;

	@ModelAttribute
	public void commonMethod(Model model, Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		if (!model.containsAttribute("countCart")) {
			List<CartItem> cartItemList = cartItemRepo.findByUser(user);
			int count = 0;
			for (CartItem cart : cartItemList) {
				if ("C".equals(cart.getWishlist())) {
					count += cart.getQuantity();
				}
			}
			model.addAttribute("countCart", count);
		}
		if (!model.containsAttribute("countWishlist")) {
			List<CartItem> cartItemList = cartItemRepo.findByUser(user);
			int count1 = 0;
			for (CartItem cart : cartItemList) {
				if ("W".equals(cart.getWishlist())) {
					count1 += cart.getQuantity();
				}
			}
			model.addAttribute("countWishlist", count1);
		}
		if (!model.containsAttribute("walletAmtList")) {
			Wallet walletAmtList = walletService.findByUserAndRoleType(user, "U");
			model.addAttribute("walletAmtList", walletAmtList);
		}

		if (!model.containsAttribute("categoryList")) {
			List<Category> categoryList = categoryService.getAllCategory();
			model.addAttribute("categoryList", categoryList);
		}
		if (!model.containsAttribute("totalAmt") || !model.containsAttribute("cartItemList")) {
			List<CartItem> cartItemList = cartItemRepo.findByUserAndWishlist(user, "C");
			double price = cartItemList.stream().mapToDouble(CartItem::getTotalAmount).sum();
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("totalAmt", price);
		}

		if (!model.containsAttribute("username")) {
			model.addAttribute("username", principal.getName());
		}
	}

	@GetMapping("/home")
	public String userHomePage(Model model, Principal principal) {

		List<Category> categoryList = categoryService.getAllCategory();
		model.addAttribute("categoryList", categoryList);

		model.addAttribute("productDetilData", productDetailRepo.findByTopMinPriceProducts());
		model.addAttribute("productDetilDataTopMaxPrice", productDetailRepo.findByTopMaxPriceProducts());

		List<Category> categories = categoryService.getAllCategory();
		Map<String, List<ProductsDetail>> productsByCategory = new HashMap<>();
		for (Category category : categories) {
			Pageable pageable = PageRequest.of(0, 1);
			List<ProductsDetail> productsDetail = productDetailService.getProductsDetailByCategory(category.getName(),
					pageable);
			System.out.println("Products for category " + category.getName() + ": " + productsDetail);
			if (productsDetail != null && productsDetail.size() > 0) {
				productsByCategory.put(category.getName(), productsDetail);
			}
		}
		model.addAttribute("productsByCategory", productsByCategory);

//		Side-bar cart view
		User user = userRepository.findByEmail(principal.getName());
		List<CartItem> cartItemList = cartItemRepo.findByUserAndWishlist(user, "C");
		model.addAttribute("cartItemList", cartItemList);
		double price = cartItemList.stream().mapToDouble(CartItem::getTotalAmount).sum();
		model.addAttribute("totalAmt", price);

		return "amazonpage";
	}

	@GetMapping("/category/{name}")
	public String viewProductsByCategory(@PathVariable("name") String categoryName, Model model, Principal principal) {
		Pageable pageable = PageRequest.of(0, 100);
//		1 ==>> means show the product on shop
		model.addAttribute("productsDetail",
				productDetailService.getProductsDetailByCategoryAndPublish(categoryName, 1, pageable));
		model.addAttribute("selectedCategoryName", categoryName);
		return "viewProductsByCategory";
	}

	@GetMapping("/category/productDetail/{id}")
	public String viewProductDetail(HttpSession session, Principal principal, Model model,
			@PathVariable("id") Long id) {
		model.addAttribute("productsDetail", productDetailService.findProductDetailById(id).get());
		ShopDetail shopDetail = shopDetailRepo.findShopByProductDetailId(id);
		ProductsDetail productsDetailWithStore = productDetailService.findByStoreDetailAndId(shopDetail, id);
		model.addAttribute("productsDetailWithStore", productsDetailWithStore);
		return "viewProductDetail";
	}

	@RequestMapping("/addCart/{id}")
	public String addToCart(@PathVariable Long id, HttpSession session, Model model,
			@ModelAttribute("cartItem") CartItem cartItem, Principal principal) {
		System.out.println("=======================ADDD CART METHOD=================================");
		User user = userRepository.findByEmail(principal.getName());
		cartItem.setUser(user);
		cartItem.setWishlist("C");
		cartItemService.addProductInCart(cartItem, id, user);
		return "redirect:/cart";
	}

	@GetMapping("/cart")
	public String viewCartProduct(HttpSession session, Principal principal, Model model) {
		User user = userRepository.findByEmail(principal.getName());

		model.addAttribute("categoryList", categoryService.getAllCategory());

		List<CartItem> cartItemList = cartItemRepo.findByUserAndWishlist(user, "C");
		if (cartItemList == null || cartItemList.isEmpty()) {
			return "emptyCart";
		} else {
			model.addAttribute("cartItemList", cartItemList);
			double price = 0.0;
			for (CartItem cart : cartItemList) {
				price += cart.getTotalAmount();
			}
//			double price = cartItemList.stream().mapToDouble(CartItem::getTotalAmount).sum();
			model.addAttribute("totalAmt", price);

			int categoryId = cartItemList.get(0).getProductsDetail().getProducts().getCategory().getId();
			List<ProductsDetail> relatedProductsList = productDetailRepo.findAllByProducts_Category_Id(categoryId);
			Long firstProductId = cartItemList.get(0).getProductsDetail().getProducts().getId();

			relatedProductsList = relatedProductsList.stream()
					.filter(productDetail -> !productDetail.getProducts().getId().equals(firstProductId))
					.collect(Collectors.toList());

			int endIndex = Math.min(relatedProductsList.size(), 5);
			List<ProductsDetail> first5RelatedProducts = relatedProductsList.subList(0, endIndex);
			model.addAttribute("relatedProductsList", first5RelatedProducts);

			return "cart";
		}
	}

	@PostMapping("/buy_order")
	public String buyOrder(@RequestBody Map<String, Object> data, Model model, Principal principal, Wallet wallet,
			@ModelAttribute("message") String message, @ModelAttribute("alertClass") String alertClass,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		User user = userRepository.findByEmail(principal.getName());

		List<CartItem> cartItems = cartItemRepo.findByUser(user);
		List<CartItem> filteredCartItems = cartItems.stream().filter(cartItem -> "C".equals(cartItem.getWishlist()))
				.collect(Collectors.toList());

		String buyAmt = data.get("buyAmount").toString();

//		user shipping address
		String shippingAddress = data.get("shippingAddress").toString();
		String state = data.get("state").toString();
		String country = data.get("country").toString();
		String pincode = data.get("pincode").toString();
		String shippingPhoneNo = data.get("shippingPhoneNo").toString();
		String remarks = data.get("remarks").toString();
		shippingOrderService.addUserShippingAddress(shippingAddress, state, country, pincode, shippingPhoneNo, remarks,
				user);

		List<String> idList = (List<String>) data.get("productDetailId");
		Long[] productDetailId = new Long[idList.size()];
		for (int i = 0; i < idList.size(); i++) {
			String input = idList.get(i).trim();
			productDetailId[i] = Long.parseLong(input);
		}

		List<ProductsDetail> productsDetail = productDetailRepo.findAllById(Arrays.asList(productDetailId));
		if (buyAmt == null || buyAmt.equals("0.0") || buyAmt.isEmpty() || buyAmt.equals(" ") || buyAmt.equals("0")) {
			return "redirect:/home";
		} else {
			double butAmt1 = Double.parseDouble(buyAmt);
			Wallet totalWalletAmt = walletService.findByUser(user);

			if (totalWalletAmt == null) {
				model.addAttribute("message", message);
				model.addAttribute("alertClass", alertClass);
				return "redirect:/cart";
			} else {
				if (totalWalletAmt.getWalletAmount() >= butAmt1) {
					double newWalletAmount = totalWalletAmt.getWalletAmount() - butAmt1;
					totalWalletAmt.setWalletAmount(newWalletAmount);

					String token = RandomString.make(3).toUpperCase();

					Random random = new Random();
					int randomNo = random.nextInt(999999999);
					MyOrder order = new MyOrder();
					order.setAmount(buyAmt);
					order.setUser(user);
					order.setOrderId(totalWalletAmt.getId() + String.valueOf(randomNo));
					order.setPaymentId(token + String.valueOf(randomNo) + totalWalletAmt.getId());
					order.setReceipt("FSAST" + totalWalletAmt.getId() + String.valueOf(randomNo));
					order.setStatus("N");
					order.setUserPayemetnStatus("Y");
					order.setAdminDeliveryStatus("N");
					order.setAdminIncomeAmt(0);
					order.setDate(new Date());
					order.setShippingOrder(shippingOrderRepo.findByUser(user));

					List<MyOrderDetail> myOrderDetailData = new ArrayList<>();
					for (ProductsDetail detail : productsDetail) {
						ProductsDetail productDetail = productDetailService
								.findProductDetailById(detail.getProductDetailId()).get();
						CartItem cartItem = cartItemRepo
								.findByProductsDetail_ProductDetailIdAndUser(detail.getProductDetailId(), user);
						int totalqty = cartItem.getQuantity();
						double productPrice = cartItem.getTotalAmount();
//						long totalPrice = (new Double(productPrice)).longValue();
						long totalPrice = (long) productPrice;

						MyOrderDetail myOrderDetail = new MyOrderDetail();
						myOrderDetail.setShopDetail(cartItem.getShopDetail());
						myOrderDetail.setProductsDetail(productDetail);
						myOrderDetail.setMyOrder(order);
						myOrderDetail.setProductPrice(totalPrice);
						myOrderDetail.setTotalqty(totalqty);
						myOrderDetail.setEstimatedProfit(productDetail.getEstimatedProfit() * totalqty);
						myOrderDetail.setProductReturnOrNot("N");
						myOrderDetail.setMallproductprice(productDetail.getProducts().getPrice() * totalqty);
						myOrderDetailData.add(myOrderDetail);
					}

					List<ShopDetail> shopDetails = shopDetailRepo
							.findShopByProductDetail(Arrays.asList(productDetailId));

					double newtotalAmtWalletInShop = 0;
					for (ShopDetail detail : shopDetails) {
						Wallet shopWallet = walletService.findByUser(detail.getUser());

						if (shopWallet != null) {
							double totalAmtWalletByShop = shopWallet.getWalletAmount();
							newtotalAmtWalletInShop = totalAmtWalletByShop + butAmt1;
							shopWallet.setWalletAmount(newtotalAmtWalletInShop);
							walletService.addWalletAmtUserByShop(shopWallet);
						} else {
							shopWallet = new Wallet();
							shopWallet.setUser(detail.getUser());
							shopWallet.setWalletAmount(butAmt1);
							walletService.addWalletAmtUserByShop(shopWallet);
						}
					}
					walletService.addWalletAmt(totalWalletAmt);
					List<CartItem> filteredCartItems1 = cartItems.stream()
							.filter(cartItem -> "C".equals(cartItem.getWishlist())).collect(Collectors.toList());
					cartItemRepo.deleteAll(filteredCartItems1);

					myOrderService.addOrder(order);
					myOrderDetailRepo.saveAll(myOrderDetailData);

					model.addAttribute("message", message);
					model.addAttribute("alertClass", alertClass);
					return "redirect:/home";
				} else {
					model.addAttribute("message", message);
					model.addAttribute("alertClass", alertClass);
					return "redirect:/home";
				}
			}
		}
	}

//	 @PostMapping("/buy_order")
//	    public String buyOrder(@RequestBody Map<String, Object> data, Model model, Principal principal,
//	                           Wallet wallet, @ModelAttribute("message") String message,
//	                           @ModelAttribute("alertClass") String alertClass, RedirectAttributes redirectAttributes,
//	                           HttpServletRequest request) {
//		 myOrderService.processBuyOrder(data, principal, model, message, alertClass);
//	        // handle return or redirect as needed
//	        return "redirect:/home";
//	    }

	@PostMapping("/recharge_walletbyuser")
	public String addAmountInWalletByUser(@ModelAttribute("rechargeWallet") RechargeWallet rechargeWallet, Model model,
			Principal principal, @RequestParam("amountByUser") double amount,
			@RequestParam("walletImg") MultipartFile file) {

		if (file.isEmpty()) {
			rechargeWallet.setImageScreenshot("default");
		} else {
			try {
				String filename = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))
						+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				System.out.println("file name : " + filename);

				File saveFileFolder = new ClassPathResource("static/").getFile();
//				for local
//				String uploadDirAddAmtByUser = saveFileFolder.getAbsolutePath();
				uploadDirAddAmtByUser = uploadDirAddAmtByUser.trim();
				uploadDirAddAmtByUser = uploadDirAddAmtByUser.replaceAll("\\s", "");

				System.out.println("upload path ======> " + saveFileFolder.getAbsolutePath());
				FileService.saveFile(uploadDirAddAmtByUser, filename, file);
				rechargeWallet.setImageScreenshot(filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		rechargeWallet.setAmountByUser(amount);
		rechargeWallet.setUser(userRepository.findByEmail(principal.getName()));
		rechargeWallet.setDate(new Date());
		rechargeWallet.setRoleType("U");

		walletService.addRechargeWallet(rechargeWallet);
		model.addAttribute("rechargeWalletAmt", new RechargeWallet());
		return "redirect:/home";
	}

	@GetMapping("/your_order")
	public String viewYourOrder(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());

		model.addAttribute("userDetail", user);

		List<MyOrderDetail> myOrderDetailData = myOrderDetailRepo.findByUser(user);

		if (myOrderDetailData == null || myOrderDetailData.isEmpty()) {
			return "emptyYourOrder";
		} else {
			model.addAttribute("myOrderDetailData", myOrderDetailData);

			String price = "0";
			for (MyOrderDetail orderDetail : myOrderDetailData) {
				price = orderDetail.getMyOrder().getAmount();
			}
			model.addAttribute("price", price);

			List<ReturnOrder> returnorderList = returnOrderRepo.findByUser(user);
			Map<String, ReturnOrder> returnOrderMap = new HashMap<>();
			for (ReturnOrder returnOrder : returnorderList) {
				Long orderID = returnOrder.getMyOrderDetail().getOrderDetailId();
				ReturnOrder returnOrder11 = returnOrderRepo.findByUserAndMyOrderDetail(user, orderID);
				returnOrderMap.put(orderID.toString(), returnOrder11);
			}
			model.addAttribute("returnOrderMap", returnOrderMap);
			return "yourOrder";
		}
	}

	@GetMapping("/wishlist")
	public String viewWishlist(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		List<CartItem> wishlistedItems = cartItemRepo.findByWishlistAndUser("W", user);

		if (wishlistedItems == null || wishlistedItems.isEmpty() || wishlistedItems.equals(" ")) {
			return "emptyWishlist";
		} else {
			model.addAttribute("wishlistedItems", wishlistedItems);
			double price = 0.0;
			for (CartItem cart : wishlistedItems) {
				price += cart.getTotalAmount();
			}
			model.addAttribute("totalAmt", price);
			int categoryId = wishlistedItems.get(0).getProductsDetail().getProducts().getCategory().getId();
			List<ProductsDetail> relatedProductsList = productDetailRepo.findAllByProducts_Category_Id(categoryId);

			Long firstProductId = wishlistedItems.get(0).getProductsDetail().getProducts().getId();
			relatedProductsList = relatedProductsList.stream()
					.filter(productDetail -> !productDetail.getProducts().getId().equals(firstProductId))
					.collect(Collectors.toList());

			// Add this code to retrieve a sublist of the first 5 related products
			int endIndex = Math.min(relatedProductsList.size(), 5);
			List<ProductsDetail> first5RelatedProducts = relatedProductsList.subList(0, endIndex);
			model.addAttribute("relatedProductsList", first5RelatedProducts);
			return "wishlist";
		}
	}

	@RequestMapping("/addToWishlist/{id}")
	public String addToWishlist(@PathVariable Long id, HttpSession session, Model model,
			@ModelAttribute("cartItem") CartItem cartItem, Principal principal) {
		System.out.println("=======================ADDD WISHLIST METHOD=================================");
		User user = userRepository.findByEmail(principal.getName());
		cartItem.setUser(user);
		cartItem.setWishlist("W");
		cartItemService.addProductInCart(cartItem, id, user);
		return "redirect:/wishlist";
	}

//	=========================================================different functionality

	@PostMapping("/addComment")
	public String addComment(@RequestParam String comment, @RequestParam("myOrderId") Long myOrderId) {
		myOrderService.updateComments(comment, myOrderId);
		return "redirect:/your_order";
	}

	@PostMapping("/rating")
	public String saveRating(@RequestParam Integer rating, @RequestParam("myOrderId") Long myOrderId) {
		myOrderService.addRating(rating, myOrderId);
		return "redirect:/your_order";
	}

	@GetMapping("/search")
	public String searchProductsAndCategoryInSearchBar(@RequestParam(required = false) String name, Model model,
			HttpSession session, Principal principal) {
		model.addAttribute("username", principal.getName());
		session.setAttribute("searchText", name);
//		1 means show the product on shop
		List<ProductsDetail> product_detail = productDetailService
				.findByPublishAndProductNameOrCategoryNameAndShopNameContaining(name, name, name, 1);
		if (product_detail == null || product_detail.isEmpty()) {
			System.out.println("No product found");
			return "emptyserachresult";
		} else {
			model.addAttribute("product_detail", product_detail);
			return "searchResults";
		}
	}

	@PostMapping("/updateQuantity")
	public String updateQuantityFromCart(@RequestParam("productDetailId") Long productDetailId,
			@RequestParam("quantity") int quantity, Principal principal) {
		User user = userRepository.findByEmail(principal.getName());

		ProductsDetail productsDetail = productDetailService.findProductDetailById(productDetailId).get();

		List<CartItem> cartItemList = cartItemRepo.findByUser(user);
		for (CartItem cart : cartItemList) {
			if (cart.getProductsDetail().getProductDetailId() == productDetailId) {
				cart.setQuantity(quantity);
				cart.setTotalAmount(productsDetail.getUnitPrice() * quantity);
				cartItemRepo.save(cart);
				break;
			}
		}
		return "redirect:/cart";
	}

	@PostMapping("/cart_remove")
	public String removeProductFromCart(@RequestParam("productDetailId[]") Long productDetailId, Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		cartItemServices.deleteCartItemsByUserAndProductId(productDetailId, user);
		return "redirect:/cart";
	}

	@PostMapping("/addToReturn")
	public String addToReturnOrder(@RequestParam Long myOrderId, @RequestParam String reason, Model model,
			Principal principal, RedirectAttributes redirectAttributes) {
		User user = userRepository.findByEmail(principal.getName());
		returnOrderService.addRetunOrder(user, myOrderId, reason, redirectAttributes);
		return "redirect:/your_order";
	}

	@RequestMapping("/add_to_wishlist")
	public String addToWishlist(@RequestParam("productDetailId[]") Long[] productDetailIds, Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		for (Long productDetailId : productDetailIds) {
			cartItemRepo.findByProductsDetail_ProductDetailIdAndUser(productDetailId, user);
			cartItemRepo.updateWishlist("W", user, productDetailId);
		}
		return "redirect:/cart";
	}

	@RequestMapping("/add_to_cart_from_wishlist")
	public String addToCartFromWishlist(@RequestParam("productDetailId[]") Long[] productDetailIds,
			Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		for (Long productDetailId : productDetailIds) {
			CartItem cartItem = cartItemRepo.findByProductsDetail_ProductDetailIdAndUser(productDetailId, user);
			cartItem.setWishlist("C");
			cartItemRepo.save(cartItem);
		}
		return "redirect:/cart";
	}

	@GetMapping("/allproducts")
	public String viewOnlyClickScroll(Model model) {
		model.addAttribute("products", productDetailRepo.findAll());
		return "scrollbar_imagehome";
	}

	@GetMapping("/about")
	public String viewAbout(Principal principal, Model model) {
		model.addAttribute("username", principal.getName());
		return "about";
	}

	@PostMapping("/update-profile")
	public String updateUserProfile(@ModelAttribute("user") User user) {
		userRepository.save(user);
		return "redirect:/your_order";
	}

	@GetMapping("/shopbyproducts")
	public String viewShopByProducts(@RequestParam(name = "minPrice", required = false) Long minPrice,
			@RequestParam(name = "maxPrice", required = false) Long maxPrice,
			@RequestParam(name = "sortOrder", defaultValue = "default") String sortOrder, Model model) {

		List<ProductsDetail> allProducts = productDetailRepo.findAll();
		List<ProductDetailDTO> filteredProducts;

		if (minPrice != null && maxPrice != null) {
			filteredProducts = filterProducts(minPrice, maxPrice);
//			List<ProductsDetail> filteredProducts1 = productDetailService.getProductsByPriceRange(minPrice, maxPrice);
//			filteredProducts =filteredProducts1.stream().map(ProductDetailDTO::new).collect(Collectors.toList());

		} else {
			filteredProducts = allProducts.stream().map(ProductDetailDTO::new).collect(Collectors.toList());
		}

		// Sort the products based on the selected order
		if ("lowToHigh".equals(sortOrder)) {
			filteredProducts.sort(Comparator.comparing(ProductDetailDTO::getUnitPrice));
		} else if ("highToLow".equals(sortOrder)) {
			filteredProducts.sort(Comparator.comparing(ProductDetailDTO::getUnitPrice).reversed());
		}

		model.addAttribute("products", allProducts);
		model.addAttribute("filteredProducts", filteredProducts);

//		category list on the filter sidebar
		List<Category> categories = categoryService.getAllCategory();
		Map<String, List<ProductsDetail>> productsByCategory = new HashMap<>();
		for (Category category : categories) {
			Pageable pageable = PageRequest.of(0, 1);
			List<ProductsDetail> productsDetail = productDetailService.getProductsDetailByCategory(category.getName(),
					pageable);
			System.out.println("Products for category " + category.getName() + ": " + productsDetail);
			if (productsDetail != null && productsDetail.size() > 0) {
				productsByCategory.put(category.getName(), productsDetail);
			}
		}
		model.addAttribute("productsByCategory", productsByCategory);

// top rated product		
		List<ProductsDetail> minPriceTop5Products = productDetailRepo.minPriceTop5Products();
		model.addAttribute("minPriceTop5Products", minPriceTop5Products);

		return "singleshop_Allproduct";
	}

	@ResponseBody
	@GetMapping("/filterProducts")
	public List<ProductDetailDTO> filterProducts(@RequestParam(name = "minPrice", required = false) Long minPrice,
			@RequestParam(name = "maxPrice", required = false) Long maxPrice) {
		List<ProductsDetail> filteredProducts = productDetailService.getProductsByPriceRange(minPrice, maxPrice);
		return filteredProducts.stream().map(ProductDetailDTO::new).collect(Collectors.toList());
	}

	@ResponseBody
	@GetMapping("/getPriceRange")
	public ProductDetailDTO getPriceRange() {
		Long minPrice = productDetailRepo.findMinPrice();
		Long maxPrice = productDetailRepo.findMaxPrice();
		return new ProductDetailDTO(minPrice, maxPrice);
	}

	@GetMapping("/checkout")
	public String viewCheckout(Model model, Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		ShippingOrder shippingOrderAddress = shippingOrderRepo.findByUser(user);

		if (shippingOrderAddress != null) {
			model.addAttribute("shippingOrderAddress", shippingOrderAddress);
		} else {
			model.addAttribute("shippingOrderAddress", new ShippingOrder());
		}
		return "checkout";
	}
}
