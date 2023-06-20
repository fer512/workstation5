package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WaitingRoom;
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
public class WaitingRoomRepositoryWithBagRelationshipsImpl implements WaitingRoomRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<WaitingRoom> fetchBagRelationships(Optional<WaitingRoom> waitingRoom) {
        return waitingRoom.map(this::fetchBranches);
    }

    @Override
    public Page<WaitingRoom> fetchBagRelationships(Page<WaitingRoom> waitingRooms) {
        return new PageImpl<>(
            fetchBagRelationships(waitingRooms.getContent()),
            waitingRooms.getPageable(),
            waitingRooms.getTotalElements()
        );
    }

    @Override
    public List<WaitingRoom> fetchBagRelationships(List<WaitingRoom> waitingRooms) {
        return Optional.of(waitingRooms).map(this::fetchBranches).orElse(Collections.emptyList());
    }

    WaitingRoom fetchBranches(WaitingRoom result) {
        return entityManager
            .createQuery(
                "select waitingRoom from WaitingRoom waitingRoom left join fetch waitingRoom.branches where waitingRoom.id = :id",
                WaitingRoom.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<WaitingRoom> fetchBranches(List<WaitingRoom> waitingRooms) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, waitingRooms.size()).forEach(index -> order.put(waitingRooms.get(index).getId(), index));
        List<WaitingRoom> result = entityManager
            .createQuery(
                "select waitingRoom from WaitingRoom waitingRoom left join fetch waitingRoom.branches where waitingRoom in :waitingRooms",
                WaitingRoom.class
            )
            .setParameter("waitingRooms", waitingRooms)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
