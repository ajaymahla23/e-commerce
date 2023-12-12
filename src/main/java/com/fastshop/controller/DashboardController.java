package com.fastshop.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastshop.Repo.CategoryRepo;
import com.fastshop.Repo.MyOrderDetailRepo;
import com.fastshop.Repo.MyOrderRepository;
import com.fastshop.Repo.ProductDetailRepo;
import com.fastshop.Repo.ProductsRepo;
import com.fastshop.Repo.RechargeWalletRepo;
import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.Repo.ShopVerifcationRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.Repo.WalletRepository;
import com.fastshop.Repo.WithdrawalRequestRepo;
import com.fastshop.model.Category;
import com.fastshop.model.MyOrder;
import com.fastshop.model.Products;
import com.fastshop.model.RechargeWallet;
import com.fastshop.model.ShopVerification;
import com.fastshop.model.User;
import com.fastshop.model.Wallet;
import com.fastshop.service.CategoryService;
import com.fastshop.service.ProductDetailService;
import com.fastshop.service.ProductsService;
import com.fastshop.service.StoreVerificationService;
import com.fastshop.service.UserService;
import com.fastshop.service.WalletService;
import com.fastshop.service.WithdrawlRequestService;

@Controller
public class DashboardController {
	@Autowired
	MyOrderDetailRepo myOrderDetailRepo;
	@Autowired
	ProductDetailRepo productDetailRepo;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	ShopVerifcationRepo shopVerifcationRepo;
	@Autowired
	ShopDetailRepo shopDetailRepo;
	@Autowired
	RechargeWalletRepo rechargeWalletRepo;
	@Autowired
	WalletRepository walletRepo;
	@Autowired
	ProductDetailService productDetailService;
	@Autowired
	ProductsRepo productsRepo;
	@Autowired
	WalletService walletService;
	@Autowired
	StoreVerificationService storeVerificationService;
	@Autowired
	MyOrderRepository myOrderRepository;
	@Autowired
	ProductsService productsService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	UserService userService;
	@Autowired
	WithdrawalRequestRepo withdrawalRequestRepo;
	@Autowired
	WithdrawlRequestService withdrawlRequestService;

	@Value("${krishoop.addproduct.image}")
	private String uploadDirAddProuduct;

	@Value("${krishoop.shopverify.doc}")
	private String uploadDirShopVerify;

	@GetMapping("/admin-panel")
	public String dashboard(Model model, Principal principal) {

		Long countByuser = userRepository.countByUser();
		Long countByActiveUser = userRepository.countByUserActive("I");
		double sumOfAmtLastOneMonth = myOrderRepository.sumOfAmtLastOneMonth();
		double sumOfAmtLastOneWeek = myOrderRepository.sumOfAmtLastOneWeek();
		String sumOfTotalIncome = myOrderRepository.sumOfTotalAmount();
		String listofverifyStore = shopVerifcationRepo.listofverifyStore("Y");

		Long sumOfAmtLastOneMonth1 = (long) sumOfAmtLastOneMonth;
		Long sumOfAmtLastOneWeek1 = (long) sumOfAmtLastOneWeek;

		model.addAttribute("countByuser", countByuser != null ? countByuser : 0);
		model.addAttribute("countByActiveUser", countByActiveUser != null ? countByActiveUser : 0);
		model.addAttribute("sumOfAmtLastOneMonth", sumOfAmtLastOneMonth1 != null ? sumOfAmtLastOneMonth1 : 0);
		model.addAttribute("sumOfAmtLastOneWeek", sumOfAmtLastOneWeek1 != null ? sumOfAmtLastOneWeek1 : 0);
		model.addAttribute("sumOfTotalIncome", sumOfTotalIncome != null ? sumOfTotalIncome : 0);
		model.addAttribute("listofverifyStore", listofverifyStore != null ? listofverifyStore : 0);

		model.addAttribute("username", principal.getName());

		return "dashboard/dashboard";
	}

//	========================================================================Order History
	@GetMapping("/orderhistory")
	public String listofOrderHistory(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("myOrderDetail", myOrderDetailRepo.findAll());
		return "dashboard/pages/orderhistory";
	}

//	========================================================================Customer Management
	@GetMapping("/customermanagement")
	public String listofCustomerManagement(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		List<Map<String, Object>> userList = userRepository.findAllUsersWithDetails();
		model.addAttribute("userList", userList);
		model.addAttribute("countByuser", userRepository.countByUser());
		return "dashboard/pages/customerMangement";
	}

