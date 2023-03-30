package com.betting.security.config;

public enum ApplicationUserPermission {
    MAKE_BET("player:make_bet"),
    ADD_EVENT("admin:add_event"),
    SET_RESULTS("admin:set_results");
    private final String permission;
    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
    public String getPermission() {
        return this.permission;
    }

}
