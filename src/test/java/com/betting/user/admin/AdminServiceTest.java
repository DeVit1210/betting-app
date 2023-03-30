package com.betting.user.admin;

import com.betting.user.admin.Admin;
import com.betting.user.admin.AdminRepository;
import com.betting.user.admin.AdminService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private AdminService adminService;
    @Value("${test.username}")
    private String username;
    @Test
    void testLoadAdminByUsernameSuccess() {
        Admin admin = mock(Admin.class);
        when(adminRepository.findAdminByUsername(anyString())).thenReturn(Optional.of(admin));
        Optional<Admin> foundAdmin = adminService.loadAdminByUsername(username);
        verify(adminRepository, times(1)).findAdminByUsername(username);
        assertTrue(foundAdmin.isPresent());
    }

    @Test
    void tesLoadAdminByUsernameNotFound() {
        when(adminRepository.findAdminByUsername(anyString())).thenReturn(Optional.empty());
        Optional<Admin> foundAdmin = adminService.loadAdminByUsername(username);
        verify(adminRepository, times(1)).findAdminByUsername(username);
        assertFalse(foundAdmin.isPresent());
    }
}