package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AttencionChannel;
import com.mycompany.myapp.service.dto.AttencionChannelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AttencionChannel} and its DTO {@link AttencionChannelDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttencionChannelMapper extends EntityMapper<AttencionChannelDTO, AttencionChannel> {}
