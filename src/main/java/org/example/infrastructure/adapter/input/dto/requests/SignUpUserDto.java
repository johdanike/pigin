package org.example.infrastructure.adapter.input.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpUserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String userName;
    private String email;
    private Boolean isVerified;
}
