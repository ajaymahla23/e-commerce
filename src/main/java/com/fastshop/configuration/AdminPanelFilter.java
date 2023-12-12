package com.fastshop.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AdminPanelFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		String requestURI = request.getRequestURI();
		if (requestURI.startsWith("/admin-panel") || requestURI.startsWith("/customermanagement")
				|| requestURI.startsWith("/productManagement") || requestURI.startsWith("/recharge_management")
				|| requestURI.startsWith("/store_management") || requestURI.startsWith("/orderhistory")
				|| requestURI.startsWith("/category_management") || requestURI.startsWith("/order_management")
				|| requestURI.startsWith("/address_management") || requestURI.startsWith("/hot_product")
				|| requestURI.startsWith("/search_order") || requestURI.startsWith("/virtual-clients")
				|| requestURI.startsWith("/shoplevel") || requestURI.startsWith("/add_email")
				|| requestURI.startsWith("/comment_management") || requestURI.startsWith("/return_review")
				|| requestURI.startsWith("/productlist") || requestURI.startsWith("/change_email")
				|| requestURI.startsWith("/add_newcategory") || requestURI.startsWith("/search_category")
				|| requestURI.startsWith("/search_ordermanagement") || requestURI.startsWith("/search_address")
				|| requestURI.startsWith("/searchstore") || requestURI.startsWith("/addProductManagement")
				|| requestURI.startsWith("/search_product") || requestURI.startsWith("/search_hotproduct")
				|| requestURI.startsWith("/withdraw_request")) {
			// Check if the user has logged in through the /panel URL
			Boolean loggedInThroughKrisshopPanel = (Boolean) session.getAttribute("loggedInThroughKrisshopPanel");
			if (loggedInThroughKrisshopPanel == null || !loggedInThroughKrisshopPanel) {
				// Redirect the user to an error page or display an error message
//				response.sendRedirect("/404");
				response.sendRedirect("/error");
				return;
			}
		} else if (requestURI.startsWith("/panel")) {
			// Set a session attribute to indicate that the user has logged in through the
			// /panel URL
			logger.debug("loggedInThroughKrisshopPanel: " + session.getAttribute("loggedInThroughKrisshopPanel"));
			session.setAttribute("loggedInThroughKrisshopPanel", true);
		}
		filterChain.doFilter(request, response);
	}

}
