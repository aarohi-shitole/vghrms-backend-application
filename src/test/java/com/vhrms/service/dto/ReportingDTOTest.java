package com.vhrms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vhrms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingDTO.class);
        ReportingDTO reportingDTO1 = new ReportingDTO();
        reportingDTO1.setId(1L);
        ReportingDTO reportingDTO2 = new ReportingDTO();
        assertThat(reportingDTO1).isNotEqualTo(reportingDTO2);
        reportingDTO2.setId(reportingDTO1.getId());
        assertThat(reportingDTO1).isEqualTo(reportingDTO2);
        reportingDTO2.setId(2L);
        assertThat(reportingDTO1).isNotEqualTo(reportingDTO2);
        reportingDTO1.setId(null);
        assertThat(reportingDTO1).isNotEqualTo(reportingDTO2);
    }
}
