package com.fastshop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastshop.Repo.CartItemRepository;
import com.fastshop.Repo.MyOrderDetailRepo;
import com.fastshop.Repo.MyOrderRepository;
import com.fastshop.Repo.ProductDetailRepo;
import com.fastshop.Repo.ReturnOrderRepo;
import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.Repo.ShopVerifcationRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.Repo.WalletRepository;
import com.fastshop.model.CartItem;
import com.fastshop.model.MyOrder;
import com.fastshop.model.MyOrderDetail;
import com.fastshop.model.ReturnOrder;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.ShopVerification;
import com.fastshop.model.User;
import com.fastshop.model.Wallet;
import com.fastshop.service.ProductsService;
import com.fastshop.service.ShopDetailService;
import com.fastshop.service.StoreVerificationService;
import com.fastshop.service.UserService;
import com.fastshop.service.WalletService;

@Controller
public class MainPanelController {
	@Autowired
	ShopDetailService shopDetailService;
	@Autowired
	MyOrderRepository myOrderRepository;
	@Autowired
	ProductsService productsService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ShopDetailRepo shopDetailRepo;
	@Autowired
	StoreVerificationService storeVerificationService;
	@Autowired
	ShopVerifcationRepo shopVerifcationRepo;
	@Autowired
	MyOrderDetailRepo myOrderDetailRepo;
	@Autowired
	UserService userService;
	@Autowired
	CartItemRepository cartItemRepo;
	@Autowired
	ProductDetailRepo productDetailRepo;
	@Autowired
	ReturnOrderRepo returnOrderRepo;
	@Autowired
	WalletService walletService;
	@Autowired
	WalletRepository walletRepository;

	@Value("${krishoop.img.logo}")
	private String uploadDirShoplogo;

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

	@GetMapping("/mainpanel")
	public String viewMainpanelPage(Principal principal, Model model) {

		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);

		model.addAttribute("sumOfEstimatedProfitPrice", myOrderDetailRepo.sumOfEstimatedProfitPrice(shopDetail, "N"));

		model.addAttribute("countOfProduct", productDetailRepo.countOfProuducts("Y", shopDetail));

		model.addAttribute("sumOfProductPriceSale", myOrderDetailRepo.sumOfProuductPriceSaleByShop(shopDetail, "N"));

		model.addAttribute("totalSuccessfulOrders", myOrderDetailRepo.countOfSuccessfullOrderByShop(shopDetail, "N"));

		model.addAttribute("countOfReturnProduct", returnOrderRepo.countOfReturnProduct(shopDetail, "U"));

		model.addAttribute("countOfCancelOrder", returnOrderRepo.countOfReturnProduct(shopDetail, "A"));

		model.addAttribute("countOfPendingOrder", returnOrderRepo.countOfReturnProduct(shopDetail, "U"));

		model.addAttribute("usernew", shopDetailRepo.findAll());

		ShopDetail logoImg = shopDetailRepo.findByUser(user);
		model.addAttribute("logoImg", logoImg);

		ShopVerification verification = shopVerifcationRepo.findByUser(user);

