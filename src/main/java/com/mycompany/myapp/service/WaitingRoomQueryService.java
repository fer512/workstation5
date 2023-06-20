package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.WaitingRoom;
import com.mycompany.myapp.repository.WaitingRoomRepository;
import com.mycompany.myapp.service.criteria.WaitingRoomCriteria;
import com.mycompany.myapp.service.dto.WaitingRoomDTO;
import com.mycompany.myapp.service.mapper.WaitingRoomMapper;
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
 * Service for executing complex queries for {@link WaitingRoom} entities in the database.
 * The main input is a {@link WaitingRoomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WaitingRoomDTO} or a {@link Page} of {@link WaitingRoomDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WaitingRoomQueryService extends QueryService<WaitingRoom> {

    private final Logger log = LoggerFactory.getLogger(WaitingRoomQueryService.class);

    private final WaitingRoomRepository waitingRoomRepository;

    private final WaitingRoomMapper waitingRoomMapper;

    public WaitingRoomQueryService(WaitingRoomRepository waitingRoomRepository, WaitingRoomMapper waitingRoomMapper) {
        this.waitingRoomRepository = waitingRoomRepository;
        this.waitingRoomMapper = waitingRoomMapper;
    }

    /**
     * Return a {@link List} of {@link WaitingRoomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WaitingRoomDTO> findByCriteria(WaitingRoomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WaitingRoom> specification = createSpecification(criteria);
        return waitingRoomMapper.toDto(waitingRoomRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WaitingRoomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WaitingRoomDTO> findByCriteria(WaitingRoomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WaitingRoom> specification = createSpecification(criteria);
        return waitingRoomRepository.findAll(specification, page).map(waitingRoomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WaitingRoomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WaitingRoom> specification = createSpecification(criteria);
        return waitingRoomRepository.count(specification);
    }

    /**
     * Function to convert {@link WaitingRoomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WaitingRoom> createSpecification(WaitingRoomCriteria criteria) {
        Specification<WaitingRoom> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WaitingRoom_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), WaitingRoom_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), WaitingRoom_.status));
            }
            if (criteria.getAttencionChannelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAttencionChannelId(),
                            root -> root.join(WaitingRoom_.attencionChannel, JoinType.LEFT).get(WaitingRoomAttencionChannel_.id)
                        )
                    );
            }
            if (criteria.getWorkerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWorkerId(), root -> root.join(WaitingRoom_.workers, JoinType.LEFT).get(Worker_.id))
                    );
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompanyId(), root -> root.join(WaitingRoom_.company, JoinType.LEFT).get(Company_.id))
                    );
            }
            if (criteria.getBranchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBranchId(), root -> root.join(WaitingRoom_.branches, JoinType.LEFT).get(Branch_.id))
                    );
            }
        }
        return specification;
    }
}
