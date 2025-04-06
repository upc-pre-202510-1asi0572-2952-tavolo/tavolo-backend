package com.tavolo.platform.iam.domain.model.queries;

import com.tavolo.platform.iam.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(Roles name) {
}
