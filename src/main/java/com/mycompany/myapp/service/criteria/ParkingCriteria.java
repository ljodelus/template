package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.ParkingStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Parking} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ParkingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parkings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParkingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ParkingStatus
     */
    public static class ParkingStatusFilter extends Filter<ParkingStatus> {

        public ParkingStatusFilter() {}

        public ParkingStatusFilter(ParkingStatusFilter filter) {
            super(filter);
        }

        @Override
        public ParkingStatusFilter copy() {
            return new ParkingStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private StringFilter code;

    private ParkingStatusFilter parkingStatus;

    private StringFilter createBy;

    private InstantFilter createDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter reservationsId;

    private Boolean distinct;

    public ParkingCriteria() {}

    public ParkingCriteria(ParkingCriteria other) {
        this.code = other.code == null ? null : other.code.copy();
        this.parkingStatus = other.parkingStatus == null ? null : other.parkingStatus.copy();
        this.createBy = other.createBy == null ? null : other.createBy.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.reservationsId = other.reservationsId == null ? null : other.reservationsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ParkingCriteria copy() {
        return new ParkingCriteria(this);
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public ParkingStatusFilter getParkingStatus() {
        return parkingStatus;
    }

    public ParkingStatusFilter parkingStatus() {
        if (parkingStatus == null) {
            parkingStatus = new ParkingStatusFilter();
        }
        return parkingStatus;
    }

    public void setParkingStatus(ParkingStatusFilter parkingStatus) {
        this.parkingStatus = parkingStatus;
    }

    public StringFilter getCreateBy() {
        return createBy;
    }

    public StringFilter createBy() {
        if (createBy == null) {
            createBy = new StringFilter();
        }
        return createBy;
    }

    public void setCreateBy(StringFilter createBy) {
        this.createBy = createBy;
    }

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public InstantFilter createDate() {
        if (createDate == null) {
            createDate = new InstantFilter();
        }
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
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

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getReservationsId() {
        return reservationsId;
    }

    public LongFilter reservationsId() {
        if (reservationsId == null) {
            reservationsId = new LongFilter();
        }
        return reservationsId;
    }

    public void setReservationsId(LongFilter reservationsId) {
        this.reservationsId = reservationsId;
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
        final ParkingCriteria that = (ParkingCriteria) o;
        return (
            Objects.equals(code, that.code) &&
            Objects.equals(parkingStatus, that.parkingStatus) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(reservationsId, that.reservationsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, parkingStatus, createBy, createDate, lastModifiedBy, lastModifiedDate, reservationsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParkingCriteria{" +
            (code != null ? "code=" + code + ", " : "") +
            (parkingStatus != null ? "parkingStatus=" + parkingStatus + ", " : "") +
            (createBy != null ? "createBy=" + createBy + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (reservationsId != null ? "reservationsId=" + reservationsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
