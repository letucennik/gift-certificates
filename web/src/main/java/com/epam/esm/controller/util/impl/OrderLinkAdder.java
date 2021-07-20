package com.epam.esm.controller.util.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.UserService;
import com.epam.esm.controller.util.LinkAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class OrderLinkAdder extends AbstractLinkAdder<OrderDto> {

    private static final Class<UserController> CONTROLLER = UserController.class;

    private final LinkAdder<UserDto> userDtoLinkAdder;
    private final LinkAdder<GiftCertificateDto> certificateDtoLinkAdder;

    private final UserService userService;

    public OrderLinkAdder(LinkAdder<UserDto> userDtoLinkAdder,
                          LinkAdder<GiftCertificateDto> certificateDtoLinkAdder,
                          UserService userService) {
        this.userDtoLinkAdder = userDtoLinkAdder;
        this.certificateDtoLinkAdder = certificateDtoLinkAdder;
        this.userService = userService;
    }

    @Override
    public void addLinks(OrderDto entity) {
        UserDto userDto = entity.getUser();
        entity.add(linkTo(CONTROLLER)
                .slash(userDto.getId())
                .slash("orders")
                .slash(entity.getId())
                .withRel(SELF_LINK_NAME));
        userDtoLinkAdder.addLinks(userDto);
        entity.getCertificatesDto().forEach(certificateDtoLinkAdder::addLinks);
    }
}
