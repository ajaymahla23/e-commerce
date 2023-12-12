package com.fastshop.service;

import java.util.Optional;

import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;

public interface ShopDetailService {

	public ShopDetail addShopDetail(ShopDetail ShopDetail, User user);

	public Optional<ShopDetail> editShopDetail(Long shopDetailId);

}
