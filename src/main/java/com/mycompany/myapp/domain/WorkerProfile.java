package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.WorkerProfileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkerProfile.
 */
@Entity
@Table(name = "worker_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkerProfile implements Serializable {

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
    @Column(name = "status", nullable = false)
    private WorkerProfileStatus status;

    @JsonIgnoreProperties(value = { "workerProfile" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private WorkerProfileAttencionChannel attencionChannel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workerProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "queue", "workerProfile" }, allowSetters = true)
    private Set<OrderQueue> callableQueues = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "waitingRooms", "branches", "queues", "workers", "workerProfiles" }, allowSetters = true)
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_worker_profile__branches",
        joinColumns = @JoinColumn(name = "worker_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "branches_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company", "waitingRooms", "queues", "workers", "workerProfiles" }, allowSetters = true)
    private Set<Branch> branches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkerProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WorkerProfile name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkerProfileStatus getStatus() {
        return this.status;
    }

    public WorkerProfile status(WorkerProfileStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WorkerProfileStatus status) {
        this.status = status;
    }

    public WorkerProfileAttencionChannel getAttencionChannel() {
        return this.attencionChannel;
    }

    public void setAttencionChannel(WorkerProfileAttencionChannel workerProfileAttencionChannel) {
        this.attencionChannel = workerProfileAttencionChannel;
    }

    public WorkerProfile attencionChannel(WorkerProfileAttencionChannel workerProfileAttencionChannel) {
        this.setAttencionChannel(workerProfileAttencionChannel);
        return this;
    }

    public Set<OrderQueue> getCallableQueues() {
        return this.callableQueues;
    }

    public void setCallableQueues(Set<OrderQueue> orderQueues) {
        if (this.callableQueues != null) {
            this.callableQueues.forEach(i -> i.setWorkerProfile(null));
        }
        if (orderQueues != null) {
            orderQueues.forEach(i -> i.setWorkerProfile(this));
        }
        this.callableQueues = orderQueues;
    }

    public WorkerProfile callableQueues(Set<OrderQueue> orderQueues) {
        this.setCallableQueues(orderQueues);
        return this;
    }

    public WorkerProfile addCallableQueue(OrderQueue orderQueue) {
        this.callableQueues.add(orderQueue);
        orderQueue.setWorkerProfile(this);
        return this;
    }

    public WorkerProfile removeCallableQueue(OrderQueue orderQueue) {
        this.callableQueues.remove(orderQueue);
        orderQueue.setWorkerProfile(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public WorkerProfile company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Branch> getBranches() {
        return this.branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public WorkerProfile branches(Set<Branch> branches) {
        this.setBranches(branches);
        return this;
    }

    public WorkerProfile addBranches(Branch branch) {
        this.branches.add(branch);
        branch.getWorkerProfiles().add(this);
        return this;
    }

    public WorkerProfile removeBranches(Branch branch) {
        this.branches.remove(branch);
        branch.getWorkerProfiles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkerProfile)) {
            return false;
        }
        return id != null && id.equals(((WorkerProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkerProfile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
