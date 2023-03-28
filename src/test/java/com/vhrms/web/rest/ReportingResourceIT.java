package com.vhrms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vhrms.IntegrationTest;
import com.vhrms.domain.Reporting;
import com.vhrms.repository.ReportingRepository;
import com.vhrms.service.criteria.ReportingCriteria;
import com.vhrms.service.dto.ReportingDTO;
import com.vhrms.service.mapper.ReportingMapper;
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
 * Integration tests for the {@link ReportingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportingResourceIT {

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;
    private static final Long SMALLER_EMPLOYEE_ID = 1L - 1L;

    private static final Long DEFAULT_REPORTING_EMP_ID = 1L;
    private static final Long UPDATED_REPORTING_EMP_ID = 2L;
    private static final Long SMALLER_REPORTING_EMP_ID = 1L - 1L;

    private static final Long DEFAULT_REPORTING_ID = 1L;
    private static final Long UPDATED_REPORTING_ID = 2L;
    private static final Long SMALLER_REPORTING_ID = 1L - 1L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;
    private static final Long SMALLER_COMPANY_ID = 1L - 1L;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reportings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportingRepository reportingRepository;

    @Autowired
    private ReportingMapper reportingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingMockMvc;

    private Reporting reporting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reporting createEntity(EntityManager em) {
        Reporting reporting = new Reporting()
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .reportingEmpId(DEFAULT_REPORTING_EMP_ID)
            .reportingId(DEFAULT_REPORTING_ID)
            .status(DEFAULT_STATUS)
            .companyId(DEFAULT_COMPANY_ID)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return reporting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reporting createUpdatedEntity(EntityManager em) {
        Reporting reporting = new Reporting()
            .employeeId(UPDATED_EMPLOYEE_ID)
            .reportingEmpId(UPDATED_REPORTING_EMP_ID)
            .reportingId(UPDATED_REPORTING_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return reporting;
    }

    @BeforeEach
    public void initTest() {
        reporting = createEntity(em);
    }

    @Test
    @Transactional
    void createReporting() throws Exception {
        int databaseSizeBeforeCreate = reportingRepository.findAll().size();
        // Create the Reporting
        ReportingDTO reportingDTO = reportingMapper.toDto(reporting);
        restReportingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportingDTO)))
            .andExpect(status().isCreated());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeCreate + 1);
        Reporting testReporting = reportingList.get(reportingList.size() - 1);
        assertThat(testReporting.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testReporting.getReportingEmpId()).isEqualTo(DEFAULT_REPORTING_EMP_ID);
        assertThat(testReporting.getReportingId()).isEqualTo(DEFAULT_REPORTING_ID);
        assertThat(testReporting.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReporting.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testReporting.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testReporting.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createReportingWithExistingId() throws Exception {
        // Create the Reporting with an existing ID
        reporting.setId(1L);
        ReportingDTO reportingDTO = reportingMapper.toDto(reporting);

        int databaseSizeBeforeCreate = reportingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportings() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList
        restReportingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reporting.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportingEmpId").value(hasItem(DEFAULT_REPORTING_EMP_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportingId").value(hasItem(DEFAULT_REPORTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getReporting() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get the reporting
        restReportingMockMvc
            .perform(get(ENTITY_API_URL_ID, reporting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reporting.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.reportingEmpId").value(DEFAULT_REPORTING_EMP_ID.intValue()))
            .andExpect(jsonPath("$.reportingId").value(DEFAULT_REPORTING_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getReportingsByIdFiltering() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        Long id = reporting.getId();

        defaultReportingShouldBeFound("id.equals=" + id);
        defaultReportingShouldNotBeFound("id.notEquals=" + id);

        defaultReportingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportingShouldNotBeFound("id.greaterThan=" + id);

        defaultReportingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportingsByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultReportingShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the reportingList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultReportingShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultReportingShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the reportingList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultReportingShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where employeeId is not null
        defaultReportingShouldBeFound("employeeId.specified=true");

        // Get all the reportingList where employeeId is null
        defaultReportingShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingsByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultReportingShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the reportingList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultReportingShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultReportingShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the reportingList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultReportingShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultReportingShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the reportingList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultReportingShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultReportingShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the reportingList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultReportingShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingEmpIdIsEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingEmpId equals to DEFAULT_REPORTING_EMP_ID
        defaultReportingShouldBeFound("reportingEmpId.equals=" + DEFAULT_REPORTING_EMP_ID);

        // Get all the reportingList where reportingEmpId equals to UPDATED_REPORTING_EMP_ID
        defaultReportingShouldNotBeFound("reportingEmpId.equals=" + UPDATED_REPORTING_EMP_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingEmpIdIsInShouldWork() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingEmpId in DEFAULT_REPORTING_EMP_ID or UPDATED_REPORTING_EMP_ID
        defaultReportingShouldBeFound("reportingEmpId.in=" + DEFAULT_REPORTING_EMP_ID + "," + UPDATED_REPORTING_EMP_ID);

        // Get all the reportingList where reportingEmpId equals to UPDATED_REPORTING_EMP_ID
        defaultReportingShouldNotBeFound("reportingEmpId.in=" + UPDATED_REPORTING_EMP_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingEmpIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingEmpId is not null
        defaultReportingShouldBeFound("reportingEmpId.specified=true");

        // Get all the reportingList where reportingEmpId is null
        defaultReportingShouldNotBeFound("reportingEmpId.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingsByReportingEmpIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingEmpId is greater than or equal to DEFAULT_REPORTING_EMP_ID
        defaultReportingShouldBeFound("reportingEmpId.greaterThanOrEqual=" + DEFAULT_REPORTING_EMP_ID);

        // Get all the reportingList where reportingEmpId is greater than or equal to UPDATED_REPORTING_EMP_ID
        defaultReportingShouldNotBeFound("reportingEmpId.greaterThanOrEqual=" + UPDATED_REPORTING_EMP_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingEmpIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingEmpId is less than or equal to DEFAULT_REPORTING_EMP_ID
        defaultReportingShouldBeFound("reportingEmpId.lessThanOrEqual=" + DEFAULT_REPORTING_EMP_ID);

        // Get all the reportingList where reportingEmpId is less than or equal to SMALLER_REPORTING_EMP_ID
        defaultReportingShouldNotBeFound("reportingEmpId.lessThanOrEqual=" + SMALLER_REPORTING_EMP_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingEmpIdIsLessThanSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingEmpId is less than DEFAULT_REPORTING_EMP_ID
        defaultReportingShouldNotBeFound("reportingEmpId.lessThan=" + DEFAULT_REPORTING_EMP_ID);

        // Get all the reportingList where reportingEmpId is less than UPDATED_REPORTING_EMP_ID
        defaultReportingShouldBeFound("reportingEmpId.lessThan=" + UPDATED_REPORTING_EMP_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingEmpIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingEmpId is greater than DEFAULT_REPORTING_EMP_ID
        defaultReportingShouldNotBeFound("reportingEmpId.greaterThan=" + DEFAULT_REPORTING_EMP_ID);

        // Get all the reportingList where reportingEmpId is greater than SMALLER_REPORTING_EMP_ID
        defaultReportingShouldBeFound("reportingEmpId.greaterThan=" + SMALLER_REPORTING_EMP_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingId equals to DEFAULT_REPORTING_ID
        defaultReportingShouldBeFound("reportingId.equals=" + DEFAULT_REPORTING_ID);

        // Get all the reportingList where reportingId equals to UPDATED_REPORTING_ID
        defaultReportingShouldNotBeFound("reportingId.equals=" + UPDATED_REPORTING_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingIdIsInShouldWork() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingId in DEFAULT_REPORTING_ID or UPDATED_REPORTING_ID
        defaultReportingShouldBeFound("reportingId.in=" + DEFAULT_REPORTING_ID + "," + UPDATED_REPORTING_ID);

        // Get all the reportingList where reportingId equals to UPDATED_REPORTING_ID
        defaultReportingShouldNotBeFound("reportingId.in=" + UPDATED_REPORTING_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingId is not null
        defaultReportingShouldBeFound("reportingId.specified=true");

        // Get all the reportingList where reportingId is null
        defaultReportingShouldNotBeFound("reportingId.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingsByReportingIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingId is greater than or equal to DEFAULT_REPORTING_ID
        defaultReportingShouldBeFound("reportingId.greaterThanOrEqual=" + DEFAULT_REPORTING_ID);

        // Get all the reportingList where reportingId is greater than or equal to UPDATED_REPORTING_ID
        defaultReportingShouldNotBeFound("reportingId.greaterThanOrEqual=" + UPDATED_REPORTING_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingId is less than or equal to DEFAULT_REPORTING_ID
        defaultReportingShouldBeFound("reportingId.lessThanOrEqual=" + DEFAULT_REPORTING_ID);

        // Get all the reportingList where reportingId is less than or equal to SMALLER_REPORTING_ID
        defaultReportingShouldNotBeFound("reportingId.lessThanOrEqual=" + SMALLER_REPORTING_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingIdIsLessThanSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingId is less than DEFAULT_REPORTING_ID
        defaultReportingShouldNotBeFound("reportingId.lessThan=" + DEFAULT_REPORTING_ID);

        // Get all the reportingList where reportingId is less than UPDATED_REPORTING_ID
        defaultReportingShouldBeFound("reportingId.lessThan=" + UPDATED_REPORTING_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByReportingIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where reportingId is greater than DEFAULT_REPORTING_ID
        defaultReportingShouldNotBeFound("reportingId.greaterThan=" + DEFAULT_REPORTING_ID);

        // Get all the reportingList where reportingId is greater than SMALLER_REPORTING_ID
        defaultReportingShouldBeFound("reportingId.greaterThan=" + SMALLER_REPORTING_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where status equals to DEFAULT_STATUS
        defaultReportingShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the reportingList where status equals to UPDATED_STATUS
        defaultReportingShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReportingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultReportingShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the reportingList where status equals to UPDATED_STATUS
        defaultReportingShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReportingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where status is not null
        defaultReportingShouldBeFound("status.specified=true");

        // Get all the reportingList where status is null
        defaultReportingShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingsByStatusContainsSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where status contains DEFAULT_STATUS
        defaultReportingShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the reportingList where status contains UPDATED_STATUS
        defaultReportingShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReportingsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where status does not contain DEFAULT_STATUS
        defaultReportingShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the reportingList where status does not contain UPDATED_STATUS
        defaultReportingShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReportingsByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where companyId equals to DEFAULT_COMPANY_ID
        defaultReportingShouldBeFound("companyId.equals=" + DEFAULT_COMPANY_ID);

        // Get all the reportingList where companyId equals to UPDATED_COMPANY_ID
        defaultReportingShouldNotBeFound("companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where companyId in DEFAULT_COMPANY_ID or UPDATED_COMPANY_ID
        defaultReportingShouldBeFound("companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID);

        // Get all the reportingList where companyId equals to UPDATED_COMPANY_ID
        defaultReportingShouldNotBeFound("companyId.in=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where companyId is not null
        defaultReportingShouldBeFound("companyId.specified=true");

        // Get all the reportingList where companyId is null
        defaultReportingShouldNotBeFound("companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingsByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where companyId is greater than or equal to DEFAULT_COMPANY_ID
        defaultReportingShouldBeFound("companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the reportingList where companyId is greater than or equal to UPDATED_COMPANY_ID
        defaultReportingShouldNotBeFound("companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where companyId is less than or equal to DEFAULT_COMPANY_ID
        defaultReportingShouldBeFound("companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the reportingList where companyId is less than or equal to SMALLER_COMPANY_ID
        defaultReportingShouldNotBeFound("companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where companyId is less than DEFAULT_COMPANY_ID
        defaultReportingShouldNotBeFound("companyId.lessThan=" + DEFAULT_COMPANY_ID);

        // Get all the reportingList where companyId is less than UPDATED_COMPANY_ID
        defaultReportingShouldBeFound("companyId.lessThan=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where companyId is greater than DEFAULT_COMPANY_ID
        defaultReportingShouldNotBeFound("companyId.greaterThan=" + DEFAULT_COMPANY_ID);

        // Get all the reportingList where companyId is greater than SMALLER_COMPANY_ID
        defaultReportingShouldBeFound("companyId.greaterThan=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllReportingsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultReportingShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the reportingList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultReportingShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllReportingsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultReportingShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the reportingList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultReportingShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllReportingsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where lastModified is not null
        defaultReportingShouldBeFound("lastModified.specified=true");

        // Get all the reportingList where lastModified is null
        defaultReportingShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultReportingShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the reportingList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultReportingShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllReportingsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultReportingShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the reportingList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultReportingShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllReportingsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where lastModifiedBy is not null
        defaultReportingShouldBeFound("lastModifiedBy.specified=true");

        // Get all the reportingList where lastModifiedBy is null
        defaultReportingShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultReportingShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the reportingList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultReportingShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllReportingsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        // Get all the reportingList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultReportingShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the reportingList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultReportingShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportingShouldBeFound(String filter) throws Exception {
        restReportingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reporting.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportingEmpId").value(hasItem(DEFAULT_REPORTING_EMP_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportingId").value(hasItem(DEFAULT_REPORTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restReportingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportingShouldNotBeFound(String filter) throws Exception {
        restReportingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReporting() throws Exception {
        // Get the reporting
        restReportingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReporting() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();

        // Update the reporting
        Reporting updatedReporting = reportingRepository.findById(reporting.getId()).get();
        // Disconnect from session so that the updates on updatedReporting are not directly saved in db
        em.detach(updatedReporting);
        updatedReporting
            .employeeId(UPDATED_EMPLOYEE_ID)
            .reportingEmpId(UPDATED_REPORTING_EMP_ID)
            .reportingId(UPDATED_REPORTING_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        ReportingDTO reportingDTO = reportingMapper.toDto(updatedReporting);

        restReportingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
        Reporting testReporting = reportingList.get(reportingList.size() - 1);
        assertThat(testReporting.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testReporting.getReportingEmpId()).isEqualTo(UPDATED_REPORTING_EMP_ID);
        assertThat(testReporting.getReportingId()).isEqualTo(UPDATED_REPORTING_ID);
        assertThat(testReporting.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReporting.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testReporting.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testReporting.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingReporting() throws Exception {
        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();
        reporting.setId(count.incrementAndGet());

        // Create the Reporting
        ReportingDTO reportingDTO = reportingMapper.toDto(reporting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReporting() throws Exception {
        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();
        reporting.setId(count.incrementAndGet());

        // Create the Reporting
        ReportingDTO reportingDTO = reportingMapper.toDto(reporting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReporting() throws Exception {
        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();
        reporting.setId(count.incrementAndGet());

        // Create the Reporting
        ReportingDTO reportingDTO = reportingMapper.toDto(reporting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportingWithPatch() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();

        // Update the reporting using partial update
        Reporting partialUpdatedReporting = new Reporting();
        partialUpdatedReporting.setId(reporting.getId());

        partialUpdatedReporting
            .employeeId(UPDATED_EMPLOYEE_ID)
            .reportingEmpId(UPDATED_REPORTING_EMP_ID)
            .reportingId(UPDATED_REPORTING_ID)
            .companyId(UPDATED_COMPANY_ID);

        restReportingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReporting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReporting))
            )
            .andExpect(status().isOk());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
        Reporting testReporting = reportingList.get(reportingList.size() - 1);
        assertThat(testReporting.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testReporting.getReportingEmpId()).isEqualTo(UPDATED_REPORTING_EMP_ID);
        assertThat(testReporting.getReportingId()).isEqualTo(UPDATED_REPORTING_ID);
        assertThat(testReporting.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReporting.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testReporting.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testReporting.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateReportingWithPatch() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();

        // Update the reporting using partial update
        Reporting partialUpdatedReporting = new Reporting();
        partialUpdatedReporting.setId(reporting.getId());

        partialUpdatedReporting
            .employeeId(UPDATED_EMPLOYEE_ID)
            .reportingEmpId(UPDATED_REPORTING_EMP_ID)
            .reportingId(UPDATED_REPORTING_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restReportingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReporting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReporting))
            )
            .andExpect(status().isOk());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
        Reporting testReporting = reportingList.get(reportingList.size() - 1);
        assertThat(testReporting.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testReporting.getReportingEmpId()).isEqualTo(UPDATED_REPORTING_EMP_ID);
        assertThat(testReporting.getReportingId()).isEqualTo(UPDATED_REPORTING_ID);
        assertThat(testReporting.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReporting.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testReporting.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testReporting.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingReporting() throws Exception {
        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();
        reporting.setId(count.incrementAndGet());

        // Create the Reporting
        ReportingDTO reportingDTO = reportingMapper.toDto(reporting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReporting() throws Exception {
        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();
        reporting.setId(count.incrementAndGet());

        // Create the Reporting
        ReportingDTO reportingDTO = reportingMapper.toDto(reporting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReporting() throws Exception {
        int databaseSizeBeforeUpdate = reportingRepository.findAll().size();
        reporting.setId(count.incrementAndGet());

        // Create the Reporting
        ReportingDTO reportingDTO = reportingMapper.toDto(reporting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reportingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reporting in the database
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReporting() throws Exception {
        // Initialize the database
        reportingRepository.saveAndFlush(reporting);

        int databaseSizeBeforeDelete = reportingRepository.findAll().size();

        // Delete the reporting
        restReportingMockMvc
            .perform(delete(ENTITY_API_URL_ID, reporting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reporting> reportingList = reportingRepository.findAll();
        assertThat(reportingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
