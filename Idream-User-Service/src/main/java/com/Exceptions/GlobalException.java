package com.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.DTO.ErrorHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalException {

	@ExceptionHandler(ResourseFoundException.class)
	public ResponseEntity<ErrorHandler> UserAlreadyExist(ResourseFoundException re){
		
		log.debug(re.getMessage());
		
		return new ResponseEntity(new ErrorHandler().builder().msg(re.getMessage()).build(), HttpStatus.NOT_FOUND);
			
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorHandler> UserNotExist(UserNotFoundException re){
		
		log.debug(re.getMessage());
		
		return new ResponseEntity(new ErrorHandler().builder().msg(re.getMessage()).build(), HttpStatus.NOT_FOUND);
			
	}
	
	@ExceptionHandler(RoleFoundException.class)
	public ResponseEntity<ErrorHandler> RoleAlreadyExist(RoleFoundException re){
		
		log.debug(re.getMessage());
		
		return new ResponseEntity(new ErrorHandler().builder().msg(re.getMessage()).build(), HttpStatus.NOT_FOUND);
			
	}
	
}
