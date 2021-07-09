package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import com.epam.esm.util.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/users")
public class UserController {

    private final UserService userService;
    private final LinkAdder<UserDto> userDtoLinkAdder;

    @Autowired
    public UserController(UserService userService, LinkAdder<UserDto> userDtoLinkAdder) {
        this.userService = userService;
        this.userDtoLinkAdder = userDtoLinkAdder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto userDto) {
        UserDto dto = userService.create(userDto);
        userDtoLinkAdder.addLinks(dto);
        return dto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        List<UserDto> users = userService.getAll(page, size);
        return users.stream()
                .peek(userDtoLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getById(@PathVariable long id) {
        UserDto userDto = userService.read(id);
        userDtoLinkAdder.addLinks(userDto);
        return userDto;
    }
}
