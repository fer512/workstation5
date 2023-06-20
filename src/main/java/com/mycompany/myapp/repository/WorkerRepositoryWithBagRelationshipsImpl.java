package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Worker;
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
public class WorkerRepositoryWithBagRelationshipsImpl implements WorkerRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Worker> fetchBagRelationships(Optional<Worker> worker) {
        return worker.map(this::fetchBranches);
    }

    @Override
    public Page<Worker> fetchBagRelationships(Page<Worker> workers) {
        return new PageImpl<>(fetchBagRelationships(workers.getContent()), workers.getPageable(), workers.getTotalElements());
    }

    @Override
    public List<Worker> fetchBagRelationships(List<Worker> workers) {
        return Optional.of(workers).map(this::fetchBranches).orElse(Collections.emptyList());
    }

    Worker fetchBranches(Worker result) {
        return entityManager
            .createQuery("select worker from Worker worker left join fetch worker.branches where worker.id = :id", Worker.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Worker> fetchBranches(List<Worker> workers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, workers.size()).forEach(index -> order.put(workers.get(index).getId(), index));
        List<Worker> result = entityManager
            .createQuery("select worker from Worker worker left join fetch worker.branches where worker in :workers", Worker.class)
            .setParameter("workers", workers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
