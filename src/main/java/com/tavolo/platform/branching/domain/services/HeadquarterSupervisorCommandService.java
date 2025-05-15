package com.tavolo.platform.branching.domain.services;

import com.tavolo.platform.branching.domain.model.commands.AddSupervisorToHeadquarterCommand;
import com.tavolo.platform.branching.domain.model.commands.RemoveSupervisorFromHeadquarterCommand;
import com.tavolo.platform.branching.domain.model.valueobjects.HeadquarterData;

import java.util.Optional;

public interface HeadquarterSupervisorCommandService {
    Optional<HeadquarterData> handle(AddSupervisorToHeadquarterCommand command);
    Optional<HeadquarterData> handle(RemoveSupervisorFromHeadquarterCommand command);
}
