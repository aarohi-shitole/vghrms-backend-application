package com.vhrms.service;

import com.vhrms.domain.*; // for static metamodels
import com.vhrms.domain.Reporting;
import com.vhrms.repository.ReportingRepository;
import com.vhrms.service.criteria.ReportingCriteria;
import com.vhrms.service.dto.ReportingDTO;
import com.vhrms.service.mapper.ReportingMapper;
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
 * Service for executing complex queries for {@link Reporting} entities in the database.
 * The main input is a {@link ReportingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportingDTO} or a {@link Page} of {@link ReportingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportingQueryService extends QueryService<Reporting> {

    private final Logger log = LoggerFactory.getLogger(ReportingQueryService.class);

    private final ReportingRepository reportingRepository;

    private final ReportingMapper reportingMapper;

    public ReportingQueryService(ReportingRepository reportingRepository, ReportingMapper reportingMapper) {
        this.reportingRepository = reportingRepository;
        this.reportingMapper = reportingMapper;
    }

    /**
     * Return a {@link List} of {@link ReportingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportingDTO> findByCriteria(ReportingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reporting> specification = createSpecification(criteria);
        return reportingMapper.toDto(reportingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportingDTO> findByCriteria(ReportingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reporting> specification = createSpecification(criteria);
        return reportingRepository.findAll(specification, page).map(reportingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reporting> specification = createSpecification(criteria);
        return reportingRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reporting> createSpecification(ReportingCriteria criteria) {
        Specification<Reporting> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reporting_.id));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmployeeId(), Reporting_.employeeId));
            }
            if (criteria.getReportingEmpId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportingEmpId(), Reporting_.reportingEmpId));
            }
            if (criteria.getReportingId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportingId(), Reporting_.reportingId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Reporting_.status));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompanyId(), Reporting_.companyId));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), Reporting_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Reporting_.lastModifiedBy));
            }
        }
        return specification;
    }
}
