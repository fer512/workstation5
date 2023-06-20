package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.WorkerStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Worker} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkerDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private WorkerStatus status;

    private CompanyDTO company;

    private Set<BranchDTO> branches = new HashSet<>();

    private WaitingRoomDTO waitingRoom;

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

    public WorkerStatus getStatus() {
        return status;
    }

    public void setStatus(WorkerStatus status) {
        this.status = status;
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

    public WaitingRoomDTO getWaitingRoom() {
        return waitingRoom;
    }

    public void setWaitingRoom(WaitingRoomDTO waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkerDTO)) {
            return false;
        }

        WorkerDTO workerDTO = (WorkerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", company=" + getCompany() +
            ", branches=" + getBranches() +
            ", waitingRoom=" + getWaitingRoom() +
            "}";
    }
}
