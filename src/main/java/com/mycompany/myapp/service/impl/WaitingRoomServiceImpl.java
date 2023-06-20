package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.WaitingRoom;
import com.mycompany.myapp.repository.WaitingRoomRepository;
import com.mycompany.myapp.service.WaitingRoomService;
import com.mycompany.myapp.service.dto.WaitingRoomDTO;
import com.mycompany.myapp.service.mapper.WaitingRoomMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WaitingRoom}.
 */
@Service
@Transactional
public class WaitingRoomServiceImpl implements WaitingRoomService {

    private final Logger log = LoggerFactory.getLogger(WaitingRoomServiceImpl.class);

    private final WaitingRoomRepository waitingRoomRepository;

    private final WaitingRoomMapper waitingRoomMapper;

    public WaitingRoomServiceImpl(WaitingRoomRepository waitingRoomRepository, WaitingRoomMapper waitingRoomMapper) {
        this.waitingRoomRepository = waitingRoomRepository;
        this.waitingRoomMapper = waitingRoomMapper;
    }

    @Override
    public WaitingRoomDTO save(WaitingRoomDTO waitingRoomDTO) {
        log.debug("Request to save WaitingRoom : {}", waitingRoomDTO);
        WaitingRoom waitingRoom = waitingRoomMapper.toEntity(waitingRoomDTO);
        waitingRoom = waitingRoomRepository.save(waitingRoom);
        return waitingRoomMapper.toDto(waitingRoom);
    }

    @Override
    public WaitingRoomDTO update(WaitingRoomDTO waitingRoomDTO) {
        log.debug("Request to update WaitingRoom : {}", waitingRoomDTO);
        WaitingRoom waitingRoom = waitingRoomMapper.toEntity(waitingRoomDTO);
        waitingRoom = waitingRoomRepository.save(waitingRoom);
        return waitingRoomMapper.toDto(waitingRoom);
    }

    @Override
    public Optional<WaitingRoomDTO> partialUpdate(WaitingRoomDTO waitingRoomDTO) {
        log.debug("Request to partially update WaitingRoom : {}", waitingRoomDTO);

        return waitingRoomRepository
            .findById(waitingRoomDTO.getId())
            .map(existingWaitingRoom -> {
                waitingRoomMapper.partialUpdate(existingWaitingRoom, waitingRoomDTO);

                return existingWaitingRoom;
            })
            .map(waitingRoomRepository::save)
            .map(waitingRoomMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WaitingRoomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WaitingRooms");
        return waitingRoomRepository.findAll(pageable).map(waitingRoomMapper::toDto);
    }

    public Page<WaitingRoomDTO> findAllWithEagerRelationships(Pageable pageable) {
        return waitingRoomRepository.findAllWithEagerRelationships(pageable).map(waitingRoomMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WaitingRoomDTO> findOne(Long id) {
        log.debug("Request to get WaitingRoom : {}", id);
        return waitingRoomRepository.findOneWithEagerRelationships(id).map(waitingRoomMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WaitingRoom : {}", id);
        waitingRoomRepository.deleteById(id);
    }
}
