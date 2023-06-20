package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.WorkerProfileAttencionChannel;
import com.mycompany.myapp.service.dto.WorkerProfileAttencionChannelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkerProfileAttencionChannel} and its DTO {@link WorkerProfileAttencionChannelDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkerProfileAttencionChannelMapper
    extends EntityMapper<WorkerProfileAttencionChannelDTO, WorkerProfileAttencionChannel> {}
