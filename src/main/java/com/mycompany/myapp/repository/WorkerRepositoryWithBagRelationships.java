package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Worker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface WorkerRepositoryWithBagRelationships {
    Optional<Worker> fetchBagRelationships(Optional<Worker> worker);

    List<Worker> fetchBagRelationships(List<Worker> workers);

    Page<Worker> fetchBagRelationships(Page<Worker> workers);
}
