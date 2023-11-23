package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Parking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Parking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParkingRepository extends JpaRepository<Parking, String>, JpaSpecificationExecutor<Parking> {}
