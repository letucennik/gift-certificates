package com.epam.esm.service.security;

import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.TokenDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserDetailsServiceImpl(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenDto logIn(UserDto userDto) {
        String name = userDto.getName();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, userDto.getPassword()));
        UserDto user = userService.findByName(name);
        String token = jwtTokenProvider.createToken(user);
        return new TokenDto(name, token, jwtTokenProvider.getValidityInMilliseconds());
    }

    public TokenDto signUp(UserDto userDto) {
        UserDto registeredUser = userService.register(userDto);
        String token = jwtTokenProvider.createToken(registeredUser);
        return new TokenDto(userDto.getName(), token, jwtTokenProvider.getValidityInMilliseconds());
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDto user = userService.findByName(s);
        return UserDetailsFactory.create(user);
    }
}
