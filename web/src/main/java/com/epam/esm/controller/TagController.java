package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.util.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public TagDto create(@RequestBody TagDto tag) {
        TagDto tagDto = tagService.create(tag);
        tagDtoLinkAdder.addLinks(tagDto);
        return tagDto;
    }

    @GetMapping("{id}")
    public TagDto read(@PathVariable long id) {
        TagDto tagDto = tagService.read(id);
        tagDtoLinkAdder.addLinks(tagDto);
        return tagDto;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        tagService.delete(id);
    }



}
