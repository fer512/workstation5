package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkerProfileAttencionChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkerProfileAttencionChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkerProfileAttencionChannelRepository extends JpaRepository<WorkerProfileAttencionChannel, Long> {}
