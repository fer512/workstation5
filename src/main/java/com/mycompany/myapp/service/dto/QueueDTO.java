package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.QueueStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Queue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QueueDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private QueueStatus status;

    private AttencionChannelDTO attencionChannel;

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

    public QueueStatus getStatus() {
        return status;
    }

    public void setStatus(QueueStatus status) {
        this.status = status;
    }

    public AttencionChannelDTO getAttencionChannel() {
        return attencionChannel;
    }

    public void setAttencionChannel(AttencionChannelDTO attencionChannel) {
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
        if (!(o instanceof QueueDTO)) {
            return false;
        }

        QueueDTO queueDTO = (QueueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, queueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QueueDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", attencionChannel=" + getAttencionChannel() +
            ", company=" + getCompany() +
            ", branches=" + getBranches() +
            "}";
    }
}
