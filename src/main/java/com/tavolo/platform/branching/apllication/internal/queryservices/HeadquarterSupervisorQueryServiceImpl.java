package com.tavolo.platform.branching.apllication.internal.queryservices;

import com.tavolo.platform.branching.apllication.internal.outboundedservice.acl.ExternalIamService;
import com.tavolo.platform.branching.domain.model.queries.GetAllHeadquarterSupervisorByIdHeadquarter;
import com.tavolo.platform.branching.domain.model.valueobjects.HeadquarterData;
import com.tavolo.platform.branching.domain.services.HeadquarterSupervisorQueryService;
import com.tavolo.platform.branching.infrastructure.persistence.jpa.repositories.HeadquarterRepository;
import com.tavolo.platform.branching.infrastructure.persistence.jpa.repositories.HeadquarterSupervisorRepository;
import com.tavolo.platform.shared.application.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HeadquarterSupervisorQueryServiceImpl implements HeadquarterSupervisorQueryService {

    private final HeadquarterRepository headquarterRepository;
    private final HeadquarterSupervisorRepository headquarterSupervisorRepository;
    private final ExternalIamService externalIamService;
    private static final Logger logger = LoggerFactory.getLogger(HeadquarterSupervisorQueryServiceImpl.class);

    public HeadquarterSupervisorQueryServiceImpl(
            HeadquarterRepository headquarterRepository,
            HeadquarterSupervisorRepository headquarterSupervisorRepository,
            ExternalIamService externalIamService) {
        this.headquarterRepository = headquarterRepository;
        this.headquarterSupervisorRepository = headquarterSupervisorRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public List<HeadquarterData> handle(GetAllHeadquarterSupervisorByIdHeadquarter query) {
        logger.info("Procesando consulta para obtener supervisores de la sede con ID {}", query.headquarterId());

        boolean headquarterExists = headquarterRepository.existsById(query.headquarterId());
        if (!headquarterExists) {
            logger.warn("Sede con ID {} no encontrada", query.headquarterId());
            throw new ResourceNotFoundException("Sede " + query.headquarterId());
        }

        var supervisors = headquarterSupervisorRepository.findByHeadquarterId(query.headquarterId());
        logger.info("Encontrados {} supervisores para la sede con ID {}", supervisors.size(), query.headquarterId());

        return supervisors.stream()
                .map(supervisor -> {
                    Long userId = supervisor.getUserId().userId();
                    String username = externalIamService.getUsernameById(userId);
                    return new HeadquarterData(userId, username, query.headquarterId());
                })
                .collect(Collectors.toList());
    }
}