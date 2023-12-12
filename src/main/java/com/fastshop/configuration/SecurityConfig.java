package com.fastshop.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fastshop.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}

//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//		
//	}

//	show password
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance() ;
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

//	 configure method
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		auth.inMemoryAuthentication().withUser("admn").password(this.passwordEncoder().encode("admn")).roles("ADMIN");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/amazon/images/**");
		web.ignoring().antMatchers("/amazon/latest_image/**");
		web.ignoring().antMatchers("/img/**");
		web.ignoring().antMatchers("/assets/css/**");
		web.ignoring().antMatchers("/assets/js/**");
		web.ignoring().antMatchers("/assets/img/slider/**");
		web.ignoring().antMatchers("/assets/img/product/offer/**");
		web.ignoring().antMatchers("/assets/img/product/**");
		web.ignoring().antMatchers("/assets/img/blog/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/resources/*").permitAll().antMatchers("/addproductimg/**").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER")
				.antMatchers("/amazonviewonly/app.js", "/dashboard/assets/css/style.css",
						"/https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css",
						"/dashboard/istockphoto.jpg", "/amazonviewonly/fsast_shop.png")
				.permitAll()
				.antMatchers("/assets/css/main.css", "/assets/css/bootstrap.css", "/assets/img/login/login-shape-1.png",
						"/assets/img/login/login-shape-2.png", "/assets/img/login/login-shape-3.png",
						"/assets/img/login/login-shape-4.png", "/assets/js/main.js", "/assets/js/ajax-form.js",
						"/assets/js/vendor/jquery.js", "/assets/js/vendor/waypoints.js",
						"/assets/js/bootstrap-bundle.js", "/assets/js/meanmenu.js", "/assets/js/swiper-bundle.js",
						"/assets/js/slick.js", "/assets/css/animate.css", "/assets/css/swiper-bundle.css",
						"/assets/css/slick.css", "/assets/css/magnific-popup.css", "/assets/css/font-awesome-pro.css")
				.permitAll()
				.antMatchers("/login", "/register", "/", "/shoplogin", "/panel", "/forgot_password", "/reset_password")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/")
//				.loginPage("/login")
				.loginProcessingUrl("/dologin")
//				.defaultSuccessUrl("/krisshop",true)
				.successHandler(successHandler).and().logout().logoutSuccessUrl("/").logoutUrl("/logout").and().csrf()
				.disable();
	}

	@Component
	public static class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			String logintype = request.getParameter("login");
			String hiddenCaptcha = request.getParameter("hiddenCaptcha");
			String captcha = request.getParameter("captcha");
			HttpSession session = request.getSession();

			// Check if captcha is null or empty
			if (captcha == null || captcha.isBlank()) {

				if ("true".equals(logintype)) {
					setDefaultTargetUrl("/home");
				}

				if ("false".equals(logintype)) {
					setDefaultTargetUrl("/mainpanel");
				}

				if ("onlyy_admin".equals(logintype)) {
					setDefaultTargetUrl("/panel");
					session.setAttribute("popErrorMessage", "Captcha Confirmation Required!!!");
				}
			} else {
				if ("onlyy_admin".equals(logintype) && hiddenCaptcha.equals(captcha)) {
					setDefaultTargetUrl("/verify");
				} else {
					setDefaultTargetUrl("/panel");
					session.setAttribute("popErrorMessage", "Invalid Captcha!!!");
				}
			}
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
}