//package com.fastshop.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class ExceptionHandler {
//
//	@ResponseStatus(value = HttpStatus.NOT_FOUND)
//	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
//	public String exceptionHandlerGenric() {
//		return "krisshop_error";
//	}
//
//	@ResponseStatus(value = HttpStatus.NOT_FOUND)
//	@org.springframework.web.bind.annotation.ExceptionHandler({ NumberFormatException.class,
//			NullPointerException.class })
//	public String exceptionHandler() {
//		return "krisshop_error";
//	}
//
//}
