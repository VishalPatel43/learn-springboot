package com.springboot.coding.securityApplication.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.coding.securityApplication.entities.enums.Role;
import com.springboot.coding.securityApplication.entities.enums.SubscriptionPlan;
import com.springboot.coding.securityApplication.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "users",
//        indexes = @Index(name = "idx_email", columnList = "email"),
        uniqueConstraints = @UniqueConstraint(name = "uc_email", columnNames = "email"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email; // Username for authentication

    @JsonIgnore // Prevent password from being serialized
    private String password;

    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // Approach 1
        return roles.stream()
                .flatMap(role -> Stream.concat(
                        PermissionMapping.getAuthoritiesForRole(role).stream(),
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
                ))
                .collect(Collectors.toSet());

        /* Approach 2
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> {
            Set<SimpleGrantedAuthority> permissions = PermissionMapping.getAuthoritiesForRole(role);
            authorities.addAll(permissions);
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        });
        return authorities;
        */
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
