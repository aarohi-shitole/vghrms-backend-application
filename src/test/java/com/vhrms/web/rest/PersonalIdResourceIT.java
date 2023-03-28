package com.vhrms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vhrms.IntegrationTest;
import com.vhrms.domain.PersonalId;
import com.vhrms.repository.PersonalIdRepository;
import com.vhrms.service.criteria.PersonalIdCriteria;
import com.vhrms.service.dto.PersonalIdDTO;
import com.vhrms.service.mapper.PersonalIdMapper;
import java.time.Instant;
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
 * Integration tests for the {@link PersonalIdResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalIdResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXP_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXP_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ISSUING_AUTHORITY = "AAAAAAAAAA";
    private static final String UPDATED_ISSUING_AUTHORITY = "BBBBBBBBBB";

    private static final String DEFAULT_DOC_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOC_URL = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/personal-ids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalIdRepository personalIdRepository;

    @Autowired
    private PersonalIdMapper personalIdMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalIdMockMvc;

    private PersonalId personalId;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalId createEntity(EntityManager em) {
        PersonalId personalId = new PersonalId()
            .type(DEFAULT_TYPE)
            .number(DEFAULT_NUMBER)
            .issueDate(DEFAULT_ISSUE_DATE)
            .expDate(DEFAULT_EXP_DATE)
            .issuingAuthority(DEFAULT_ISSUING_AUTHORITY)
            .docUrl(DEFAULT_DOC_URL)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .status(DEFAULT_STATUS)
            .companyId(DEFAULT_COMPANY_ID)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return personalId;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalId createUpdatedEntity(EntityManager em) {
        PersonalId personalId = new PersonalId()
            .type(UPDATED_TYPE)
            .number(UPDATED_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .expDate(UPDATED_EXP_DATE)
            .issuingAuthority(UPDATED_ISSUING_AUTHORITY)
            .docUrl(UPDATED_DOC_URL)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return personalId;
    }

    @BeforeEach
    public void initTest() {
        personalId = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalId() throws Exception {
        int databaseSizeBeforeCreate = personalIdRepository.findAll().size();
        // Create the PersonalId
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(personalId);
        restPersonalIdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalIdDTO)))
            .andExpect(status().isCreated());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalId testPersonalId = personalIdList.get(personalIdList.size() - 1);
        assertThat(testPersonalId.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPersonalId.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPersonalId.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testPersonalId.getExpDate()).isEqualTo(DEFAULT_EXP_DATE);
        assertThat(testPersonalId.getIssuingAuthority()).isEqualTo(DEFAULT_ISSUING_AUTHORITY);
        assertThat(testPersonalId.getDocUrl()).isEqualTo(DEFAULT_DOC_URL);
        assertThat(testPersonalId.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testPersonalId.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPersonalId.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testPersonalId.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPersonalId.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createPersonalIdWithExistingId() throws Exception {
        // Create the PersonalId with an existing ID
        personalId.setId(1L);
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(personalId);

        int databaseSizeBeforeCreate = personalIdRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalIdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalIdDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonalIds() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList
        restPersonalIdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalId.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())))
            .andExpect(jsonPath("$.[*].issuingAuthority").value(hasItem(DEFAULT_ISSUING_AUTHORITY)))
            .andExpect(jsonPath("$.[*].docUrl").value(hasItem(DEFAULT_DOC_URL)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getPersonalId() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get the personalId
        restPersonalIdMockMvc
            .perform(get(ENTITY_API_URL_ID, personalId.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalId.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.expDate").value(DEFAULT_EXP_DATE.toString()))
            .andExpect(jsonPath("$.issuingAuthority").value(DEFAULT_ISSUING_AUTHORITY))
            .andExpect(jsonPath("$.docUrl").value(DEFAULT_DOC_URL))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getPersonalIdsByIdFiltering() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        Long id = personalId.getId();

        defaultPersonalIdShouldBeFound("id.equals=" + id);
        defaultPersonalIdShouldNotBeFound("id.notEquals=" + id);

        defaultPersonalIdShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonalIdShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonalIdShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonalIdShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where type equals to DEFAULT_TYPE
        defaultPersonalIdShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the personalIdList where type equals to UPDATED_TYPE
        defaultPersonalIdShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPersonalIdShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the personalIdList where type equals to UPDATED_TYPE
        defaultPersonalIdShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where type is not null
        defaultPersonalIdShouldBeFound("type.specified=true");

        // Get all the personalIdList where type is null
        defaultPersonalIdShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByTypeContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where type contains DEFAULT_TYPE
        defaultPersonalIdShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the personalIdList where type contains UPDATED_TYPE
        defaultPersonalIdShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where type does not contain DEFAULT_TYPE
        defaultPersonalIdShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the personalIdList where type does not contain UPDATED_TYPE
        defaultPersonalIdShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where number equals to DEFAULT_NUMBER
        defaultPersonalIdShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the personalIdList where number equals to UPDATED_NUMBER
        defaultPersonalIdShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultPersonalIdShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the personalIdList where number equals to UPDATED_NUMBER
        defaultPersonalIdShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where number is not null
        defaultPersonalIdShouldBeFound("number.specified=true");

        // Get all the personalIdList where number is null
        defaultPersonalIdShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByNumberContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where number contains DEFAULT_NUMBER
        defaultPersonalIdShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the personalIdList where number contains UPDATED_NUMBER
        defaultPersonalIdShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where number does not contain DEFAULT_NUMBER
        defaultPersonalIdShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the personalIdList where number does not contain UPDATED_NUMBER
        defaultPersonalIdShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where issueDate equals to DEFAULT_ISSUE_DATE
        defaultPersonalIdShouldBeFound("issueDate.equals=" + DEFAULT_ISSUE_DATE);

        // Get all the personalIdList where issueDate equals to UPDATED_ISSUE_DATE
        defaultPersonalIdShouldNotBeFound("issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where issueDate in DEFAULT_ISSUE_DATE or UPDATED_ISSUE_DATE
        defaultPersonalIdShouldBeFound("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE);

        // Get all the personalIdList where issueDate equals to UPDATED_ISSUE_DATE
        defaultPersonalIdShouldNotBeFound("issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where issueDate is not null
        defaultPersonalIdShouldBeFound("issueDate.specified=true");

        // Get all the personalIdList where issueDate is null
        defaultPersonalIdShouldNotBeFound("issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByExpDateIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where expDate equals to DEFAULT_EXP_DATE
        defaultPersonalIdShouldBeFound("expDate.equals=" + DEFAULT_EXP_DATE);

        // Get all the personalIdList where expDate equals to UPDATED_EXP_DATE
        defaultPersonalIdShouldNotBeFound("expDate.equals=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByExpDateIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where expDate in DEFAULT_EXP_DATE or UPDATED_EXP_DATE
        defaultPersonalIdShouldBeFound("expDate.in=" + DEFAULT_EXP_DATE + "," + UPDATED_EXP_DATE);

        // Get all the personalIdList where expDate equals to UPDATED_EXP_DATE
        defaultPersonalIdShouldNotBeFound("expDate.in=" + UPDATED_EXP_DATE);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByExpDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where expDate is not null
        defaultPersonalIdShouldBeFound("expDate.specified=true");

        // Get all the personalIdList where expDate is null
        defaultPersonalIdShouldNotBeFound("expDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByIssuingAuthorityIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where issuingAuthority equals to DEFAULT_ISSUING_AUTHORITY
        defaultPersonalIdShouldBeFound("issuingAuthority.equals=" + DEFAULT_ISSUING_AUTHORITY);

        // Get all the personalIdList where issuingAuthority equals to UPDATED_ISSUING_AUTHORITY
        defaultPersonalIdShouldNotBeFound("issuingAuthority.equals=" + UPDATED_ISSUING_AUTHORITY);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByIssuingAuthorityIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where issuingAuthority in DEFAULT_ISSUING_AUTHORITY or UPDATED_ISSUING_AUTHORITY
        defaultPersonalIdShouldBeFound("issuingAuthority.in=" + DEFAULT_ISSUING_AUTHORITY + "," + UPDATED_ISSUING_AUTHORITY);

        // Get all the personalIdList where issuingAuthority equals to UPDATED_ISSUING_AUTHORITY
        defaultPersonalIdShouldNotBeFound("issuingAuthority.in=" + UPDATED_ISSUING_AUTHORITY);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByIssuingAuthorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where issuingAuthority is not null
        defaultPersonalIdShouldBeFound("issuingAuthority.specified=true");

        // Get all the personalIdList where issuingAuthority is null
        defaultPersonalIdShouldNotBeFound("issuingAuthority.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByIssuingAuthorityContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where issuingAuthority contains DEFAULT_ISSUING_AUTHORITY
        defaultPersonalIdShouldBeFound("issuingAuthority.contains=" + DEFAULT_ISSUING_AUTHORITY);

        // Get all the personalIdList where issuingAuthority contains UPDATED_ISSUING_AUTHORITY
        defaultPersonalIdShouldNotBeFound("issuingAuthority.contains=" + UPDATED_ISSUING_AUTHORITY);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByIssuingAuthorityNotContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where issuingAuthority does not contain DEFAULT_ISSUING_AUTHORITY
        defaultPersonalIdShouldNotBeFound("issuingAuthority.doesNotContain=" + DEFAULT_ISSUING_AUTHORITY);

        // Get all the personalIdList where issuingAuthority does not contain UPDATED_ISSUING_AUTHORITY
        defaultPersonalIdShouldBeFound("issuingAuthority.doesNotContain=" + UPDATED_ISSUING_AUTHORITY);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByDocUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where docUrl equals to DEFAULT_DOC_URL
        defaultPersonalIdShouldBeFound("docUrl.equals=" + DEFAULT_DOC_URL);

        // Get all the personalIdList where docUrl equals to UPDATED_DOC_URL
        defaultPersonalIdShouldNotBeFound("docUrl.equals=" + UPDATED_DOC_URL);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByDocUrlIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where docUrl in DEFAULT_DOC_URL or UPDATED_DOC_URL
        defaultPersonalIdShouldBeFound("docUrl.in=" + DEFAULT_DOC_URL + "," + UPDATED_DOC_URL);

        // Get all the personalIdList where docUrl equals to UPDATED_DOC_URL
        defaultPersonalIdShouldNotBeFound("docUrl.in=" + UPDATED_DOC_URL);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByDocUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where docUrl is not null
        defaultPersonalIdShouldBeFound("docUrl.specified=true");

        // Get all the personalIdList where docUrl is null
        defaultPersonalIdShouldNotBeFound("docUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByDocUrlContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where docUrl contains DEFAULT_DOC_URL
        defaultPersonalIdShouldBeFound("docUrl.contains=" + DEFAULT_DOC_URL);

        // Get all the personalIdList where docUrl contains UPDATED_DOC_URL
        defaultPersonalIdShouldNotBeFound("docUrl.contains=" + UPDATED_DOC_URL);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByDocUrlNotContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where docUrl does not contain DEFAULT_DOC_URL
        defaultPersonalIdShouldNotBeFound("docUrl.doesNotContain=" + DEFAULT_DOC_URL);

        // Get all the personalIdList where docUrl does not contain UPDATED_DOC_URL
        defaultPersonalIdShouldBeFound("docUrl.doesNotContain=" + UPDATED_DOC_URL);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultPersonalIdShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalIdList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultPersonalIdShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultPersonalIdShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the personalIdList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultPersonalIdShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where employeeId is not null
        defaultPersonalIdShouldBeFound("employeeId.specified=true");

        // Get all the personalIdList where employeeId is null
        defaultPersonalIdShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultPersonalIdShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalIdList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultPersonalIdShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultPersonalIdShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalIdList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultPersonalIdShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultPersonalIdShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalIdList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultPersonalIdShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultPersonalIdShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalIdList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultPersonalIdShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where status equals to DEFAULT_STATUS
        defaultPersonalIdShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the personalIdList where status equals to UPDATED_STATUS
        defaultPersonalIdShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPersonalIdShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the personalIdList where status equals to UPDATED_STATUS
        defaultPersonalIdShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where status is not null
        defaultPersonalIdShouldBeFound("status.specified=true");

        // Get all the personalIdList where status is null
        defaultPersonalIdShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByStatusContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where status contains DEFAULT_STATUS
        defaultPersonalIdShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the personalIdList where status contains UPDATED_STATUS
        defaultPersonalIdShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where status does not contain DEFAULT_STATUS
        defaultPersonalIdShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the personalIdList where status does not contain UPDATED_STATUS
        defaultPersonalIdShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where companyId equals to DEFAULT_COMPANY_ID
        defaultPersonalIdShouldBeFound("companyId.equals=" + DEFAULT_COMPANY_ID);

        // Get all the personalIdList where companyId equals to UPDATED_COMPANY_ID
        defaultPersonalIdShouldNotBeFound("companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where companyId in DEFAULT_COMPANY_ID or UPDATED_COMPANY_ID
        defaultPersonalIdShouldBeFound("companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID);

        // Get all the personalIdList where companyId equals to UPDATED_COMPANY_ID
        defaultPersonalIdShouldNotBeFound("companyId.in=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where companyId is not null
        defaultPersonalIdShouldBeFound("companyId.specified=true");

        // Get all the personalIdList where companyId is null
        defaultPersonalIdShouldNotBeFound("companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where companyId is greater than or equal to DEFAULT_COMPANY_ID
        defaultPersonalIdShouldBeFound("companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the personalIdList where companyId is greater than or equal to UPDATED_COMPANY_ID
        defaultPersonalIdShouldNotBeFound("companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where companyId is less than or equal to DEFAULT_COMPANY_ID
        defaultPersonalIdShouldBeFound("companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the personalIdList where companyId is less than or equal to SMALLER_COMPANY_ID
        defaultPersonalIdShouldNotBeFound("companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where companyId is less than DEFAULT_COMPANY_ID
        defaultPersonalIdShouldNotBeFound("companyId.lessThan=" + DEFAULT_COMPANY_ID);

        // Get all the personalIdList where companyId is less than UPDATED_COMPANY_ID
        defaultPersonalIdShouldBeFound("companyId.lessThan=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where companyId is greater than DEFAULT_COMPANY_ID
        defaultPersonalIdShouldNotBeFound("companyId.greaterThan=" + DEFAULT_COMPANY_ID);

        // Get all the personalIdList where companyId is greater than SMALLER_COMPANY_ID
        defaultPersonalIdShouldBeFound("companyId.greaterThan=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultPersonalIdShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the personalIdList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPersonalIdShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultPersonalIdShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the personalIdList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPersonalIdShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where lastModified is not null
        defaultPersonalIdShouldBeFound("lastModified.specified=true");

        // Get all the personalIdList where lastModified is null
        defaultPersonalIdShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPersonalIdShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the personalIdList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPersonalIdShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPersonalIdShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the personalIdList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPersonalIdShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where lastModifiedBy is not null
        defaultPersonalIdShouldBeFound("lastModifiedBy.specified=true");

        // Get all the personalIdList where lastModifiedBy is null
        defaultPersonalIdShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalIdsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultPersonalIdShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the personalIdList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultPersonalIdShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPersonalIdsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        // Get all the personalIdList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultPersonalIdShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the personalIdList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultPersonalIdShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonalIdShouldBeFound(String filter) throws Exception {
        restPersonalIdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalId.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())))
            .andExpect(jsonPath("$.[*].issuingAuthority").value(hasItem(DEFAULT_ISSUING_AUTHORITY)))
            .andExpect(jsonPath("$.[*].docUrl").value(hasItem(DEFAULT_DOC_URL)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restPersonalIdMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonalIdShouldNotBeFound(String filter) throws Exception {
        restPersonalIdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonalIdMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonalId() throws Exception {
        // Get the personalId
        restPersonalIdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalId() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();

        // Update the personalId
        PersonalId updatedPersonalId = personalIdRepository.findById(personalId.getId()).get();
        // Disconnect from session so that the updates on updatedPersonalId are not directly saved in db
        em.detach(updatedPersonalId);
        updatedPersonalId
            .type(UPDATED_TYPE)
            .number(UPDATED_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .expDate(UPDATED_EXP_DATE)
            .issuingAuthority(UPDATED_ISSUING_AUTHORITY)
            .docUrl(UPDATED_DOC_URL)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(updatedPersonalId);

        restPersonalIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalIdDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalIdDTO))
            )
            .andExpect(status().isOk());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
        PersonalId testPersonalId = personalIdList.get(personalIdList.size() - 1);
        assertThat(testPersonalId.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPersonalId.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPersonalId.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testPersonalId.getExpDate()).isEqualTo(UPDATED_EXP_DATE);
        assertThat(testPersonalId.getIssuingAuthority()).isEqualTo(UPDATED_ISSUING_AUTHORITY);
        assertThat(testPersonalId.getDocUrl()).isEqualTo(UPDATED_DOC_URL);
        assertThat(testPersonalId.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testPersonalId.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPersonalId.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testPersonalId.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPersonalId.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingPersonalId() throws Exception {
        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();
        personalId.setId(count.incrementAndGet());

        // Create the PersonalId
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(personalId);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalIdDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalIdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalId() throws Exception {
        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();
        personalId.setId(count.incrementAndGet());

        // Create the PersonalId
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(personalId);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalIdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalId() throws Exception {
        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();
        personalId.setId(count.incrementAndGet());

        // Create the PersonalId
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(personalId);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalIdMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalIdDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalIdWithPatch() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();

        // Update the personalId using partial update
        PersonalId partialUpdatedPersonalId = new PersonalId();
        partialUpdatedPersonalId.setId(personalId.getId());

        partialUpdatedPersonalId
            .type(UPDATED_TYPE)
            .number(UPDATED_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .docUrl(UPDATED_DOC_URL)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPersonalIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalId.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalId))
            )
            .andExpect(status().isOk());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
        PersonalId testPersonalId = personalIdList.get(personalIdList.size() - 1);
        assertThat(testPersonalId.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPersonalId.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPersonalId.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testPersonalId.getExpDate()).isEqualTo(DEFAULT_EXP_DATE);
        assertThat(testPersonalId.getIssuingAuthority()).isEqualTo(DEFAULT_ISSUING_AUTHORITY);
        assertThat(testPersonalId.getDocUrl()).isEqualTo(UPDATED_DOC_URL);
        assertThat(testPersonalId.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testPersonalId.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPersonalId.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testPersonalId.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPersonalId.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdatePersonalIdWithPatch() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();

        // Update the personalId using partial update
        PersonalId partialUpdatedPersonalId = new PersonalId();
        partialUpdatedPersonalId.setId(personalId.getId());

        partialUpdatedPersonalId
            .type(UPDATED_TYPE)
            .number(UPDATED_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .expDate(UPDATED_EXP_DATE)
            .issuingAuthority(UPDATED_ISSUING_AUTHORITY)
            .docUrl(UPDATED_DOC_URL)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPersonalIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalId.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalId))
            )
            .andExpect(status().isOk());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
        PersonalId testPersonalId = personalIdList.get(personalIdList.size() - 1);
        assertThat(testPersonalId.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPersonalId.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPersonalId.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testPersonalId.getExpDate()).isEqualTo(UPDATED_EXP_DATE);
        assertThat(testPersonalId.getIssuingAuthority()).isEqualTo(UPDATED_ISSUING_AUTHORITY);
        assertThat(testPersonalId.getDocUrl()).isEqualTo(UPDATED_DOC_URL);
        assertThat(testPersonalId.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testPersonalId.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPersonalId.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testPersonalId.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPersonalId.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalId() throws Exception {
        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();
        personalId.setId(count.incrementAndGet());

        // Create the PersonalId
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(personalId);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalIdDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalIdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalId() throws Exception {
        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();
        personalId.setId(count.incrementAndGet());

        // Create the PersonalId
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(personalId);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalIdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalId() throws Exception {
        int databaseSizeBeforeUpdate = personalIdRepository.findAll().size();
        personalId.setId(count.incrementAndGet());

        // Create the PersonalId
        PersonalIdDTO personalIdDTO = personalIdMapper.toDto(personalId);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalIdMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personalIdDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalId in the database
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalId() throws Exception {
        // Initialize the database
        personalIdRepository.saveAndFlush(personalId);

        int databaseSizeBeforeDelete = personalIdRepository.findAll().size();

        // Delete the personalId
        restPersonalIdMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalId.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalId> personalIdList = personalIdRepository.findAll();
        assertThat(personalIdList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
