package com.tavolo.platform.iam.interfaces.rest.transform;

import com.tavolo.platform.iam.domain.model.entities.Role;
import com.tavolo.platform.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role entity) {
        return new RoleResource(entity.getId(), entity.getStringName());

    }
}