	@PostMapping("/editUser")
	public String editUser(@RequestParam String shopEmail, @RequestParam Integer userId, Model model) {
		shopDetailRepo.updateEmailByUser(shopEmail, userId);
		return "redirect:/customermanagement";
	}

//	==========================================================================Category Management
	@GetMapping("/category_management")
	public String showCategories(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("categories", categoryRepo.findAll());
		return "dashboard/pages/category_management";
	}

	@GetMapping("/add_newcategory")
	public String getCat(Model model) {
		model.addAttribute("category", new Category());
		return "dashboard/pages/addnewCategory";
	}

	@PostMapping("/add_newcategory")
	public String addCategory(@ModelAttribute("category") Category category) {
		categoryService.addCategory(category);
		return "redirect:/category_management";
	}

	@PostMapping("/editCategory")
	public String editCategory(@RequestParam("id") int id, @RequestParam("name") String name) {
		Optional<Category> categoryOpt = categoryRepo.findById(id);
		if (categoryOpt.isPresent()) {
			Category category = categoryOpt.get();
			category.setName(name);
			categoryRepo.save(category);
		}
		return "redirect:/category_management";
	}

//	=============================================================================Order Management
	@GetMapping("/order_management")
	public String showOrder(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("myorderdetail", myOrderDetailRepo.findAll());
		return "dashboard/pages/order_management";
	}

	@PostMapping("/update_deliveryOrder")
	public String updateDeliveryOrderStatus(@RequestParam("delivery_status") String delivery_status,
			@RequestParam("orderId") Long orderId) {
		MyOrder myOrder = myOrderRepository.findById(orderId).get();
		myOrder.setAdminDeliveryStatus(delivery_status);
		myOrderRepository.save(myOrder);
		return "redirect:/order_management";
	}

//	=================================================================================Recharges Management

	@GetMapping("/recharge_management")
	public String listofRechargeManagement(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("rechargeWalletList", rechargeWalletRepo.findByStatus("U"));
		model.addAttribute("rechargeDetail", rechargeWalletRepo.findAll(Sort.by(Sort.Direction.DESC, "id")));
		model.addAttribute("wallet", new Wallet());
		return "dashboard/pages/recharge_management";
	}

	@PostMapping("/addAmountOnWallet")
	public String addAmountOnWallet(@ModelAttribute("wallet") Wallet wallet, RedirectAttributes redirectAttributes,
			@RequestParam Long rechargeId) {

		RechargeWallet rechargeWallet = rechargeWalletRepo.findById(rechargeId).get();
		Wallet wallet1 = walletRepo.findByUser(rechargeWallet.getUser());

		if (wallet1 == null) {
			walletService.addWalletAmtAndId(wallet, rechargeWallet.getRoleType(), rechargeId);
		} else {
			double oldWalletAmount = wallet1.getWalletAmount();
			double newWalletAmount = oldWalletAmount + rechargeWallet.getAmountByUser();
			walletService.updateAmountByUser(newWalletAmount, rechargeWallet.getUser(), rechargeId);
		}
		redirectAttributes.addFlashAttribute("message", "Amount has been added!!!");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/recharge_management";
	}

	@PostMapping("/editwallet")
	public String editWallet(@RequestParam("id") Long id, @RequestParam("name") String name,
			@RequestParam("amount") double amount) {
		Optional<RechargeWallet> walletOpt = rechargeWalletRepo.findById(id);
		if (walletOpt.isPresent()) {
			RechargeWallet wallet = walletOpt.get();
			double newAmount = wallet.getAmountByUser() - amount;
			wallet.setAmountByUser(newAmount);
			rechargeWalletRepo.save(wallet);
		} else {
			System.out.println("No Wallet object found with id=" + id);
		}
		return "redirect:/recharge_management";
	}

//	========================================================================================Address Management
	@GetMapping("/address_management")
	public String listofAddressManagement(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("shopVerificationList", shopVerifcationRepo.findAll());
		return "dashboard/pages/addressmanagement";
	}

//	=========================================================================================Store Management
	@GetMapping("/store_management")
	public String listofStoreManagement11(Model model, HttpServletRequest requrest, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("rechargeWalletList", rechargeWalletRepo.findByStatus("U"));
		model.addAttribute("shopVerificationList", storeVerificationService.getAllShopVerification());
		model.addAttribute("wallet", new Wallet());
		return "dashboard/pages/storemangement";
	}

