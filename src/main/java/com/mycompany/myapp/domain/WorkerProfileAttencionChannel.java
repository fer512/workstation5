package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.WorkerProfileAttencionChannelType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkerProfileAttencionChannel.
 */
@Entity
@Table(name = "worker_profile_attencion_channel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkerProfileAttencionChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private WorkerProfileAttencionChannelType type;

    @JsonIgnoreProperties(value = { "attencionChannel", "callableQueues", "company", "branches" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "attencionChannel")
    private WorkerProfile workerProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkerProfileAttencionChannel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WorkerProfileAttencionChannel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkerProfileAttencionChannelType getType() {
        return this.type;
    }

    public WorkerProfileAttencionChannel type(WorkerProfileAttencionChannelType type) {
        this.setType(type);
        return this;
    }

    public void setType(WorkerProfileAttencionChannelType type) {
        this.type = type;
    }

    public WorkerProfile getWorkerProfile() {
        return this.workerProfile;
    }

    public void setWorkerProfile(WorkerProfile workerProfile) {
        if (this.workerProfile != null) {
            this.workerProfile.setAttencionChannel(null);
        }
        if (workerProfile != null) {
            workerProfile.setAttencionChannel(this);
        }
        this.workerProfile = workerProfile;
    }

    public WorkerProfileAttencionChannel workerProfile(WorkerProfile workerProfile) {
        this.setWorkerProfile(workerProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkerProfileAttencionChannel)) {
            return false;
        }
        return id != null && id.equals(((WorkerProfileAttencionChannel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkerProfileAttencionChannel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
