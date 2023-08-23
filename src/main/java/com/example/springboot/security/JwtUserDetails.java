package com.example.springboot.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtUserDetails implements UserDetails {

    private final String displayName;

    private final String username;

    private final String password;

    private final String emailAddress;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean isEnable;

    public JwtUserDetails(String displayName, String username, String password, String emailAddress, Collection<? extends GrantedAuthority> authorities, boolean isEnable) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.authorities = authorities;
        this.isEnable = isEnable;
    }

    public String getEmailAddress() {
        return emailAddress;
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
        return isEnable;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
