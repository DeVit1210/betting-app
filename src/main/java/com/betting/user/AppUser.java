package com.betting.user;

import com.betting.events.betting_entity.BettingEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder
public abstract class AppUser implements UserDetails, BettingEntity {
    private String username;
    private String password;
    private String fullName;
    private Boolean isNonLocked = true;
    private Boolean isEnabled = false;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO: make it clear and fix
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO: make it clear and fix
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}