package com.vhrms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonalIdMapperTest {

    private PersonalIdMapper personalIdMapper;

    @BeforeEach
    public void setUp() {
        personalIdMapper = new PersonalIdMapperImpl();
    }
}
