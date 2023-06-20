package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.WaitingRoomDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.WaitingRoom}.
 */
public interface WaitingRoomService {
    /**
     * Save a waitingRoom.
     *
     * @param waitingRoomDTO the entity to save.
     * @return the persisted entity.
     */
    WaitingRoomDTO save(WaitingRoomDTO waitingRoomDTO);

    /**
     * Updates a waitingRoom.
     *
     * @param waitingRoomDTO the entity to update.
     * @return the persisted entity.
     */
    WaitingRoomDTO update(WaitingRoomDTO waitingRoomDTO);

    /**
     * Partially updates a waitingRoom.
     *
     * @param waitingRoomDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WaitingRoomDTO> partialUpdate(WaitingRoomDTO waitingRoomDTO);

    /**
     * Get all the waitingRooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WaitingRoomDTO> findAll(Pageable pageable);

    /**
     * Get all the waitingRooms with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WaitingRoomDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" waitingRoom.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WaitingRoomDTO> findOne(Long id);

    /**
     * Delete the "id" waitingRoom.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
