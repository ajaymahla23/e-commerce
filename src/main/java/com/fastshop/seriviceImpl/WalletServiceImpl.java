package com.fastshop.seriviceImpl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastshop.Repo.RechargeWalletRepo;
import com.fastshop.Repo.WalletRepository;
import com.fastshop.model.RechargeWallet;
import com.fastshop.model.User;
import com.fastshop.model.Wallet;
import com.fastshop.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {
	@Autowired
	WalletRepository walletRepository;
	@Autowired
	RechargeWalletRepo rechargeWalletRepo;

	@Override
	public Wallet addWalletAmt(Wallet wallet) {
		List<RechargeWallet> rechargeWalletList = rechargeWalletRepo.findByStatus("U");
		for (RechargeWallet rechargeWallet : rechargeWalletList) {
			rechargeWallet.setStatus("P");
		}
		rechargeWalletRepo.saveAll(rechargeWalletList);
		return this.walletRepository.save(wallet);
	}

	@Override
	public Wallet addWalletAmtAndId(Wallet wallet, String roleType, Long id) {
		List<RechargeWallet> rechargeWalletList = rechargeWalletRepo.findByStatusAndId("U", id);
		for (RechargeWallet rechargeWallet : rechargeWalletList) {
			rechargeWallet.setStatus("P");
		}
		rechargeWalletRepo.saveAll(rechargeWalletList);
		wallet.setRoleType(roleType);
		return this.walletRepository.save(wallet);
	}

	@Override
	public Wallet addWalletAmtUserByShop(Wallet wallet) {
		return this.walletRepository.save(wallet);
	}

	@Override
	public List<Wallet> getAllWalletAmt(Wallet wallet) {
		return this.walletRepository.findAll();
	}

	@Override
	public RechargeWallet addRechargeWallet(RechargeWallet rechargeWallet) {
		return rechargeWalletRepo.save(rechargeWallet);
	}

	@Override
	public Optional<RechargeWallet> editRechargeWallet(Long id) {
		return rechargeWalletRepo.findById(id);
	}

	@Override
	public Wallet findByUser(User user) {
		return walletRepository.findByUser(user);
	}

	@Transactional
	@Override
	public void updateAmountByUser(double walletAmount, User user, Long id) {
		rechargeWalletRepo.updateAmountStatusByUser("P", id);
		walletRepository.updateAmountByUser(walletAmount, user);
	}

	@Override
	public Wallet findByUserAndRoleType(User user, String roleType) {
		return walletRepository.findByUserAndRoleType(user, roleType);
	}

}
