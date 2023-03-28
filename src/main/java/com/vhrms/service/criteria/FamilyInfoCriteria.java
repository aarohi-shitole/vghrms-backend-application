package com.vhrms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vhrms.domain.FamilyInfo} entity. This class is used
 * in {@link com.vhrms.web.rest.FamilyInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /family-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FamilyInfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter dateOfBirth;

    private StringFilter relation;

    private LongFilter addressId;

    private BooleanFilter isEmployed;

    private StringFilter employedAt;

    private LongFilter employeeId;

    private StringFilter status;

    private LongFilter companyId;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private Boolean distinct;

    public FamilyInfoCriteria() {}

    public FamilyInfoCriteria(FamilyInfoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
        this.relation = other.relation == null ? null : other.relation.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.isEmployed = other.isEmployed == null ? null : other.isEmployed.copy();
        this.employedAt = other.employedAt == null ? null : other.employedAt.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FamilyInfoCriteria copy() {
        return new FamilyInfoCriteria(this);
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

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateFilter dateOfBirth() {
        if (dateOfBirth == null) {
            dateOfBirth = new LocalDateFilter();
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public StringFilter getRelation() {
        return relation;
    }

    public StringFilter relation() {
        if (relation == null) {
            relation = new StringFilter();
        }
        return relation;
    }

    public void setRelation(StringFilter relation) {
        this.relation = relation;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public BooleanFilter getIsEmployed() {
        return isEmployed;
    }

    public BooleanFilter isEmployed() {
        if (isEmployed == null) {
            isEmployed = new BooleanFilter();
        }
        return isEmployed;
    }

    public void setIsEmployed(BooleanFilter isEmployed) {
        this.isEmployed = isEmployed;
    }

    public StringFilter getEmployedAt() {
        return employedAt;
    }

    public StringFilter employedAt() {
        if (employedAt == null) {
            employedAt = new StringFilter();
        }
        return employedAt;
    }

    public void setEmployedAt(StringFilter employedAt) {
        this.employedAt = employedAt;
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
        final FamilyInfoCriteria that = (FamilyInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(relation, that.relation) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(isEmployed, that.isEmployed) &&
            Objects.equals(employedAt, that.employedAt) &&
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
            name,
            dateOfBirth,
            relation,
            addressId,
            isEmployed,
            employedAt,
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
        return "FamilyInfoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth + ", " : "") +
            (relation != null ? "relation=" + relation + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (isEmployed != null ? "isEmployed=" + isEmployed + ", " : "") +
            (employedAt != null ? "employedAt=" + employedAt + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
