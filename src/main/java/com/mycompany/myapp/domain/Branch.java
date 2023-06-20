package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.BranchStatus;
import com.mycompany.myapp.domain.enumeration.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Branch.
 */
@Entity
@Table(name = "branch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Branch implements Serializable {

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
    private BranchStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "waitingRooms", "branches", "queues", "workers", "workerProfiles" }, allowSetters = true)
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "branches")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attencionChannel", "workers", "company", "branches" }, allowSetters = true)
    private Set<WaitingRoom> waitingRooms = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "branches")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attencionChannel", "company", "branches" }, allowSetters = true)
    private Set<Queue> queues = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "branches")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company", "branches", "waitingRoom" }, allowSetters = true)
    private Set<Worker> workers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "branches")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attencionChannel", "callableQueues", "company", "branches" }, allowSetters = true)
    private Set<WorkerProfile> workerProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Branch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Branch name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BranchStatus getStatus() {
        return this.status;
    }

    public Branch status(BranchStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BranchStatus status) {
        this.status = status;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Branch language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Branch company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<WaitingRoom> getWaitingRooms() {
        return this.waitingRooms;
    }

    public void setWaitingRooms(Set<WaitingRoom> waitingRooms) {
        if (this.waitingRooms != null) {
            this.waitingRooms.forEach(i -> i.removeBranch(this));
        }
        if (waitingRooms != null) {
            waitingRooms.forEach(i -> i.addBranch(this));
        }
        this.waitingRooms = waitingRooms;
    }

    public Branch waitingRooms(Set<WaitingRoom> waitingRooms) {
        this.setWaitingRooms(waitingRooms);
        return this;
    }

    public Branch addWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRooms.add(waitingRoom);
        waitingRoom.getBranches().add(this);
        return this;
    }

    public Branch removeWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRooms.remove(waitingRoom);
        waitingRoom.getBranches().remove(this);
        return this;
    }

    public Set<Queue> getQueues() {
        return this.queues;
    }

    public void setQueues(Set<Queue> queues) {
        if (this.queues != null) {
            this.queues.forEach(i -> i.removeBranch(this));
        }
        if (queues != null) {
            queues.forEach(i -> i.addBranch(this));
        }
        this.queues = queues;
    }

    public Branch queues(Set<Queue> queues) {
        this.setQueues(queues);
        return this;
    }

    public Branch addQueue(Queue queue) {
        this.queues.add(queue);
        queue.getBranches().add(this);
        return this;
    }

    public Branch removeQueue(Queue queue) {
        this.queues.remove(queue);
        queue.getBranches().remove(this);
        return this;
    }

    public Set<Worker> getWorkers() {
        return this.workers;
    }

    public void setWorkers(Set<Worker> workers) {
        if (this.workers != null) {
            this.workers.forEach(i -> i.removeBranch(this));
        }
        if (workers != null) {
            workers.forEach(i -> i.addBranch(this));
        }
        this.workers = workers;
    }

    public Branch workers(Set<Worker> workers) {
        this.setWorkers(workers);
        return this;
    }

    public Branch addWorker(Worker worker) {
        this.workers.add(worker);
        worker.getBranches().add(this);
        return this;
    }

    public Branch removeWorker(Worker worker) {
        this.workers.remove(worker);
        worker.getBranches().remove(this);
        return this;
    }

    public Set<WorkerProfile> getWorkerProfiles() {
        return this.workerProfiles;
    }

    public void setWorkerProfiles(Set<WorkerProfile> workerProfiles) {
        if (this.workerProfiles != null) {
            this.workerProfiles.forEach(i -> i.removeBranches(this));
        }
        if (workerProfiles != null) {
            workerProfiles.forEach(i -> i.addBranches(this));
        }
        this.workerProfiles = workerProfiles;
    }

    public Branch workerProfiles(Set<WorkerProfile> workerProfiles) {
        this.setWorkerProfiles(workerProfiles);
        return this;
    }

    public Branch addWorkerProfiles(WorkerProfile workerProfile) {
        this.workerProfiles.add(workerProfile);
        workerProfile.getBranches().add(this);
        return this;
    }

    public Branch removeWorkerProfiles(WorkerProfile workerProfile) {
        this.workerProfiles.remove(workerProfile);
        workerProfile.getBranches().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Branch)) {
            return false;
        }
        return id != null && id.equals(((Branch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Branch{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
