package com.tavolo.platform.branching.domain.model.commands;

import com.tavolo.platform.branching.domain.model.valueobjects.UserId;

public record AddSupervisorToHeadquarterCommand(Long headquarterId, UserId userId) {
}
