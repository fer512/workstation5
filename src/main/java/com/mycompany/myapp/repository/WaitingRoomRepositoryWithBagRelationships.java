package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WaitingRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface WaitingRoomRepositoryWithBagRelationships {
    Optional<WaitingRoom> fetchBagRelationships(Optional<WaitingRoom> waitingRoom);

    List<WaitingRoom> fetchBagRelationships(List<WaitingRoom> waitingRooms);

    Page<WaitingRoom> fetchBagRelationships(Page<WaitingRoom> waitingRooms);
}
