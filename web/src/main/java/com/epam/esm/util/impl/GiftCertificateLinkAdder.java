package com.epam.esm.util.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.util.LinkAdder;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateLinkAdder extends AbstractLinkAdder<GiftCertificateDto> {

    private static final Class<GiftCertificateController> CONTROLLER = GiftCertificateController.class;

    private final LinkAdder<TagDto> tagDtoLinkAdder;

    public GiftCertificateLinkAdder(LinkAdder<TagDto> tagDtoLinkAdder) {
        this.tagDtoLinkAdder = tagDtoLinkAdder;
    }

    @Override
    public void addLinks(GiftCertificateDto entity) {
        addIdLinks(CONTROLLER, entity, entity.getId(), SELF_LINK_NAME, UPDATE_LINK_NAME, DELETE_LINK_NAME);
        if (entity.getTags() != null) {
            entity.getTags().forEach(tagDtoLinkAdder::addLinks);
        }
    }
}
