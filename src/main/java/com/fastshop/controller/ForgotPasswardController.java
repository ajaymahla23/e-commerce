package com.fastshop.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastshop.Repo.UserRepository;
import com.fastshop.model.User;
import com.fastshop.service.CustomerNotFoundException;
import com.fastshop.service.UserService;
import com.fastshop.utility.EmailUtility;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswardController {
	@Autowired
	JavaMailSender mailSender;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;

	@GetMapping("/forgot_password")
	public String forgotPassword() {
		return "dashboard/pages/password/forgotpassword";
	}

	@PostMapping("/forgot_password")
	public String processForgotView(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("==============forgot pwd process============");

		String email = request.getParameter("email");
		System.out.println("email id ;" + email);

		String token = RandomString.make(45);
		try {
			userService.updateResetPassword(token, email);
			String resetPasswordLink = EmailUtility.getSiteURL(request) + "/reset_password?token=" + token;
			System.out.println("reset Password Link :" + resetPasswordLink);
			sendEmail(email, resetPasswordLink);
			redirectAttributes.addFlashAttribute("errorMessage",
					"We have sent a reset password link to your email. Please check.");

		} catch (CustomerNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error while sending email...");
		}
		model.addAttribute("pagetitle", "Forgot Password");

		return "redirect:/";
	}

	private void sendEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("krishop.shop@gmail.com", "FSAST-Shop Alert");
		helper.setTo(email);

		String subject = "Here's the link to reset password";
		String content = "<p>Hello,</p>" + "You have requested to reset your password." + "<p><b><a href=\""
				+ resetPasswordLink + "\">Change your password</a><b></p>";

		helper.setSubject(subject);
		helper.setText(content, true);

		mailSender.send(message);
	}

	@GetMapping("/reset_password")
	public String showResetPassword(@Param("token") String token, Model model) {
		User myuser = userService.get(token);
		if (myuser == null) {
			model.addAttribute("title", "Reset your password");
			model.addAttribute("message", "Invalid token");
			return "message";
		}
		model.addAttribute("token", token);
		model.addAttribute("pageTitle", "Reset your password");
		return "dashboard/pages/password/reset_password";
	}

	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {

		String token = request.getParameter("token");
		String password = request.getParameter("password");
		User myuser = userService.get(token);

		if (myuser == null) {
			model.addAttribute("title", "Reset your password");
			model.addAttribute("message", "Invalid token");
		} else {
			userService.updatePassword(myuser, password);
			model.addAttribute("message", "You have successfully changed your password.");
		}
		return "redirect:/";

	}

}
