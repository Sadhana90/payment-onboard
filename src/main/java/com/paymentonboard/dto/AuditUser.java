package com.paymentonboard.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class AuditUser implements UserDetails {

    private String userName;
    private String id;
    private List<String> roles = new ArrayList<>();

    public AuditUser(String id, String userName, List<String> roles) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
    }

    public AuditUser(String userName, String roles) {
        this.userName = userName;
        this.roles.add(roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        roles.forEach(role -> list.add(new SimpleGrantedAuthority(role)));
        return list;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Long getId() {
        return Long.parseLong(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
