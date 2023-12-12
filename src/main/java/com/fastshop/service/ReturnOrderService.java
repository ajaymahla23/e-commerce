package com.fastshop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastshop.Repo.MyOrderDetailRepo;
import com.fastshop.Repo.ReturnOrderRepo;
import com.fastshop.model.MyOrderDetail;
import com.fastshop.model.ReturnOrder;
import com.fastshop.model.User;

@Service
public class ReturnOrderService {
	@Autowired
	ReturnOrderRepo returnOrderRepo;
	@Autowired
	MyOrderDetailRepo myOrderDetailRepo;

	public void addRetunOrder(User user, Long myOrderId, String reason, RedirectAttributes redirectAttributes) {
		ReturnOrder returnOrder11 = returnOrderRepo.findByUserAndMyOrderDetail(user, myOrderId);
		if (returnOrder11 != null) {
			redirectAttributes.addFlashAttribute("message", "Your request has been alreday sent!!!");
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		} else {
			MyOrderDetail myOrderDetail = myOrderDetailRepo.findById(myOrderId).get();

			String productPrice = myOrderDetail.getProductPrice().toString();

			ReturnOrder returnOrder = new ReturnOrder();
			returnOrder.setDate(new Date());
			returnOrder.setMyOrderDetail(myOrderDetail);
			returnOrder.setProductsDetail(myOrderDetail.getProductsDetail());
			returnOrder.setOrderNumner(myOrderDetail.getMyOrder().getOrderId());
			returnOrder.setProdouctTotalAmt(productPrice);
			returnOrder.setReason(reason);
			returnOrder.setReasonStatus("U");
			returnOrder.setShopDetail(myOrderDetail.getShopDetail());
			returnOrder.setUser(myOrderDetail.getMyOrder().getUser());
			returnOrderRepo.save(returnOrder);
		}
	}

}
