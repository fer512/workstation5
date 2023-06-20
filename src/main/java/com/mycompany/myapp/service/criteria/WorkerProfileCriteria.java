package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.WorkerProfileStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.WorkerProfile} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.WorkerProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /worker-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkerProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkerProfileStatus
     */
    public static class WorkerProfileStatusFilter extends Filter<WorkerProfileStatus> {

        public WorkerProfileStatusFilter() {}

        public WorkerProfileStatusFilter(WorkerProfileStatusFilter filter) {
            super(filter);
        }

        @Override
        public WorkerProfileStatusFilter copy() {
            return new WorkerProfileStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private WorkerProfileStatusFilter status;

    private LongFilter attencionChannelId;

    private LongFilter callableQueueId;

    private LongFilter companyId;

    private LongFilter branchesId;

    private Boolean distinct;

    public WorkerProfileCriteria() {}

    public WorkerProfileCriteria(WorkerProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.attencionChannelId = other.attencionChannelId == null ? null : other.attencionChannelId.copy();
        this.callableQueueId = other.callableQueueId == null ? null : other.callableQueueId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.branchesId = other.branchesId == null ? null : other.branchesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WorkerProfileCriteria copy() {
        return new WorkerProfileCriteria(this);
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

    public WorkerProfileStatusFilter getStatus() {
        return status;
    }

    public WorkerProfileStatusFilter status() {
        if (status == null) {
            status = new WorkerProfileStatusFilter();
        }
        return status;
    }

    public void setStatus(WorkerProfileStatusFilter status) {
        this.status = status;
    }

    public LongFilter getAttencionChannelId() {
        return attencionChannelId;
    }

    public LongFilter attencionChannelId() {
        if (attencionChannelId == null) {
            attencionChannelId = new LongFilter();
        }
        return attencionChannelId;
    }

    public void setAttencionChannelId(LongFilter attencionChannelId) {
        this.attencionChannelId = attencionChannelId;
    }

    public LongFilter getCallableQueueId() {
        return callableQueueId;
    }

    public LongFilter callableQueueId() {
        if (callableQueueId == null) {
            callableQueueId = new LongFilter();
        }
        return callableQueueId;
    }

    public void setCallableQueueId(LongFilter callableQueueId) {
        this.callableQueueId = callableQueueId;
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

    public LongFilter getBranchesId() {
        return branchesId;
    }

    public LongFilter branchesId() {
        if (branchesId == null) {
            branchesId = new LongFilter();
        }
        return branchesId;
    }

    public void setBranchesId(LongFilter branchesId) {
        this.branchesId = branchesId;
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
        final WorkerProfileCriteria that = (WorkerProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(attencionChannelId, that.attencionChannelId) &&
            Objects.equals(callableQueueId, that.callableQueueId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(branchesId, that.branchesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, attencionChannelId, callableQueueId, companyId, branchesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkerProfileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (attencionChannelId != null ? "attencionChannelId=" + attencionChannelId + ", " : "") +
            (callableQueueId != null ? "callableQueueId=" + callableQueueId + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (branchesId != null ? "branchesId=" + branchesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
