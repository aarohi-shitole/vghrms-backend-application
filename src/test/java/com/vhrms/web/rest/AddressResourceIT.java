package com.vhrms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vhrms.IntegrationTest;
import com.vhrms.domain.Address;
import com.vhrms.repository.AddressRepository;
import com.vhrms.service.criteria.AddressCriteria;
import com.vhrms.service.dto.AddressDTO;
import com.vhrms.service.mapper.AddressMapper;
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
 * Integration tests for the {@link AddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_ADD = false;
    private static final Boolean UPDATED_DEFAULT_ADD = true;

    private static final String DEFAULT_LAND_MARK = "AAAAAAAAAA";
    private static final String UPDATED_LAND_MARK = "BBBBBBBBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final String DEFAULT_REF_TABLE = "AAAAAAAAAA";
    private static final String UPDATED_REF_TABLE = "BBBBBBBBBB";

    private static final Long DEFAULT_REF_TABLE_ID = 1L;
    private static final Long UPDATED_REF_TABLE_ID = 2L;
    private static final Long SMALLER_REF_TABLE_ID = 1L - 1L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;
    private static final Long SMALLER_COMPANY_ID = 1L - 1L;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private Address address;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .type(DEFAULT_TYPE)
            .line1(DEFAULT_LINE_1)
            .line2(DEFAULT_LINE_2)
            .country(DEFAULT_COUNTRY)
            .state(DEFAULT_STATE)
            .city(DEFAULT_CITY)
            .pincode(DEFAULT_PINCODE)
            .defaultAdd(DEFAULT_DEFAULT_ADD)
            .landMark(DEFAULT_LAND_MARK)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .refTable(DEFAULT_REF_TABLE)
            .refTableId(DEFAULT_REF_TABLE_ID)
            .status(DEFAULT_STATUS)
            .companyId(DEFAULT_COMPANY_ID)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return address;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createUpdatedEntity(EntityManager em) {
        Address address = new Address()
            .type(UPDATED_TYPE)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .city(UPDATED_CITY)
            .pincode(UPDATED_PINCODE)
            .defaultAdd(UPDATED_DEFAULT_ADD)
            .landMark(UPDATED_LAND_MARK)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .refTable(UPDATED_REF_TABLE)
            .refTableId(UPDATED_REF_TABLE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return address;
    }

    @BeforeEach
    public void initTest() {
        address = createEntity(em);
    }

    @Test
    @Transactional
    void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();
        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAddress.getLine1()).isEqualTo(DEFAULT_LINE_1);
        assertThat(testAddress.getLine2()).isEqualTo(DEFAULT_LINE_2);
        assertThat(testAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testAddress.getDefaultAdd()).isEqualTo(DEFAULT_DEFAULT_ADD);
        assertThat(testAddress.getLandMark()).isEqualTo(DEFAULT_LAND_MARK);
        assertThat(testAddress.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testAddress.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testAddress.getRefTable()).isEqualTo(DEFAULT_REF_TABLE);
        assertThat(testAddress.getRefTableId()).isEqualTo(DEFAULT_REF_TABLE_ID);
        assertThat(testAddress.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAddress.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testAddress.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testAddress.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createAddressWithExistingId() throws Exception {
        // Create the Address with an existing ID
        address.setId(1L);
        AddressDTO addressDTO = addressMapper.toDto(address);

        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].line1").value(hasItem(DEFAULT_LINE_1)))
            .andExpect(jsonPath("$.[*].line2").value(hasItem(DEFAULT_LINE_2)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].defaultAdd").value(hasItem(DEFAULT_DEFAULT_ADD.booleanValue())))
            .andExpect(jsonPath("$.[*].landMark").value(hasItem(DEFAULT_LAND_MARK)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].refTable").value(hasItem(DEFAULT_REF_TABLE)))
            .andExpect(jsonPath("$.[*].refTableId").value(hasItem(DEFAULT_REF_TABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.line1").value(DEFAULT_LINE_1))
            .andExpect(jsonPath("$.line2").value(DEFAULT_LINE_2))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.defaultAdd").value(DEFAULT_DEFAULT_ADD.booleanValue()))
            .andExpect(jsonPath("$.landMark").value(DEFAULT_LAND_MARK))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.refTable").value(DEFAULT_REF_TABLE))
            .andExpect(jsonPath("$.refTableId").value(DEFAULT_REF_TABLE_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getAddressesByIdFiltering() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        Long id = address.getId();

        defaultAddressShouldBeFound("id.equals=" + id);
        defaultAddressShouldNotBeFound("id.notEquals=" + id);

        defaultAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAddressesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where type equals to DEFAULT_TYPE
        defaultAddressShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the addressList where type equals to UPDATED_TYPE
        defaultAddressShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAddressesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAddressShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the addressList where type equals to UPDATED_TYPE
        defaultAddressShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAddressesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where type is not null
        defaultAddressShouldBeFound("type.specified=true");

        // Get all the addressList where type is null
        defaultAddressShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByTypeContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where type contains DEFAULT_TYPE
        defaultAddressShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the addressList where type contains UPDATED_TYPE
        defaultAddressShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAddressesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where type does not contain DEFAULT_TYPE
        defaultAddressShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the addressList where type does not contain UPDATED_TYPE
        defaultAddressShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAddressesByLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line1 equals to DEFAULT_LINE_1
        defaultAddressShouldBeFound("line1.equals=" + DEFAULT_LINE_1);

        // Get all the addressList where line1 equals to UPDATED_LINE_1
        defaultAddressShouldNotBeFound("line1.equals=" + UPDATED_LINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByLine1IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line1 in DEFAULT_LINE_1 or UPDATED_LINE_1
        defaultAddressShouldBeFound("line1.in=" + DEFAULT_LINE_1 + "," + UPDATED_LINE_1);

        // Get all the addressList where line1 equals to UPDATED_LINE_1
        defaultAddressShouldNotBeFound("line1.in=" + UPDATED_LINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line1 is not null
        defaultAddressShouldBeFound("line1.specified=true");

        // Get all the addressList where line1 is null
        defaultAddressShouldNotBeFound("line1.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLine1ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line1 contains DEFAULT_LINE_1
        defaultAddressShouldBeFound("line1.contains=" + DEFAULT_LINE_1);

        // Get all the addressList where line1 contains UPDATED_LINE_1
        defaultAddressShouldNotBeFound("line1.contains=" + UPDATED_LINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByLine1NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line1 does not contain DEFAULT_LINE_1
        defaultAddressShouldNotBeFound("line1.doesNotContain=" + DEFAULT_LINE_1);

        // Get all the addressList where line1 does not contain UPDATED_LINE_1
        defaultAddressShouldBeFound("line1.doesNotContain=" + UPDATED_LINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line2 equals to DEFAULT_LINE_2
        defaultAddressShouldBeFound("line2.equals=" + DEFAULT_LINE_2);

        // Get all the addressList where line2 equals to UPDATED_LINE_2
        defaultAddressShouldNotBeFound("line2.equals=" + UPDATED_LINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByLine2IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line2 in DEFAULT_LINE_2 or UPDATED_LINE_2
        defaultAddressShouldBeFound("line2.in=" + DEFAULT_LINE_2 + "," + UPDATED_LINE_2);

        // Get all the addressList where line2 equals to UPDATED_LINE_2
        defaultAddressShouldNotBeFound("line2.in=" + UPDATED_LINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line2 is not null
        defaultAddressShouldBeFound("line2.specified=true");

        // Get all the addressList where line2 is null
        defaultAddressShouldNotBeFound("line2.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLine2ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line2 contains DEFAULT_LINE_2
        defaultAddressShouldBeFound("line2.contains=" + DEFAULT_LINE_2);

        // Get all the addressList where line2 contains UPDATED_LINE_2
        defaultAddressShouldNotBeFound("line2.contains=" + UPDATED_LINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByLine2NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line2 does not contain DEFAULT_LINE_2
        defaultAddressShouldNotBeFound("line2.doesNotContain=" + DEFAULT_LINE_2);

        // Get all the addressList where line2 does not contain UPDATED_LINE_2
        defaultAddressShouldBeFound("line2.doesNotContain=" + UPDATED_LINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where country equals to DEFAULT_COUNTRY
        defaultAddressShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the addressList where country equals to UPDATED_COUNTRY
        defaultAddressShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllAddressesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultAddressShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the addressList where country equals to UPDATED_COUNTRY
        defaultAddressShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllAddressesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where country is not null
        defaultAddressShouldBeFound("country.specified=true");

        // Get all the addressList where country is null
        defaultAddressShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByCountryContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where country contains DEFAULT_COUNTRY
        defaultAddressShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the addressList where country contains UPDATED_COUNTRY
        defaultAddressShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllAddressesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where country does not contain DEFAULT_COUNTRY
        defaultAddressShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the addressList where country does not contain UPDATED_COUNTRY
        defaultAddressShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllAddressesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where state equals to DEFAULT_STATE
        defaultAddressShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the addressList where state equals to UPDATED_STATE
        defaultAddressShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllAddressesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where state in DEFAULT_STATE or UPDATED_STATE
        defaultAddressShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the addressList where state equals to UPDATED_STATE
        defaultAddressShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllAddressesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where state is not null
        defaultAddressShouldBeFound("state.specified=true");

        // Get all the addressList where state is null
        defaultAddressShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByStateContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where state contains DEFAULT_STATE
        defaultAddressShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the addressList where state contains UPDATED_STATE
        defaultAddressShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllAddressesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where state does not contain DEFAULT_STATE
        defaultAddressShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the addressList where state does not contain UPDATED_STATE
        defaultAddressShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city equals to DEFAULT_CITY
        defaultAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the addressList where city equals to UPDATED_CITY
        defaultAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the addressList where city equals to UPDATED_CITY
        defaultAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city is not null
        defaultAddressShouldBeFound("city.specified=true");

        // Get all the addressList where city is null
        defaultAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city contains DEFAULT_CITY
        defaultAddressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the addressList where city contains UPDATED_CITY
        defaultAddressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city does not contain DEFAULT_CITY
        defaultAddressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the addressList where city does not contain UPDATED_CITY
        defaultAddressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByPincodeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where pincode equals to DEFAULT_PINCODE
        defaultAddressShouldBeFound("pincode.equals=" + DEFAULT_PINCODE);

        // Get all the addressList where pincode equals to UPDATED_PINCODE
        defaultAddressShouldNotBeFound("pincode.equals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPincodeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where pincode in DEFAULT_PINCODE or UPDATED_PINCODE
        defaultAddressShouldBeFound("pincode.in=" + DEFAULT_PINCODE + "," + UPDATED_PINCODE);

        // Get all the addressList where pincode equals to UPDATED_PINCODE
        defaultAddressShouldNotBeFound("pincode.in=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPincodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where pincode is not null
        defaultAddressShouldBeFound("pincode.specified=true");

        // Get all the addressList where pincode is null
        defaultAddressShouldNotBeFound("pincode.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByPincodeContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where pincode contains DEFAULT_PINCODE
        defaultAddressShouldBeFound("pincode.contains=" + DEFAULT_PINCODE);

        // Get all the addressList where pincode contains UPDATED_PINCODE
        defaultAddressShouldNotBeFound("pincode.contains=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPincodeNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where pincode does not contain DEFAULT_PINCODE
        defaultAddressShouldNotBeFound("pincode.doesNotContain=" + DEFAULT_PINCODE);

        // Get all the addressList where pincode does not contain UPDATED_PINCODE
        defaultAddressShouldBeFound("pincode.doesNotContain=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultAddIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultAdd equals to DEFAULT_DEFAULT_ADD
        defaultAddressShouldBeFound("defaultAdd.equals=" + DEFAULT_DEFAULT_ADD);

        // Get all the addressList where defaultAdd equals to UPDATED_DEFAULT_ADD
        defaultAddressShouldNotBeFound("defaultAdd.equals=" + UPDATED_DEFAULT_ADD);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultAddIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultAdd in DEFAULT_DEFAULT_ADD or UPDATED_DEFAULT_ADD
        defaultAddressShouldBeFound("defaultAdd.in=" + DEFAULT_DEFAULT_ADD + "," + UPDATED_DEFAULT_ADD);

        // Get all the addressList where defaultAdd equals to UPDATED_DEFAULT_ADD
        defaultAddressShouldNotBeFound("defaultAdd.in=" + UPDATED_DEFAULT_ADD);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultAddIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultAdd is not null
        defaultAddressShouldBeFound("defaultAdd.specified=true");

        // Get all the addressList where defaultAdd is null
        defaultAddressShouldNotBeFound("defaultAdd.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLandMarkIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where landMark equals to DEFAULT_LAND_MARK
        defaultAddressShouldBeFound("landMark.equals=" + DEFAULT_LAND_MARK);

        // Get all the addressList where landMark equals to UPDATED_LAND_MARK
        defaultAddressShouldNotBeFound("landMark.equals=" + UPDATED_LAND_MARK);
    }

    @Test
    @Transactional
    void getAllAddressesByLandMarkIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where landMark in DEFAULT_LAND_MARK or UPDATED_LAND_MARK
        defaultAddressShouldBeFound("landMark.in=" + DEFAULT_LAND_MARK + "," + UPDATED_LAND_MARK);

        // Get all the addressList where landMark equals to UPDATED_LAND_MARK
        defaultAddressShouldNotBeFound("landMark.in=" + UPDATED_LAND_MARK);
    }

    @Test
    @Transactional
    void getAllAddressesByLandMarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where landMark is not null
        defaultAddressShouldBeFound("landMark.specified=true");

        // Get all the addressList where landMark is null
        defaultAddressShouldNotBeFound("landMark.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLandMarkContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where landMark contains DEFAULT_LAND_MARK
        defaultAddressShouldBeFound("landMark.contains=" + DEFAULT_LAND_MARK);

        // Get all the addressList where landMark contains UPDATED_LAND_MARK
        defaultAddressShouldNotBeFound("landMark.contains=" + UPDATED_LAND_MARK);
    }

    @Test
    @Transactional
    void getAllAddressesByLandMarkNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where landMark does not contain DEFAULT_LAND_MARK
        defaultAddressShouldNotBeFound("landMark.doesNotContain=" + DEFAULT_LAND_MARK);

        // Get all the addressList where landMark does not contain UPDATED_LAND_MARK
        defaultAddressShouldBeFound("landMark.doesNotContain=" + UPDATED_LAND_MARK);
    }

    @Test
    @Transactional
    void getAllAddressesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where longitude equals to DEFAULT_LONGITUDE
        defaultAddressShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the addressList where longitude equals to UPDATED_LONGITUDE
        defaultAddressShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultAddressShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the addressList where longitude equals to UPDATED_LONGITUDE
        defaultAddressShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where longitude is not null
        defaultAddressShouldBeFound("longitude.specified=true");

        // Get all the addressList where longitude is null
        defaultAddressShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultAddressShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the addressList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultAddressShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultAddressShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the addressList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultAddressShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where longitude is less than DEFAULT_LONGITUDE
        defaultAddressShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the addressList where longitude is less than UPDATED_LONGITUDE
        defaultAddressShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where longitude is greater than DEFAULT_LONGITUDE
        defaultAddressShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the addressList where longitude is greater than SMALLER_LONGITUDE
        defaultAddressShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where latitude equals to DEFAULT_LATITUDE
        defaultAddressShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the addressList where latitude equals to UPDATED_LATITUDE
        defaultAddressShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultAddressShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the addressList where latitude equals to UPDATED_LATITUDE
        defaultAddressShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where latitude is not null
        defaultAddressShouldBeFound("latitude.specified=true");

        // Get all the addressList where latitude is null
        defaultAddressShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultAddressShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the addressList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultAddressShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultAddressShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the addressList where latitude is less than or equal to SMALLER_LATITUDE
        defaultAddressShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where latitude is less than DEFAULT_LATITUDE
        defaultAddressShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the addressList where latitude is less than UPDATED_LATITUDE
        defaultAddressShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where latitude is greater than DEFAULT_LATITUDE
        defaultAddressShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the addressList where latitude is greater than SMALLER_LATITUDE
        defaultAddressShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTable equals to DEFAULT_REF_TABLE
        defaultAddressShouldBeFound("refTable.equals=" + DEFAULT_REF_TABLE);

        // Get all the addressList where refTable equals to UPDATED_REF_TABLE
        defaultAddressShouldNotBeFound("refTable.equals=" + UPDATED_REF_TABLE);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTable in DEFAULT_REF_TABLE or UPDATED_REF_TABLE
        defaultAddressShouldBeFound("refTable.in=" + DEFAULT_REF_TABLE + "," + UPDATED_REF_TABLE);

        // Get all the addressList where refTable equals to UPDATED_REF_TABLE
        defaultAddressShouldNotBeFound("refTable.in=" + UPDATED_REF_TABLE);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTable is not null
        defaultAddressShouldBeFound("refTable.specified=true");

        // Get all the addressList where refTable is null
        defaultAddressShouldNotBeFound("refTable.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTable contains DEFAULT_REF_TABLE
        defaultAddressShouldBeFound("refTable.contains=" + DEFAULT_REF_TABLE);

        // Get all the addressList where refTable contains UPDATED_REF_TABLE
        defaultAddressShouldNotBeFound("refTable.contains=" + UPDATED_REF_TABLE);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTable does not contain DEFAULT_REF_TABLE
        defaultAddressShouldNotBeFound("refTable.doesNotContain=" + DEFAULT_REF_TABLE);

        // Get all the addressList where refTable does not contain UPDATED_REF_TABLE
        defaultAddressShouldBeFound("refTable.doesNotContain=" + UPDATED_REF_TABLE);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIdIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTableId equals to DEFAULT_REF_TABLE_ID
        defaultAddressShouldBeFound("refTableId.equals=" + DEFAULT_REF_TABLE_ID);

        // Get all the addressList where refTableId equals to UPDATED_REF_TABLE_ID
        defaultAddressShouldNotBeFound("refTableId.equals=" + UPDATED_REF_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIdIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTableId in DEFAULT_REF_TABLE_ID or UPDATED_REF_TABLE_ID
        defaultAddressShouldBeFound("refTableId.in=" + DEFAULT_REF_TABLE_ID + "," + UPDATED_REF_TABLE_ID);

        // Get all the addressList where refTableId equals to UPDATED_REF_TABLE_ID
        defaultAddressShouldNotBeFound("refTableId.in=" + UPDATED_REF_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTableId is not null
        defaultAddressShouldBeFound("refTableId.specified=true");

        // Get all the addressList where refTableId is null
        defaultAddressShouldNotBeFound("refTableId.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTableId is greater than or equal to DEFAULT_REF_TABLE_ID
        defaultAddressShouldBeFound("refTableId.greaterThanOrEqual=" + DEFAULT_REF_TABLE_ID);

        // Get all the addressList where refTableId is greater than or equal to UPDATED_REF_TABLE_ID
        defaultAddressShouldNotBeFound("refTableId.greaterThanOrEqual=" + UPDATED_REF_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTableId is less than or equal to DEFAULT_REF_TABLE_ID
        defaultAddressShouldBeFound("refTableId.lessThanOrEqual=" + DEFAULT_REF_TABLE_ID);

        // Get all the addressList where refTableId is less than or equal to SMALLER_REF_TABLE_ID
        defaultAddressShouldNotBeFound("refTableId.lessThanOrEqual=" + SMALLER_REF_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIdIsLessThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTableId is less than DEFAULT_REF_TABLE_ID
        defaultAddressShouldNotBeFound("refTableId.lessThan=" + DEFAULT_REF_TABLE_ID);

        // Get all the addressList where refTableId is less than UPDATED_REF_TABLE_ID
        defaultAddressShouldBeFound("refTableId.lessThan=" + UPDATED_REF_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByRefTableIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where refTableId is greater than DEFAULT_REF_TABLE_ID
        defaultAddressShouldNotBeFound("refTableId.greaterThan=" + DEFAULT_REF_TABLE_ID);

        // Get all the addressList where refTableId is greater than SMALLER_REF_TABLE_ID
        defaultAddressShouldBeFound("refTableId.greaterThan=" + SMALLER_REF_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where status equals to DEFAULT_STATUS
        defaultAddressShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the addressList where status equals to UPDATED_STATUS
        defaultAddressShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAddressesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAddressShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the addressList where status equals to UPDATED_STATUS
        defaultAddressShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAddressesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where status is not null
        defaultAddressShouldBeFound("status.specified=true");

        // Get all the addressList where status is null
        defaultAddressShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByStatusContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where status contains DEFAULT_STATUS
        defaultAddressShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the addressList where status contains UPDATED_STATUS
        defaultAddressShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAddressesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where status does not contain DEFAULT_STATUS
        defaultAddressShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the addressList where status does not contain UPDATED_STATUS
        defaultAddressShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where companyId equals to DEFAULT_COMPANY_ID
        defaultAddressShouldBeFound("companyId.equals=" + DEFAULT_COMPANY_ID);

        // Get all the addressList where companyId equals to UPDATED_COMPANY_ID
        defaultAddressShouldNotBeFound("companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where companyId in DEFAULT_COMPANY_ID or UPDATED_COMPANY_ID
        defaultAddressShouldBeFound("companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID);

        // Get all the addressList where companyId equals to UPDATED_COMPANY_ID
        defaultAddressShouldNotBeFound("companyId.in=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where companyId is not null
        defaultAddressShouldBeFound("companyId.specified=true");

        // Get all the addressList where companyId is null
        defaultAddressShouldNotBeFound("companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where companyId is greater than or equal to DEFAULT_COMPANY_ID
        defaultAddressShouldBeFound("companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the addressList where companyId is greater than or equal to UPDATED_COMPANY_ID
        defaultAddressShouldNotBeFound("companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where companyId is less than or equal to DEFAULT_COMPANY_ID
        defaultAddressShouldBeFound("companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the addressList where companyId is less than or equal to SMALLER_COMPANY_ID
        defaultAddressShouldNotBeFound("companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where companyId is less than DEFAULT_COMPANY_ID
        defaultAddressShouldNotBeFound("companyId.lessThan=" + DEFAULT_COMPANY_ID);

        // Get all the addressList where companyId is less than UPDATED_COMPANY_ID
        defaultAddressShouldBeFound("companyId.lessThan=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where companyId is greater than DEFAULT_COMPANY_ID
        defaultAddressShouldNotBeFound("companyId.greaterThan=" + DEFAULT_COMPANY_ID);

        // Get all the addressList where companyId is greater than SMALLER_COMPANY_ID
        defaultAddressShouldBeFound("companyId.greaterThan=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultAddressShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the addressList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultAddressShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultAddressShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the addressList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultAddressShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModified is not null
        defaultAddressShouldBeFound("lastModified.specified=true");

        // Get all the addressList where lastModified is null
        defaultAddressShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultAddressShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the addressList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy is not null
        defaultAddressShouldBeFound("lastModifiedBy.specified=true");

        // Get all the addressList where lastModifiedBy is null
        defaultAddressShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultAddressShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultAddressShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].line1").value(hasItem(DEFAULT_LINE_1)))
            .andExpect(jsonPath("$.[*].line2").value(hasItem(DEFAULT_LINE_2)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].defaultAdd").value(hasItem(DEFAULT_DEFAULT_ADD.booleanValue())))
            .andExpect(jsonPath("$.[*].landMark").value(hasItem(DEFAULT_LAND_MARK)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].refTable").value(hasItem(DEFAULT_REF_TABLE)))
            .andExpect(jsonPath("$.[*].refTableId").value(hasItem(DEFAULT_REF_TABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findById(address.getId()).get();
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .type(UPDATED_TYPE)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .city(UPDATED_CITY)
            .pincode(UPDATED_PINCODE)
            .defaultAdd(UPDATED_DEFAULT_ADD)
            .landMark(UPDATED_LAND_MARK)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .refTable(UPDATED_REF_TABLE)
            .refTableId(UPDATED_REF_TABLE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        AddressDTO addressDTO = addressMapper.toDto(updatedAddress);

        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAddress.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testAddress.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddress.getDefaultAdd()).isEqualTo(UPDATED_DEFAULT_ADD);
        assertThat(testAddress.getLandMark()).isEqualTo(UPDATED_LAND_MARK);
        assertThat(testAddress.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testAddress.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAddress.getRefTable()).isEqualTo(UPDATED_REF_TABLE);
        assertThat(testAddress.getRefTableId()).isEqualTo(UPDATED_REF_TABLE_ID);
        assertThat(testAddress.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAddress.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testAddress.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAddress.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .defaultAdd(UPDATED_DEFAULT_ADD)
            .latitude(UPDATED_LATITUDE);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAddress.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testAddress.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddress.getDefaultAdd()).isEqualTo(UPDATED_DEFAULT_ADD);
        assertThat(testAddress.getLandMark()).isEqualTo(DEFAULT_LAND_MARK);
        assertThat(testAddress.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testAddress.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAddress.getRefTable()).isEqualTo(DEFAULT_REF_TABLE);
        assertThat(testAddress.getRefTableId()).isEqualTo(DEFAULT_REF_TABLE_ID);
        assertThat(testAddress.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAddress.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testAddress.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testAddress.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .type(UPDATED_TYPE)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .city(UPDATED_CITY)
            .pincode(UPDATED_PINCODE)
            .defaultAdd(UPDATED_DEFAULT_ADD)
            .landMark(UPDATED_LAND_MARK)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .refTable(UPDATED_REF_TABLE)
            .refTableId(UPDATED_REF_TABLE_ID)
            .status(UPDATED_STATUS)
            .companyId(UPDATED_COMPANY_ID)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAddress.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testAddress.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddress.getDefaultAdd()).isEqualTo(UPDATED_DEFAULT_ADD);
        assertThat(testAddress.getLandMark()).isEqualTo(UPDATED_LAND_MARK);
        assertThat(testAddress.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testAddress.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAddress.getRefTable()).isEqualTo(UPDATED_REF_TABLE);
        assertThat(testAddress.getRefTableId()).isEqualTo(UPDATED_REF_TABLE_ID);
        assertThat(testAddress.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAddress.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testAddress.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAddress.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, address.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
