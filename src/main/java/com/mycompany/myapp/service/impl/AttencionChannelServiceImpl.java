package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AttencionChannel;
import com.mycompany.myapp.repository.AttencionChannelRepository;
import com.mycompany.myapp.service.AttencionChannelService;
import com.mycompany.myapp.service.dto.AttencionChannelDTO;
import com.mycompany.myapp.service.mapper.AttencionChannelMapper;
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
 * Service Implementation for managing {@link AttencionChannel}.
 */
@Service
@Transactional
public class AttencionChannelServiceImpl implements AttencionChannelService {

    private final Logger log = LoggerFactory.getLogger(AttencionChannelServiceImpl.class);

    private final AttencionChannelRepository attencionChannelRepository;

    private final AttencionChannelMapper attencionChannelMapper;

    public AttencionChannelServiceImpl(
        AttencionChannelRepository attencionChannelRepository,
        AttencionChannelMapper attencionChannelMapper
    ) {
        this.attencionChannelRepository = attencionChannelRepository;
        this.attencionChannelMapper = attencionChannelMapper;
    }

    @Override
    public AttencionChannelDTO save(AttencionChannelDTO attencionChannelDTO) {
        log.debug("Request to save AttencionChannel : {}", attencionChannelDTO);
        AttencionChannel attencionChannel = attencionChannelMapper.toEntity(attencionChannelDTO);
        attencionChannel = attencionChannelRepository.save(attencionChannel);
        return attencionChannelMapper.toDto(attencionChannel);
    }

    @Override
    public AttencionChannelDTO update(AttencionChannelDTO attencionChannelDTO) {
        log.debug("Request to update AttencionChannel : {}", attencionChannelDTO);
        AttencionChannel attencionChannel = attencionChannelMapper.toEntity(attencionChannelDTO);
        attencionChannel = attencionChannelRepository.save(attencionChannel);
        return attencionChannelMapper.toDto(attencionChannel);
    }

    @Override
    public Optional<AttencionChannelDTO> partialUpdate(AttencionChannelDTO attencionChannelDTO) {
        log.debug("Request to partially update AttencionChannel : {}", attencionChannelDTO);

        return attencionChannelRepository
            .findById(attencionChannelDTO.getId())
            .map(existingAttencionChannel -> {
                attencionChannelMapper.partialUpdate(existingAttencionChannel, attencionChannelDTO);

                return existingAttencionChannel;
            })
            .map(attencionChannelRepository::save)
            .map(attencionChannelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttencionChannelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AttencionChannels");
        return attencionChannelRepository.findAll(pageable).map(attencionChannelMapper::toDto);
    }

    /**
     *  Get all the attencionChannels where Queue is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AttencionChannelDTO> findAllWhereQueueIsNull() {
        log.debug("Request to get all attencionChannels where Queue is null");
        return StreamSupport
            .stream(attencionChannelRepository.findAll().spliterator(), false)
            .filter(attencionChannel -> attencionChannel.getQueue() == null)
            .map(attencionChannelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttencionChannelDTO> findOne(Long id) {
        log.debug("Request to get AttencionChannel : {}", id);
        return attencionChannelRepository.findById(id).map(attencionChannelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttencionChannel : {}", id);
        attencionChannelRepository.deleteById(id);
    }
}
