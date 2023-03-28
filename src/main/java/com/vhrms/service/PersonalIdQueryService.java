package com.vhrms.service;

import com.vhrms.domain.*; // for static metamodels
import com.vhrms.domain.PersonalId;
import com.vhrms.repository.PersonalIdRepository;
import com.vhrms.service.criteria.PersonalIdCriteria;
import com.vhrms.service.dto.PersonalIdDTO;
import com.vhrms.service.mapper.PersonalIdMapper;
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
 * Service for executing complex queries for {@link PersonalId} entities in the database.
 * The main input is a {@link PersonalIdCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonalIdDTO} or a {@link Page} of {@link PersonalIdDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonalIdQueryService extends QueryService<PersonalId> {

    private final Logger log = LoggerFactory.getLogger(PersonalIdQueryService.class);

    private final PersonalIdRepository personalIdRepository;

    private final PersonalIdMapper personalIdMapper;

    public PersonalIdQueryService(PersonalIdRepository personalIdRepository, PersonalIdMapper personalIdMapper) {
        this.personalIdRepository = personalIdRepository;
        this.personalIdMapper = personalIdMapper;
    }

    /**
     * Return a {@link List} of {@link PersonalIdDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonalIdDTO> findByCriteria(PersonalIdCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PersonalId> specification = createSpecification(criteria);
        return personalIdMapper.toDto(personalIdRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonalIdDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonalIdDTO> findByCriteria(PersonalIdCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonalId> specification = createSpecification(criteria);
        return personalIdRepository.findAll(specification, page).map(personalIdMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonalIdCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PersonalId> specification = createSpecification(criteria);
        return personalIdRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonalIdCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PersonalId> createSpecification(PersonalIdCriteria criteria) {
        Specification<PersonalId> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PersonalId_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), PersonalId_.type));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), PersonalId_.number));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), PersonalId_.issueDate));
            }
            if (criteria.getExpDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpDate(), PersonalId_.expDate));
            }
            if (criteria.getIssuingAuthority() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssuingAuthority(), PersonalId_.issuingAuthority));
            }
            if (criteria.getDocUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocUrl(), PersonalId_.docUrl));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), PersonalId_.employeeId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), PersonalId_.status));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompanyId(), PersonalId_.companyId));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), PersonalId_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), PersonalId_.lastModifiedBy));
            }
        }
        return specification;
    }
}
