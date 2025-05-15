package com.tavolo.platform.branching.domain.model.commands;


import com.tavolo.platform.branching.domain.model.valueobjects.UserId;

public record RemoveSupervisorFromHeadquarterCommand(Long headquarterId, UserId userId) {
}
