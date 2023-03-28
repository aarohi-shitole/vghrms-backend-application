package com.vhrms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vhrms.domain.Reporting} entity. This class is used
 * in {@link com.vhrms.web.rest.ReportingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reportings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter employeeId;

    private LongFilter reportingEmpId;

    private LongFilter reportingId;

    private StringFilter status;

    private LongFilter companyId;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private Boolean distinct;

    public ReportingCriteria() {}

    public ReportingCriteria(ReportingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.reportingEmpId = other.reportingEmpId == null ? null : other.reportingEmpId.copy();
        this.reportingId = other.reportingId == null ? null : other.reportingId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportingCriteria copy() {
        return new ReportingCriteria(this);
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

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            employeeId = new LongFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getReportingEmpId() {
        return reportingEmpId;
    }

    public LongFilter reportingEmpId() {
        if (reportingEmpId == null) {
            reportingEmpId = new LongFilter();
        }
        return reportingEmpId;
    }

    public void setReportingEmpId(LongFilter reportingEmpId) {
        this.reportingEmpId = reportingEmpId;
    }

    public LongFilter getReportingId() {
        return reportingId;
    }

    public LongFilter reportingId() {
        if (reportingId == null) {
            reportingId = new LongFilter();
        }
        return reportingId;
    }

    public void setReportingId(LongFilter reportingId) {
        this.reportingId = reportingId;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
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

    public InstantFilter getLastModified() {
        return lastModified;
    }

    public InstantFilter lastModified() {
        if (lastModified == null) {
            lastModified = new InstantFilter();
        }
        return lastModified;
    }

    public void setLastModified(InstantFilter lastModified) {
        this.lastModified = lastModified;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
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
        final ReportingCriteria that = (ReportingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(reportingEmpId, that.reportingEmpId) &&
            Objects.equals(reportingId, that.reportingId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeId, reportingEmpId, reportingId, status, companyId, lastModified, lastModifiedBy, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (reportingEmpId != null ? "reportingEmpId=" + reportingEmpId + ", " : "") +
            (reportingId != null ? "reportingId=" + reportingId + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
