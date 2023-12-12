package com.fastshop.seriviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastshop.Repo.ShopVerifcationRepo;
import com.fastshop.model.ShopVerification;
import com.fastshop.service.StoreVerificationService;

@Service
public class StoreVerificationSeriveImpl implements StoreVerificationService {
	@Autowired
	ShopVerifcationRepo shopVerifcationRepo;

	@Override
	public ShopVerification addShopVerificatonData(ShopVerification shopVerification) {
		return this.shopVerifcationRepo.save(shopVerification);
	}

	@Override
	public List<ShopVerification> getAllShopVerification() {
		return shopVerifcationRepo.findAll();
	}

	@Override
	public Optional<ShopVerification> editShopVerification(Long id) {
		return shopVerifcationRepo.findById(id);
	}

	@Transactional
	@Override
	public void updateStatusShopVerificationId(Long id, String status) {
		shopVerifcationRepo.updateStatusByShopVerificationId(status, id);
	}

}
