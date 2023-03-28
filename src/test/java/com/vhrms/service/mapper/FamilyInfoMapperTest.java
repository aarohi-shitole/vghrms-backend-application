package com.vhrms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FamilyInfoMapperTest {

    private FamilyInfoMapper familyInfoMapper;

    @BeforeEach
    public void setUp() {
        familyInfoMapper = new FamilyInfoMapperImpl();
    }
}
