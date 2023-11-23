package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Parking;
import com.mycompany.myapp.domain.Reservation;
import com.mycompany.myapp.domain.enumeration.ParkingStatus;
import com.mycompany.myapp.repository.ParkingRepository;
import com.mycompany.myapp.service.criteria.ParkingCriteria;
import com.mycompany.myapp.service.dto.ParkingDTO;
import com.mycompany.myapp.service.mapper.ParkingMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParkingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParkingResourceIT {

    private static final ParkingStatus DEFAULT_PARKING_STATUS = ParkingStatus.FREE;
    private static final ParkingStatus UPDATED_PARKING_STATUS = ParkingStatus.USE;

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/parkings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{code}";

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ParkingMapper parkingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParkingMockMvc;

    private Parking parking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking createEntity(EntityManager em) {
        Parking parking = new Parking()
            .parkingStatus(DEFAULT_PARKING_STATUS)
            .createBy(DEFAULT_CREATE_BY)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return parking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parking createUpdatedEntity(EntityManager em) {
        Parking parking = new Parking()
            .parkingStatus(UPDATED_PARKING_STATUS)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return parking;
    }

    @BeforeEach
    public void initTest() {
        parking = createEntity(em);
    }

    @Test
    @Transactional
    void createParking() throws Exception {
        int databaseSizeBeforeCreate = parkingRepository.findAll().size();
        // Create the Parking
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);
        restParkingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingDTO)))
            .andExpect(status().isCreated());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeCreate + 1);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getParkingStatus()).isEqualTo(DEFAULT_PARKING_STATUS);
        assertThat(testParking.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testParking.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testParking.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testParking.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createParkingWithExistingId() throws Exception {
        // Create the Parking with an existing ID
        parking.setCode("existing_id");
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        int databaseSizeBeforeCreate = parkingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkParkingStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingRepository.findAll().size();
        // set the field null
        parking.setParkingStatus(null);

        // Create the Parking, which fails.
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        restParkingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingDTO)))
            .andExpect(status().isBadRequest());

        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateByIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingRepository.findAll().size();
        // set the field null
        parking.setCreateBy(null);

        // Create the Parking, which fails.
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        restParkingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingDTO)))
            .andExpect(status().isBadRequest());

        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingRepository.findAll().size();
        // set the field null
        parking.setCreateDate(null);

        // Create the Parking, which fails.
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        restParkingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingDTO)))
            .andExpect(status().isBadRequest());

        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParkings() throws Exception {
        // Initialize the database
        parking.setCode(UUID.randomUUID().toString());
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList
        restParkingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=code,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].code").value(hasItem(parking.getCode())))
            .andExpect(jsonPath("$.[*].parkingStatus").value(hasItem(DEFAULT_PARKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getParking() throws Exception {
        // Initialize the database
        parking.setCode(UUID.randomUUID().toString());
        parkingRepository.saveAndFlush(parking);

        // Get the parking
        restParkingMockMvc
            .perform(get(ENTITY_API_URL_ID, parking.getCode()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.code").value(parking.getCode()))
            .andExpect(jsonPath("$.parkingStatus").value(DEFAULT_PARKING_STATUS.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getParkingsByIdFiltering() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        String id = parking.getCode();

        defaultParkingShouldBeFound("code.equals=" + id);
        defaultParkingShouldNotBeFound("code.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllParkingsByParkingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where parkingStatus equals to DEFAULT_PARKING_STATUS
        defaultParkingShouldBeFound("parkingStatus.equals=" + DEFAULT_PARKING_STATUS);

        // Get all the parkingList where parkingStatus equals to UPDATED_PARKING_STATUS
        defaultParkingShouldNotBeFound("parkingStatus.equals=" + UPDATED_PARKING_STATUS);
    }

    @Test
    @Transactional
    void getAllParkingsByParkingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where parkingStatus in DEFAULT_PARKING_STATUS or UPDATED_PARKING_STATUS
        defaultParkingShouldBeFound("parkingStatus.in=" + DEFAULT_PARKING_STATUS + "," + UPDATED_PARKING_STATUS);

        // Get all the parkingList where parkingStatus equals to UPDATED_PARKING_STATUS
        defaultParkingShouldNotBeFound("parkingStatus.in=" + UPDATED_PARKING_STATUS);
    }

    @Test
    @Transactional
    void getAllParkingsByParkingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where parkingStatus is not null
        defaultParkingShouldBeFound("parkingStatus.specified=true");

        // Get all the parkingList where parkingStatus is null
        defaultParkingShouldNotBeFound("parkingStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingsByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where createBy equals to DEFAULT_CREATE_BY
        defaultParkingShouldBeFound("createBy.equals=" + DEFAULT_CREATE_BY);

        // Get all the parkingList where createBy equals to UPDATED_CREATE_BY
        defaultParkingShouldNotBeFound("createBy.equals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllParkingsByCreateByIsInShouldWork() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where createBy in DEFAULT_CREATE_BY or UPDATED_CREATE_BY
        defaultParkingShouldBeFound("createBy.in=" + DEFAULT_CREATE_BY + "," + UPDATED_CREATE_BY);

        // Get all the parkingList where createBy equals to UPDATED_CREATE_BY
        defaultParkingShouldNotBeFound("createBy.in=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllParkingsByCreateByIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where createBy is not null
        defaultParkingShouldBeFound("createBy.specified=true");

        // Get all the parkingList where createBy is null
        defaultParkingShouldNotBeFound("createBy.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingsByCreateByContainsSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where createBy contains DEFAULT_CREATE_BY
        defaultParkingShouldBeFound("createBy.contains=" + DEFAULT_CREATE_BY);

        // Get all the parkingList where createBy contains UPDATED_CREATE_BY
        defaultParkingShouldNotBeFound("createBy.contains=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllParkingsByCreateByNotContainsSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where createBy does not contain DEFAULT_CREATE_BY
        defaultParkingShouldNotBeFound("createBy.doesNotContain=" + DEFAULT_CREATE_BY);

        // Get all the parkingList where createBy does not contain UPDATED_CREATE_BY
        defaultParkingShouldBeFound("createBy.doesNotContain=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllParkingsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where createDate equals to DEFAULT_CREATE_DATE
        defaultParkingShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the parkingList where createDate equals to UPDATED_CREATE_DATE
        defaultParkingShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllParkingsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultParkingShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the parkingList where createDate equals to UPDATED_CREATE_DATE
        defaultParkingShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllParkingsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where createDate is not null
        defaultParkingShouldBeFound("createDate.specified=true");

        // Get all the parkingList where createDate is null
        defaultParkingShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultParkingShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the parkingList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultParkingShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllParkingsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultParkingShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the parkingList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultParkingShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllParkingsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where lastModifiedBy is not null
        defaultParkingShouldBeFound("lastModifiedBy.specified=true");

        // Get all the parkingList where lastModifiedBy is null
        defaultParkingShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultParkingShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the parkingList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultParkingShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllParkingsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultParkingShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the parkingList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultParkingShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllParkingsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultParkingShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the parkingList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultParkingShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllParkingsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultParkingShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the parkingList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultParkingShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllParkingsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingRepository.saveAndFlush(parking);

        // Get all the parkingList where lastModifiedDate is not null
        defaultParkingShouldBeFound("lastModifiedDate.specified=true");

        // Get all the parkingList where lastModifiedDate is null
        defaultParkingShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingsByReservationsIsEqualToSomething() throws Exception {
        Reservation reservations;
        if (TestUtil.findAll(em, Reservation.class).isEmpty()) {
            parkingRepository.saveAndFlush(parking);
            reservations = ReservationResourceIT.createEntity(em);
        } else {
            reservations = TestUtil.findAll(em, Reservation.class).get(0);
        }
        em.persist(reservations);
        em.flush();
        parking.addReservations(reservations);
        parkingRepository.saveAndFlush(parking);
        Long reservationsId = reservations.getId();

        // Get all the parkingList where reservations equals to reservationsId
        defaultParkingShouldBeFound("reservationsId.equals=" + reservationsId);

        // Get all the parkingList where reservations equals to (reservationsId + 1)
        defaultParkingShouldNotBeFound("reservationsId.equals=" + (reservationsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParkingShouldBeFound(String filter) throws Exception {
        restParkingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=code,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].code").value(hasItem(parking.getCode())))
            .andExpect(jsonPath("$.[*].parkingStatus").value(hasItem(DEFAULT_PARKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restParkingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=code,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParkingShouldNotBeFound(String filter) throws Exception {
        restParkingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=code,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParkingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=code,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParking() throws Exception {
        // Get the parking
        restParkingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParking() throws Exception {
        // Initialize the database
        parking.setCode(UUID.randomUUID().toString());
        parkingRepository.saveAndFlush(parking);

        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();

        // Update the parking
        Parking updatedParking = parkingRepository.findById(parking.getCode()).get();
        // Disconnect from session so that the updates on updatedParking are not directly saved in db
        em.detach(updatedParking);
        updatedParking
            .parkingStatus(UPDATED_PARKING_STATUS)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ParkingDTO parkingDTO = parkingMapper.toDto(updatedParking);

        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parkingDTO.getCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getParkingStatus()).isEqualTo(UPDATED_PARKING_STATUS);
        assertThat(testParking.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testParking.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testParking.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testParking.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setCode(UUID.randomUUID().toString());

        // Create the Parking
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parkingDTO.getCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setCode(UUID.randomUUID().toString());

        // Create the Parking
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setCode(UUID.randomUUID().toString());

        // Create the Parking
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParkingWithPatch() throws Exception {
        // Initialize the database
        parking.setCode(UUID.randomUUID().toString());
        parkingRepository.saveAndFlush(parking);

        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();

        // Update the parking using partial update
        Parking partialUpdatedParking = new Parking();
        partialUpdatedParking.setCode(parking.getCode());

        partialUpdatedParking.lastModifiedBy(UPDATED_LAST_MODIFIED_BY).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParking.getCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParking))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getParkingStatus()).isEqualTo(DEFAULT_PARKING_STATUS);
        assertThat(testParking.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testParking.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testParking.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testParking.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateParkingWithPatch() throws Exception {
        // Initialize the database
        parking.setCode(UUID.randomUUID().toString());
        parkingRepository.saveAndFlush(parking);

        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();

        // Update the parking using partial update
        Parking partialUpdatedParking = new Parking();
        partialUpdatedParking.setCode(parking.getCode());

        partialUpdatedParking
            .parkingStatus(UPDATED_PARKING_STATUS)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParking.getCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParking))
            )
            .andExpect(status().isOk());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
        Parking testParking = parkingList.get(parkingList.size() - 1);
        assertThat(testParking.getParkingStatus()).isEqualTo(UPDATED_PARKING_STATUS);
        assertThat(testParking.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testParking.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testParking.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testParking.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setCode(UUID.randomUUID().toString());

        // Create the Parking
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parkingDTO.getCode())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setCode(UUID.randomUUID().toString());

        // Create the Parking
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParking() throws Exception {
        int databaseSizeBeforeUpdate = parkingRepository.findAll().size();
        parking.setCode(UUID.randomUUID().toString());

        // Create the Parking
        ParkingDTO parkingDTO = parkingMapper.toDto(parking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parkingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parking in the database
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParking() throws Exception {
        // Initialize the database
        parking.setCode(UUID.randomUUID().toString());
        parkingRepository.saveAndFlush(parking);

        int databaseSizeBeforeDelete = parkingRepository.findAll().size();

        // Delete the parking
        restParkingMockMvc
            .perform(delete(ENTITY_API_URL_ID, parking.getCode()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parking> parkingList = parkingRepository.findAll();
        assertThat(parkingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
