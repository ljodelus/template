package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Parking;
import com.mycompany.myapp.repository.ParkingRepository;
import com.mycompany.myapp.service.criteria.ParkingCriteria;
import com.mycompany.myapp.service.dto.ParkingDTO;
import com.mycompany.myapp.service.mapper.ParkingMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Parking} entities in the database.
 * The main input is a {@link ParkingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParkingDTO} or a {@link Page} of {@link ParkingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParkingQueryService extends QueryService<Parking> {

    private final Logger log = LoggerFactory.getLogger(ParkingQueryService.class);

    private final ParkingRepository parkingRepository;

    private final ParkingMapper parkingMapper;

    public ParkingQueryService(ParkingRepository parkingRepository, ParkingMapper parkingMapper) {
        this.parkingRepository = parkingRepository;
        this.parkingMapper = parkingMapper;
    }

    /**
     * Return a {@link List} of {@link ParkingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParkingDTO> findByCriteria(ParkingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Parking> specification = createSpecification(criteria);
        return parkingMapper.toDto(parkingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ParkingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParkingDTO> findByCriteria(ParkingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Parking> specification = createSpecification(criteria);
        return parkingRepository.findAll(specification, page).map(parkingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParkingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Parking> specification = createSpecification(criteria);
        return parkingRepository.count(specification);
    }

    /**
     * Function to convert {@link ParkingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Parking> createSpecification(ParkingCriteria criteria) {
        Specification<Parking> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Parking_.code));
            }
            if (criteria.getParkingStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getParkingStatus(), Parking_.parkingStatus));
            }
            if (criteria.getCreateBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreateBy(), Parking_.createBy));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Parking_.createDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Parking_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Parking_.lastModifiedDate));
            }
            if (criteria.getReservationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReservationsId(),
                            root -> root.join(Parking_.reservations, JoinType.LEFT).get(Reservation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
