package com.fastshop.utility;

import javax.servlet.http.HttpServletRequest;

public class EmailUtility {
	
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL= request.getRequestURI().toString();
//		return siteURL.replace(request.getServletPath(), "http://localhost:8080");
		return siteURL.replace(request.getServletPath(), "http://fasastshop.shop");
	}

}
