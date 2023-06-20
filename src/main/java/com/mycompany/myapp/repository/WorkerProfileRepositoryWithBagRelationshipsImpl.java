package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WorkerProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class WorkerProfileRepositoryWithBagRelationshipsImpl implements WorkerProfileRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<WorkerProfile> fetchBagRelationships(Optional<WorkerProfile> workerProfile) {
        return workerProfile.map(this::fetchBranches);
    }

    @Override
    public Page<WorkerProfile> fetchBagRelationships(Page<WorkerProfile> workerProfiles) {
        return new PageImpl<>(
            fetchBagRelationships(workerProfiles.getContent()),
            workerProfiles.getPageable(),
            workerProfiles.getTotalElements()
        );
    }

    @Override
    public List<WorkerProfile> fetchBagRelationships(List<WorkerProfile> workerProfiles) {
        return Optional.of(workerProfiles).map(this::fetchBranches).orElse(Collections.emptyList());
    }

    WorkerProfile fetchBranches(WorkerProfile result) {
        return entityManager
            .createQuery(
                "select workerProfile from WorkerProfile workerProfile left join fetch workerProfile.branches where workerProfile.id = :id",
                WorkerProfile.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<WorkerProfile> fetchBranches(List<WorkerProfile> workerProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, workerProfiles.size()).forEach(index -> order.put(workerProfiles.get(index).getId(), index));
        List<WorkerProfile> result = entityManager
            .createQuery(
                "select workerProfile from WorkerProfile workerProfile left join fetch workerProfile.branches where workerProfile in :workerProfiles",
                WorkerProfile.class
            )
            .setParameter("workerProfiles", workerProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
