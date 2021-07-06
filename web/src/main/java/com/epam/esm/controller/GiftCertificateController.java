package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.repository.query.SortContext;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final LinkAdder<GiftCertificateDto> certificateDtoLinkAdder;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, LinkAdder<GiftCertificateDto> certificateDtoLinkAdder) {
        this.giftCertificateService = giftCertificateService;
        this.certificateDtoLinkAdder = certificateDtoLinkAdder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto dto = giftCertificateService.create(giftCertificateDto);
        certificateDtoLinkAdder.addLinks(dto);
        return dto;
    }

    @GetMapping("{id}")
    public GiftCertificateDto read(@PathVariable long id) {
        GiftCertificateDto dto = giftCertificateService.read(id);
        certificateDtoLinkAdder.addLinks(dto);
        return dto;
    }

    @PatchMapping("{id}")
    public GiftCertificateDto update(@PathVariable long id,
                                     @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto dto = giftCertificateService.update(id, giftCertificateDto);
        certificateDtoLinkAdder.addLinks(dto);
        return dto;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        giftCertificateService.delete(id);
    }

    @GetMapping
    public List<GiftCertificateDto> findByParameters(
            @RequestParam(name = "tag_name", required = false) String tagName,
            @RequestParam(name = "part_info", required = false) String partInfo,
            @RequestParam(name = "sort", required = false) List<String> sortColumns,
            @RequestParam(name = "order", required = false) List<SortContext.OrderType> orderTypes,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        List<GiftCertificateDto> certificates = giftCertificateService.findByParameters(tagName, partInfo, new SortContext(sortColumns, orderTypes), page, size);
        return certificates.stream().peek(certificateDtoLinkAdder::addLinks).collect(Collectors.toList());
    }
}
