package com.vhrms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonalDetailsMapperTest {

    private PersonalDetailsMapper personalDetailsMapper;

    @BeforeEach
    public void setUp() {
        personalDetailsMapper = new PersonalDetailsMapperImpl();
    }
}
