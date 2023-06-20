package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkerProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface WorkerProfileRepositoryWithBagRelationships {
    Optional<WorkerProfile> fetchBagRelationships(Optional<WorkerProfile> workerProfile);

    List<WorkerProfile> fetchBagRelationships(List<WorkerProfile> workerProfiles);

    Page<WorkerProfile> fetchBagRelationships(Page<WorkerProfile> workerProfiles);
}
