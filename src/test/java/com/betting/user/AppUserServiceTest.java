package com.betting.user;

import com.betting.user.AppUserService;
import com.betting.user.admin.Admin;
import com.betting.user.admin.AdminService;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class AppUserServiceTest {
    @Mock
    private PlayerService playerService;
    @Mock
    private AdminService adminService;
    @InjectMocks
    private AppUserService appUserService;
    @Value("${test.username}")
    private final String USERNAME = "mozolden7@gmail.com";

    @Test
    void testLoadUserByUsernamePlayerFound() {
        when(playerService.loadPlayerByUsername(USERNAME)).thenReturn(Optional.of(new Player()));
        when(adminService.loadAdminByUsername(USERNAME)).thenReturn(Optional.empty());
        UserDetails appUser = appUserService.loadUserByUsername(USERNAME);
        assertNotNull(appUser);
    }

    @Test
    void testLoadUserByUsernameAdminFound() {
        when(playerService.loadPlayerByUsername(USERNAME)).thenReturn(Optional.empty());
        when(adminService.loadAdminByUsername(USERNAME)).thenReturn(Optional.of(new Admin()));
        UserDetails appUser = appUserService.loadUserByUsername(USERNAME);
        assertNotNull(appUser);
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(playerService.loadPlayerByUsername(USERNAME)).thenReturn(Optional.empty());
        when(adminService.loadAdminByUsername(USERNAME)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername(USERNAME));
    }

}