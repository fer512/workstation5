package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OrderQueue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderQueue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderQueueRepository extends JpaRepository<OrderQueue, Long> {}
