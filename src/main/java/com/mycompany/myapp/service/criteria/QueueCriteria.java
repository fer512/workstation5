package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.QueueStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Queue} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.QueueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /queues?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QueueCriteria implements Serializable, Criteria {

    /**
     * Class for filtering QueueStatus
     */
    public static class QueueStatusFilter extends Filter<QueueStatus> {

        public QueueStatusFilter() {}

        public QueueStatusFilter(QueueStatusFilter filter) {
            super(filter);
        }

        @Override
        public QueueStatusFilter copy() {
            return new QueueStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private QueueStatusFilter status;

    private LongFilter attencionChannelId;

    private LongFilter companyId;

    private LongFilter branchId;

    private Boolean distinct;

    public QueueCriteria() {}

    public QueueCriteria(QueueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.attencionChannelId = other.attencionChannelId == null ? null : other.attencionChannelId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.branchId = other.branchId == null ? null : other.branchId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QueueCriteria copy() {
        return new QueueCriteria(this);
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

    public QueueStatusFilter getStatus() {
        return status;
    }

    public QueueStatusFilter status() {
        if (status == null) {
            status = new QueueStatusFilter();
        }
        return status;
    }

    public void setStatus(QueueStatusFilter status) {
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
        final QueueCriteria that = (QueueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(attencionChannelId, that.attencionChannelId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, attencionChannelId, companyId, branchId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QueueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (attencionChannelId != null ? "attencionChannelId=" + attencionChannelId + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (branchId != null ? "branchId=" + branchId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
