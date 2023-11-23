package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParkingMapperTest {

    private ParkingMapper parkingMapper;

    @BeforeEach
    public void setUp() {
        parkingMapper = new ParkingMapperImpl();
    }
}