		if (verification != null && verification.getStatus().equals("Y")) {
			return "sidebar/mainpanelverifyshop";
		} else {
			return "sidebar/mainpanel";
		}

	}

	@GetMapping("/shopsetting")
	public String viewShopSetting(Model model, Principal principal, RedirectAttributes redirectAttributes) {
		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);
		if (shopDetail == null) {
			shopDetail = new ShopDetail();
		}
		model.addAttribute("shopdetail", shopDetail);
		return "sidebar/shopsetting";
	}

	@PostMapping("/shopsetting")
	public String addNewShopDetail(@ModelAttribute("shopdetail") ShopDetail shopdetail, Principal principal,
			RedirectAttributes redirectAttributes, @RequestParam("shoplogo") MultipartFile file,
			@RequestParam("imgName") String imgName) throws IOException {

		User user = userRepository.findByEmail(principal.getName());
		ShopDetail oldShopDetail = shopDetailRepo.findByUser(user);
		if (file.isEmpty()) {
			shopdetail.setShopLogo("default");
		} else {
			String imageUUID;
			if (!file.isEmpty()) {
				if (oldShopDetail != null) {
					imageUUID = file.getOriginalFilename();
					uploadDirShoplogo = uploadDirShoplogo.trim().replaceAll("\\s", "");

					// Delete the old image file
					Path oldImagePath = Paths.get(uploadDirShoplogo, imgName);
					Files.deleteIfExists(oldImagePath);

					// Save the new image file
					Path fileNameAndPath = Paths.get(uploadDirShoplogo, imageUUID);
					Files.write(fileNameAndPath, file.getBytes());
				} else {
					imageUUID = file.getOriginalFilename();
					uploadDirShoplogo = uploadDirShoplogo.trim().replaceAll("\\s", "");

					// Save the new image file
					Path fileNameAndPath = Paths.get(uploadDirShoplogo, imageUUID);
					Files.write(fileNameAndPath, file.getBytes());
				}
			} else {
				imageUUID = imgName;
			}
			shopdetail.setShopLogo(imageUUID);
		}
		shopDetailService.addShopDetail(shopdetail, user);
		redirectAttributes.addFlashAttribute("message", "Record has been added successfully!");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/shopsetting";
	}

	@GetMapping("/manageprofile")
	public String viewManageProfile(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());

		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		ShopDetail shopDetail = shopDetailRepo.findByUser(user);
		if (shopDetail != null) {
			model.addAttribute("shopDetail", shopDetail);
			model.addAttribute("payemtingsettingCreated", true);
			return "sidebar/2ndpaymentsetting";
		} else {
			model.addAttribute("payemtingsettingCreated", false);
			return "sidebar/2ndpaymentsetting";
		}
	}

	@PostMapping("/updateManageProfile")
	public String upadateManageProfile(@RequestParam String email, @RequestParam String mobileNumber,
			@RequestParam String password, Principal principal, RedirectAttributes redirectAttributes) {
		User user = userRepository.findByEmail(principal.getName());
		userService.updateEmailAndPhoneNo(email, mobileNumber, password, user.getId());
		redirectAttributes.addFlashAttribute("message", "Record has been updated successfully!");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/shoplogin";
	}

	@GetMapping("/orderlist/viewProductOrder/{id}")
	public String viewProductOrder(@PathVariable("id") Long orderDetailId, Model model, Principal principal) {
		MyOrderDetail myOrderDetail = myOrderDetailRepo.findById(orderDetailId).get();
		model.addAttribute("myOrderDetail", myOrderDetail);

		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);
		List<MyOrderDetail> myOrderDetailByShop = myOrderDetailRepo.findByShopDetailAndMyOrder(shopDetail,
				myOrderDetail.getMyOrder().getMyOrderId());
		model.addAttribute("myOrderDetailByShop", myOrderDetailByShop);

		if (myOrderDetail.getMyOrder().getAdminDeliveryStatus().equals("A")) {
			return "sidebar/viewOrderProductApproval";
		} else {
			return "sidebar/viewOrderProduct";
		}

	}

	@GetMapping("/orderlist")
	public String viewOrderList(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);
		model.addAttribute("myOrderList", myOrderDetailRepo.findByShopDetail(shopDetail));

		ShopVerification shopVerification = shopVerifcationRepo.findByUserAndShopDetail(user, shopDetail);
		if (shopVerification != null) {
			model.addAttribute("storeverification", shopVerification);
		}
		return "sidebar/orderlist";
	}

	@PostMapping("/purchaseProduct")
	public String reqPurchaseProductSellerByAdmin(@RequestParam("orderId") Long orderId,
			RedirectAttributes redirectAttributes, Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		MyOrder myOrder = myOrderRepository.findById(orderId).get();
		List<MyOrderDetail> myOrderDetailList = myOrder.getMyOrderDetailList();

//		sum of total profit
		Long totalEstimatedProfit = 0L;
		for (MyOrderDetail myOrderDetail : myOrderDetailList) {
			totalEstimatedProfit += myOrderDetail.getEstimatedProfit();
		}

//	    Long totalEstimatedProfit = myOrderDetailList.stream()
//	            .mapToLong(MyOrderDetail::getEstimatedProfit).sum();

		Long totalEstimatedProfitSeller = totalEstimatedProfit.longValue();

		String totalOrderAmt = myOrder.getAmount();
		double totalOrderAmt1 = Double.parseDouble(totalOrderAmt);

		Wallet walletAmtList = walletService.findByUser(user);

		if (walletAmtList != null) {
			Double totalAmtInShopWallet = walletAmtList.getWalletAmount();
			if (totalAmtInShopWallet >= totalOrderAmt1) {
				double totalMallWalletAmt = totalOrderAmt1 - totalEstimatedProfitSeller;
				double pendingtotalAmtInSellerWallet = totalAmtInShopWallet - totalMallWalletAmt;
//				seller save amount
				walletRepository.updateAmountofSeller(pendingtotalAmtInSellerWallet, user);

				myOrder.setApprovalDate(new Date());
				myOrder.setStatus("Y");
				myOrder.setAdminDeliveryStatus("W");
				myOrder.setAdminIncomeAmt(totalMallWalletAmt);
				myOrderRepository.save(myOrder);
			} else {
				redirectAttributes.addFlashAttribute("message",
						"You have $" + totalAmtInShopWallet + " in wallet. Please, add amount in the wallet!!!");
				redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			}
		} else {
			redirectAttributes.addFlashAttribute("message", "Please, add amount in the wallet!!!");
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		}

		redirectAttributes.addFlashAttribute("message", "Record has been added successfully!");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "sidebar/purchasesucessful";
	}

	@GetMapping("/financial")
	public String viewfinancial(Model model, Principal principal, HttpServletRequest request) {
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");

		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);

		if (fromDate != null || toDate != null) {

			model.addAttribute("sumOfProductPriceSale",
					getDateBetweenSumOfProductPriceSale(shopDetail, fromDate, toDate));

			model.addAttribute("sumOfDiscountPrice",
					getDateBetweenSumOfEstimatedProfitPrice(shopDetail, fromDate, toDate));
		} else {

			model.addAttribute("sumOfProductPriceSale",
					myOrderDetailRepo.sumOfProuductPriceSaleByShop(shopDetail, "N"));

			model.addAttribute("sumOfDiscountPrice", myOrderDetailRepo.sumOfEstimatedProfitPrice(shopDetail, "N"));
		}
		return "sidebar/financial";
	}

	private Long getDateBetweenSumOfProductPriceSale(ShopDetail shopDetail, String fromDate, String toDate) {
		Date startDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = null;
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return myOrderDetailRepo.sumOfProuductPriceSaleByShopBetweenDate(shopDetail, startDate, endDate);
	}

	private Long getDateBetweenSumOfEstimatedProfitPrice(ShopDetail shopDetail, String fromDate, String toDate) {
		Date startDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = null;
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return myOrderDetailRepo.sumOfEstimatedProfitPriceBetweenDate(shopDetail, startDate, endDate);
	}

	@GetMapping("/refund")
	public String viewRefund(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);

		if (shopDetail == null) {
			return "sidebar/emptyRefund";
		} else {
			List<ReturnOrder> returnOrder = returnOrderRepo.findByShopDetail(shopDetail);
			model.addAttribute("returnOrder", returnOrder);
			return "sidebar/refund";
		}
	}

	@PostMapping("/returnorder_status")
	public String updateApprovalStatusForReturnOrderAndUpdateTotalAmtInWallet(
			@RequestParam("returnStatus") String returnStatus, @RequestParam("returnoderid") Long returnoderid,
			Principal principal, RedirectAttributes redirectAttributes) {
		User user = userRepository.findByEmail(principal.getName());

		ReturnOrder returnOrder = returnOrderRepo.findById(returnoderid).get();
		double productReturnAmt = Double.parseDouble(returnOrder.getProdouctTotalAmt());

		Wallet walletAmtList = walletService.findByUser(user);

		Wallet totalAmtInUserWallet = walletService.findByUser(returnOrder.getUser());
		double totalAmtInUserWallet1 = totalAmtInUserWallet.getWalletAmount();

		if (returnStatus.equals("A")) {
			if (walletAmtList != null) {
				Double totalAmtInShopWallet = walletAmtList.getWalletAmount();
				if (totalAmtInShopWallet >= productReturnAmt) {
					double pendingtotalAmtInUserWallet = totalAmtInShopWallet - productReturnAmt;
					double addRetunAmtAndUserTotalAmt = productReturnAmt + totalAmtInUserWallet1;

					returnOrderRepo.updateApprovalStatusForReturnOrder(returnStatus, returnoderid);
					walletRepository.updateAmountByUserAndShopbyReturnOrder(pendingtotalAmtInUserWallet, user);
					walletRepository.updateAmountByUserAndShopbyReturnOrder(addRetunAmtAndUserTotalAmt,
							returnOrder.getUser());

					myOrderDetailRepo.returnProductfromOrder("R", returnOrder.getMyOrderDetail().getOrderDetailId());
//					redirectAttributes.addFlashAttribute("message", "Record has been added successfully!");
//					redirectAttributes.addFlashAttribute("alertClass", "alert-success");
				} else {
					redirectAttributes.addFlashAttribute("message",
							"You have $" + totalAmtInShopWallet + " in wallet. Please, add amount in the wallet!!!");
					redirectAttributes.addFlashAttribute("alertClass", "alert-success");
				}
			} else {
				redirectAttributes.addFlashAttribute("message", "Please, add amount in the wallet!!!");
				redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			}
		} else {
			returnOrderRepo.updateApprovalStatusForReturnOrder(returnStatus, returnoderid);
		}
		return "redirect:/refund";
	}

	@GetMapping("/visitors")
	public String viewVisitors(Model model, Principal principal, User user) {
		model.addAttribute("username", principal.getName());
		return "sidebar/visitors";
	}

	@GetMapping("/classified")
	public String viewclassified(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		model.addAttribute("productList", productsService.getAllProducts());
		return "sidebar/classified";
	}

	@GetMapping("/customer-packages")
	public String viewCustomerPackages(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		return "sidebar/classified-premiumpackage";
	}

	@GetMapping("/conversation")
	public String viewconversation(Model model, Principal principal, User user) {
		model.addAttribute("username", principal.getName());
		return "sidebar/conversation";
	}

	@GetMapping("/product_reviews")
	public String viewProductReview(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		User user = userRepository.findByEmail(principal.getName());
		ShopDetail shopDetail = shopDetailRepo.findByUser(user);
		model.addAttribute("myorderDetailReview", myOrderDetailRepo.findByShopDetail(shopDetail));
		return "sidebar/reviews";
	}

	@GetMapping("/affiliate")
	public String viewaffiliate(Model model, Principal principal, User user) {
		model.addAttribute("username", principal.getName());
		return "sidebar/affiliate";
	}
}
