package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.WorkerProfileAttencionChannelType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.WorkerProfileAttencionChannel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkerProfileAttencionChannelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private WorkerProfileAttencionChannelType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkerProfileAttencionChannelType getType() {
        return type;
    }

    public void setType(WorkerProfileAttencionChannelType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkerProfileAttencionChannelDTO)) {
            return false;
        }

        WorkerProfileAttencionChannelDTO workerProfileAttencionChannelDTO = (WorkerProfileAttencionChannelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workerProfileAttencionChannelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkerProfileAttencionChannelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
