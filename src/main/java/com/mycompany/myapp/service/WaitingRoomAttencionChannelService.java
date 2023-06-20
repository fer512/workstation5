package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.WaitingRoomAttencionChannelDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.WaitingRoomAttencionChannel}.
 */
public interface WaitingRoomAttencionChannelService {
    /**
     * Save a waitingRoomAttencionChannel.
     *
     * @param waitingRoomAttencionChannelDTO the entity to save.
     * @return the persisted entity.
     */
    WaitingRoomAttencionChannelDTO save(WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO);

    /**
     * Updates a waitingRoomAttencionChannel.
     *
     * @param waitingRoomAttencionChannelDTO the entity to update.
     * @return the persisted entity.
     */
    WaitingRoomAttencionChannelDTO update(WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO);

    /**
     * Partially updates a waitingRoomAttencionChannel.
     *
     * @param waitingRoomAttencionChannelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WaitingRoomAttencionChannelDTO> partialUpdate(WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO);

    /**
     * Get all the waitingRoomAttencionChannels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WaitingRoomAttencionChannelDTO> findAll(Pageable pageable);

    /**
     * Get all the WaitingRoomAttencionChannelDTO where WaitingRoom is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<WaitingRoomAttencionChannelDTO> findAllWhereWaitingRoomIsNull();

    /**
     * Get the "id" waitingRoomAttencionChannel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WaitingRoomAttencionChannelDTO> findOne(Long id);

    /**
     * Delete the "id" waitingRoomAttencionChannel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
