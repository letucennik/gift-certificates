package com.epam.esm.controller.util.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.service.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagLinkAdder extends AbstractLinkAdder<TagDto> {

    private static final Class<TagController> CONTROLLER = TagController.class;

    @Override
    public void addLinks(TagDto entity) {
        addIdLinks(CONTROLLER, entity, entity.getId(), SELF_LINK_NAME, DELETE_LINK_NAME);
    }
}
