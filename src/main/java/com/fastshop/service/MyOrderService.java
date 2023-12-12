package com.fastshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastshop.Repo.CartItemRepository;
import com.fastshop.Repo.MyOrderDetailRepo;
import com.fastshop.Repo.MyOrderRepository;
import com.fastshop.Repo.ProductDetailRepo;
import com.fastshop.Repo.ShippingOrderRepo;
import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.model.MyOrder;
import com.fastshop.model.MyOrderDetail;

@Service
public class MyOrderService {
	@Autowired
	MyOrderRepository myOrderRepository;
	@Autowired
	MyOrderDetailRepo myOrderDetailRepo;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CartItemRepository cartItemRepo;
	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	ProductDetailRepo productDetailRepo;
	@Autowired
	WalletService walletService;
	@Autowired
	ShippingOrderRepo shippingOrderRepo;
	@Autowired
	ProductDetailService productDetailService;
	@Autowired
	ShopDetailRepo shopDetailRepo;

	public MyOrder addOrder(MyOrder myOrder) {
		return this.myOrderRepository.save(myOrder);
	}

	public void updateComments(String comment, Long myOrderId) {
		MyOrderDetail myOrderDetail = myOrderDetailRepo.findById(myOrderId).orElse(null);
		if (myOrderDetail != null) {
			myOrderDetail.setRemarks(comment);
			myOrderDetailRepo.save(myOrderDetail);
		}
	}

