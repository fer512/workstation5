package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.OrderQueue;
import com.mycompany.myapp.domain.Queue;
import com.mycompany.myapp.domain.WorkerProfile;
import com.mycompany.myapp.service.dto.OrderQueueDTO;
import com.mycompany.myapp.service.dto.QueueDTO;
import com.mycompany.myapp.service.dto.WorkerProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderQueue} and its DTO {@link OrderQueueDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderQueueMapper extends EntityMapper<OrderQueueDTO, OrderQueue> {
    @Mapping(target = "queue", source = "queue", qualifiedByName = "queueId")
    @Mapping(target = "workerProfile", source = "workerProfile", qualifiedByName = "workerProfileId")
    OrderQueueDTO toDto(OrderQueue s);

    @Named("queueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QueueDTO toDtoQueueId(Queue queue);

    @Named("workerProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkerProfileDTO toDtoWorkerProfileId(WorkerProfile workerProfile);
}
