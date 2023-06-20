package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.AttencionChannelType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AttencionChannel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttencionChannelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private AttencionChannelType type;

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

    public AttencionChannelType getType() {
        return type;
    }

    public void setType(AttencionChannelType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttencionChannelDTO)) {
            return false;
        }

        AttencionChannelDTO attencionChannelDTO = (AttencionChannelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attencionChannelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttencionChannelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
