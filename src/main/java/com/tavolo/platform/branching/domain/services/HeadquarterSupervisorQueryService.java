package com.tavolo.platform.branching.domain.services;

import com.tavolo.platform.branching.domain.model.queries.GetAllHeadquarterSupervisorByIdHeadquarter;
import com.tavolo.platform.branching.domain.model.queries.GetHeadquarterIdBySupervisorId;
import com.tavolo.platform.branching.domain.model.valueobjects.HeadquarterData;

import java.util.List;
import java.util.Optional;

public interface HeadquarterSupervisorQueryService {
    List<HeadquarterData> handle(GetAllHeadquarterSupervisorByIdHeadquarter query);
    Optional<HeadquarterData> handle(GetHeadquarterIdBySupervisorId headquarterData);
}
