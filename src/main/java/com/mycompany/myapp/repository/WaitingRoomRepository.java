package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WaitingRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WaitingRoom entity.
 *
 * When extending this class, extend WaitingRoomRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface WaitingRoomRepository
    extends WaitingRoomRepositoryWithBagRelationships, JpaRepository<WaitingRoom, Long>, JpaSpecificationExecutor<WaitingRoom> {
    default Optional<WaitingRoom> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<WaitingRoom> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<WaitingRoom> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
