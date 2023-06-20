package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.WorkerStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Worker} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.WorkerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkerStatus
     */
    public static class WorkerStatusFilter extends Filter<WorkerStatus> {

        public WorkerStatusFilter() {}

        public WorkerStatusFilter(WorkerStatusFilter filter) {
            super(filter);
        }

        @Override
        public WorkerStatusFilter copy() {
            return new WorkerStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private WorkerStatusFilter status;

    private LongFilter companyId;

    private LongFilter branchId;

    private LongFilter waitingRoomId;

    private Boolean distinct;

    public WorkerCriteria() {}

    public WorkerCriteria(WorkerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.branchId = other.branchId == null ? null : other.branchId.copy();
        this.waitingRoomId = other.waitingRoomId == null ? null : other.waitingRoomId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WorkerCriteria copy() {
        return new WorkerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public WorkerStatusFilter getStatus() {
        return status;
    }

    public WorkerStatusFilter status() {
        if (status == null) {
            status = new WorkerStatusFilter();
        }
        return status;
    }

    public void setStatus(WorkerStatusFilter status) {
        this.status = status;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public LongFilter companyId() {
        if (companyId == null) {
            companyId = new LongFilter();
        }
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getBranchId() {
        return branchId;
    }

    public LongFilter branchId() {
        if (branchId == null) {
            branchId = new LongFilter();
        }
        return branchId;
    }

    public void setBranchId(LongFilter branchId) {
        this.branchId = branchId;
    }

    public LongFilter getWaitingRoomId() {
        return waitingRoomId;
    }

    public LongFilter waitingRoomId() {
        if (waitingRoomId == null) {
            waitingRoomId = new LongFilter();
        }
        return waitingRoomId;
    }

    public void setWaitingRoomId(LongFilter waitingRoomId) {
        this.waitingRoomId = waitingRoomId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorkerCriteria that = (WorkerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(waitingRoomId, that.waitingRoomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, companyId, branchId, waitingRoomId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (branchId != null ? "branchId=" + branchId + ", " : "") +
            (waitingRoomId != null ? "waitingRoomId=" + waitingRoomId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
