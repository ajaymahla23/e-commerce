package com.fastshop.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.RechargeWallet;
import com.fastshop.model.User;

@Repository
public interface RechargeWalletRepo extends JpaRepository<RechargeWallet, Long> {

	public List<RechargeWallet> findByStatus(String status);

	public List<RechargeWallet> findByStatusAndId(String status, Long id);

	@Query("select r from RechargeWallet r where r.user.id=:id ")
	public RechargeWallet findByUserId(@Param("id") Integer userId);

	public List<RechargeWallet> findByUser(User user);

	@Query("select r from RechargeWallet r where r.user.id=:id and r.status=:status ")
	public RechargeWallet findByUserIdAndStatus(@Param("id") Integer userId, @Param("status") String status);

//	@Modifying
//	@Query("update RechargeWallet rw set rw.status=?1 where rw.user=?2")
//	public void updateAmountStatusByUser(String status, User user);

	@Modifying
	@Query("update RechargeWallet rw set rw.status=?1 where rw.id=?2")
	public void updateAmountStatusByUser(String status, Long id);

}
