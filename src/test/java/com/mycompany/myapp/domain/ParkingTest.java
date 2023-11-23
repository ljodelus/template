package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parking.class);
        Parking parking1 = new Parking();
        parking1.setCode("id1");
        Parking parking2 = new Parking();
        parking2.setCode(parking1.getCode());
        assertThat(parking1).isEqualTo(parking2);
        parking2.setCode("id2");
        assertThat(parking1).isNotEqualTo(parking2);
        parking1.setCode(null);
        assertThat(parking1).isNotEqualTo(parking2);
    }
}
