package com.fastshop.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fastshop.Repo.UserRepository;
import com.fastshop.model.CustomUserDetails;
import com.fastshop.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//		  fetching user from DB

		System.out.println("user name : " + username);

		User user = userRepository.getUserByUserName(username, username);
//		User user = userRepository.getUserByUserName(username);
		userRepository.updateOnlineStatus("I", user.getId());

		if (user == null) {
			throw new UsernameNotFoundException("Could not found user!!!");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		return customUserDetails;
	}

}
