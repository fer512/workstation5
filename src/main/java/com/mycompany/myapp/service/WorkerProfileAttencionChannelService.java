package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.WorkerProfileAttencionChannelDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.WorkerProfileAttencionChannel}.
 */
public interface WorkerProfileAttencionChannelService {
    /**
     * Save a workerProfileAttencionChannel.
     *
     * @param workerProfileAttencionChannelDTO the entity to save.
     * @return the persisted entity.
     */
    WorkerProfileAttencionChannelDTO save(WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO);

    /**
     * Updates a workerProfileAttencionChannel.
     *
     * @param workerProfileAttencionChannelDTO the entity to update.
     * @return the persisted entity.
     */
    WorkerProfileAttencionChannelDTO update(WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO);

    /**
     * Partially updates a workerProfileAttencionChannel.
     *
     * @param workerProfileAttencionChannelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkerProfileAttencionChannelDTO> partialUpdate(WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO);

    /**
     * Get all the workerProfileAttencionChannels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkerProfileAttencionChannelDTO> findAll(Pageable pageable);

    /**
     * Get all the WorkerProfileAttencionChannelDTO where WorkerProfile is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<WorkerProfileAttencionChannelDTO> findAllWhereWorkerProfileIsNull();

    /**
     * Get the "id" workerProfileAttencionChannel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkerProfileAttencionChannelDTO> findOne(Long id);

    /**
     * Delete the "id" workerProfileAttencionChannel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
