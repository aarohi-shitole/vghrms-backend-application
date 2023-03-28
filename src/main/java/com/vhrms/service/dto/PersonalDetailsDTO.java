package com.vhrms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.vhrms.domain.PersonalDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalDetailsDTO implements Serializable {

    private Long id;

    private String telephoneNo;

    private String nationality;

    private String maritalStatus;

    private String religion;

    private Long employeeId;

    private String bloodGroup;

    private LocalDate dateOfBirth;

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

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
        if (!(o instanceof PersonalDetailsDTO)) {
            return false;
        }

        PersonalDetailsDTO personalDetailsDTO = (PersonalDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personalDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalDetailsDTO{" +
            "id=" + getId() +
            ", telephoneNo='" + getTelephoneNo() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", religion='" + getReligion() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", bloodGroup='" + getBloodGroup() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", status='" + getStatus() + "'" +
            ", companyId=" + getCompanyId() +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
