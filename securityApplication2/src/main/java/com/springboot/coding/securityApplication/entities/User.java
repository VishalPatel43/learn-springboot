package com.springboot.coding.securityApplication.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.coding.securityApplication.entities.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users",
//        indexes = @Index(name = "idx_email", columnList = "email"),
        uniqueConstraints = @UniqueConstraint(name = "uc_email", columnNames = "email"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email; // Username for authentication

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, message = "Password should have at least 4 characters")
    @JsonIgnore // Prevent password from being serialized
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String name;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles;

    /* extra info
    private String address;
    private String gender;
    private String phoneNo;
    private String dob;*/

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                '}';
    }
}
