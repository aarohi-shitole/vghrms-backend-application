package com.vhrms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vhrms.domain.PersonalId} entity. This class is used
 * in {@link com.vhrms.web.rest.PersonalIdResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /personal-ids?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalIdCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private StringFilter number;

    private InstantFilter issueDate;

    private InstantFilter expDate;

    private StringFilter issuingAuthority;

    private StringFilter docUrl;

    private LongFilter employeeId;

    private StringFilter status;

    private LongFilter companyId;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private Boolean distinct;

    public PersonalIdCriteria() {}

    public PersonalIdCriteria(PersonalIdCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.issueDate = other.issueDate == null ? null : other.issueDate.copy();
        this.expDate = other.expDate == null ? null : other.expDate.copy();
        this.issuingAuthority = other.issuingAuthority == null ? null : other.issuingAuthority.copy();
        this.docUrl = other.docUrl == null ? null : other.docUrl.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PersonalIdCriteria copy() {
        return new PersonalIdCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getNumber() {
        return number;
    }

    public StringFilter number() {
        if (number == null) {
            number = new StringFilter();
        }
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public InstantFilter getIssueDate() {
        return issueDate;
    }

    public InstantFilter issueDate() {
        if (issueDate == null) {
            issueDate = new InstantFilter();
        }
        return issueDate;
    }

    public void setIssueDate(InstantFilter issueDate) {
        this.issueDate = issueDate;
    }

    public InstantFilter getExpDate() {
        return expDate;
    }

    public InstantFilter expDate() {
        if (expDate == null) {
            expDate = new InstantFilter();
        }
        return expDate;
    }

    public void setExpDate(InstantFilter expDate) {
        this.expDate = expDate;
    }

    public StringFilter getIssuingAuthority() {
        return issuingAuthority;
    }

    public StringFilter issuingAuthority() {
        if (issuingAuthority == null) {
            issuingAuthority = new StringFilter();
        }
        return issuingAuthority;
    }

    public void setIssuingAuthority(StringFilter issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public StringFilter getDocUrl() {
        return docUrl;
    }

    public StringFilter docUrl() {
        if (docUrl == null) {
            docUrl = new StringFilter();
        }
        return docUrl;
    }

    public void setDocUrl(StringFilter docUrl) {
        this.docUrl = docUrl;
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
        final PersonalIdCriteria that = (PersonalIdCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(number, that.number) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(expDate, that.expDate) &&
            Objects.equals(issuingAuthority, that.issuingAuthority) &&
            Objects.equals(docUrl, that.docUrl) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            type,
            number,
            issueDate,
            expDate,
            issuingAuthority,
            docUrl,
            employeeId,
            status,
            companyId,
            lastModified,
            lastModifiedBy,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalIdCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (issueDate != null ? "issueDate=" + issueDate + ", " : "") +
            (expDate != null ? "expDate=" + expDate + ", " : "") +
            (issuingAuthority != null ? "issuingAuthority=" + issuingAuthority + ", " : "") +
            (docUrl != null ? "docUrl=" + docUrl + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
