package com.tavolo.platform.iam.interfaces.rest.transform;

import com.tavolo.platform.iam.domain.model.aggregates.User;
import com.tavolo.platform.iam.domain.model.entities.Role;
import com.tavolo.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User entity, String token) {
        var roles = entity.getRoles().stream().map(Role::getStringName).toList();
        return new AuthenticatedUserResource(entity.getId(), entity.getUsername(), token, roles);
    }
}
