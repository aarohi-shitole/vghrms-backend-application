package com.vhrms.service;

import com.vhrms.domain.*; // for static metamodels
import com.vhrms.domain.FamilyInfo;
import com.vhrms.repository.FamilyInfoRepository;
import com.vhrms.service.criteria.FamilyInfoCriteria;
import com.vhrms.service.dto.FamilyInfoDTO;
import com.vhrms.service.mapper.FamilyInfoMapper;
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
 * Service for executing complex queries for {@link FamilyInfo} entities in the database.
 * The main input is a {@link FamilyInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FamilyInfoDTO} or a {@link Page} of {@link FamilyInfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FamilyInfoQueryService extends QueryService<FamilyInfo> {

    private final Logger log = LoggerFactory.getLogger(FamilyInfoQueryService.class);

    private final FamilyInfoRepository familyInfoRepository;

    private final FamilyInfoMapper familyInfoMapper;

    public FamilyInfoQueryService(FamilyInfoRepository familyInfoRepository, FamilyInfoMapper familyInfoMapper) {
        this.familyInfoRepository = familyInfoRepository;
        this.familyInfoMapper = familyInfoMapper;
    }

    /**
     * Return a {@link List} of {@link FamilyInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FamilyInfoDTO> findByCriteria(FamilyInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FamilyInfo> specification = createSpecification(criteria);
        return familyInfoMapper.toDto(familyInfoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FamilyInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilyInfoDTO> findByCriteria(FamilyInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FamilyInfo> specification = createSpecification(criteria);
        return familyInfoRepository.findAll(specification, page).map(familyInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FamilyInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FamilyInfo> specification = createSpecification(criteria);
        return familyInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link FamilyInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FamilyInfo> createSpecification(FamilyInfoCriteria criteria) {
        Specification<FamilyInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FamilyInfo_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FamilyInfo_.name));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), FamilyInfo_.dateOfBirth));
            }
            if (criteria.getRelation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelation(), FamilyInfo_.relation));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAddressId(), FamilyInfo_.addressId));
            }
            if (criteria.getIsEmployed() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEmployed(), FamilyInfo_.isEmployed));
            }
            if (criteria.getEmployedAt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmployedAt(), FamilyInfo_.employedAt));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), FamilyInfo_.employeeId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), FamilyInfo_.status));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompanyId(), FamilyInfo_.companyId));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), FamilyInfo_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), FamilyInfo_.lastModifiedBy));
            }
        }
        return specification;
    }
}
