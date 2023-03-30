package com.betting.user.admin;

import com.betting.user.admin.Admin;
import com.betting.user.admin.AdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class AdminRepositoryTest {
    @Autowired
    private AdminRepository adminRepository;
    @Value("${test.password}")
    private String password;
    @Value("${test.username}")
    private String username;
    @Value("${test.adminSecret}")
    private String secretCode;

    @BeforeEach
    void setUp() {
        Admin admin = Admin.builder()
                .username(username)
                .password(password)
                .secretCode(secretCode)
                .build();
        adminRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void testFindByUsernameSuccess() {
        Optional<Admin> admin = adminRepository.findAdminByUsername(username);
        assertThat(admin).isNotEmpty();
    }

    @Test
    void testFindByUsernameNotFound() {
        String wrongUsername = username + "wrong";
        Optional<Admin> admin = adminRepository.findAdminByUsername(wrongUsername);
        assertThat(admin).isEmpty();
    }
}