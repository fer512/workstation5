package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.WorkerStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Worker.
 */
@Entity
@Table(name = "worker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Worker implements Serializable {

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
    private WorkerStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "waitingRooms", "branches", "queues", "workers", "workerProfiles" }, allowSetters = true)
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_worker__branch",
        joinColumns = @JoinColumn(name = "worker_id"),
        inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company", "waitingRooms", "queues", "workers", "workerProfiles" }, allowSetters = true)
    private Set<Branch> branches = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attencionChannel", "workers", "company", "branches" }, allowSetters = true)
    private WaitingRoom waitingRoom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Worker id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Worker name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkerStatus getStatus() {
        return this.status;
    }

    public Worker status(WorkerStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WorkerStatus status) {
        this.status = status;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Worker company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<Branch> getBranches() {
        return this.branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public Worker branches(Set<Branch> branches) {
        this.setBranches(branches);
        return this;
    }

    public Worker addBranch(Branch branch) {
        this.branches.add(branch);
        branch.getWorkers().add(this);
        return this;
    }

    public Worker removeBranch(Branch branch) {
        this.branches.remove(branch);
        branch.getWorkers().remove(this);
        return this;
    }

    public WaitingRoom getWaitingRoom() {
        return this.waitingRoom;
    }

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    public Worker waitingRoom(WaitingRoom waitingRoom) {
        this.setWaitingRoom(waitingRoom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Worker)) {
            return false;
        }
        return id != null && id.equals(((Worker) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Worker{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
