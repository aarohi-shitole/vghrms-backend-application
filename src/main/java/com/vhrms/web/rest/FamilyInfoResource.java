package com.vhrms.web.rest;

import com.vhrms.repository.FamilyInfoRepository;
import com.vhrms.service.FamilyInfoQueryService;
import com.vhrms.service.FamilyInfoService;
import com.vhrms.service.criteria.FamilyInfoCriteria;
import com.vhrms.service.dto.FamilyInfoDTO;
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
 * REST controller for managing {@link com.vhrms.domain.FamilyInfo}.
 */
@RestController
@RequestMapping("/api")
public class FamilyInfoResource {

    private final Logger log = LoggerFactory.getLogger(FamilyInfoResource.class);

    private static final String ENTITY_NAME = "familyInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilyInfoService familyInfoService;

    private final FamilyInfoRepository familyInfoRepository;

    private final FamilyInfoQueryService familyInfoQueryService;

    public FamilyInfoResource(
        FamilyInfoService familyInfoService,
        FamilyInfoRepository familyInfoRepository,
        FamilyInfoQueryService familyInfoQueryService
    ) {
        this.familyInfoService = familyInfoService;
        this.familyInfoRepository = familyInfoRepository;
        this.familyInfoQueryService = familyInfoQueryService;
    }

    /**
     * {@code POST  /family-infos} : Create a new familyInfo.
     *
     * @param familyInfoDTO the familyInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familyInfoDTO, or with status {@code 400 (Bad Request)} if the familyInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/family-infos")
    public ResponseEntity<FamilyInfoDTO> createFamilyInfo(@RequestBody FamilyInfoDTO familyInfoDTO) throws URISyntaxException {
        log.debug("REST request to save FamilyInfo : {}", familyInfoDTO);
        if (familyInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new familyInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilyInfoDTO result = familyInfoService.save(familyInfoDTO);
        return ResponseEntity
            .created(new URI("/api/family-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /family-infos/:id} : Updates an existing familyInfo.
     *
     * @param id the id of the familyInfoDTO to save.
     * @param familyInfoDTO the familyInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyInfoDTO,
     * or with status {@code 400 (Bad Request)} if the familyInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familyInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/family-infos/{id}")
    public ResponseEntity<FamilyInfoDTO> updateFamilyInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FamilyInfoDTO familyInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FamilyInfo : {}, {}", id, familyInfoDTO);
        if (familyInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FamilyInfoDTO result = familyInfoService.update(familyInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /family-infos/:id} : Partial updates given fields of an existing familyInfo, field will ignore if it is null
     *
     * @param id the id of the familyInfoDTO to save.
     * @param familyInfoDTO the familyInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyInfoDTO,
     * or with status {@code 400 (Bad Request)} if the familyInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the familyInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the familyInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/family-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FamilyInfoDTO> partialUpdateFamilyInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FamilyInfoDTO familyInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FamilyInfo partially : {}, {}", id, familyInfoDTO);
        if (familyInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilyInfoDTO> result = familyInfoService.partialUpdate(familyInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /family-infos} : get all the familyInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familyInfos in body.
     */
    @GetMapping("/family-infos")
    public ResponseEntity<List<FamilyInfoDTO>> getAllFamilyInfos(
        FamilyInfoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FamilyInfos by criteria: {}", criteria);
        Page<FamilyInfoDTO> page = familyInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /family-infos/count} : count all the familyInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/family-infos/count")
    public ResponseEntity<Long> countFamilyInfos(FamilyInfoCriteria criteria) {
        log.debug("REST request to count FamilyInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(familyInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /family-infos/:id} : get the "id" familyInfo.
     *
     * @param id the id of the familyInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familyInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/family-infos/{id}")
    public ResponseEntity<FamilyInfoDTO> getFamilyInfo(@PathVariable Long id) {
        log.debug("REST request to get FamilyInfo : {}", id);
        Optional<FamilyInfoDTO> familyInfoDTO = familyInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familyInfoDTO);
    }

    /**
     * {@code DELETE  /family-infos/:id} : delete the "id" familyInfo.
     *
     * @param id the id of the familyInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/family-infos/{id}")
    public ResponseEntity<Void> deleteFamilyInfo(@PathVariable Long id) {
        log.debug("REST request to delete FamilyInfo : {}", id);
        familyInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
