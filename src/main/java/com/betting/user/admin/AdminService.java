package com.betting.user.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Optional<Admin> loadAdminByUsername(String username) {
        return adminRepository.findAdminByUsername(username);
    }
}
