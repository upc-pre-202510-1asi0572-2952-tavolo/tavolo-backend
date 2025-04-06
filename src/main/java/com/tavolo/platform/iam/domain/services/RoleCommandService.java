package com.tavolo.platform.iam.domain.services;

import com.tavolo.platform.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