	@PostMapping("/updateStoreMangement")
	public String updateStoreMangement(@RequestParam String storeName, @RequestParam String storeDescription,
			@RequestParam Long verifyId, @RequestParam String phoneno, @RequestParam String storeEmail) {
		shopVerifcationRepo.updateStorDetail(storeName, phoneno, storeEmail, storeDescription, verifyId);
		return "redirect:/store_management";
	}

	@PostMapping("/store_management/verification_status")
	public String updateVerificationStatus(@RequestBody Map<String, Object> data,
			RedirectAttributes redirectAttributes) {
		String input = data.get("ids").toString().replaceAll("[^\\d.]", "");
		Long verificationStatusId = Long.parseLong(input);
		storeVerificationService.updateStatusShopVerificationId(verificationStatusId, "Y");
		redirectAttributes.addFlashAttribute("shopVerificationMessage", "Shop has been verified successfully!!!");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/store_management";
	}

	@RequestMapping(value = "/store_management/download/{id}", method = RequestMethod.GET)
	public void downloadShopVerificationDocs(@PathVariable("id") Long shopVerificationId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ShopVerification shopVerification = storeVerificationService.editShopVerification(shopVerificationId).get();

//		for local
//		String uploadDirShopVerify = "D:\\Projects\\FastShop\\target\\classes\\static\\";

		Path uploadDir = Paths.get(uploadDirShopVerify);
		System.out.println("upload path :" + uploadDir);

		File file = new File(uploadDir + File.separator + shopVerification.getValidIdDoc());
		System.out.println("path :" + file);
		byte[] byteArray = Files.readAllBytes(file.toPath());
		response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM.getType());
		response.setHeader("Content-Disposition", "attachment; filename=" + shopVerification.getValidIdDoc());
		response.setContentLength(byteArray.length);
		OutputStream os = response.getOutputStream();
		try {
			os.write(byteArray, 0, byteArray.length);
		} finally {
			os.close();
		}
	}

