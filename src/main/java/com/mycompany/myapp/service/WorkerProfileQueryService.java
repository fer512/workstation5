package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.WorkerProfile;
import com.mycompany.myapp.repository.WorkerProfileRepository;
import com.mycompany.myapp.service.criteria.WorkerProfileCriteria;
import com.mycompany.myapp.service.dto.WorkerProfileDTO;
import com.mycompany.myapp.service.mapper.WorkerProfileMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link WorkerProfile} entities in the database.
 * The main input is a {@link WorkerProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkerProfileDTO} or a {@link Page} of {@link WorkerProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkerProfileQueryService extends QueryService<WorkerProfile> {

    private final Logger log = LoggerFactory.getLogger(WorkerProfileQueryService.class);

    private final WorkerProfileRepository workerProfileRepository;

    private final WorkerProfileMapper workerProfileMapper;

    public WorkerProfileQueryService(WorkerProfileRepository workerProfileRepository, WorkerProfileMapper workerProfileMapper) {
        this.workerProfileRepository = workerProfileRepository;
        this.workerProfileMapper = workerProfileMapper;
    }

    /**
     * Return a {@link List} of {@link WorkerProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkerProfileDTO> findByCriteria(WorkerProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkerProfile> specification = createSpecification(criteria);
        return workerProfileMapper.toDto(workerProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WorkerProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkerProfileDTO> findByCriteria(WorkerProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkerProfile> specification = createSpecification(criteria);
        return workerProfileRepository.findAll(specification, page).map(workerProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkerProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkerProfile> specification = createSpecification(criteria);
        return workerProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkerProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkerProfile> createSpecification(WorkerProfileCriteria criteria) {
        Specification<WorkerProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkerProfile_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), WorkerProfile_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), WorkerProfile_.status));
            }
            if (criteria.getAttencionChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAttencionChannelId(),
                            root -> root.join(WorkerProfile_.attencionChannel, JoinType.LEFT).get(WorkerProfileAttencionChannel_.id)
                        )
                    );
            }
            if (criteria.getCallableQueueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCallableQueueId(),
                            root -> root.join(WorkerProfile_.callableQueues, JoinType.LEFT).get(OrderQueue_.id)
                        )
                    );
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(WorkerProfile_.company, JoinType.LEFT).get(Company_.id)
                        )
                    );
            }
            if (criteria.getBranchesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchesId(),
                            root -> root.join(WorkerProfile_.branches, JoinType.LEFT).get(Branch_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
