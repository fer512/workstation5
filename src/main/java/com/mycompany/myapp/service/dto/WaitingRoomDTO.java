package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.WaitingRoomStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.WaitingRoom} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaitingRoomDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private WaitingRoomStatus status;

    private WaitingRoomAttencionChannelDTO attencionChannel;

    private CompanyDTO company;

    private Set<BranchDTO> branches = new HashSet<>();

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

    public WaitingRoomStatus getStatus() {
        return status;
    }

    public void setStatus(WaitingRoomStatus status) {
        this.status = status;
    }

    public WaitingRoomAttencionChannelDTO getAttencionChannel() {
        return attencionChannel;
    }

    public void setAttencionChannel(WaitingRoomAttencionChannelDTO attencionChannel) {
        this.attencionChannel = attencionChannel;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public Set<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(Set<BranchDTO> branches) {
        this.branches = branches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaitingRoomDTO)) {
            return false;
        }

        WaitingRoomDTO waitingRoomDTO = (WaitingRoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, waitingRoomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaitingRoomDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", attencionChannel=" + getAttencionChannel() +
            ", company=" + getCompany() +
            ", branches=" + getBranches() +
            "}";
    }
}
