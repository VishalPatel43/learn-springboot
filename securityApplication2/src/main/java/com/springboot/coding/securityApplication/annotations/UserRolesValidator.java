package com.springboot.coding.securityApplication.annotations;

import com.springboot.coding.securityApplication.entities.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class UserRolesValidator implements ConstraintValidator<UserRoleValidation, Set<Role>> {

    @Override
    public boolean isValid(Set<Role> roles, ConstraintValidatorContext context) {
        // Return true if roles set is empty or null (depending on whether these are allowed)
        /* allow the set to be empty

        if (roles == null || roles.isEmpty()) {
            return true; // Or return false if an empty set should be invalid
        }
*/
        // Allow the set to be empty or null
        if (roles == null || roles.isEmpty())
            return true;

        // Check if all roles in the set are valid
        for (Role role : roles)
            if (role != Role.USER && role != Role.ADMIN && role != Role.CREATOR)
                return false;

        return true;
    }
}
