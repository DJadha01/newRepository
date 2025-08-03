package com.Exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAlreadyExist extends RuntimeException {

	public UserAlreadyExist(String msg) {
		super(msg);
		log.debug(msg);
		
	}
	
}
