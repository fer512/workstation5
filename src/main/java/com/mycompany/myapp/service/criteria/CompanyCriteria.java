package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.CompanyStatus;
import com.mycompany.myapp.domain.enumeration.Language;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Company} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CompanyStatus
     */
    public static class CompanyStatusFilter extends Filter<CompanyStatus> {

        public CompanyStatusFilter() {}

        public CompanyStatusFilter(CompanyStatusFilter filter) {
            super(filter);
        }

        @Override
        public CompanyStatusFilter copy() {
            return new CompanyStatusFilter(this);
        }
    }

    /**
     * Class for filtering Language
     */
    public static class LanguageFilter extends Filter<Language> {

        public LanguageFilter() {}

        public LanguageFilter(LanguageFilter filter) {
            super(filter);
        }

        @Override
        public LanguageFilter copy() {
            return new LanguageFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private CompanyStatusFilter status;

    private LanguageFilter language;

    private LongFilter waitingRoomId;

    private LongFilter branchesId;

    private LongFilter queueId;

    private LongFilter workerId;

    private LongFilter workerProfilesId;

    private Boolean distinct;

    public CompanyCriteria() {}

    public CompanyCriteria(CompanyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.language = other.language == null ? null : other.language.copy();
        this.waitingRoomId = other.waitingRoomId == null ? null : other.waitingRoomId.copy();
        this.branchesId = other.branchesId == null ? null : other.branchesId.copy();
        this.queueId = other.queueId == null ? null : other.queueId.copy();
        this.workerId = other.workerId == null ? null : other.workerId.copy();
        this.workerProfilesId = other.workerProfilesId == null ? null : other.workerProfilesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
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

    public CompanyStatusFilter getStatus() {
        return status;
    }

    public CompanyStatusFilter status() {
        if (status == null) {
            status = new CompanyStatusFilter();
        }
        return status;
    }

    public void setStatus(CompanyStatusFilter status) {
        this.status = status;
    }

    public LanguageFilter getLanguage() {
        return language;
    }

    public LanguageFilter language() {
        if (language == null) {
            language = new LanguageFilter();
        }
        return language;
    }

    public void setLanguage(LanguageFilter language) {
        this.language = language;
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

    public LongFilter getQueueId() {
        return queueId;
    }

    public LongFilter queueId() {
        if (queueId == null) {
            queueId = new LongFilter();
        }
        return queueId;
    }

    public void setQueueId(LongFilter queueId) {
        this.queueId = queueId;
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

    public LongFilter getWorkerProfilesId() {
        return workerProfilesId;
    }

    public LongFilter workerProfilesId() {
        if (workerProfilesId == null) {
            workerProfilesId = new LongFilter();
        }
        return workerProfilesId;
    }

    public void setWorkerProfilesId(LongFilter workerProfilesId) {
        this.workerProfilesId = workerProfilesId;
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
        final CompanyCriteria that = (CompanyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(language, that.language) &&
            Objects.equals(waitingRoomId, that.waitingRoomId) &&
            Objects.equals(branchesId, that.branchesId) &&
            Objects.equals(queueId, that.queueId) &&
            Objects.equals(workerId, that.workerId) &&
            Objects.equals(workerProfilesId, that.workerProfilesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, language, waitingRoomId, branchesId, queueId, workerId, workerProfilesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (language != null ? "language=" + language + ", " : "") +
            (waitingRoomId != null ? "waitingRoomId=" + waitingRoomId + ", " : "") +
            (branchesId != null ? "branchesId=" + branchesId + ", " : "") +
            (queueId != null ? "queueId=" + queueId + ", " : "") +
            (workerId != null ? "workerId=" + workerId + ", " : "") +
            (workerProfilesId != null ? "workerProfilesId=" + workerProfilesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
