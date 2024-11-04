package com.springboot.coding.securityApplication.utils;

import com.springboot.coding.securityApplication.entities.enums.Permission;
import com.springboot.coding.securityApplication.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.springboot.coding.securityApplication.entities.enums.Permission.*;
import static com.springboot.coding.securityApplication.entities.enums.Role.*;


/*
 * When User is created, we can assign permissions to the user
 * */
// TODO: Add in OAuth2.0 and also when we signed up we didn't have to mention the "USER" role
//  because it was assigned by default so only the permissions were assigned
// for testing we can add the roles but don't mention the USER roles it will take by default

public class PermissionMapping {

    // Here permissions may be duplicated, but it's okay because we are using Set
    private static final Map<Role, Set<Permission>> map = Map.of(
            USER, Set.of(USER_VIEW, USER_UPDATE, USER_DELETE, USER_CREATE, POST_VIEW),
            CREATOR, Set.of(POST_CREATE, POST_UPDATE, POST_DELETE),
//            ADMIN, Set.of(Set.of(POST_CREATE, USER_UPDATE, POST_UPDATE, USER_DELETE, USER_CREATE, POST_DELETE)) // Admin has all the permissions
            ADMIN, Set.of() // Admin has all the permissions
    );

    private static final Map<Role, Set<Permission>> rolePermissionMap = new HashMap<>();

    static {
        rolePermissionMap.put(USER, Set.of(USER_VIEW, POST_VIEW, USER_UPDATE, USER_DELETE, USER_CREATE));
        rolePermissionMap.put(CREATOR, Set.of(POST_CREATE, POST_UPDATE, POST_DELETE));
//        rolePermissionMap.put(ADMIN, Set.of(POST_CREATE, USER_UPDATE, POST_UPDATE, USER_DELETE, USER_CREATE, POST_DELETE));
        rolePermissionMap.put(ADMIN, Set.of());
    }

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRoleAndPermission(Role role) {
        return rolePermissionMap.getOrDefault(role, Set.of()).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role) {
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}
