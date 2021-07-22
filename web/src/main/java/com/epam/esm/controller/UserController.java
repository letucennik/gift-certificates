package com.epam.esm.controller;

import com.epam.esm.controller.util.LinkAdder;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.TokenDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rest/v1/users")
public class UserController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final LinkAdder<UserDto> userDtoLinkAdder;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    public UserController(UserService userService, LinkAdder<UserDto> userDtoLinkAdder, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.userDtoLinkAdder = userDtoLinkAdder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/sign_up")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public TokenDto signUp(@RequestBody UserDto userDto) {
        return userDetailsService.signUp(userDto);
    }

    @PostMapping("/log_in")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public TokenDto login(@RequestBody UserDto userDto) {
        return userDetailsService.logIn(userDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        List<UserDto> users = userService.getAll(page, size);
        return users.stream()
                .peek(userDtoLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public UserDto getById(@PathVariable long id) {
        UserDto userDto = userService.read(id);
        userDtoLinkAdder.addLinks(userDto);
        return userDto;
    }
}
