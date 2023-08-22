package com.example.springboot.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtUserDetails implements UserDetails {

    private final String displayName;

    private final String username;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean isDisable;

    public JwtUserDetails(String displayName, String username,
                          String password, Collection<? extends GrantedAuthority> authorities, boolean isDisable) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isDisable = isDisable;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getDisplayName() {
        return displayName;
    }


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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isDisable;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDisable;
    }
}
