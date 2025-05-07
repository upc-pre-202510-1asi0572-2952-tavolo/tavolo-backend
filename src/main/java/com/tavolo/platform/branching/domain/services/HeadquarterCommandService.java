package com.tavolo.platform.branching.domain.services;

import com.tavolo.platform.branching.domain.model.aggregates.Headquarter;
import com.tavolo.platform.branching.domain.model.commands.CreateHeadquarterCommand;

import java.util.Optional;

public interface HeadquarterCommandService {
    Optional<Headquarter> handle(CreateHeadquarterCommand command);
}
