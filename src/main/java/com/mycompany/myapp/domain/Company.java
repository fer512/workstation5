package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CompanyStatus;
import com.mycompany.myapp.domain.enumeration.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable {

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
    private CompanyStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attencionChannel", "workers", "company", "branches" }, allowSetters = true)
    private Set<WaitingRoom> waitingRooms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company", "waitingRooms", "queues", "workers", "workerProfiles" }, allowSetters = true)
    private Set<Branch> branches = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attencionChannel", "company", "branches" }, allowSetters = true)
    private Set<Queue> queues = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "company", "branches", "waitingRoom" }, allowSetters = true)
    private Set<Worker> workers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attencionChannel", "callableQueues", "company", "branches" }, allowSetters = true)
    private Set<WorkerProfile> workerProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Company name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyStatus getStatus() {
        return this.status;
    }

    public Company status(CompanyStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Company language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<WaitingRoom> getWaitingRooms() {
        return this.waitingRooms;
    }

    public void setWaitingRooms(Set<WaitingRoom> waitingRooms) {
        if (this.waitingRooms != null) {
            this.waitingRooms.forEach(i -> i.setCompany(null));
        }
        if (waitingRooms != null) {
            waitingRooms.forEach(i -> i.setCompany(this));
        }
        this.waitingRooms = waitingRooms;
    }

    public Company waitingRooms(Set<WaitingRoom> waitingRooms) {
        this.setWaitingRooms(waitingRooms);
        return this;
    }

    public Company addWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRooms.add(waitingRoom);
        waitingRoom.setCompany(this);
        return this;
    }

    public Company removeWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRooms.remove(waitingRoom);
        waitingRoom.setCompany(null);
        return this;
    }

    public Set<Branch> getBranches() {
        return this.branches;
    }

    public void setBranches(Set<Branch> branches) {
        if (this.branches != null) {
            this.branches.forEach(i -> i.setCompany(null));
        }
        if (branches != null) {
            branches.forEach(i -> i.setCompany(this));
        }
        this.branches = branches;
    }

    public Company branches(Set<Branch> branches) {
        this.setBranches(branches);
        return this;
    }

    public Company addBranches(Branch branch) {
        this.branches.add(branch);
        branch.setCompany(this);
        return this;
    }

    public Company removeBranches(Branch branch) {
        this.branches.remove(branch);
        branch.setCompany(null);
        return this;
    }

    public Set<Queue> getQueues() {
        return this.queues;
    }

    public void setQueues(Set<Queue> queues) {
        if (this.queues != null) {
            this.queues.forEach(i -> i.setCompany(null));
        }
        if (queues != null) {
            queues.forEach(i -> i.setCompany(this));
        }
        this.queues = queues;
    }

    public Company queues(Set<Queue> queues) {
        this.setQueues(queues);
        return this;
    }

    public Company addQueue(Queue queue) {
        this.queues.add(queue);
        queue.setCompany(this);
        return this;
    }

    public Company removeQueue(Queue queue) {
        this.queues.remove(queue);
        queue.setCompany(null);
        return this;
    }

    public Set<Worker> getWorkers() {
        return this.workers;
    }

    public void setWorkers(Set<Worker> workers) {
        if (this.workers != null) {
            this.workers.forEach(i -> i.setCompany(null));
        }
        if (workers != null) {
            workers.forEach(i -> i.setCompany(this));
        }
        this.workers = workers;
    }

    public Company workers(Set<Worker> workers) {
        this.setWorkers(workers);
        return this;
    }

    public Company addWorker(Worker worker) {
        this.workers.add(worker);
        worker.setCompany(this);
        return this;
    }

    public Company removeWorker(Worker worker) {
        this.workers.remove(worker);
        worker.setCompany(null);
        return this;
    }

    public Set<WorkerProfile> getWorkerProfiles() {
        return this.workerProfiles;
    }

    public void setWorkerProfiles(Set<WorkerProfile> workerProfiles) {
        if (this.workerProfiles != null) {
            this.workerProfiles.forEach(i -> i.setCompany(null));
        }
        if (workerProfiles != null) {
            workerProfiles.forEach(i -> i.setCompany(this));
        }
        this.workerProfiles = workerProfiles;
    }

    public Company workerProfiles(Set<WorkerProfile> workerProfiles) {
        this.setWorkerProfiles(workerProfiles);
        return this;
    }

    public Company addWorkerProfiles(WorkerProfile workerProfile) {
        this.workerProfiles.add(workerProfile);
        workerProfile.setCompany(this);
        return this;
    }

    public Company removeWorkerProfiles(WorkerProfile workerProfile) {
        this.workerProfiles.remove(workerProfile);
        workerProfile.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
