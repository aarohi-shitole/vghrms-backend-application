package com.vhrms.service;

import com.vhrms.domain.*; // for static metamodels
import com.vhrms.domain.PersonalDetails;
import com.vhrms.repository.PersonalDetailsRepository;
import com.vhrms.service.criteria.PersonalDetailsCriteria;
import com.vhrms.service.dto.PersonalDetailsDTO;
import com.vhrms.service.mapper.PersonalDetailsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PersonalDetails} entities in the database.
 * The main input is a {@link PersonalDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonalDetailsDTO} or a {@link Page} of {@link PersonalDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonalDetailsQueryService extends QueryService<PersonalDetails> {

    private final Logger log = LoggerFactory.getLogger(PersonalDetailsQueryService.class);

    private final PersonalDetailsRepository personalDetailsRepository;

    private final PersonalDetailsMapper personalDetailsMapper;

    public PersonalDetailsQueryService(PersonalDetailsRepository personalDetailsRepository, PersonalDetailsMapper personalDetailsMapper) {
        this.personalDetailsRepository = personalDetailsRepository;
        this.personalDetailsMapper = personalDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link PersonalDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonalDetailsDTO> findByCriteria(PersonalDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PersonalDetails> specification = createSpecification(criteria);
        return personalDetailsMapper.toDto(personalDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonalDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonalDetailsDTO> findByCriteria(PersonalDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonalDetails> specification = createSpecification(criteria);
        return personalDetailsRepository.findAll(specification, page).map(personalDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonalDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PersonalDetails> specification = createSpecification(criteria);
        return personalDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonalDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PersonalDetails> createSpecification(PersonalDetailsCriteria criteria) {
        Specification<PersonalDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PersonalDetails_.id));
            }
            if (criteria.getTelephoneNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephoneNo(), PersonalDetails_.telephoneNo));
            }
            if (criteria.getNationality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationality(), PersonalDetails_.nationality));
            }
            if (criteria.getMaritalStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMaritalStatus(), PersonalDetails_.maritalStatus));
            }
            if (criteria.getReligion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReligion(), PersonalDetails_.religion));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), PersonalDetails_.employeeId));
            }
            if (criteria.getBloodGroup() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBloodGroup(), PersonalDetails_.bloodGroup));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), PersonalDetails_.dateOfBirth));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), PersonalDetails_.status));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompanyId(), PersonalDetails_.companyId));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), PersonalDetails_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), PersonalDetails_.lastModifiedBy));
            }
        }
        return specification;
    }
}
