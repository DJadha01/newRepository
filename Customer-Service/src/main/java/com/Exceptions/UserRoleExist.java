package com.Exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRoleExist extends RuntimeException {

	public UserRoleExist(String msg) {
		super(msg);
		log.debug(msg);
		
	}
}
