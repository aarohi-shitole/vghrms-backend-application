package com.vhrms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.vhrms.domain.PersonalId} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalIdDTO implements Serializable {

    private Long id;

    private String type;

    private String number;

    private Instant issueDate;

    private Instant expDate;

    private String issuingAuthority;

    private String docUrl;

    private Long employeeId;

    private String status;

    private Long companyId;

    private Instant lastModified;

    private String lastModifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Instant getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public Instant getExpDate() {
        return expDate;
    }

    public void setExpDate(Instant expDate) {
        this.expDate = expDate;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalIdDTO)) {
            return false;
        }

        PersonalIdDTO personalIdDTO = (PersonalIdDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personalIdDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalIdDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", number='" + getNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", expDate='" + getExpDate() + "'" +
            ", issuingAuthority='" + getIssuingAuthority() + "'" +
            ", docUrl='" + getDocUrl() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", status='" + getStatus() + "'" +
            ", companyId=" + getCompanyId() +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
