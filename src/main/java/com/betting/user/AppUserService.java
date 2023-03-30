package com.betting.user;

import com.betting.user.admin.AdminService;
import com.betting.user.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final PlayerService playerService;
    private final AdminService adminService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<? extends AppUser> appUser = playerService.loadPlayerByUsername(username);
        if (appUser.isPresent()) {
            return appUser.get();
        } else {
            return adminService.loadAdminByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("user not found!"));
        }
    }
}
