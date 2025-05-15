package com.tavolo.platform.branching.domain.model.queries;

public record GetAllHeadquarterSupervisorByIdHeadquarter(Long headquarterId) {
    public GetAllHeadquarterSupervisorByIdHeadquarter {
        if (headquarterId == null || headquarterId <= 0) {
            throw new IllegalArgumentException("Headquarter ID must be a positive number.");
        }
    }
}
