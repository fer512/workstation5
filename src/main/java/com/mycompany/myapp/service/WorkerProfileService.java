package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.WorkerProfileDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.WorkerProfile}.
 */
public interface WorkerProfileService {
    /**
     * Save a workerProfile.
     *
     * @param workerProfileDTO the entity to save.
     * @return the persisted entity.
     */
    WorkerProfileDTO save(WorkerProfileDTO workerProfileDTO);

    /**
     * Updates a workerProfile.
     *
     * @param workerProfileDTO the entity to update.
     * @return the persisted entity.
     */
    WorkerProfileDTO update(WorkerProfileDTO workerProfileDTO);

    /**
     * Partially updates a workerProfile.
     *
     * @param workerProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkerProfileDTO> partialUpdate(WorkerProfileDTO workerProfileDTO);

    /**
     * Get all the workerProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkerProfileDTO> findAll(Pageable pageable);

    /**
     * Get all the workerProfiles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkerProfileDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" workerProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkerProfileDTO> findOne(Long id);

    /**
     * Delete the "id" workerProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
