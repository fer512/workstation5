package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WaitingRoomAttencionChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WaitingRoomAttencionChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WaitingRoomAttencionChannelRepository extends JpaRepository<WaitingRoomAttencionChannel, Long> {}
