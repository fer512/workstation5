package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Queue;
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
public class QueueRepositoryWithBagRelationshipsImpl implements QueueRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Queue> fetchBagRelationships(Optional<Queue> queue) {
        return queue.map(this::fetchBranches);
    }

    @Override
    public Page<Queue> fetchBagRelationships(Page<Queue> queues) {
        return new PageImpl<>(fetchBagRelationships(queues.getContent()), queues.getPageable(), queues.getTotalElements());
    }

    @Override
    public List<Queue> fetchBagRelationships(List<Queue> queues) {
        return Optional.of(queues).map(this::fetchBranches).orElse(Collections.emptyList());
    }

    Queue fetchBranches(Queue result) {
        return entityManager
            .createQuery("select queue from Queue queue left join fetch queue.branches where queue.id = :id", Queue.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Queue> fetchBranches(List<Queue> queues) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, queues.size()).forEach(index -> order.put(queues.get(index).getId(), index));
        List<Queue> result = entityManager
            .createQuery("select queue from Queue queue left join fetch queue.branches where queue in :queues", Queue.class)
            .setParameter("queues", queues)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
