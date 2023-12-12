package com.fastshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastshop.Repo.ShippingOrderRepo;
import com.fastshop.model.ShippingOrder;
import com.fastshop.model.User;

@Service
public class ShippingOrderService {
	@Autowired
	ShippingOrderRepo shippingOrderRepo;

	public void addUserShippingAddress(String shippingAddress, String state, String country, String pincode,
			String shippingPhoneNo, String remarks, User user) {

		if (user != null) {
			shippingOrderRepo.updateShippingAddress(shippingAddress, state, country, pincode,
					Long.parseLong(shippingPhoneNo), remarks, user);
		} else {
			ShippingOrder shippingOrder = new ShippingOrder();
			shippingOrder.setShippingAddress(shippingAddress);
			shippingOrder.setCountry(country);
			shippingOrder.setPincode(pincode);
			shippingOrder.setRemarks(remarks);
			shippingOrder.setShippingPhoneNo(Long.parseLong(shippingPhoneNo));
			shippingOrder.setState(state);
			shippingOrder.setUser(user);
			shippingOrderRepo.save(shippingOrder);
		}

	}

}
