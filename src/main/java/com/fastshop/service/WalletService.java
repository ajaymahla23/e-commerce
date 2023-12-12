package com.fastshop.service;

import java.util.List;
import java.util.Optional;

import com.fastshop.model.RechargeWallet;
import com.fastshop.model.User;
import com.fastshop.model.Wallet;

public interface WalletService {

	public Wallet addWalletAmt(Wallet wallet);

	public Wallet addWalletAmtAndId(Wallet wallet, String roleType, Long id);

	public List<Wallet> getAllWalletAmt(Wallet wallet);

	public RechargeWallet addRechargeWallet(RechargeWallet rechargeWallet);

	public Optional<RechargeWallet> editRechargeWallet(Long id);

	public Wallet findByUser(User user);

	public void updateAmountByUser(double walletAmount, User user, Long id);

	public Wallet addWalletAmtUserByShop(Wallet wallet);

	public Wallet findByUserAndRoleType(User user, String roleType);

}
