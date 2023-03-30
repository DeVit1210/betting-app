package com.betting.user.admin;

import com.betting.security.config.ApplicationUserRole;
import com.betting.user.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "admins")
@SuperBuilder
@NoArgsConstructor
@Getter
public class Admin extends AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String secretCode;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(ApplicationUserRole.ADMIN.getPermissions());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + ApplicationUserRole.ADMIN.name()));
        return authorities;
    }
}
