package com.vhrms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vhrms.IntegrationTest;
import com.vhrms.domain.FamilyInfo;
import com.vhrms.repository.FamilyInfoRepository;
import com.vhrms.service.criteria.FamilyInfoCriteria;
import com.vhrms.service.dto.FamilyInfoDTO;
import com.vhrms.service.mapper.FamilyInfoMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FamilyInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilyInfoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_RELATION = "AAAAAAAAAA";
    private static final String UPDATED_RELATION = "BBBBBBBBBB";

    private static final Long DEFAULT_ADDRESS_ID = 1L;
    private static final Long UPDATED_ADDRESS_ID = 2L;
    private static final Long SMALLER_ADDRESS_ID = 1L - 1L;

    private static final Boolean DEFAULT_IS_EMPLOYED = false;
    private static final Boolean UPDATED_IS_EMPLOYED = true;

    private static final String DEFAULT_EMPLOYED_AT = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYED_AT = "BBBBBBBBBB";

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;
    private static final Long SMALLER_EMPLOYEE_ID = 1L - 1L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;
    private static final Long SMALLER_COMPANY_ID = 1L - 1L;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/family-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilyInfoRepository familyInfoRepository;

    @Autowired
    private FamilyInfoMapper familyInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyInfoMockMvc;

    private FamilyInfo familyInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyInfo createEntity(EntityManager em) {
        FamilyInfo familyInfo = new FamilyInfo()
            .name(DEFAULT_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .relation(DEFAULT_RELATION)
            .addressId(DEFAULT_ADDRESS_ID)
            .isEmployed(DEFAULT_IS_EMPLOYED)
            .employedAt(DEFAULT_EMPLOYED_AT)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .status(DEFAULT_STATUS)
            .companyId(DEFAULT_COMPANY_ID)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return familyInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyInfo createUpdatedEntity(EntityManager em) {
        FamilyInfo familyInfo = new FamilyInfo()
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .relation(UPDATED_RELATION)
            .addressId(UPDATED_ADDRESS_ID)
            .isEmployed(UPDATED_IS_EMPLOYED)
            .employedAt(UPDATED_EMPLOYED_AT)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return familyInfo;
    }

    @BeforeEach
    public void initTest() {
        familyInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createFamilyInfo() throws Exception {
        int databaseSizeBeforeCreate = familyInfoRepository.findAll().size();
        // Create the FamilyInfo
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(familyInfo);
        restFamilyInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeCreate + 1);
        FamilyInfo testFamilyInfo = familyInfoList.get(familyInfoList.size() - 1);
        assertThat(testFamilyInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilyInfo.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testFamilyInfo.getRelation()).isEqualTo(DEFAULT_RELATION);
        assertThat(testFamilyInfo.getAddressId()).isEqualTo(DEFAULT_ADDRESS_ID);
        assertThat(testFamilyInfo.getIsEmployed()).isEqualTo(DEFAULT_IS_EMPLOYED);
        assertThat(testFamilyInfo.getEmployedAt()).isEqualTo(DEFAULT_EMPLOYED_AT);
        assertThat(testFamilyInfo.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testFamilyInfo.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFamilyInfo.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testFamilyInfo.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testFamilyInfo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createFamilyInfoWithExistingId() throws Exception {
        // Create the FamilyInfo with an existing ID
        familyInfo.setId(1L);
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(familyInfo);

        int databaseSizeBeforeCreate = familyInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFamilyInfos() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList
        restFamilyInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].relation").value(hasItem(DEFAULT_RELATION)))
            .andExpect(jsonPath("$.[*].addressId").value(hasItem(DEFAULT_ADDRESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].isEmployed").value(hasItem(DEFAULT_IS_EMPLOYED.booleanValue())))
            .andExpect(jsonPath("$.[*].employedAt").value(hasItem(DEFAULT_EMPLOYED_AT)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getFamilyInfo() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get the familyInfo
        restFamilyInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, familyInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familyInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.relation").value(DEFAULT_RELATION))
            .andExpect(jsonPath("$.addressId").value(DEFAULT_ADDRESS_ID.intValue()))
            .andExpect(jsonPath("$.isEmployed").value(DEFAULT_IS_EMPLOYED.booleanValue()))
            .andExpect(jsonPath("$.employedAt").value(DEFAULT_EMPLOYED_AT))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getFamilyInfosByIdFiltering() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        Long id = familyInfo.getId();

        defaultFamilyInfoShouldBeFound("id.equals=" + id);
        defaultFamilyInfoShouldNotBeFound("id.notEquals=" + id);

        defaultFamilyInfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFamilyInfoShouldNotBeFound("id.greaterThan=" + id);

        defaultFamilyInfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFamilyInfoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where name equals to DEFAULT_NAME
        defaultFamilyInfoShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the familyInfoList where name equals to UPDATED_NAME
        defaultFamilyInfoShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFamilyInfoShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the familyInfoList where name equals to UPDATED_NAME
        defaultFamilyInfoShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where name is not null
        defaultFamilyInfoShouldBeFound("name.specified=true");

        // Get all the familyInfoList where name is null
        defaultFamilyInfoShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByNameContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where name contains DEFAULT_NAME
        defaultFamilyInfoShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the familyInfoList where name contains UPDATED_NAME
        defaultFamilyInfoShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where name does not contain DEFAULT_NAME
        defaultFamilyInfoShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the familyInfoList where name does not contain UPDATED_NAME
        defaultFamilyInfoShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultFamilyInfoShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the familyInfoList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultFamilyInfoShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultFamilyInfoShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the familyInfoList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultFamilyInfoShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where dateOfBirth is not null
        defaultFamilyInfoShouldBeFound("dateOfBirth.specified=true");

        // Get all the familyInfoList where dateOfBirth is null
        defaultFamilyInfoShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultFamilyInfoShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the familyInfoList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultFamilyInfoShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultFamilyInfoShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the familyInfoList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultFamilyInfoShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultFamilyInfoShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the familyInfoList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultFamilyInfoShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultFamilyInfoShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the familyInfoList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultFamilyInfoShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByRelationIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where relation equals to DEFAULT_RELATION
        defaultFamilyInfoShouldBeFound("relation.equals=" + DEFAULT_RELATION);

        // Get all the familyInfoList where relation equals to UPDATED_RELATION
        defaultFamilyInfoShouldNotBeFound("relation.equals=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByRelationIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where relation in DEFAULT_RELATION or UPDATED_RELATION
        defaultFamilyInfoShouldBeFound("relation.in=" + DEFAULT_RELATION + "," + UPDATED_RELATION);

        // Get all the familyInfoList where relation equals to UPDATED_RELATION
        defaultFamilyInfoShouldNotBeFound("relation.in=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByRelationIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where relation is not null
        defaultFamilyInfoShouldBeFound("relation.specified=true");

        // Get all the familyInfoList where relation is null
        defaultFamilyInfoShouldNotBeFound("relation.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByRelationContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where relation contains DEFAULT_RELATION
        defaultFamilyInfoShouldBeFound("relation.contains=" + DEFAULT_RELATION);

        // Get all the familyInfoList where relation contains UPDATED_RELATION
        defaultFamilyInfoShouldNotBeFound("relation.contains=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByRelationNotContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where relation does not contain DEFAULT_RELATION
        defaultFamilyInfoShouldNotBeFound("relation.doesNotContain=" + DEFAULT_RELATION);

        // Get all the familyInfoList where relation does not contain UPDATED_RELATION
        defaultFamilyInfoShouldBeFound("relation.doesNotContain=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByAddressIdIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where addressId equals to DEFAULT_ADDRESS_ID
        defaultFamilyInfoShouldBeFound("addressId.equals=" + DEFAULT_ADDRESS_ID);

        // Get all the familyInfoList where addressId equals to UPDATED_ADDRESS_ID
        defaultFamilyInfoShouldNotBeFound("addressId.equals=" + UPDATED_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByAddressIdIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where addressId in DEFAULT_ADDRESS_ID or UPDATED_ADDRESS_ID
        defaultFamilyInfoShouldBeFound("addressId.in=" + DEFAULT_ADDRESS_ID + "," + UPDATED_ADDRESS_ID);

        // Get all the familyInfoList where addressId equals to UPDATED_ADDRESS_ID
        defaultFamilyInfoShouldNotBeFound("addressId.in=" + UPDATED_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByAddressIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where addressId is not null
        defaultFamilyInfoShouldBeFound("addressId.specified=true");

        // Get all the familyInfoList where addressId is null
        defaultFamilyInfoShouldNotBeFound("addressId.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByAddressIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where addressId is greater than or equal to DEFAULT_ADDRESS_ID
        defaultFamilyInfoShouldBeFound("addressId.greaterThanOrEqual=" + DEFAULT_ADDRESS_ID);

        // Get all the familyInfoList where addressId is greater than or equal to UPDATED_ADDRESS_ID
        defaultFamilyInfoShouldNotBeFound("addressId.greaterThanOrEqual=" + UPDATED_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByAddressIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where addressId is less than or equal to DEFAULT_ADDRESS_ID
        defaultFamilyInfoShouldBeFound("addressId.lessThanOrEqual=" + DEFAULT_ADDRESS_ID);

        // Get all the familyInfoList where addressId is less than or equal to SMALLER_ADDRESS_ID
        defaultFamilyInfoShouldNotBeFound("addressId.lessThanOrEqual=" + SMALLER_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByAddressIdIsLessThanSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where addressId is less than DEFAULT_ADDRESS_ID
        defaultFamilyInfoShouldNotBeFound("addressId.lessThan=" + DEFAULT_ADDRESS_ID);

        // Get all the familyInfoList where addressId is less than UPDATED_ADDRESS_ID
        defaultFamilyInfoShouldBeFound("addressId.lessThan=" + UPDATED_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByAddressIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where addressId is greater than DEFAULT_ADDRESS_ID
        defaultFamilyInfoShouldNotBeFound("addressId.greaterThan=" + DEFAULT_ADDRESS_ID);

        // Get all the familyInfoList where addressId is greater than SMALLER_ADDRESS_ID
        defaultFamilyInfoShouldBeFound("addressId.greaterThan=" + SMALLER_ADDRESS_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByIsEmployedIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where isEmployed equals to DEFAULT_IS_EMPLOYED
        defaultFamilyInfoShouldBeFound("isEmployed.equals=" + DEFAULT_IS_EMPLOYED);

        // Get all the familyInfoList where isEmployed equals to UPDATED_IS_EMPLOYED
        defaultFamilyInfoShouldNotBeFound("isEmployed.equals=" + UPDATED_IS_EMPLOYED);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByIsEmployedIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where isEmployed in DEFAULT_IS_EMPLOYED or UPDATED_IS_EMPLOYED
        defaultFamilyInfoShouldBeFound("isEmployed.in=" + DEFAULT_IS_EMPLOYED + "," + UPDATED_IS_EMPLOYED);

        // Get all the familyInfoList where isEmployed equals to UPDATED_IS_EMPLOYED
        defaultFamilyInfoShouldNotBeFound("isEmployed.in=" + UPDATED_IS_EMPLOYED);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByIsEmployedIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where isEmployed is not null
        defaultFamilyInfoShouldBeFound("isEmployed.specified=true");

        // Get all the familyInfoList where isEmployed is null
        defaultFamilyInfoShouldNotBeFound("isEmployed.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employedAt equals to DEFAULT_EMPLOYED_AT
        defaultFamilyInfoShouldBeFound("employedAt.equals=" + DEFAULT_EMPLOYED_AT);

        // Get all the familyInfoList where employedAt equals to UPDATED_EMPLOYED_AT
        defaultFamilyInfoShouldNotBeFound("employedAt.equals=" + UPDATED_EMPLOYED_AT);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployedAtIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employedAt in DEFAULT_EMPLOYED_AT or UPDATED_EMPLOYED_AT
        defaultFamilyInfoShouldBeFound("employedAt.in=" + DEFAULT_EMPLOYED_AT + "," + UPDATED_EMPLOYED_AT);

        // Get all the familyInfoList where employedAt equals to UPDATED_EMPLOYED_AT
        defaultFamilyInfoShouldNotBeFound("employedAt.in=" + UPDATED_EMPLOYED_AT);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employedAt is not null
        defaultFamilyInfoShouldBeFound("employedAt.specified=true");

        // Get all the familyInfoList where employedAt is null
        defaultFamilyInfoShouldNotBeFound("employedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployedAtContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employedAt contains DEFAULT_EMPLOYED_AT
        defaultFamilyInfoShouldBeFound("employedAt.contains=" + DEFAULT_EMPLOYED_AT);

        // Get all the familyInfoList where employedAt contains UPDATED_EMPLOYED_AT
        defaultFamilyInfoShouldNotBeFound("employedAt.contains=" + UPDATED_EMPLOYED_AT);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployedAtNotContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employedAt does not contain DEFAULT_EMPLOYED_AT
        defaultFamilyInfoShouldNotBeFound("employedAt.doesNotContain=" + DEFAULT_EMPLOYED_AT);

        // Get all the familyInfoList where employedAt does not contain UPDATED_EMPLOYED_AT
        defaultFamilyInfoShouldBeFound("employedAt.doesNotContain=" + UPDATED_EMPLOYED_AT);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultFamilyInfoShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the familyInfoList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultFamilyInfoShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultFamilyInfoShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the familyInfoList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultFamilyInfoShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employeeId is not null
        defaultFamilyInfoShouldBeFound("employeeId.specified=true");

        // Get all the familyInfoList where employeeId is null
        defaultFamilyInfoShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultFamilyInfoShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the familyInfoList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultFamilyInfoShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultFamilyInfoShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the familyInfoList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultFamilyInfoShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultFamilyInfoShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the familyInfoList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultFamilyInfoShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultFamilyInfoShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the familyInfoList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultFamilyInfoShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where status equals to DEFAULT_STATUS
        defaultFamilyInfoShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the familyInfoList where status equals to UPDATED_STATUS
        defaultFamilyInfoShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultFamilyInfoShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the familyInfoList where status equals to UPDATED_STATUS
        defaultFamilyInfoShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where status is not null
        defaultFamilyInfoShouldBeFound("status.specified=true");

        // Get all the familyInfoList where status is null
        defaultFamilyInfoShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByStatusContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where status contains DEFAULT_STATUS
        defaultFamilyInfoShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the familyInfoList where status contains UPDATED_STATUS
        defaultFamilyInfoShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where status does not contain DEFAULT_STATUS
        defaultFamilyInfoShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the familyInfoList where status does not contain UPDATED_STATUS
        defaultFamilyInfoShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where companyId equals to DEFAULT_COMPANY_ID
        defaultFamilyInfoShouldBeFound("companyId.equals=" + DEFAULT_COMPANY_ID);

        // Get all the familyInfoList where companyId equals to UPDATED_COMPANY_ID
        defaultFamilyInfoShouldNotBeFound("companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where companyId in DEFAULT_COMPANY_ID or UPDATED_COMPANY_ID
        defaultFamilyInfoShouldBeFound("companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID);

        // Get all the familyInfoList where companyId equals to UPDATED_COMPANY_ID
        defaultFamilyInfoShouldNotBeFound("companyId.in=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where companyId is not null
        defaultFamilyInfoShouldBeFound("companyId.specified=true");

        // Get all the familyInfoList where companyId is null
        defaultFamilyInfoShouldNotBeFound("companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where companyId is greater than or equal to DEFAULT_COMPANY_ID
        defaultFamilyInfoShouldBeFound("companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the familyInfoList where companyId is greater than or equal to UPDATED_COMPANY_ID
        defaultFamilyInfoShouldNotBeFound("companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where companyId is less than or equal to DEFAULT_COMPANY_ID
        defaultFamilyInfoShouldBeFound("companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the familyInfoList where companyId is less than or equal to SMALLER_COMPANY_ID
        defaultFamilyInfoShouldNotBeFound("companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where companyId is less than DEFAULT_COMPANY_ID
        defaultFamilyInfoShouldNotBeFound("companyId.lessThan=" + DEFAULT_COMPANY_ID);

        // Get all the familyInfoList where companyId is less than UPDATED_COMPANY_ID
        defaultFamilyInfoShouldBeFound("companyId.lessThan=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where companyId is greater than DEFAULT_COMPANY_ID
        defaultFamilyInfoShouldNotBeFound("companyId.greaterThan=" + DEFAULT_COMPANY_ID);

        // Get all the familyInfoList where companyId is greater than SMALLER_COMPANY_ID
        defaultFamilyInfoShouldBeFound("companyId.greaterThan=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultFamilyInfoShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the familyInfoList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultFamilyInfoShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultFamilyInfoShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the familyInfoList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultFamilyInfoShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where lastModified is not null
        defaultFamilyInfoShouldBeFound("lastModified.specified=true");

        // Get all the familyInfoList where lastModified is null
        defaultFamilyInfoShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultFamilyInfoShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the familyInfoList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultFamilyInfoShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultFamilyInfoShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the familyInfoList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultFamilyInfoShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where lastModifiedBy is not null
        defaultFamilyInfoShouldBeFound("lastModifiedBy.specified=true");

        // Get all the familyInfoList where lastModifiedBy is null
        defaultFamilyInfoShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyInfosByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultFamilyInfoShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the familyInfoList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultFamilyInfoShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllFamilyInfosByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        // Get all the familyInfoList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultFamilyInfoShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the familyInfoList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultFamilyInfoShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFamilyInfoShouldBeFound(String filter) throws Exception {
        restFamilyInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].relation").value(hasItem(DEFAULT_RELATION)))
            .andExpect(jsonPath("$.[*].addressId").value(hasItem(DEFAULT_ADDRESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].isEmployed").value(hasItem(DEFAULT_IS_EMPLOYED.booleanValue())))
            .andExpect(jsonPath("$.[*].employedAt").value(hasItem(DEFAULT_EMPLOYED_AT)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restFamilyInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFamilyInfoShouldNotBeFound(String filter) throws Exception {
        restFamilyInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFamilyInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFamilyInfo() throws Exception {
        // Get the familyInfo
        restFamilyInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFamilyInfo() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();

        // Update the familyInfo
        FamilyInfo updatedFamilyInfo = familyInfoRepository.findById(familyInfo.getId()).get();
        // Disconnect from session so that the updates on updatedFamilyInfo are not directly saved in db
        em.detach(updatedFamilyInfo);
        updatedFamilyInfo
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .relation(UPDATED_RELATION)
            .addressId(UPDATED_ADDRESS_ID)
            .isEmployed(UPDATED_IS_EMPLOYED)
            .employedAt(UPDATED_EMPLOYED_AT)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(updatedFamilyInfo);

        restFamilyInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
        FamilyInfo testFamilyInfo = familyInfoList.get(familyInfoList.size() - 1);
        assertThat(testFamilyInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyInfo.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testFamilyInfo.getRelation()).isEqualTo(UPDATED_RELATION);
        assertThat(testFamilyInfo.getAddressId()).isEqualTo(UPDATED_ADDRESS_ID);
        assertThat(testFamilyInfo.getIsEmployed()).isEqualTo(UPDATED_IS_EMPLOYED);
        assertThat(testFamilyInfo.getEmployedAt()).isEqualTo(UPDATED_EMPLOYED_AT);
        assertThat(testFamilyInfo.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testFamilyInfo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFamilyInfo.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testFamilyInfo.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testFamilyInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingFamilyInfo() throws Exception {
        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();
        familyInfo.setId(count.incrementAndGet());

        // Create the FamilyInfo
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(familyInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilyInfo() throws Exception {
        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();
        familyInfo.setId(count.incrementAndGet());

        // Create the FamilyInfo
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(familyInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilyInfo() throws Exception {
        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();
        familyInfo.setId(count.incrementAndGet());

        // Create the FamilyInfo
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(familyInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyInfoWithPatch() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();

        // Update the familyInfo using partial update
        FamilyInfo partialUpdatedFamilyInfo = new FamilyInfo();
        partialUpdatedFamilyInfo.setId(familyInfo.getId());

        partialUpdatedFamilyInfo
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .relation(UPDATED_RELATION)
            .addressId(UPDATED_ADDRESS_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .status(UPDATED_STATUS);

        restFamilyInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilyInfo))
            )
            .andExpect(status().isOk());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
        FamilyInfo testFamilyInfo = familyInfoList.get(familyInfoList.size() - 1);
        assertThat(testFamilyInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyInfo.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testFamilyInfo.getRelation()).isEqualTo(UPDATED_RELATION);
        assertThat(testFamilyInfo.getAddressId()).isEqualTo(UPDATED_ADDRESS_ID);
        assertThat(testFamilyInfo.getIsEmployed()).isEqualTo(DEFAULT_IS_EMPLOYED);
        assertThat(testFamilyInfo.getEmployedAt()).isEqualTo(DEFAULT_EMPLOYED_AT);
        assertThat(testFamilyInfo.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testFamilyInfo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFamilyInfo.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testFamilyInfo.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testFamilyInfo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateFamilyInfoWithPatch() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();

        // Update the familyInfo using partial update
        FamilyInfo partialUpdatedFamilyInfo = new FamilyInfo();
        partialUpdatedFamilyInfo.setId(familyInfo.getId());

        partialUpdatedFamilyInfo
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .relation(UPDATED_RELATION)
            .addressId(UPDATED_ADDRESS_ID)
            .isEmployed(UPDATED_IS_EMPLOYED)
            .employedAt(UPDATED_EMPLOYED_AT)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restFamilyInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilyInfo))
            )
            .andExpect(status().isOk());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
        FamilyInfo testFamilyInfo = familyInfoList.get(familyInfoList.size() - 1);
        assertThat(testFamilyInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyInfo.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testFamilyInfo.getRelation()).isEqualTo(UPDATED_RELATION);
        assertThat(testFamilyInfo.getAddressId()).isEqualTo(UPDATED_ADDRESS_ID);
        assertThat(testFamilyInfo.getIsEmployed()).isEqualTo(UPDATED_IS_EMPLOYED);
        assertThat(testFamilyInfo.getEmployedAt()).isEqualTo(UPDATED_EMPLOYED_AT);
        assertThat(testFamilyInfo.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testFamilyInfo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFamilyInfo.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testFamilyInfo.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testFamilyInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingFamilyInfo() throws Exception {
        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();
        familyInfo.setId(count.incrementAndGet());

        // Create the FamilyInfo
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(familyInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familyInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilyInfo() throws Exception {
        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();
        familyInfo.setId(count.incrementAndGet());

        // Create the FamilyInfo
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(familyInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilyInfo() throws Exception {
        int databaseSizeBeforeUpdate = familyInfoRepository.findAll().size();
        familyInfo.setId(count.incrementAndGet());

        // Create the FamilyInfo
        FamilyInfoDTO familyInfoDTO = familyInfoMapper.toDto(familyInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(familyInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyInfo in the database
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilyInfo() throws Exception {
        // Initialize the database
        familyInfoRepository.saveAndFlush(familyInfo);

        int databaseSizeBeforeDelete = familyInfoRepository.findAll().size();

        // Delete the familyInfo
        restFamilyInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, familyInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FamilyInfo> familyInfoList = familyInfoRepository.findAll();
        assertThat(familyInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
