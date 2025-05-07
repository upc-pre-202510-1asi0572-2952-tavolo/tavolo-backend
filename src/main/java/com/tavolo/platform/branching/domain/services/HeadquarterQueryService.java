package com.tavolo.platform.branching.domain.services;

import com.tavolo.platform.branching.domain.model.aggregates.Headquarter;
import com.tavolo.platform.branching.domain.model.queries.GetAllHeadquartersQuery;
import com.tavolo.platform.branching.domain.model.queries.GetHeadquarterByIdQuery;

import java.util.List;
import java.util.Optional;

public interface HeadquarterQueryService {
    List<Headquarter> handle(GetAllHeadquartersQuery query);
    Optional<Headquarter> handle(GetHeadquarterByIdQuery query);
}
