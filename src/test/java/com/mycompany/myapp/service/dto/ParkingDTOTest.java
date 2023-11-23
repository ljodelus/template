package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParkingDTO.class);
        ParkingDTO parkingDTO1 = new ParkingDTO();
        parkingDTO1.setCode("id1");
        ParkingDTO parkingDTO2 = new ParkingDTO();
        assertThat(parkingDTO1).isNotEqualTo(parkingDTO2);
        parkingDTO2.setCode(parkingDTO1.getCode());
        assertThat(parkingDTO1).isEqualTo(parkingDTO2);
        parkingDTO2.setCode("id2");
        assertThat(parkingDTO1).isNotEqualTo(parkingDTO2);
        parkingDTO1.setCode(null);
        assertThat(parkingDTO1).isNotEqualTo(parkingDTO2);
    }
}
