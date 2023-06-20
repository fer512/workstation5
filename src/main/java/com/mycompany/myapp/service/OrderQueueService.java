package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.OrderQueueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.OrderQueue}.
 */
public interface OrderQueueService {
    /**
     * Save a orderQueue.
     *
     * @param orderQueueDTO the entity to save.
     * @return the persisted entity.
     */
    OrderQueueDTO save(OrderQueueDTO orderQueueDTO);

    /**
     * Updates a orderQueue.
     *
     * @param orderQueueDTO the entity to update.
     * @return the persisted entity.
     */
    OrderQueueDTO update(OrderQueueDTO orderQueueDTO);

    /**
     * Partially updates a orderQueue.
     *
     * @param orderQueueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderQueueDTO> partialUpdate(OrderQueueDTO orderQueueDTO);

    /**
     * Get all the orderQueues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderQueueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" orderQueue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderQueueDTO> findOne(Long id);

    /**
     * Delete the "id" orderQueue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
