package org.example.domain.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private Boolean isLoggedIn;
    private String phone;
    private String bvn;
    private String profilePictureUrl;


    public String getFullName() {
        return firstName + " " + lastName;
    }

}
