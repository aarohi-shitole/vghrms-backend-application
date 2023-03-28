package com.vhrms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vhrms.domain.Address} entity. This class is used
 * in {@link com.vhrms.web.rest.AddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private StringFilter line1;

    private StringFilter line2;

    private StringFilter country;

    private StringFilter state;

    private StringFilter city;

    private StringFilter pincode;

    private BooleanFilter defaultAdd;

    private StringFilter landMark;

    private DoubleFilter longitude;

    private DoubleFilter latitude;

    private StringFilter refTable;

    private LongFilter refTableId;

    private StringFilter status;

    private LongFilter companyId;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private Boolean distinct;

    public AddressCriteria() {}

    public AddressCriteria(AddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.line1 = other.line1 == null ? null : other.line1.copy();
        this.line2 = other.line2 == null ? null : other.line2.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.pincode = other.pincode == null ? null : other.pincode.copy();
        this.defaultAdd = other.defaultAdd == null ? null : other.defaultAdd.copy();
        this.landMark = other.landMark == null ? null : other.landMark.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.refTable = other.refTable == null ? null : other.refTable.copy();
        this.refTableId = other.refTableId == null ? null : other.refTableId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AddressCriteria copy() {
        return new AddressCriteria(this);
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

    public StringFilter getLine1() {
        return line1;
    }

    public StringFilter line1() {
        if (line1 == null) {
            line1 = new StringFilter();
        }
        return line1;
    }

    public void setLine1(StringFilter line1) {
        this.line1 = line1;
    }

    public StringFilter getLine2() {
        return line2;
    }

    public StringFilter line2() {
        if (line2 == null) {
            line2 = new StringFilter();
        }
        return line2;
    }

    public void setLine2(StringFilter line2) {
        this.line2 = line2;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getPincode() {
        return pincode;
    }

    public StringFilter pincode() {
        if (pincode == null) {
            pincode = new StringFilter();
        }
        return pincode;
    }

    public void setPincode(StringFilter pincode) {
        this.pincode = pincode;
    }

    public BooleanFilter getDefaultAdd() {
        return defaultAdd;
    }

    public BooleanFilter defaultAdd() {
        if (defaultAdd == null) {
            defaultAdd = new BooleanFilter();
        }
        return defaultAdd;
    }

    public void setDefaultAdd(BooleanFilter defaultAdd) {
        this.defaultAdd = defaultAdd;
    }

    public StringFilter getLandMark() {
        return landMark;
    }

    public StringFilter landMark() {
        if (landMark == null) {
            landMark = new StringFilter();
        }
        return landMark;
    }

    public void setLandMark(StringFilter landMark) {
        this.landMark = landMark;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public DoubleFilter longitude() {
        if (longitude == null) {
            longitude = new DoubleFilter();
        }
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public DoubleFilter latitude() {
        if (latitude == null) {
            latitude = new DoubleFilter();
        }
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getRefTable() {
        return refTable;
    }

    public StringFilter refTable() {
        if (refTable == null) {
            refTable = new StringFilter();
        }
        return refTable;
    }

    public void setRefTable(StringFilter refTable) {
        this.refTable = refTable;
    }

    public LongFilter getRefTableId() {
        return refTableId;
    }

    public LongFilter refTableId() {
        if (refTableId == null) {
            refTableId = new LongFilter();
        }
        return refTableId;
    }

    public void setRefTableId(LongFilter refTableId) {
        this.refTableId = refTableId;
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
        final AddressCriteria that = (AddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(line1, that.line1) &&
            Objects.equals(line2, that.line2) &&
            Objects.equals(country, that.country) &&
            Objects.equals(state, that.state) &&
            Objects.equals(city, that.city) &&
            Objects.equals(pincode, that.pincode) &&
            Objects.equals(defaultAdd, that.defaultAdd) &&
            Objects.equals(landMark, that.landMark) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(refTable, that.refTable) &&
            Objects.equals(refTableId, that.refTableId) &&
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
            line1,
            line2,
            country,
            state,
            city,
            pincode,
            defaultAdd,
            landMark,
            longitude,
            latitude,
            refTable,
            refTableId,
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
        return "AddressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (line1 != null ? "line1=" + line1 + ", " : "") +
            (line2 != null ? "line2=" + line2 + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (pincode != null ? "pincode=" + pincode + ", " : "") +
            (defaultAdd != null ? "defaultAdd=" + defaultAdd + ", " : "") +
            (landMark != null ? "landMark=" + landMark + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (refTable != null ? "refTable=" + refTable + ", " : "") +
            (refTableId != null ? "refTableId=" + refTableId + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
