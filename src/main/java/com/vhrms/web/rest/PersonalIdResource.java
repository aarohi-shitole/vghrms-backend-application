package com.vhrms.web.rest;

import com.vhrms.repository.PersonalIdRepository;
import com.vhrms.service.PersonalIdQueryService;
import com.vhrms.service.PersonalIdService;
import com.vhrms.service.criteria.PersonalIdCriteria;
import com.vhrms.service.dto.PersonalIdDTO;
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
 * REST controller for managing {@link com.vhrms.domain.PersonalId}.
 */
@RestController
@RequestMapping("/api")
public class PersonalIdResource {

    private final Logger log = LoggerFactory.getLogger(PersonalIdResource.class);

    private static final String ENTITY_NAME = "personalId";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalIdService personalIdService;

    private final PersonalIdRepository personalIdRepository;

    private final PersonalIdQueryService personalIdQueryService;

    public PersonalIdResource(
        PersonalIdService personalIdService,
        PersonalIdRepository personalIdRepository,
        PersonalIdQueryService personalIdQueryService
    ) {
        this.personalIdService = personalIdService;
        this.personalIdRepository = personalIdRepository;
        this.personalIdQueryService = personalIdQueryService;
    }

    /**
     * {@code POST  /personal-ids} : Create a new personalId.
     *
     * @param personalIdDTO the personalIdDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalIdDTO, or with status {@code 400 (Bad Request)} if the personalId has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personal-ids")
    public ResponseEntity<PersonalIdDTO> createPersonalId(@RequestBody PersonalIdDTO personalIdDTO) throws URISyntaxException {
        log.debug("REST request to save PersonalId : {}", personalIdDTO);
        if (personalIdDTO.getId() != null) {
            throw new BadRequestAlertException("A new personalId cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalIdDTO result = personalIdService.save(personalIdDTO);
        return ResponseEntity
            .created(new URI("/api/personal-ids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personal-ids/:id} : Updates an existing personalId.
     *
     * @param id the id of the personalIdDTO to save.
     * @param personalIdDTO the personalIdDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalIdDTO,
     * or with status {@code 400 (Bad Request)} if the personalIdDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalIdDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personal-ids/{id}")
    public ResponseEntity<PersonalIdDTO> updatePersonalId(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalIdDTO personalIdDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PersonalId : {}, {}", id, personalIdDTO);
        if (personalIdDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalIdDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalIdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonalIdDTO result = personalIdService.update(personalIdDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalIdDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personal-ids/:id} : Partial updates given fields of an existing personalId, field will ignore if it is null
     *
     * @param id the id of the personalIdDTO to save.
     * @param personalIdDTO the personalIdDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalIdDTO,
     * or with status {@code 400 (Bad Request)} if the personalIdDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personalIdDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalIdDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personal-ids/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalIdDTO> partialUpdatePersonalId(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalIdDTO personalIdDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonalId partially : {}, {}", id, personalIdDTO);
        if (personalIdDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalIdDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalIdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalIdDTO> result = personalIdService.partialUpdate(personalIdDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalIdDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /personal-ids} : get all the personalIds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalIds in body.
     */
    @GetMapping("/personal-ids")
    public ResponseEntity<List<PersonalIdDTO>> getAllPersonalIds(
        PersonalIdCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PersonalIds by criteria: {}", criteria);
        Page<PersonalIdDTO> page = personalIdQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personal-ids/count} : count all the personalIds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/personal-ids/count")
    public ResponseEntity<Long> countPersonalIds(PersonalIdCriteria criteria) {
        log.debug("REST request to count PersonalIds by criteria: {}", criteria);
        return ResponseEntity.ok().body(personalIdQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /personal-ids/:id} : get the "id" personalId.
     *
     * @param id the id of the personalIdDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalIdDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personal-ids/{id}")
    public ResponseEntity<PersonalIdDTO> getPersonalId(@PathVariable Long id) {
        log.debug("REST request to get PersonalId : {}", id);
        Optional<PersonalIdDTO> personalIdDTO = personalIdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalIdDTO);
    }

    /**
     * {@code DELETE  /personal-ids/:id} : delete the "id" personalId.
     *
     * @param id the id of the personalIdDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personal-ids/{id}")
    public ResponseEntity<Void> deletePersonalId(@PathVariable Long id) {
        log.debug("REST request to delete PersonalId : {}", id);
        personalIdService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
