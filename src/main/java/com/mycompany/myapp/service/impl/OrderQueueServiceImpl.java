package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.OrderQueue;
import com.mycompany.myapp.repository.OrderQueueRepository;
import com.mycompany.myapp.service.OrderQueueService;
import com.mycompany.myapp.service.dto.OrderQueueDTO;
import com.mycompany.myapp.service.mapper.OrderQueueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderQueue}.
 */
@Service
@Transactional
public class OrderQueueServiceImpl implements OrderQueueService {

    private final Logger log = LoggerFactory.getLogger(OrderQueueServiceImpl.class);

    private final OrderQueueRepository orderQueueRepository;

    private final OrderQueueMapper orderQueueMapper;

    public OrderQueueServiceImpl(OrderQueueRepository orderQueueRepository, OrderQueueMapper orderQueueMapper) {
        this.orderQueueRepository = orderQueueRepository;
        this.orderQueueMapper = orderQueueMapper;
    }

    @Override
    public OrderQueueDTO save(OrderQueueDTO orderQueueDTO) {
        log.debug("Request to save OrderQueue : {}", orderQueueDTO);
        OrderQueue orderQueue = orderQueueMapper.toEntity(orderQueueDTO);
        orderQueue = orderQueueRepository.save(orderQueue);
        return orderQueueMapper.toDto(orderQueue);
    }

    @Override
    public OrderQueueDTO update(OrderQueueDTO orderQueueDTO) {
        log.debug("Request to update OrderQueue : {}", orderQueueDTO);
        OrderQueue orderQueue = orderQueueMapper.toEntity(orderQueueDTO);
        orderQueue = orderQueueRepository.save(orderQueue);
        return orderQueueMapper.toDto(orderQueue);
    }

    @Override
    public Optional<OrderQueueDTO> partialUpdate(OrderQueueDTO orderQueueDTO) {
        log.debug("Request to partially update OrderQueue : {}", orderQueueDTO);

        return orderQueueRepository
            .findById(orderQueueDTO.getId())
            .map(existingOrderQueue -> {
                orderQueueMapper.partialUpdate(existingOrderQueue, orderQueueDTO);

                return existingOrderQueue;
            })
            .map(orderQueueRepository::save)
            .map(orderQueueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderQueueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderQueues");
        return orderQueueRepository.findAll(pageable).map(orderQueueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderQueueDTO> findOne(Long id) {
        log.debug("Request to get OrderQueue : {}", id);
        return orderQueueRepository.findById(id).map(orderQueueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderQueue : {}", id);
        orderQueueRepository.deleteById(id);
    }
}
