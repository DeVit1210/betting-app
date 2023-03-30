package com.betting.security.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import static com.betting.security.config.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    PLAYER(new ArrayList<>(List.of(MAKE_BET))),
    ADMIN(new ArrayList<>(List.of(ADD_EVENT, SET_RESULTS)));

    List<ApplicationUserPermission> permissions;

    ApplicationUserRole(List<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getPermissions() {
        return permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .toList();
    }
}
