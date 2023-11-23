package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Parking;
import com.mycompany.myapp.repository.ParkingRepository;
import com.mycompany.myapp.service.ParkingService;
import com.mycompany.myapp.service.dto.ParkingDTO;
import com.mycompany.myapp.service.mapper.ParkingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Parking}.
 */
@Service
@Transactional
public class ParkingServiceImpl implements ParkingService {

    private final Logger log = LoggerFactory.getLogger(ParkingServiceImpl.class);

    private final ParkingRepository parkingRepository;

    private final ParkingMapper parkingMapper;

    public ParkingServiceImpl(ParkingRepository parkingRepository, ParkingMapper parkingMapper) {
        this.parkingRepository = parkingRepository;
        this.parkingMapper = parkingMapper;
    }

    @Override
    public ParkingDTO save(ParkingDTO parkingDTO) {
        log.debug("Request to save Parking : {}", parkingDTO);
        Parking parking = parkingMapper.toEntity(parkingDTO);
        parking = parkingRepository.save(parking);
        return parkingMapper.toDto(parking);
    }

    @Override
    public ParkingDTO update(ParkingDTO parkingDTO) {
        log.debug("Request to update Parking : {}", parkingDTO);
        Parking parking = parkingMapper.toEntity(parkingDTO);
        parking.setIsPersisted();
        parking = parkingRepository.save(parking);
        return parkingMapper.toDto(parking);
    }

    @Override
    public Optional<ParkingDTO> partialUpdate(ParkingDTO parkingDTO) {
        log.debug("Request to partially update Parking : {}", parkingDTO);

        return parkingRepository
            .findById(parkingDTO.getCode())
            .map(existingParking -> {
                parkingMapper.partialUpdate(existingParking, parkingDTO);

                return existingParking;
            })
            .map(parkingRepository::save)
            .map(parkingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParkingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parkings");
        return parkingRepository.findAll(pageable).map(parkingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParkingDTO> findOne(String id) {
        log.debug("Request to get Parking : {}", id);
        return parkingRepository.findById(id).map(parkingMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Parking : {}", id);
        parkingRepository.deleteById(id);
    }
}
