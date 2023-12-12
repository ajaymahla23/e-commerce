package com.fastshop.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastshop.Repo.UserRepository;
import com.fastshop.model.User;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	SessionFactory sessionFactory;

	public List<User> findUserByEmail() {
		return userRepository.findAll();
	}

	public void addUserRegister(User user) {
		userRepository.save(user);
	}

	public Optional<User> getUserById(Integer id) {
		return userRepository.findById(id);
	}

	public User findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	@Transactional
	public void updateEmailAndPhoneNo(String email, String mobileNumber, String password, Integer id) {
		userRepository.updateEmailAndMobileNumber(email, mobileNumber, password, id);
	}

	public void updateResetPassword(String token, String email) throws CustomerNotFoundException {
		User myUser = userRepository.findByEmail(email);
		if (myUser != null) {
			myUser.setResetPasswordToken(token);
			userRepository.save(myUser);
		} else {
			throw new CustomerNotFoundException("Could not found any coustomer with email " + email);
		}
	}

	public void updateotp(User myUser, String otp) {
//		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		myUser.setResetPasswordToken(null);
		myUser.setOtp(null);
		myUser.setOtpRequestedTime(null);
		userRepository.save(myUser);
	}

	public User get(String resetPasswordToken) {
		return userRepository.findByResetPasswordToken(resetPasswordToken);
	}

	public void updatePassword(User myUser, String newPassword) {
		myUser.setPassword(newPassword);
		myUser.setResetPasswordToken(null);
		myUser.setOtp(null);
		userRepository.save(myUser);
	}

}
