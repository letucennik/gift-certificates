package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.query.SortContext;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/v1/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.create(giftCertificateDto);
    }

    @GetMapping("{id}")
    public GiftCertificate read(@PathVariable long id) {
        return giftCertificateService.read(id);
    }

    @PatchMapping("{id}")
    public GiftCertificateDto update(@PathVariable long id,
                                     @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.update(id, giftCertificateDto);
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
            @RequestParam(name = "order", required = false) List<SortContext.OrderType> orderTypes) {
        return giftCertificateService.findByParameters(tagName, partInfo, new SortContext(sortColumns, orderTypes));
    }
}
