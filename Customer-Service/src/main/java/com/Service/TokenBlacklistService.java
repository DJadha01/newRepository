package com.Service;

public interface TokenBlacklistService {

	void blacklistToken(String email); // or void blacklistToken(String jwt);
    boolean isBlacklisted(String email);
}
