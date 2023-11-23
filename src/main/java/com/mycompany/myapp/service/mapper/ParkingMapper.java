package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Parking;
import com.mycompany.myapp.service.dto.ParkingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parking} and its DTO {@link ParkingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParkingMapper extends EntityMapper<ParkingDTO, Parking> {}
