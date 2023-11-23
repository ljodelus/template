package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.ParkingStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Parking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParkingDTO implements Serializable {

    @NotNull
    private String code;

    @NotNull
    private ParkingStatus parkingStatus;

    @NotNull
    private String createBy;

    @NotNull
    private Instant createDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ParkingStatus getParkingStatus() {
        return parkingStatus;
    }

    public void setParkingStatus(ParkingStatus parkingStatus) {
        this.parkingStatus = parkingStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParkingDTO)) {
            return false;
        }

        ParkingDTO parkingDTO = (ParkingDTO) o;
        if (this.code == null) {
            return false;
        }
        return Objects.equals(this.code, parkingDTO.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParkingDTO{" +
            "code='" + getCode() + "'" +
            ", parkingStatus='" + getParkingStatus() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
