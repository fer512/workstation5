package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AttencionChannelDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AttencionChannel}.
 */
public interface AttencionChannelService {
    /**
     * Save a attencionChannel.
     *
     * @param attencionChannelDTO the entity to save.
     * @return the persisted entity.
     */
    AttencionChannelDTO save(AttencionChannelDTO attencionChannelDTO);

    /**
     * Updates a attencionChannel.
     *
     * @param attencionChannelDTO the entity to update.
     * @return the persisted entity.
     */
    AttencionChannelDTO update(AttencionChannelDTO attencionChannelDTO);

    /**
     * Partially updates a attencionChannel.
     *
     * @param attencionChannelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AttencionChannelDTO> partialUpdate(AttencionChannelDTO attencionChannelDTO);

    /**
     * Get all the attencionChannels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AttencionChannelDTO> findAll(Pageable pageable);

    /**
     * Get all the AttencionChannelDTO where Queue is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AttencionChannelDTO> findAllWhereQueueIsNull();

    /**
     * Get the "id" attencionChannel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttencionChannelDTO> findOne(Long id);

    /**
     * Delete the "id" attencionChannel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
