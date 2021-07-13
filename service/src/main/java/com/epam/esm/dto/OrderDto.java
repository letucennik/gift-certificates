package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";

    private long id;
    private UserDto user;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime date;
    private BigDecimal cost;
    private List<GiftCertificateDto> certificates = new ArrayList<>();
}
