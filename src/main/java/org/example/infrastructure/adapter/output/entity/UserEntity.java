package org.example.infrastructure.adapter.output.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, unique = true)
    private String id;

    @Version
    private Long version;

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String userName;
    private Boolean isLoggedIn;
    private String phone;
    private String bvn;
    private String profilePictureUrl;
}
