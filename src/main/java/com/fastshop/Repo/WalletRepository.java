package com.fastshop.Repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.User;
import com.fastshop.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

	@Query("select r from Wallet r where r.user.id=:id ")
	public Wallet findByUserId(@Param("id") Integer userId);

	public Wallet findByUser(User user);

	public Wallet findByUserAndRoleType(User user, String roleType);

	@Modifying
	@Query("update Wallet w set w.walletAmount=?1 where w.user=?2")
	void updateAmountByUser(double walletAmount, User user);

	@Transactional
	@Modifying
	@Query("update Wallet w set w.walletAmount=?1 where w.user=?2")
	void updateAmountByUserAndShopbyReturnOrder(double walletAmount, User user);

	@Transactional
	@Modifying
	@Query("update Wallet w set w.walletAmount=?1 where w.id=?2")
	void updateWalletAmountById(double walletAmount, Long id);

	@Transactional
	@Modifying
	@Query("update Wallet w set w.walletAmount=?1 where w.user=?2")
	void updateAmountofSeller(double walletAmount, User user);

}
