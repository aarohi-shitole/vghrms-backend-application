package com.vhrms.service;

import com.vhrms.domain.PersonalId;
import com.vhrms.repository.PersonalIdRepository;
import com.vhrms.service.dto.PersonalIdDTO;
import com.vhrms.service.mapper.PersonalIdMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonalId}.
 */
@Service
@Transactional
public class PersonalIdService {

    private final Logger log = LoggerFactory.getLogger(PersonalIdService.class);

    private final PersonalIdRepository personalIdRepository;

    private final PersonalIdMapper personalIdMapper;

    public PersonalIdService(PersonalIdRepository personalIdRepository, PersonalIdMapper personalIdMapper) {
        this.personalIdRepository = personalIdRepository;
        this.personalIdMapper = personalIdMapper;
    }

    /**
     * Save a personalId.
     *
     * @param personalIdDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonalIdDTO save(PersonalIdDTO personalIdDTO) {
        log.debug("Request to save PersonalId : {}", personalIdDTO);
        PersonalId personalId = personalIdMapper.toEntity(personalIdDTO);
        personalId = personalIdRepository.save(personalId);
        return personalIdMapper.toDto(personalId);
    }

    /**
     * Update a personalId.
     *
     * @param personalIdDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonalIdDTO update(PersonalIdDTO personalIdDTO) {
        log.debug("Request to update PersonalId : {}", personalIdDTO);
        PersonalId personalId = personalIdMapper.toEntity(personalIdDTO);
        personalId = personalIdRepository.save(personalId);
        return personalIdMapper.toDto(personalId);
    }

    /**
     * Partially update a personalId.
     *
     * @param personalIdDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonalIdDTO> partialUpdate(PersonalIdDTO personalIdDTO) {
        log.debug("Request to partially update PersonalId : {}", personalIdDTO);

        return personalIdRepository
            .findById(personalIdDTO.getId())
            .map(existingPersonalId -> {
                personalIdMapper.partialUpdate(existingPersonalId, personalIdDTO);

                return existingPersonalId;
            })
            .map(personalIdRepository::save)
            .map(personalIdMapper::toDto);
    }

    /**
     * Get all the personalIds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonalIdDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PersonalIds");
        return personalIdRepository.findAll(pageable).map(personalIdMapper::toDto);
    }

    /**
     * Get one personalId by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonalIdDTO> findOne(Long id) {
        log.debug("Request to get PersonalId : {}", id);
        return personalIdRepository.findById(id).map(personalIdMapper::toDto);
    }

    /**
     * Delete the personalId by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonalId : {}", id);
        personalIdRepository.deleteById(id);
    }
}
