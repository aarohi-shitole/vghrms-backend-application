package com.vhrms.service;

import com.vhrms.domain.Reporting;
import com.vhrms.repository.ReportingRepository;
import com.vhrms.service.dto.ReportingDTO;
import com.vhrms.service.mapper.ReportingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reporting}.
 */
@Service
@Transactional
public class ReportingService {

    private final Logger log = LoggerFactory.getLogger(ReportingService.class);

    private final ReportingRepository reportingRepository;

    private final ReportingMapper reportingMapper;

    public ReportingService(ReportingRepository reportingRepository, ReportingMapper reportingMapper) {
        this.reportingRepository = reportingRepository;
        this.reportingMapper = reportingMapper;
    }

    /**
     * Save a reporting.
     *
     * @param reportingDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingDTO save(ReportingDTO reportingDTO) {
        log.debug("Request to save Reporting : {}", reportingDTO);
        Reporting reporting = reportingMapper.toEntity(reportingDTO);
        reporting = reportingRepository.save(reporting);
        return reportingMapper.toDto(reporting);
    }

    /**
     * Update a reporting.
     *
     * @param reportingDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingDTO update(ReportingDTO reportingDTO) {
        log.debug("Request to update Reporting : {}", reportingDTO);
        Reporting reporting = reportingMapper.toEntity(reportingDTO);
        reporting = reportingRepository.save(reporting);
        return reportingMapper.toDto(reporting);
    }

    /**
     * Partially update a reporting.
     *
     * @param reportingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportingDTO> partialUpdate(ReportingDTO reportingDTO) {
        log.debug("Request to partially update Reporting : {}", reportingDTO);

        return reportingRepository
            .findById(reportingDTO.getId())
            .map(existingReporting -> {
                reportingMapper.partialUpdate(existingReporting, reportingDTO);

                return existingReporting;
            })
            .map(reportingRepository::save)
            .map(reportingMapper::toDto);
    }

    /**
     * Get all the reportings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reportings");
        return reportingRepository.findAll(pageable).map(reportingMapper::toDto);
    }

    /**
     * Get one reporting by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportingDTO> findOne(Long id) {
        log.debug("Request to get Reporting : {}", id);
        return reportingRepository.findById(id).map(reportingMapper::toDto);
    }

    /**
     * Delete the reporting by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reporting : {}", id);
        reportingRepository.deleteById(id);
    }
}
