package com.fastshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastshop.Repo.WalletRepository;
import com.fastshop.Repo.WithdrawalRequestRepo;
import com.fastshop.model.Wallet;
import com.fastshop.model.WithdrawalRequest;

@Service
public class WithdrawlRequestService {
	@Autowired
	WithdrawalRequestRepo withdrawalRequestRepo;
	@Autowired
	WalletRepository walletRepository;

	public WithdrawalRequest addWithdrawReq(WithdrawalRequest withdrawalRequest) {
		return this.withdrawalRequestRepo.save(withdrawalRequest);
	}

	public void updateTotalWalletAmt(Long withdrawReqid) {
		WithdrawalRequest withdrawalRequest = withdrawalRequestRepo.findById(withdrawReqid).get();
		Wallet wallet = walletRepository.findByUser(withdrawalRequest.getUser());
		double withdrawlReqAmt = withdrawalRequest.getWithdrawAmout();
		double pendingTotalAmtInWallet = wallet.getWalletAmount() - withdrawlReqAmt;
		withdrawalRequestRepo.updateAmountStatusforWithdraw("A", withdrawReqid);
		walletRepository.updateWalletAmountById(pendingTotalAmtInWallet, wallet.getId());
	}
}
