package com.example.wms.infrastructure.security.domain;

import com.example.wms.user.application.domain.enums.UserRole;
import com.example.wms.user.application.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<>();

        if (this.user.getUserRole().equals(UserRole.ROLE_ADMIN)) {
            auth.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            auth.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return auth;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    public UserRole getUserRole() {
        return this.user.getUserRole();
    }

    @Override
    public String getUsername() {
        return this.user.getStaffNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
