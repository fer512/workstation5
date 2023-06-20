package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.WaitingRoomAttencionChannel;
import com.mycompany.myapp.service.dto.WaitingRoomAttencionChannelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WaitingRoomAttencionChannel} and its DTO {@link WaitingRoomAttencionChannelDTO}.
 */
@Mapper(componentModel = "spring")
public interface WaitingRoomAttencionChannelMapper extends EntityMapper<WaitingRoomAttencionChannelDTO, WaitingRoomAttencionChannel> {}
