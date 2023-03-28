package com.vhrms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingMapperTest {

    private ReportingMapper reportingMapper;

    @BeforeEach
    public void setUp() {
        reportingMapper = new ReportingMapperImpl();
    }
}
