package com.vhrms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vhrms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyInfoDTO.class);
        FamilyInfoDTO familyInfoDTO1 = new FamilyInfoDTO();
        familyInfoDTO1.setId(1L);
        FamilyInfoDTO familyInfoDTO2 = new FamilyInfoDTO();
        assertThat(familyInfoDTO1).isNotEqualTo(familyInfoDTO2);
        familyInfoDTO2.setId(familyInfoDTO1.getId());
        assertThat(familyInfoDTO1).isEqualTo(familyInfoDTO2);
        familyInfoDTO2.setId(2L);
        assertThat(familyInfoDTO1).isNotEqualTo(familyInfoDTO2);
        familyInfoDTO1.setId(null);
        assertThat(familyInfoDTO1).isNotEqualTo(familyInfoDTO2);
    }
}
