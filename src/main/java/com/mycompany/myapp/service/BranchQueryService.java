package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Branch;
import com.mycompany.myapp.repository.BranchRepository;
import com.mycompany.myapp.service.criteria.BranchCriteria;
import com.mycompany.myapp.service.dto.BranchDTO;
import com.mycompany.myapp.service.mapper.BranchMapper;
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
 * Service for executing complex queries for {@link Branch} entities in the database.
 * The main input is a {@link BranchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BranchDTO} or a {@link Page} of {@link BranchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BranchQueryService extends QueryService<Branch> {

    private final Logger log = LoggerFactory.getLogger(BranchQueryService.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    public BranchQueryService(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    /**
     * Return a {@link List} of {@link BranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BranchDTO> findByCriteria(BranchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchMapper.toDto(branchRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BranchDTO> findByCriteria(BranchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchRepository.findAll(specification, page).map(branchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BranchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchRepository.count(specification);
    }

    /**
     * Function to convert {@link BranchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Branch> createSpecification(BranchCriteria criteria) {
        Specification<Branch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Branch_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Branch_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Branch_.status));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), Branch_.language));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompanyId(), root -> root.join(Branch_.company, JoinType.LEFT).get(Company_.id))
                    );
            }
            if (criteria.getWaitingRoomId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWaitingRoomId(),
                            root -> root.join(Branch_.waitingRooms, JoinType.LEFT).get(WaitingRoom_.id)
                        )
                    );
            }
            if (criteria.getQueueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getQueueId(), root -> root.join(Branch_.queues, JoinType.LEFT).get(Queue_.id))
                    );
            }
            if (criteria.getWorkerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWorkerId(), root -> root.join(Branch_.workers, JoinType.LEFT).get(Worker_.id))
                    );
            }
            if (criteria.getWorkerProfilesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWorkerProfilesId(),
                            root -> root.join(Branch_.workerProfiles, JoinType.LEFT).get(WorkerProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
