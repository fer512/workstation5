package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Queue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface QueueRepositoryWithBagRelationships {
    Optional<Queue> fetchBagRelationships(Optional<Queue> queue);

    List<Queue> fetchBagRelationships(List<Queue> queues);

    Page<Queue> fetchBagRelationships(Page<Queue> queues);
}
