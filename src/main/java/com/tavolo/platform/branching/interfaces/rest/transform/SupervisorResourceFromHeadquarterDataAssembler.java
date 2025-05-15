package com.tavolo.platform.branching.interfaces.rest.transform;

import com.tavolo.platform.branching.domain.model.valueobjects.HeadquarterData;
import com.tavolo.platform.branching.interfaces.rest.resources.SupervisorResource;

public class SupervisorResourceFromHeadquarterDataAssembler {

    public static SupervisorResource toResourceFromEntity(HeadquarterData data) {
        return new SupervisorResource(
                data.userId(),
                data.username(),
                data.headquarterId()
        );
    }
}