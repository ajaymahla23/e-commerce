package com.fastshop.seriviceImpl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;
import com.fastshop.service.ShopDetailService;

@Service
public class ShopDetailServiceImpl implements ShopDetailService {
	@Autowired
	ShopDetailRepo shopDetailRepo;

	@Override
	public ShopDetail addShopDetail(ShopDetail shopDetail, User user) {
		shopDetail.setUser(user);
		shopDetail.setDate(new Date());
		return shopDetailRepo.save(shopDetail);
	}

	@Override
	public Optional<ShopDetail> editShopDetail(Long shopDetailId) {
		return shopDetailRepo.findById(shopDetailId);
	}

}
