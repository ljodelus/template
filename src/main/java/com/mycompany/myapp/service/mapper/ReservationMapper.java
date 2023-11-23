package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Parking;
import com.mycompany.myapp.domain.Reservation;
import com.mycompany.myapp.service.dto.ParkingDTO;
import com.mycompany.myapp.service.dto.ReservationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "parking", source = "parking", qualifiedByName = "parkingCode")
    ReservationDTO toDto(Reservation s);

    @Named("parkingCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "code", source = "code")
    ParkingDTO toDtoParkingCode(Parking parking);
}
