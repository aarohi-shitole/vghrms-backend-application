package com.vhrms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vhrms.IntegrationTest;
import com.vhrms.domain.PersonalDetails;
import com.vhrms.repository.PersonalDetailsRepository;
import com.vhrms.service.criteria.PersonalDetailsCriteria;
import com.vhrms.service.dto.PersonalDetailsDTO;
import com.vhrms.service.mapper.PersonalDetailsMapper;
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
 * Integration tests for the {@link PersonalDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalDetailsResourceIT {

    private static final String DEFAULT_TELEPHONE_NO = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_MARITAL_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_MARITAL_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_RELIGION = "AAAAAAAAAA";
    private static final String UPDATED_RELIGION = "BBBBBBBBBB";

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;
    private static final Long SMALLER_EMPLOYEE_ID = 1L - 1L;

    private static final String DEFAULT_BLOOD_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_BLOOD_GROUP = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;
    private static final Long SMALLER_COMPANY_ID = 1L - 1L;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personal-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;

    @Autowired
    private PersonalDetailsMapper personalDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalDetailsMockMvc;

    private PersonalDetails personalDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalDetails createEntity(EntityManager em) {
        PersonalDetails personalDetails = new PersonalDetails()
            .telephoneNo(DEFAULT_TELEPHONE_NO)
            .nationality(DEFAULT_NATIONALITY)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .religion(DEFAULT_RELIGION)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .bloodGroup(DEFAULT_BLOOD_GROUP)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .status(DEFAULT_STATUS)
            .companyId(DEFAULT_COMPANY_ID)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return personalDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalDetails createUpdatedEntity(EntityManager em) {
        PersonalDetails personalDetails = new PersonalDetails()
            .telephoneNo(UPDATED_TELEPHONE_NO)
            .nationality(UPDATED_NATIONALITY)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .religion(UPDATED_RELIGION)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return personalDetails;
    }

    @BeforeEach
    public void initTest() {
        personalDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalDetails() throws Exception {
        int databaseSizeBeforeCreate = personalDetailsRepository.findAll().size();
        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);
        restPersonalDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalDetails testPersonalDetails = personalDetailsList.get(personalDetailsList.size() - 1);
        assertThat(testPersonalDetails.getTelephoneNo()).isEqualTo(DEFAULT_TELEPHONE_NO);
        assertThat(testPersonalDetails.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testPersonalDetails.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testPersonalDetails.getReligion()).isEqualTo(DEFAULT_RELIGION);
        assertThat(testPersonalDetails.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testPersonalDetails.getBloodGroup()).isEqualTo(DEFAULT_BLOOD_GROUP);
        assertThat(testPersonalDetails.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testPersonalDetails.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPersonalDetails.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testPersonalDetails.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPersonalDetails.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createPersonalDetailsWithExistingId() throws Exception {
        // Create the PersonalDetails with an existing ID
        personalDetails.setId(1L);
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        int databaseSizeBeforeCreate = personalDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonalDetails() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList
        restPersonalDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].telephoneNo").value(hasItem(DEFAULT_TELEPHONE_NO)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS)))
            .andExpect(jsonPath("$.[*].religion").value(hasItem(DEFAULT_RELIGION)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].bloodGroup").value(hasItem(DEFAULT_BLOOD_GROUP)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getPersonalDetails() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get the personalDetails
        restPersonalDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, personalDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalDetails.getId().intValue()))
            .andExpect(jsonPath("$.telephoneNo").value(DEFAULT_TELEPHONE_NO))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS))
            .andExpect(jsonPath("$.religion").value(DEFAULT_RELIGION))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.bloodGroup").value(DEFAULT_BLOOD_GROUP))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getPersonalDetailsByIdFiltering() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        Long id = personalDetails.getId();

        defaultPersonalDetailsShouldBeFound("id.equals=" + id);
        defaultPersonalDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultPersonalDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonalDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonalDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonalDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByTelephoneNoIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where telephoneNo equals to DEFAULT_TELEPHONE_NO
        defaultPersonalDetailsShouldBeFound("telephoneNo.equals=" + DEFAULT_TELEPHONE_NO);

        // Get all the personalDetailsList where telephoneNo equals to UPDATED_TELEPHONE_NO
        defaultPersonalDetailsShouldNotBeFound("telephoneNo.equals=" + UPDATED_TELEPHONE_NO);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByTelephoneNoIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where telephoneNo in DEFAULT_TELEPHONE_NO or UPDATED_TELEPHONE_NO
        defaultPersonalDetailsShouldBeFound("telephoneNo.in=" + DEFAULT_TELEPHONE_NO + "," + UPDATED_TELEPHONE_NO);

        // Get all the personalDetailsList where telephoneNo equals to UPDATED_TELEPHONE_NO
        defaultPersonalDetailsShouldNotBeFound("telephoneNo.in=" + UPDATED_TELEPHONE_NO);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByTelephoneNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where telephoneNo is not null
        defaultPersonalDetailsShouldBeFound("telephoneNo.specified=true");

        // Get all the personalDetailsList where telephoneNo is null
        defaultPersonalDetailsShouldNotBeFound("telephoneNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByTelephoneNoContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where telephoneNo contains DEFAULT_TELEPHONE_NO
        defaultPersonalDetailsShouldBeFound("telephoneNo.contains=" + DEFAULT_TELEPHONE_NO);

        // Get all the personalDetailsList where telephoneNo contains UPDATED_TELEPHONE_NO
        defaultPersonalDetailsShouldNotBeFound("telephoneNo.contains=" + UPDATED_TELEPHONE_NO);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByTelephoneNoNotContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where telephoneNo does not contain DEFAULT_TELEPHONE_NO
        defaultPersonalDetailsShouldNotBeFound("telephoneNo.doesNotContain=" + DEFAULT_TELEPHONE_NO);

        // Get all the personalDetailsList where telephoneNo does not contain UPDATED_TELEPHONE_NO
        defaultPersonalDetailsShouldBeFound("telephoneNo.doesNotContain=" + UPDATED_TELEPHONE_NO);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where nationality equals to DEFAULT_NATIONALITY
        defaultPersonalDetailsShouldBeFound("nationality.equals=" + DEFAULT_NATIONALITY);

        // Get all the personalDetailsList where nationality equals to UPDATED_NATIONALITY
        defaultPersonalDetailsShouldNotBeFound("nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where nationality in DEFAULT_NATIONALITY or UPDATED_NATIONALITY
        defaultPersonalDetailsShouldBeFound("nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY);

        // Get all the personalDetailsList where nationality equals to UPDATED_NATIONALITY
        defaultPersonalDetailsShouldNotBeFound("nationality.in=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where nationality is not null
        defaultPersonalDetailsShouldBeFound("nationality.specified=true");

        // Get all the personalDetailsList where nationality is null
        defaultPersonalDetailsShouldNotBeFound("nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByNationalityContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where nationality contains DEFAULT_NATIONALITY
        defaultPersonalDetailsShouldBeFound("nationality.contains=" + DEFAULT_NATIONALITY);

        // Get all the personalDetailsList where nationality contains UPDATED_NATIONALITY
        defaultPersonalDetailsShouldNotBeFound("nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where nationality does not contain DEFAULT_NATIONALITY
        defaultPersonalDetailsShouldNotBeFound("nationality.doesNotContain=" + DEFAULT_NATIONALITY);

        // Get all the personalDetailsList where nationality does not contain UPDATED_NATIONALITY
        defaultPersonalDetailsShouldBeFound("nationality.doesNotContain=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByMaritalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where maritalStatus equals to DEFAULT_MARITAL_STATUS
        defaultPersonalDetailsShouldBeFound("maritalStatus.equals=" + DEFAULT_MARITAL_STATUS);

        // Get all the personalDetailsList where maritalStatus equals to UPDATED_MARITAL_STATUS
        defaultPersonalDetailsShouldNotBeFound("maritalStatus.equals=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByMaritalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where maritalStatus in DEFAULT_MARITAL_STATUS or UPDATED_MARITAL_STATUS
        defaultPersonalDetailsShouldBeFound("maritalStatus.in=" + DEFAULT_MARITAL_STATUS + "," + UPDATED_MARITAL_STATUS);

        // Get all the personalDetailsList where maritalStatus equals to UPDATED_MARITAL_STATUS
        defaultPersonalDetailsShouldNotBeFound("maritalStatus.in=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByMaritalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where maritalStatus is not null
        defaultPersonalDetailsShouldBeFound("maritalStatus.specified=true");

        // Get all the personalDetailsList where maritalStatus is null
        defaultPersonalDetailsShouldNotBeFound("maritalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByMaritalStatusContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where maritalStatus contains DEFAULT_MARITAL_STATUS
        defaultPersonalDetailsShouldBeFound("maritalStatus.contains=" + DEFAULT_MARITAL_STATUS);

        // Get all the personalDetailsList where maritalStatus contains UPDATED_MARITAL_STATUS
        defaultPersonalDetailsShouldNotBeFound("maritalStatus.contains=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByMaritalStatusNotContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where maritalStatus does not contain DEFAULT_MARITAL_STATUS
        defaultPersonalDetailsShouldNotBeFound("maritalStatus.doesNotContain=" + DEFAULT_MARITAL_STATUS);

        // Get all the personalDetailsList where maritalStatus does not contain UPDATED_MARITAL_STATUS
        defaultPersonalDetailsShouldBeFound("maritalStatus.doesNotContain=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByReligionIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where religion equals to DEFAULT_RELIGION
        defaultPersonalDetailsShouldBeFound("religion.equals=" + DEFAULT_RELIGION);

        // Get all the personalDetailsList where religion equals to UPDATED_RELIGION
        defaultPersonalDetailsShouldNotBeFound("religion.equals=" + UPDATED_RELIGION);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByReligionIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where religion in DEFAULT_RELIGION or UPDATED_RELIGION
        defaultPersonalDetailsShouldBeFound("religion.in=" + DEFAULT_RELIGION + "," + UPDATED_RELIGION);

        // Get all the personalDetailsList where religion equals to UPDATED_RELIGION
        defaultPersonalDetailsShouldNotBeFound("religion.in=" + UPDATED_RELIGION);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByReligionIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where religion is not null
        defaultPersonalDetailsShouldBeFound("religion.specified=true");

        // Get all the personalDetailsList where religion is null
        defaultPersonalDetailsShouldNotBeFound("religion.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByReligionContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where religion contains DEFAULT_RELIGION
        defaultPersonalDetailsShouldBeFound("religion.contains=" + DEFAULT_RELIGION);

        // Get all the personalDetailsList where religion contains UPDATED_RELIGION
        defaultPersonalDetailsShouldNotBeFound("religion.contains=" + UPDATED_RELIGION);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByReligionNotContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where religion does not contain DEFAULT_RELIGION
        defaultPersonalDetailsShouldNotBeFound("religion.doesNotContain=" + DEFAULT_RELIGION);

        // Get all the personalDetailsList where religion does not contain UPDATED_RELIGION
        defaultPersonalDetailsShouldBeFound("religion.doesNotContain=" + UPDATED_RELIGION);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultPersonalDetailsShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalDetailsList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultPersonalDetailsShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultPersonalDetailsShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the personalDetailsList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultPersonalDetailsShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where employeeId is not null
        defaultPersonalDetailsShouldBeFound("employeeId.specified=true");

        // Get all the personalDetailsList where employeeId is null
        defaultPersonalDetailsShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultPersonalDetailsShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalDetailsList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultPersonalDetailsShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultPersonalDetailsShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalDetailsList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultPersonalDetailsShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultPersonalDetailsShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalDetailsList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultPersonalDetailsShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultPersonalDetailsShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the personalDetailsList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultPersonalDetailsShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByBloodGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where bloodGroup equals to DEFAULT_BLOOD_GROUP
        defaultPersonalDetailsShouldBeFound("bloodGroup.equals=" + DEFAULT_BLOOD_GROUP);

        // Get all the personalDetailsList where bloodGroup equals to UPDATED_BLOOD_GROUP
        defaultPersonalDetailsShouldNotBeFound("bloodGroup.equals=" + UPDATED_BLOOD_GROUP);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByBloodGroupIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where bloodGroup in DEFAULT_BLOOD_GROUP or UPDATED_BLOOD_GROUP
        defaultPersonalDetailsShouldBeFound("bloodGroup.in=" + DEFAULT_BLOOD_GROUP + "," + UPDATED_BLOOD_GROUP);

        // Get all the personalDetailsList where bloodGroup equals to UPDATED_BLOOD_GROUP
        defaultPersonalDetailsShouldNotBeFound("bloodGroup.in=" + UPDATED_BLOOD_GROUP);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByBloodGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where bloodGroup is not null
        defaultPersonalDetailsShouldBeFound("bloodGroup.specified=true");

        // Get all the personalDetailsList where bloodGroup is null
        defaultPersonalDetailsShouldNotBeFound("bloodGroup.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByBloodGroupContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where bloodGroup contains DEFAULT_BLOOD_GROUP
        defaultPersonalDetailsShouldBeFound("bloodGroup.contains=" + DEFAULT_BLOOD_GROUP);

        // Get all the personalDetailsList where bloodGroup contains UPDATED_BLOOD_GROUP
        defaultPersonalDetailsShouldNotBeFound("bloodGroup.contains=" + UPDATED_BLOOD_GROUP);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByBloodGroupNotContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where bloodGroup does not contain DEFAULT_BLOOD_GROUP
        defaultPersonalDetailsShouldNotBeFound("bloodGroup.doesNotContain=" + DEFAULT_BLOOD_GROUP);

        // Get all the personalDetailsList where bloodGroup does not contain UPDATED_BLOOD_GROUP
        defaultPersonalDetailsShouldBeFound("bloodGroup.doesNotContain=" + UPDATED_BLOOD_GROUP);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultPersonalDetailsShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the personalDetailsList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultPersonalDetailsShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultPersonalDetailsShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the personalDetailsList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultPersonalDetailsShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where dateOfBirth is not null
        defaultPersonalDetailsShouldBeFound("dateOfBirth.specified=true");

        // Get all the personalDetailsList where dateOfBirth is null
        defaultPersonalDetailsShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultPersonalDetailsShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the personalDetailsList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultPersonalDetailsShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultPersonalDetailsShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the personalDetailsList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultPersonalDetailsShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultPersonalDetailsShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the personalDetailsList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultPersonalDetailsShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultPersonalDetailsShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the personalDetailsList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultPersonalDetailsShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where status equals to DEFAULT_STATUS
        defaultPersonalDetailsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the personalDetailsList where status equals to UPDATED_STATUS
        defaultPersonalDetailsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPersonalDetailsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the personalDetailsList where status equals to UPDATED_STATUS
        defaultPersonalDetailsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where status is not null
        defaultPersonalDetailsShouldBeFound("status.specified=true");

        // Get all the personalDetailsList where status is null
        defaultPersonalDetailsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByStatusContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where status contains DEFAULT_STATUS
        defaultPersonalDetailsShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the personalDetailsList where status contains UPDATED_STATUS
        defaultPersonalDetailsShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where status does not contain DEFAULT_STATUS
        defaultPersonalDetailsShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the personalDetailsList where status does not contain UPDATED_STATUS
        defaultPersonalDetailsShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where companyId equals to DEFAULT_COMPANY_ID
        defaultPersonalDetailsShouldBeFound("companyId.equals=" + DEFAULT_COMPANY_ID);

        // Get all the personalDetailsList where companyId equals to UPDATED_COMPANY_ID
        defaultPersonalDetailsShouldNotBeFound("companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where companyId in DEFAULT_COMPANY_ID or UPDATED_COMPANY_ID
        defaultPersonalDetailsShouldBeFound("companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID);

        // Get all the personalDetailsList where companyId equals to UPDATED_COMPANY_ID
        defaultPersonalDetailsShouldNotBeFound("companyId.in=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where companyId is not null
        defaultPersonalDetailsShouldBeFound("companyId.specified=true");

        // Get all the personalDetailsList where companyId is null
        defaultPersonalDetailsShouldNotBeFound("companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where companyId is greater than or equal to DEFAULT_COMPANY_ID
        defaultPersonalDetailsShouldBeFound("companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the personalDetailsList where companyId is greater than or equal to UPDATED_COMPANY_ID
        defaultPersonalDetailsShouldNotBeFound("companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where companyId is less than or equal to DEFAULT_COMPANY_ID
        defaultPersonalDetailsShouldBeFound("companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the personalDetailsList where companyId is less than or equal to SMALLER_COMPANY_ID
        defaultPersonalDetailsShouldNotBeFound("companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where companyId is less than DEFAULT_COMPANY_ID
        defaultPersonalDetailsShouldNotBeFound("companyId.lessThan=" + DEFAULT_COMPANY_ID);

        // Get all the personalDetailsList where companyId is less than UPDATED_COMPANY_ID
        defaultPersonalDetailsShouldBeFound("companyId.lessThan=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where companyId is greater than DEFAULT_COMPANY_ID
        defaultPersonalDetailsShouldNotBeFound("companyId.greaterThan=" + DEFAULT_COMPANY_ID);

        // Get all the personalDetailsList where companyId is greater than SMALLER_COMPANY_ID
        defaultPersonalDetailsShouldBeFound("companyId.greaterThan=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultPersonalDetailsShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the personalDetailsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPersonalDetailsShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultPersonalDetailsShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the personalDetailsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPersonalDetailsShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where lastModified is not null
        defaultPersonalDetailsShouldBeFound("lastModified.specified=true");

        // Get all the personalDetailsList where lastModified is null
        defaultPersonalDetailsShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPersonalDetailsShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the personalDetailsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPersonalDetailsShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPersonalDetailsShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the personalDetailsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPersonalDetailsShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where lastModifiedBy is not null
        defaultPersonalDetailsShouldBeFound("lastModifiedBy.specified=true");

        // Get all the personalDetailsList where lastModifiedBy is null
        defaultPersonalDetailsShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultPersonalDetailsShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the personalDetailsList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultPersonalDetailsShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPersonalDetailsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        // Get all the personalDetailsList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultPersonalDetailsShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the personalDetailsList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultPersonalDetailsShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonalDetailsShouldBeFound(String filter) throws Exception {
        restPersonalDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].telephoneNo").value(hasItem(DEFAULT_TELEPHONE_NO)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS)))
            .andExpect(jsonPath("$.[*].religion").value(hasItem(DEFAULT_RELIGION)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].bloodGroup").value(hasItem(DEFAULT_BLOOD_GROUP)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restPersonalDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonalDetailsShouldNotBeFound(String filter) throws Exception {
        restPersonalDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonalDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonalDetails() throws Exception {
        // Get the personalDetails
        restPersonalDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalDetails() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();

        // Update the personalDetails
        PersonalDetails updatedPersonalDetails = personalDetailsRepository.findById(personalDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPersonalDetails are not directly saved in db
        em.detach(updatedPersonalDetails);
        updatedPersonalDetails
            .telephoneNo(UPDATED_TELEPHONE_NO)
            .nationality(UPDATED_NATIONALITY)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .religion(UPDATED_RELIGION)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(updatedPersonalDetails);

        restPersonalDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
        PersonalDetails testPersonalDetails = personalDetailsList.get(personalDetailsList.size() - 1);
        assertThat(testPersonalDetails.getTelephoneNo()).isEqualTo(UPDATED_TELEPHONE_NO);
        assertThat(testPersonalDetails.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testPersonalDetails.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testPersonalDetails.getReligion()).isEqualTo(UPDATED_RELIGION);
        assertThat(testPersonalDetails.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testPersonalDetails.getBloodGroup()).isEqualTo(UPDATED_BLOOD_GROUP);
        assertThat(testPersonalDetails.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPersonalDetails.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPersonalDetails.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testPersonalDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPersonalDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingPersonalDetails() throws Exception {
        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();
        personalDetails.setId(count.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalDetails() throws Exception {
        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();
        personalDetails.setId(count.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalDetails() throws Exception {
        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();
        personalDetails.setId(count.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalDetailsWithPatch() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();

        // Update the personalDetails using partial update
        PersonalDetails partialUpdatedPersonalDetails = new PersonalDetails();
        partialUpdatedPersonalDetails.setId(personalDetails.getId());

        partialUpdatedPersonalDetails
            .telephoneNo(UPDATED_TELEPHONE_NO)
            .nationality(UPDATED_NATIONALITY)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .companyId(UPDATED_COMPANY_ID);

        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalDetails))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
        PersonalDetails testPersonalDetails = personalDetailsList.get(personalDetailsList.size() - 1);
        assertThat(testPersonalDetails.getTelephoneNo()).isEqualTo(UPDATED_TELEPHONE_NO);
        assertThat(testPersonalDetails.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testPersonalDetails.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testPersonalDetails.getReligion()).isEqualTo(DEFAULT_RELIGION);
        assertThat(testPersonalDetails.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testPersonalDetails.getBloodGroup()).isEqualTo(DEFAULT_BLOOD_GROUP);
        assertThat(testPersonalDetails.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPersonalDetails.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPersonalDetails.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testPersonalDetails.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPersonalDetails.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdatePersonalDetailsWithPatch() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();

        // Update the personalDetails using partial update
        PersonalDetails partialUpdatedPersonalDetails = new PersonalDetails();
        partialUpdatedPersonalDetails.setId(personalDetails.getId());

        partialUpdatedPersonalDetails
            .telephoneNo(UPDATED_TELEPHONE_NO)
            .nationality(UPDATED_NATIONALITY)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .religion(UPDATED_RELIGION)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalDetails))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
        PersonalDetails testPersonalDetails = personalDetailsList.get(personalDetailsList.size() - 1);
        assertThat(testPersonalDetails.getTelephoneNo()).isEqualTo(UPDATED_TELEPHONE_NO);
        assertThat(testPersonalDetails.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testPersonalDetails.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testPersonalDetails.getReligion()).isEqualTo(UPDATED_RELIGION);
        assertThat(testPersonalDetails.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testPersonalDetails.getBloodGroup()).isEqualTo(UPDATED_BLOOD_GROUP);
        assertThat(testPersonalDetails.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testPersonalDetails.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPersonalDetails.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testPersonalDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPersonalDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalDetails() throws Exception {
        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();
        personalDetails.setId(count.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalDetails() throws Exception {
        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();
        personalDetails.setId(count.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalDetails() throws Exception {
        int databaseSizeBeforeUpdate = personalDetailsRepository.findAll().size();
        personalDetails.setId(count.incrementAndGet());

        // Create the PersonalDetails
        PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalDetails in the database
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalDetails() throws Exception {
        // Initialize the database
        personalDetailsRepository.saveAndFlush(personalDetails);

        int databaseSizeBeforeDelete = personalDetailsRepository.findAll().size();

        // Delete the personalDetails
        restPersonalDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
        assertThat(personalDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
