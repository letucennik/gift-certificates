package com.epam.esm.service.dto;

import com.epam.esm.repository.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

    private long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private UserRole userRole;
}
