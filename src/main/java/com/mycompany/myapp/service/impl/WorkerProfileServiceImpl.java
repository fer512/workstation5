package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.WorkerProfile;
import com.mycompany.myapp.repository.WorkerProfileRepository;
import com.mycompany.myapp.service.WorkerProfileService;
import com.mycompany.myapp.service.dto.WorkerProfileDTO;
import com.mycompany.myapp.service.mapper.WorkerProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkerProfile}.
 */
@Service
@Transactional
public class WorkerProfileServiceImpl implements WorkerProfileService {

    private final Logger log = LoggerFactory.getLogger(WorkerProfileServiceImpl.class);

    private final WorkerProfileRepository workerProfileRepository;

    private final WorkerProfileMapper workerProfileMapper;

    public WorkerProfileServiceImpl(WorkerProfileRepository workerProfileRepository, WorkerProfileMapper workerProfileMapper) {
        this.workerProfileRepository = workerProfileRepository;
        this.workerProfileMapper = workerProfileMapper;
    }

    @Override
    public WorkerProfileDTO save(WorkerProfileDTO workerProfileDTO) {
        log.debug("Request to save WorkerProfile : {}", workerProfileDTO);
        WorkerProfile workerProfile = workerProfileMapper.toEntity(workerProfileDTO);
        workerProfile = workerProfileRepository.save(workerProfile);
        return workerProfileMapper.toDto(workerProfile);
    }

    @Override
    public WorkerProfileDTO update(WorkerProfileDTO workerProfileDTO) {
        log.debug("Request to update WorkerProfile : {}", workerProfileDTO);
        WorkerProfile workerProfile = workerProfileMapper.toEntity(workerProfileDTO);
        workerProfile = workerProfileRepository.save(workerProfile);
        return workerProfileMapper.toDto(workerProfile);
    }

    @Override
    public Optional<WorkerProfileDTO> partialUpdate(WorkerProfileDTO workerProfileDTO) {
        log.debug("Request to partially update WorkerProfile : {}", workerProfileDTO);

        return workerProfileRepository
            .findById(workerProfileDTO.getId())
            .map(existingWorkerProfile -> {
                workerProfileMapper.partialUpdate(existingWorkerProfile, workerProfileDTO);

                return existingWorkerProfile;
            })
            .map(workerProfileRepository::save)
            .map(workerProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkerProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkerProfiles");
        return workerProfileRepository.findAll(pageable).map(workerProfileMapper::toDto);
    }

    public Page<WorkerProfileDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workerProfileRepository.findAllWithEagerRelationships(pageable).map(workerProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkerProfileDTO> findOne(Long id) {
        log.debug("Request to get WorkerProfile : {}", id);
        return workerProfileRepository.findOneWithEagerRelationships(id).map(workerProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkerProfile : {}", id);
        workerProfileRepository.deleteById(id);
    }
}
