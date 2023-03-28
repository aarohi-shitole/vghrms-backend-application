package com.vhrms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vhrms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reporting.class);
        Reporting reporting1 = new Reporting();
        reporting1.setId(1L);
        Reporting reporting2 = new Reporting();
        reporting2.setId(reporting1.getId());
        assertThat(reporting1).isEqualTo(reporting2);
        reporting2.setId(2L);
        assertThat(reporting1).isNotEqualTo(reporting2);
        reporting1.setId(null);
        assertThat(reporting1).isNotEqualTo(reporting2);
    }
}
