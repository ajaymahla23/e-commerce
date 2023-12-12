package com.fastshop.service;

import java.util.List;
import java.util.Optional;

import com.fastshop.model.ShopVerification;

public interface StoreVerificationService {

	public ShopVerification addShopVerificatonData(ShopVerification shopVerification);

	public List<ShopVerification> getAllShopVerification();

	public Optional<ShopVerification> editShopVerification(Long id);

	public void updateStatusShopVerificationId(Long id, String status);

}
