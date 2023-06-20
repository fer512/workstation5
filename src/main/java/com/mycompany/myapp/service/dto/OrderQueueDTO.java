package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.OrderQueue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderQueueDTO implements Serializable {

    private Long id;

    @NotNull
    private Long order;

    private QueueDTO queue;

    private WorkerProfileDTO workerProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public QueueDTO getQueue() {
        return queue;
    }

    public void setQueue(QueueDTO queue) {
        this.queue = queue;
    }

    public WorkerProfileDTO getWorkerProfile() {
        return workerProfile;
    }

    public void setWorkerProfile(WorkerProfileDTO workerProfile) {
        this.workerProfile = workerProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderQueueDTO)) {
            return false;
        }

        OrderQueueDTO orderQueueDTO = (OrderQueueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderQueueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderQueueDTO{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", queue=" + getQueue() +
            ", workerProfile=" + getWorkerProfile() +
            "}";
    }
}
