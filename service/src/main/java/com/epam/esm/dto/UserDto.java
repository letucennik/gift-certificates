package com.epam.esm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

    private long id;
    private String name;

}
