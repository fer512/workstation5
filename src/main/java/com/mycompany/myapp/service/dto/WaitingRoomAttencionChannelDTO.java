package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.WaitingRoomAttencionChannelType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.WaitingRoomAttencionChannel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaitingRoomAttencionChannelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private WaitingRoomAttencionChannelType type;

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

    public WaitingRoomAttencionChannelType getType() {
        return type;
    }

    public void setType(WaitingRoomAttencionChannelType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaitingRoomAttencionChannelDTO)) {
            return false;
        }

        WaitingRoomAttencionChannelDTO waitingRoomAttencionChannelDTO = (WaitingRoomAttencionChannelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, waitingRoomAttencionChannelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaitingRoomAttencionChannelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
