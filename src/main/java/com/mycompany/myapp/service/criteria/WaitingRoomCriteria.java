package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.WaitingRoomStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.WaitingRoom} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.WaitingRoomResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /waiting-rooms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaitingRoomCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WaitingRoomStatus
     */
    public static class WaitingRoomStatusFilter extends Filter<WaitingRoomStatus> {

        public WaitingRoomStatusFilter() {}

        public WaitingRoomStatusFilter(WaitingRoomStatusFilter filter) {
            super(filter);
        }

        @Override
        public WaitingRoomStatusFilter copy() {
            return new WaitingRoomStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private WaitingRoomStatusFilter status;

    private LongFilter attencionChannelId;

    private LongFilter workerId;

    private LongFilter companyId;

    private LongFilter branchId;

    private Boolean distinct;

    public WaitingRoomCriteria() {}

    public WaitingRoomCriteria(WaitingRoomCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.attencionChannelId = other.attencionChannelId == null ? null : other.attencionChannelId.copy();
        this.workerId = other.workerId == null ? null : other.workerId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.branchId = other.branchId == null ? null : other.branchId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WaitingRoomCriteria copy() {
        return new WaitingRoomCriteria(this);
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

    public WaitingRoomStatusFilter getStatus() {
        return status;
    }

    public WaitingRoomStatusFilter status() {
        if (status == null) {
            status = new WaitingRoomStatusFilter();
        }
        return status;
    }

    public void setStatus(WaitingRoomStatusFilter status) {
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

    public LongFilter getWorkerId() {
        return workerId;
    }

    public LongFilter workerId() {
        if (workerId == null) {
            workerId = new LongFilter();
        }
        return workerId;
    }

    public void setWorkerId(LongFilter workerId) {
        this.workerId = workerId;
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
        final WaitingRoomCriteria that = (WaitingRoomCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(attencionChannelId, that.attencionChannelId) &&
            Objects.equals(workerId, that.workerId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, attencionChannelId, workerId, companyId, branchId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaitingRoomCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (attencionChannelId != null ? "attencionChannelId=" + attencionChannelId + ", " : "") +
            (workerId != null ? "workerId=" + workerId + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (branchId != null ? "branchId=" + branchId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
