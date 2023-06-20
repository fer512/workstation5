package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Queue;
import com.mycompany.myapp.repository.QueueRepository;
import com.mycompany.myapp.service.criteria.QueueCriteria;
import com.mycompany.myapp.service.dto.QueueDTO;
import com.mycompany.myapp.service.mapper.QueueMapper;
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
 * Service for executing complex queries for {@link Queue} entities in the database.
 * The main input is a {@link QueueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QueueDTO} or a {@link Page} of {@link QueueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QueueQueryService extends QueryService<Queue> {

    private final Logger log = LoggerFactory.getLogger(QueueQueryService.class);

    private final QueueRepository queueRepository;

    private final QueueMapper queueMapper;

    public QueueQueryService(QueueRepository queueRepository, QueueMapper queueMapper) {
        this.queueRepository = queueRepository;
        this.queueMapper = queueMapper;
    }

    /**
     * Return a {@link List} of {@link QueueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QueueDTO> findByCriteria(QueueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Queue> specification = createSpecification(criteria);
        return queueMapper.toDto(queueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QueueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QueueDTO> findByCriteria(QueueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Queue> specification = createSpecification(criteria);
        return queueRepository.findAll(specification, page).map(queueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QueueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Queue> specification = createSpecification(criteria);
        return queueRepository.count(specification);
    }

    /**
     * Function to convert {@link QueueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Queue> createSpecification(QueueCriteria criteria) {
        Specification<Queue> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Queue_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Queue_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Queue_.status));
            }
            if (criteria.getAttencionChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAttencionChannelId(),
                            root -> root.join(Queue_.attencionChannel, JoinType.LEFT).get(AttencionChannel_.id)
                        )
                    );
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompanyId(), root -> root.join(Queue_.company, JoinType.LEFT).get(Company_.id))
                    );
            }
            if (criteria.getBranchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBranchId(), root -> root.join(Queue_.branches, JoinType.LEFT).get(Branch_.id))
                    );
            }
        }
        return specification;
    }
}
