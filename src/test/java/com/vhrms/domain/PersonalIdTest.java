package com.vhrms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vhrms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalIdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalId.class);
        PersonalId personalId1 = new PersonalId();
        personalId1.setId(1L);
        PersonalId personalId2 = new PersonalId();
        personalId2.setId(personalId1.getId());
        assertThat(personalId1).isEqualTo(personalId2);
        personalId2.setId(2L);
        assertThat(personalId1).isNotEqualTo(personalId2);
        personalId1.setId(null);
        assertThat(personalId1).isNotEqualTo(personalId2);
    }
}
