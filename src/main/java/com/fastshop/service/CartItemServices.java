package com.fastshop.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastshop.Repo.CartItemRepository;
import com.fastshop.model.CartItem;
import com.fastshop.model.ProductsDetail;
import com.fastshop.model.User;

@Service
public class CartItemServices {
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	ProductDetailService productDetailService;

	@Transactional
	public void addProductInCart(CartItem cartItem, Long productDetilId, User user) {
		ProductsDetail productsDetail = productDetailService.findProductDetailById(productDetilId).get();

		CartItem cart = cartItemRepository.findByUserAndProduct(user, productDetilId);
		if (cart != null) {
			int oldqty = cart.getQuantity();
			int newQty = oldqty + 1;
			double totalAmt = cart.getProductPrice() * newQty;
			cartItemRepository.updateCartItemQuantity(newQty, totalAmt, user, productDetilId);
		} else {
			cartItem.setDate(new Date());
			cartItem.setProductsDetail(productsDetail);
			cartItem.setQuantity(cartItem.getQuantity());
			cartItem.setShopDetail(productsDetail.getShopDetail());
			cartItem.setTotalAmount(productsDetail.getUnitPrice());
			cartItem.setProductPrice(productsDetail.getUnitPrice());
			cartItemRepository.save(cartItem);
		}
	}

	public List<CartItem> getAllCartItem() {
		return cartItemRepository.findAll();
	}

	public CartItem deleteById(CartItem cartItem) {
		return this.deleteById(cartItem);

	}

	public void deleteCartItemById(Long id) {
		cartItemRepository.deleteById(id);
	}

	public void deleteCartItemsByUserAndProductId(Long id, User user) {
		List<CartItem> cartItems = cartItemRepository.findByUserAndProductId(user, id);
		cartItemRepository.deleteInBatch(cartItems);
	}

	@Transactional
	public void updateByUserAndProductId(int quantity, double totalAmount, User user, Long productDetailId) {
		cartItemRepository.updateCartItemQuantity(quantity, totalAmount, user, productDetailId);
	}

	public void deleteCartByUser(User user) {
		List<CartItem> cartItems = cartItemRepository.findByUser(user);
		cartItemRepository.deleteInBatch(cartItems);
	}

}
