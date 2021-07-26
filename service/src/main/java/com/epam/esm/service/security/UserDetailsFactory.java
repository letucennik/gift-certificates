package com.epam.esm.service.security;

import com.epam.esm.repository.entity.UserRole;
import com.epam.esm.service.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsFactory {

    public static UserDetails create(UserDto user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getName(),
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
