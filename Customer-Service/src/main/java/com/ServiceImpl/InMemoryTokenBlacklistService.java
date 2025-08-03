package com.ServiceImpl;

import org.springframework.stereotype.Service;

import com.Service.TokenBlacklistService;

import java.util.HashSet;
import java.util.Set;

@Service
public class InMemoryTokenBlacklistService implements TokenBlacklistService {

    private final Set<String> blacklistedUsers = new HashSet<>();

    @Override
    public void blacklistToken(String email) {
        blacklistedUsers.add(email);
    }

    @Override
    public boolean isBlacklisted(String email) {
        return blacklistedUsers.contains(email);
    }
}
