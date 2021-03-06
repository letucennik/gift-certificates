package com.epam.esm.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.controller.util.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1/tags")
public class TagController {

    private final TagService tagService;
    private final LinkAdder<TagDto> tagDtoLinkAdder;

    @Autowired
    public TagController(TagService tagService, LinkAdder<TagDto> tagDtoLinkAdder) {
        this.tagService = tagService;
        this.tagDtoLinkAdder = tagDtoLinkAdder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public TagDto create(@RequestBody TagDto tag) {
        TagDto tagDto = tagService.create(tag);
        tagDtoLinkAdder.addLinks(tagDto);
        return tagDto;
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public TagDto read(@PathVariable long id) {
        TagDto tagDto = tagService.read(id);
        tagDtoLinkAdder.addLinks(tagDto);
        return tagDto;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteById(@PathVariable long id) {
        tagService.delete(id);
    }

    @GetMapping("/most-used-tag")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public TagDto getMostUsedTagOfUserWithHighestCostOfOrders() {
        return tagService.getMostWidelyUsedTag();
    }

}
