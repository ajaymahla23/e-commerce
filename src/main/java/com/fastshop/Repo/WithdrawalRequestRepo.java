package com.fastshop.Repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fastshop.model.User;
import com.fastshop.model.WithdrawalRequest;

@Repository
public interface WithdrawalRequestRepo extends JpaRepository<WithdrawalRequest, Long> {

	@Transactional
	@Modifying
	@Query("update WithdrawalRequest w set w.status=?1 where w.id=?2")
	public void updateAmountStatusforWithdraw(String status, Long id);

	public List<WithdrawalRequest> findByUser(User user);

	public WithdrawalRequest findByUserAndStatus(User user, String status);

}
