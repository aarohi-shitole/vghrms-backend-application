package com.vhrms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vhrms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyInfo.class);
        FamilyInfo familyInfo1 = new FamilyInfo();
        familyInfo1.setId(1L);
        FamilyInfo familyInfo2 = new FamilyInfo();
        familyInfo2.setId(familyInfo1.getId());
        assertThat(familyInfo1).isEqualTo(familyInfo2);
        familyInfo2.setId(2L);
        assertThat(familyInfo1).isNotEqualTo(familyInfo2);
        familyInfo1.setId(null);
        assertThat(familyInfo1).isNotEqualTo(familyInfo2);
    }
}
