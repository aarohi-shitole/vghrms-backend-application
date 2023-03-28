package com.vhrms.web.rest;

import com.vhrms.repository.ReportingRepository;
import com.vhrms.service.ReportingQueryService;
import com.vhrms.service.ReportingService;
import com.vhrms.service.criteria.ReportingCriteria;
import com.vhrms.service.dto.ReportingDTO;
import com.vhrms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.vhrms.domain.Reporting}.
 */
@RestController
@RequestMapping("/api")
public class ReportingResource {

    private final Logger log = LoggerFactory.getLogger(ReportingResource.class);

    private static final String ENTITY_NAME = "reporting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingService reportingService;

    private final ReportingRepository reportingRepository;

    private final ReportingQueryService reportingQueryService;

    public ReportingResource(
        ReportingService reportingService,
        ReportingRepository reportingRepository,
        ReportingQueryService reportingQueryService
    ) {
        this.reportingService = reportingService;
        this.reportingRepository = reportingRepository;
        this.reportingQueryService = reportingQueryService;
    }

    /**
     * {@code POST  /reportings} : Create a new reporting.
     *
     * @param reportingDTO the reportingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingDTO, or with status {@code 400 (Bad Request)} if the reporting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reportings")
    public ResponseEntity<ReportingDTO> createReporting(@RequestBody ReportingDTO reportingDTO) throws URISyntaxException {
        log.debug("REST request to save Reporting : {}", reportingDTO);
        if (reportingDTO.getId() != null) {
            throw new BadRequestAlertException("A new reporting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportingDTO result = reportingService.save(reportingDTO);
        return ResponseEntity
            .created(new URI("/api/reportings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reportings/:id} : Updates an existing reporting.
     *
     * @param id the id of the reportingDTO to save.
     * @param reportingDTO the reportingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingDTO,
     * or with status {@code 400 (Bad Request)} if the reportingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reportings/{id}")
    public ResponseEntity<ReportingDTO> updateReporting(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportingDTO reportingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reporting : {}, {}", id, reportingDTO);
        if (reportingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportingDTO result = reportingService.update(reportingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reportings/:id} : Partial updates given fields of an existing reporting, field will ignore if it is null
     *
     * @param id the id of the reportingDTO to save.
     * @param reportingDTO the reportingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingDTO,
     * or with status {@code 400 (Bad Request)} if the reportingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reportings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportingDTO> partialUpdateReporting(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportingDTO reportingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reporting partially : {}, {}", id, reportingDTO);
        if (reportingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingDTO> result = reportingService.partialUpdate(reportingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reportings} : get all the reportings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportings in body.
     */
    @GetMapping("/reportings")
    public ResponseEntity<List<ReportingDTO>> getAllReportings(
        ReportingCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Reportings by criteria: {}", criteria);
        Page<ReportingDTO> page = reportingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reportings/count} : count all the reportings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reportings/count")
    public ResponseEntity<Long> countReportings(ReportingCriteria criteria) {
        log.debug("REST request to count Reportings by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reportings/:id} : get the "id" reporting.
     *
     * @param id the id of the reportingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reportings/{id}")
    public ResponseEntity<ReportingDTO> getReporting(@PathVariable Long id) {
        log.debug("REST request to get Reporting : {}", id);
        Optional<ReportingDTO> reportingDTO = reportingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingDTO);
    }

    /**
     * {@code DELETE  /reportings/:id} : delete the "id" reporting.
     *
     * @param id the id of the reportingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reportings/{id}")
    public ResponseEntity<Void> deleteReporting(@PathVariable Long id) {
        log.debug("REST request to delete Reporting : {}", id);
        reportingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
