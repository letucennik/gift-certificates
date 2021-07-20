package com.epam.esm.service.security;

import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsFactory {

    public static UserDetails create(User user) {
        return new UserDetailsImpl(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getUserRole())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(UserRole userRole) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole.name()));
        return authorities;
    }
}
