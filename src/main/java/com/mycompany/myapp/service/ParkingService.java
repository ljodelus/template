package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ParkingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Parking}.
 */
public interface ParkingService {
    /**
     * Save a parking.
     *
     * @param parkingDTO the entity to save.
     * @return the persisted entity.
     */
    ParkingDTO save(ParkingDTO parkingDTO);

    /**
     * Updates a parking.
     *
     * @param parkingDTO the entity to update.
     * @return the persisted entity.
     */
    ParkingDTO update(ParkingDTO parkingDTO);

    /**
     * Partially updates a parking.
     *
     * @param parkingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ParkingDTO> partialUpdate(ParkingDTO parkingDTO);

    /**
     * Get all the parkings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParkingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" parking.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParkingDTO> findOne(String id);

    /**
     * Delete the "id" parking.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
