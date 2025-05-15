package com.tavolo.platform.branching.domain.services;

import com.tavolo.platform.branching.domain.model.queries.GetAllHeadquarterSupervisorByIdHeadquarter;
import com.tavolo.platform.branching.domain.model.queries.GetAllHeadquartersQuery;
import com.tavolo.platform.branching.domain.model.valueobjects.HeadquarterData;

import java.util.List;

public interface HeadquarterSupervisorQueryService {
    List<HeadquarterData> handle(GetAllHeadquarterSupervisorByIdHeadquarter query);
}
