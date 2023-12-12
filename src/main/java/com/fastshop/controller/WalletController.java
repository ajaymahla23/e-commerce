package com.fastshop.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastshop.Repo.CartItemRepository;
import com.fastshop.Repo.RechargeWalletRepo;
import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.Repo.ShopVerifcationRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.Repo.WalletRepository;
import com.fastshop.Repo.WithdrawalRequestRepo;
import com.fastshop.helper.FileService;
import com.fastshop.model.CartItem;
import com.fastshop.model.RechargeWallet;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.ShopVerification;
import com.fastshop.model.User;
import com.fastshop.model.Wallet;
import com.fastshop.model.WithdrawalRequest;
import com.fastshop.service.WalletService;
import com.fastshop.service.WithdrawlRequestService;

@Controller
public class WalletController {
	@Autowired
	WalletService walletService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	WalletRepository walletRepository;
	@Autowired
	RechargeWalletRepo rechargeWalletRepo;
	@Autowired
	CartItemRepository cartItemRepo;
	@Autowired
	ShopDetailRepo shopDetailRepo;
	@Autowired
	WithdrawlRequestService withdrawlRequestService;
	@Autowired
	WithdrawalRequestRepo withdrawalRequestRepo;
	@Autowired
	ShopVerifcationRepo shopVerifcationRepo;

	@Value("${krishoop.addAmtByUser.screenshot}")
	private String uploadDirAddAmtByUser;

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

	@GetMapping("/wallet")
	public String viewWallet(Model model, Principal principal, Wallet wallet) {
		model.addAttribute("username", principal.getName());

		User user = userRepository.findByEmail(principal.getName());
		List<RechargeWallet> walletAmtListByUser = rechargeWalletRepo.findByUser(user);
		model.addAttribute("walletAmtListByUser", walletAmtListByUser);
//		model.addAttribute("walletAmtList", walletService.findByUser(user));
		model.addAttribute("walletAmtList", walletService.findByUserAndRoleType(user, "S"));

		List<WithdrawalRequest> withdrawalRequestList = withdrawalRequestRepo.findByUser(user);
		model.addAttribute("withdrawalRequestList", withdrawalRequestList);

		WithdrawalRequest withdrawalRequestAmt = withdrawalRequestRepo.findByUserAndStatus(user, "U");
		model.addAttribute("withdrawalRequestAmt", withdrawalRequestAmt);

		return "sidebar/wallet";
	}

	@PostMapping("/recharge_wallet")
	public String addRechargeInWalletByShop(@ModelAttribute("rechargeWallet") RechargeWallet rechargeWallet,
			Model model, Principal principal, @RequestParam("amountByUser") double amount,
			@RequestParam("walletImg") MultipartFile file) {

		if (file.isEmpty()) {
			rechargeWallet.setImageScreenshot("default");
		} else {
			try {
				String filename = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))
						+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				System.out.println("file name : " + filename);

//				File saveFileFolder = new ClassPathResource("static/").getFile();
//				String uploadDirAddAmtByUser = saveFileFolder.getAbsolutePath();

				uploadDirAddAmtByUser = uploadDirAddAmtByUser.trim().replaceAll("\\s", "");

//				System.out.println("upload path ======> " + saveFileFolder.getAbsolutePath());
				FileService.saveFile(uploadDirAddAmtByUser, filename, file);
				rechargeWallet.setImageScreenshot(filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		User user = userRepository.findByEmail(principal.getName());
		rechargeWallet.setAmountByUser(amount);
		rechargeWallet.setUser(user);
		rechargeWallet.setDate(new Date());
		rechargeWallet.setRoleType("S");

		walletService.addRechargeWallet(rechargeWallet);
		model.addAttribute("rechargeWalletAmt", new RechargeWallet());
		return "redirect:/wallet";
	}

	@PostMapping("/send_withdraw_req")
	public String sendWithdrawlReqByShop(@ModelAttribute("withdrawalRequest") WithdrawalRequest withdrawalRequest,
			Model model, Principal principal, @RequestParam("withdraw_req") double amount,
			RedirectAttributes redirectAttributes) {
		User user = userRepository.findByEmail(principal.getName());

		Wallet wallet = walletRepository.findByUser(user);
		WithdrawalRequest withdrawalRequest1 = withdrawalRequestRepo.findByUserAndStatus(user, "U");
		if (withdrawalRequest1 != null) {
			redirectAttributes.addFlashAttribute("message", "Request no send!!!");
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		} else {
			if (wallet.getWalletAmount() >= amount) {
				withdrawalRequest.setUser(user);
				withdrawalRequest.setRoleType("S");
				withdrawalRequest.setStatus("U");
				withdrawalRequest.setDate(new Date());
				withdrawalRequest.setWithdrawAmout(amount);
				withdrawlRequestService.addWithdrawReq(withdrawalRequest);
				model.addAttribute("withdrawalRequest", new WithdrawalRequest());
			} else {
				redirectAttributes.addFlashAttribute("message", "Please, add amount in the wallet!!!");
				redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			}
		}
		return "redirect:/wallet";
	}
}