	public void addRating(Integer rating, Long myOrderId) {
		MyOrderDetail myOrderDetail = myOrderDetailRepo.findById(myOrderId).orElse(null);
		if (myOrderDetail != null) {
			myOrderDetail.setRating(rating);
			myOrderDetailRepo.save(myOrderDetail);
		}
	}

//	process buy order
//	public void processBuyOrder(Map<String, Object> data, Principal principal, Model model, String message,
//			String alertClass) {
//		User user = userRepository.findByEmail(principal.getName());
//
//		List<CartItem> cartItems = cartItemRepo.findByUser(user);
//		List<CartItem> filteredCartItems = cartItems.stream().filter(cartItem -> "C".equals(cartItem.getWishlist()))
//				.collect(Collectors.toList());
//
//		String buyAmt = data.get("buyAmount").toString();
//
//// user shipping address
//		String shippingAddress = data.get("shippingAddress").toString();
//		String state = data.get("state").toString();
//		String country = data.get("country").toString();
//		String pincode = data.get("pincode").toString();
//		String shippingPhoneNo = data.get("shippingPhoneNo").toString();
//		String remarks = data.get("remarks").toString();
//		shippingOrderService.addUserShippingAddress(shippingAddress, state, country, pincode, shippingPhoneNo, remarks,
//				user);
//
//		List<String> idList = (List<String>) data.get("productDetailId");
//		Long[] productDetailId = new Long[idList.size()];
//		for (int i = 0; i < idList.size(); i++) {
//			String input = idList.get(i).trim();
//			productDetailId[i] = Long.parseLong(input);
//		}
//
//		List<ProductsDetail> productsDetail = productDetailRepo.findAllById(Arrays.asList(productDetailId));
//
//		if (buyAmt == null || buyAmt.equals("0.0") || buyAmt.isEmpty() || buyAmt.equals(" ") || buyAmt.equals("0")) {
//// handle redirect or return as needed
//			return;
//		} else {
//			double butAmt1 = Double.parseDouble(buyAmt);
//			Wallet totalWalletAmt = walletService.findByUser(user);
//
//			if (totalWalletAmt == null) {
//				model.addAttribute("message", message);
//				model.addAttribute("alertClass", alertClass);
//// handle redirect or return as needed
//				return;
//			} else {
//				if (totalWalletAmt.getWalletAmount() >= butAmt1) {
//					if (totalWalletAmt.getWalletAmount() >= butAmt1) {
//						double newWalletAmount = totalWalletAmt.getWalletAmount() - butAmt1;
//						totalWalletAmt.setWalletAmount(newWalletAmount);
//
//						String token = RandomString.make(3).toUpperCase();
//
//						Random random = new Random();
//						int randomNo = random.nextInt(999999999);
//						MyOrder order = new MyOrder();
//						order.setAmount(buyAmt);
//						order.setUser(user);
//						order.setOrderId(totalWalletAmt.getId() + String.valueOf(randomNo));
//						order.setPaymentId(token + String.valueOf(randomNo) + totalWalletAmt.getId());
//						order.setReceipt("FSAST" + totalWalletAmt.getId() + String.valueOf(randomNo));
//						order.setStatus("N");
//						order.setUserPayemetnStatus("Y");
//						order.setAdminDeliveryStatus("N");
//						order.setAdminIncomeAmt(0);
//						order.setDate(new Date());
//						order.setShippingOrder(shippingOrderRepo.findByUser(user));
//						// Create MyOrderDetail objects
//						List<MyOrderDetail> myOrderDetailData = new ArrayList<>();
//						for (ProductsDetail detail : productsDetail) {
//							ProductsDetail productDetail = productDetailService
//									.findProductDetailById(detail.getProductDetailId()).get();
//							CartItem cartItem = cartItemRepo
//									.findByProductsDetail_ProductDetailIdAndUser(detail.getProductDetailId(), user);
//							int totalqty = cartItem.getQuantity();
//							double productPrice = cartItem.getTotalAmount();
////						long totalPrice = (new Double(productPrice)).longValue();
//							long totalPrice = (long) productPrice;
//
//							MyOrderDetail myOrderDetail = new MyOrderDetail();
//							myOrderDetail.setShopDetail(cartItem.getShopDetail());
//							myOrderDetail.setProductsDetail(productDetail);
//							myOrderDetail.setMyOrder(order);
//							myOrderDetail.setProductPrice(totalPrice);
//							myOrderDetail.setTotalqty(totalqty);
//							myOrderDetail.setEstimatedProfit(productDetail.getEstimatedProfit() * totalqty);
//							myOrderDetail.setProductReturnOrNot("N");
//							myOrderDetail.setMallproductprice(productDetail.getProducts().getPrice() * totalqty);
//							myOrderDetailData.add(myOrderDetail);
//						}
//
//						List<ShopDetail> shopDetails = shopDetailRepo
//								.findShopByProductDetail(Arrays.asList(productDetailId));
//
//						double newtotalAmtWalletInShop = 0;
//						for (ShopDetail detail : shopDetails) {
//							Wallet shopWallet = walletService.findByUser(detail.getUser());
//
//							if (shopWallet != null) {
//								double totalAmtWalletByShop = shopWallet.getWalletAmount();
//								newtotalAmtWalletInShop = totalAmtWalletByShop + butAmt1;
//								shopWallet.setWalletAmount(newtotalAmtWalletInShop);
//								walletService.addWalletAmtUserByShop(shopWallet);
//							} else {
//								shopWallet = new Wallet();
//								shopWallet.setUser(detail.getUser());
//								shopWallet.setWalletAmount(butAmt1);
//								walletService.addWalletAmtUserByShop(shopWallet);
//							}
//						}
//
//						// Save data using repository or service methods
//						walletService.addWalletAmt(totalWalletAmt);
//						List<CartItem> filteredCartItems1 = cartItems.stream()
//								.filter(cartItem -> "C".equals(cartItem.getWishlist())).collect(Collectors.toList());
//						cartItemRepo.deleteAll(filteredCartItems1);
//						myOrderService.addOrder(order);
//						myOrderDetailRepo.saveAll(myOrderDetailData);
//
//						// ... (remaining code)
//
//						model.addAttribute("message", message);
//						model.addAttribute("alertClass", alertClass);
//					} else {
//						model.addAttribute("message", message);
//						model.addAttribute("alertClass", alertClass);
//						// handle redirect or return as needed
//					}
//				}
//			}
//		}
//	}
}
