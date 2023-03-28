package com.vhrms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vhrms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalIdDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalIdDTO.class);
        PersonalIdDTO personalIdDTO1 = new PersonalIdDTO();
        personalIdDTO1.setId(1L);
        PersonalIdDTO personalIdDTO2 = new PersonalIdDTO();
        assertThat(personalIdDTO1).isNotEqualTo(personalIdDTO2);
        personalIdDTO2.setId(personalIdDTO1.getId());
        assertThat(personalIdDTO1).isEqualTo(personalIdDTO2);
        personalIdDTO2.setId(2L);
        assertThat(personalIdDTO1).isNotEqualTo(personalIdDTO2);
        personalIdDTO1.setId(null);
        assertThat(personalIdDTO1).isNotEqualTo(personalIdDTO2);
    }
}
