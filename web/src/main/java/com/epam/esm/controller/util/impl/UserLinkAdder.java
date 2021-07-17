package com.epam.esm.controller.util.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserLinkAdder extends AbstractLinkAdder<UserDto> {

    private static final Class<UserController> CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserDto entity) {
        long id = entity.getId();
        addIdLink(CONTROLLER, entity, entity.getId(), SELF_LINK_NAME);
    }
}
