package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AttencionChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AttencionChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttencionChannelRepository extends JpaRepository<AttencionChannel, Long> {}
