package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.WaitingRoomAttencionChannel;
import com.mycompany.myapp.repository.WaitingRoomAttencionChannelRepository;
import com.mycompany.myapp.service.WaitingRoomAttencionChannelService;
import com.mycompany.myapp.service.dto.WaitingRoomAttencionChannelDTO;
import com.mycompany.myapp.service.mapper.WaitingRoomAttencionChannelMapper;
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
 * Service Implementation for managing {@link WaitingRoomAttencionChannel}.
 */
@Service
@Transactional
public class WaitingRoomAttencionChannelServiceImpl implements WaitingRoomAttencionChannelService {

    private final Logger log = LoggerFactory.getLogger(WaitingRoomAttencionChannelServiceImpl.class);

    private final WaitingRoomAttencionChannelRepository waitingRoomAttencionChannelRepository;

    private final WaitingRoomAttencionChannelMapper waitingRoomAttencionChannelMapper;

    public WaitingRoomAttencionChannelServiceImpl(
        WaitingRoomAttencionChannelRepository waitingRoomAttencionChannelRepository,
        WaitingRoomAttencionChannelMapper waitingRoomAttencionChannelMapper
    ) {
        this.waitingRoomAttencionChannelRepository = waitingRoomAttencionChannelRepository;
        this.waitingRoomAttencionChannelMapper = waitingRoomAttencionChannelMapper;
    }

    @Override
    public WaitingRoomAttencionChannelDTO save(WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO) {
        log.debug("Request to save WaitingRoomAttencionChannel : {}", waitingRoomAttencionChannelDTO);
        WaitingRoomAttencionChannel waitingRoomAttencionChannel = waitingRoomAttencionChannelMapper.toEntity(
            waitingRoomAttencionChannelDTO
        );
        waitingRoomAttencionChannel = waitingRoomAttencionChannelRepository.save(waitingRoomAttencionChannel);
        return waitingRoomAttencionChannelMapper.toDto(waitingRoomAttencionChannel);
    }

    @Override
    public WaitingRoomAttencionChannelDTO update(WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO) {
        log.debug("Request to update WaitingRoomAttencionChannel : {}", waitingRoomAttencionChannelDTO);
        WaitingRoomAttencionChannel waitingRoomAttencionChannel = waitingRoomAttencionChannelMapper.toEntity(
            waitingRoomAttencionChannelDTO
        );
        waitingRoomAttencionChannel = waitingRoomAttencionChannelRepository.save(waitingRoomAttencionChannel);
        return waitingRoomAttencionChannelMapper.toDto(waitingRoomAttencionChannel);
    }

    @Override
    public Optional<WaitingRoomAttencionChannelDTO> partialUpdate(WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO) {
        log.debug("Request to partially update WaitingRoomAttencionChannel : {}", waitingRoomAttencionChannelDTO);

        return waitingRoomAttencionChannelRepository
            .findById(waitingRoomAttencionChannelDTO.getId())
            .map(existingWaitingRoomAttencionChannel -> {
                waitingRoomAttencionChannelMapper.partialUpdate(existingWaitingRoomAttencionChannel, waitingRoomAttencionChannelDTO);

                return existingWaitingRoomAttencionChannel;
            })
            .map(waitingRoomAttencionChannelRepository::save)
            .map(waitingRoomAttencionChannelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WaitingRoomAttencionChannelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WaitingRoomAttencionChannels");
        return waitingRoomAttencionChannelRepository.findAll(pageable).map(waitingRoomAttencionChannelMapper::toDto);
    }

    /**
     *  Get all the waitingRoomAttencionChannels where WaitingRoom is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WaitingRoomAttencionChannelDTO> findAllWhereWaitingRoomIsNull() {
        log.debug("Request to get all waitingRoomAttencionChannels where WaitingRoom is null");
        return StreamSupport
            .stream(waitingRoomAttencionChannelRepository.findAll().spliterator(), false)
            .filter(waitingRoomAttencionChannel -> waitingRoomAttencionChannel.getWaitingRoom() == null)
            .map(waitingRoomAttencionChannelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WaitingRoomAttencionChannelDTO> findOne(Long id) {
        log.debug("Request to get WaitingRoomAttencionChannel : {}", id);
        return waitingRoomAttencionChannelRepository.findById(id).map(waitingRoomAttencionChannelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WaitingRoomAttencionChannel : {}", id);
        waitingRoomAttencionChannelRepository.deleteById(id);
    }
}
