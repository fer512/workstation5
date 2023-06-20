package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.WorkerProfileAttencionChannel;
import com.mycompany.myapp.repository.WorkerProfileAttencionChannelRepository;
import com.mycompany.myapp.service.WorkerProfileAttencionChannelService;
import com.mycompany.myapp.service.dto.WorkerProfileAttencionChannelDTO;
import com.mycompany.myapp.service.mapper.WorkerProfileAttencionChannelMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkerProfileAttencionChannel}.
 */
@Service
@Transactional
public class WorkerProfileAttencionChannelServiceImpl implements WorkerProfileAttencionChannelService {

    private final Logger log = LoggerFactory.getLogger(WorkerProfileAttencionChannelServiceImpl.class);

    private final WorkerProfileAttencionChannelRepository workerProfileAttencionChannelRepository;

    private final WorkerProfileAttencionChannelMapper workerProfileAttencionChannelMapper;

    public WorkerProfileAttencionChannelServiceImpl(
        WorkerProfileAttencionChannelRepository workerProfileAttencionChannelRepository,
        WorkerProfileAttencionChannelMapper workerProfileAttencionChannelMapper
    ) {
        this.workerProfileAttencionChannelRepository = workerProfileAttencionChannelRepository;
        this.workerProfileAttencionChannelMapper = workerProfileAttencionChannelMapper;
    }

    @Override
    public WorkerProfileAttencionChannelDTO save(WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO) {
        log.debug("Request to save WorkerProfileAttencionChannel : {}", workerProfileAttencionChannelDTO);
        WorkerProfileAttencionChannel workerProfileAttencionChannel = workerProfileAttencionChannelMapper.toEntity(
            workerProfileAttencionChannelDTO
        );
        workerProfileAttencionChannel = workerProfileAttencionChannelRepository.save(workerProfileAttencionChannel);
        return workerProfileAttencionChannelMapper.toDto(workerProfileAttencionChannel);
    }

    @Override
    public WorkerProfileAttencionChannelDTO update(WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO) {
        log.debug("Request to update WorkerProfileAttencionChannel : {}", workerProfileAttencionChannelDTO);
        WorkerProfileAttencionChannel workerProfileAttencionChannel = workerProfileAttencionChannelMapper.toEntity(
            workerProfileAttencionChannelDTO
        );
        workerProfileAttencionChannel = workerProfileAttencionChannelRepository.save(workerProfileAttencionChannel);
        return workerProfileAttencionChannelMapper.toDto(workerProfileAttencionChannel);
    }

    @Override
    public Optional<WorkerProfileAttencionChannelDTO> partialUpdate(WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO) {
        log.debug("Request to partially update WorkerProfileAttencionChannel : {}", workerProfileAttencionChannelDTO);

        return workerProfileAttencionChannelRepository
            .findById(workerProfileAttencionChannelDTO.getId())
            .map(existingWorkerProfileAttencionChannel -> {
                workerProfileAttencionChannelMapper.partialUpdate(existingWorkerProfileAttencionChannel, workerProfileAttencionChannelDTO);

                return existingWorkerProfileAttencionChannel;
            })
            .map(workerProfileAttencionChannelRepository::save)
            .map(workerProfileAttencionChannelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkerProfileAttencionChannelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkerProfileAttencionChannels");
        return workerProfileAttencionChannelRepository.findAll(pageable).map(workerProfileAttencionChannelMapper::toDto);
    }

    /**
     *  Get all the workerProfileAttencionChannels where WorkerProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkerProfileAttencionChannelDTO> findAllWhereWorkerProfileIsNull() {
        log.debug("Request to get all workerProfileAttencionChannels where WorkerProfile is null");
        return StreamSupport
            .stream(workerProfileAttencionChannelRepository.findAll().spliterator(), false)
            .filter(workerProfileAttencionChannel -> workerProfileAttencionChannel.getWorkerProfile() == null)
            .map(workerProfileAttencionChannelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkerProfileAttencionChannelDTO> findOne(Long id) {
        log.debug("Request to get WorkerProfileAttencionChannel : {}", id);
        return workerProfileAttencionChannelRepository.findById(id).map(workerProfileAttencionChannelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkerProfileAttencionChannel : {}", id);
        workerProfileAttencionChannelRepository.deleteById(id);
    }
}