//	=========================================================================================Product Management

	@GetMapping("/productManagement")
	public String listofproductManagement(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("productsDetailList", productDetailRepo.findAll());
		return "dashboard/pages/productMangement";
	}

	@GetMapping("/productlist")
	public String listofproductList(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("productList", productsRepo.findAll());
		return "dashboard/pages/productlist";
	}

	@GetMapping("/addProductManagement")
	public String addProductManagement(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("product", new Products());
		return "dashboard/pages/addProductManagement";
	}

	@PostMapping("/addProductManagement")
	public String addNewProduct(@ModelAttribute("product") Products product,
			@RequestParam("productImage") MultipartFile file, @RequestParam("imgName") String imgName)
			throws IOException {
//		for loacal
//		String uploadDirAddProuduct = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
		uploadDirAddProuduct = uploadDirAddProuduct.trim().replaceAll("\\s", "");

		String imageUUID;
		if (!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(uploadDirAddProuduct, imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		} else {
			imageUUID = imgName;
		}
		product.setImageName(imageUUID);
		productsService.addProduct(product);
		return "redirect:/addProductManagement";
	}

	@GetMapping("/editProductManagement/{id}")
	public String editProductManagement(@PathVariable("id") Long id, Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("productsDetail", productDetailRepo.findById(id).get());
		return "dashboard/pages/edit/editProductManagement";
	}

	@PostMapping("/updateProductDetails")
	public String updateProductDetails(Model model, HttpServletRequest request,
			@RequestParam("productImg") MultipartFile file, @RequestParam("imgName") String imgName)
			throws IOException {
		try {
			Long productId = Long.parseLong(request.getParameter("id"));
			Long productDetailId = Long.parseLong(request.getParameter("productDetailId"));
			Products products = productsRepo.findById(productId).get();
			if (file.isEmpty()) {
				products.setImageName("default");
			} else {
				String imageUUID;
				if (!file.isEmpty()) {
					imageUUID = file.getOriginalFilename();
					uploadDirAddProuduct = uploadDirAddProuduct.trim().replaceAll("\\s", "");

					// Save the new image file
					Path fileNameAndPath = Paths.get(uploadDirAddProuduct, imageUUID);
					Files.write(fileNameAndPath, file.getBytes());
				} else {
					imageUUID = imgName;
				}
				products.setImageName(imageUUID);
			}
			productsRepo.updateProductByAdmin(products.getQty(), products.getPrice(), products.getName(),
					products.getRemarks(), productId);
			productDetailService.updateProductPriceByAdmin(products.getPrice(), productId, productDetailId, request);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "redirect:/productManagement";
	}

//	==================================================================================================Hot Product Management
	@GetMapping("/hot_product")
	public String hotproduct(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("hotproduct", productDetailRepo.findAll());
		return "dashboard/pages/hotproduct_management";
	}

//	===================================================================================================Virtual Client

	@GetMapping("/virtual-clients")
	public String clients(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		List<Map<String, Object>> shopDetailsWithWallet = userRepository.findAllUsersWithWalletAmountAndShopDetails();
		model.addAttribute("virtualclient", shopDetailsWithWallet);
		return "dashboard/pages/virtual_client";
	}

//	===================================================================================================shop level

	@GetMapping("/shoplevel")
	public String showShopDetails(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		try {
			List<Map<String, Object>> shopDetails = shopDetailRepo.findTotalBalanceByShop();
			model.addAttribute("shopDetails", shopDetails);
		} catch (Exception e) {
			model.addAttribute("error", "Error retrieving shop details: " + e.getMessage());
		}
		return "dashboard/pages/storelevel";
	}

//	===================================================================================================Withdraw request	
	@GetMapping("/withdraw_request")
	public String showWithdrawRequest(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("user", new User());
		model.addAttribute("withdrawRequest", withdrawalRequestRepo.findAll());
		return "dashboard/pages/withdwarReqManagement";
	}

	@PostMapping("/addAmountforWithdraw")
	public String addAmountforWithdraw(RedirectAttributes redirectAttributes, @RequestParam Long withdrawReqid) {
		withdrawlRequestService.updateTotalWalletAmt(withdrawReqid);
		redirectAttributes.addFlashAttribute("message", "Amount has been added!!!");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/withdraw_request";
	}

//	========================================================
	@GetMapping("/comment_management")
	public String viewCommentManagement(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("myOrderDetail", myOrderDetailRepo.findAll());
		return "dashboard/pages/commentManagement";
	}

	@GetMapping("/return_review")
	public String viewReview(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("myorderDetailReview", myOrderDetailRepo.findAll());
		return "dashboard/pages/return";
	}

	@GetMapping("/change_email")
	public String viewChangeEmailByPanel(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("userdata", userRepository.findByIdAndRegisterStatus(user.getId(), "A"));
		return "dashboard/pages/edit/editEmail";
	}

	@PostMapping("/update_email")
	public String updateEmailByPanel(Principal principal, @RequestParam String username) {
		User user = userRepository.findByEmail(principal.getName());
		userRepository.updateEmail(username, user.getId());
		return "redirect:/panel";
	}

	@GetMapping("/add_email")
	public String addEmailByPanel(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("user", new User());
		return "dashboard/pages/register_newemail";
	}

	@PostMapping("/add_email")
	public String addNewUserforPanel(@ModelAttribute("user") User user,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			RedirectAttributes redirectAttributes, HttpSession session) {
		User user1 = userRepository.findByEmailAndMobileNumber(user.getEmail(), user.getMobileNumber());
		User user2 = userRepository.findByEmail(user.getEmail());
		User user3 = userRepository.findByMobileNumber(user.getMobileNumber());

		if (user1 != null || (user2 != null && user2.getEmail() != null)
				|| (user3 != null && user3.getMobileNumber() != null)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Please check e-mail is already exist!!!");
		} else {
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setRegisterStatus("A");
			// user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setId(user.getId());
			user.setMobileNumber(user.getEmail());
			userService.addUserRegister(user);

			model.addAttribute("user", new User());
			redirectAttributes.addFlashAttribute("errorMessage", "Registered Successfully!!!!");
		}
		return "redirect:/add_email";
	}

//	ajax for getAmtByRechargeWallet
	@ResponseBody
	@GetMapping("/wallet_amount/{user}")
	public RechargeWallet getAmtByRechargeWallet(@PathVariable(value = "user") Integer userId, Model model) {
		RechargeWallet rechargeWallet = rechargeWalletRepo.findByUserIdAndStatus(userId, "U");
		RechargeWallet rechargeWallet1 = walletService.editRechargeWallet(rechargeWallet.getId()).get();
		return rechargeWallet1;
	}
}
