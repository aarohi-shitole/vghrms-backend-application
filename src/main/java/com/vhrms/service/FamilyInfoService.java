package com.vhrms.service;

import com.vhrms.domain.FamilyInfo;
import com.vhrms.repository.FamilyInfoRepository;
import com.vhrms.service.dto.FamilyInfoDTO;
import com.vhrms.service.mapper.FamilyInfoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FamilyInfo}.
 */
@Service
@Transactional
public class FamilyInfoService {

    private final Logger log = LoggerFactory.getLogger(FamilyInfoService.class);

    private final FamilyInfoRepository familyInfoRepository;

    private final FamilyInfoMapper familyInfoMapper;

    public FamilyInfoService(FamilyInfoRepository familyInfoRepository, FamilyInfoMapper familyInfoMapper) {
        this.familyInfoRepository = familyInfoRepository;
        this.familyInfoMapper = familyInfoMapper;
    }

    /**
     * Save a familyInfo.
     *
     * @param familyInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilyInfoDTO save(FamilyInfoDTO familyInfoDTO) {
        log.debug("Request to save FamilyInfo : {}", familyInfoDTO);
        FamilyInfo familyInfo = familyInfoMapper.toEntity(familyInfoDTO);
        familyInfo = familyInfoRepository.save(familyInfo);
        return familyInfoMapper.toDto(familyInfo);
    }

    /**
     * Update a familyInfo.
     *
     * @param familyInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilyInfoDTO update(FamilyInfoDTO familyInfoDTO) {
        log.debug("Request to update FamilyInfo : {}", familyInfoDTO);
        FamilyInfo familyInfo = familyInfoMapper.toEntity(familyInfoDTO);
        familyInfo = familyInfoRepository.save(familyInfo);
        return familyInfoMapper.toDto(familyInfo);
    }

    /**
     * Partially update a familyInfo.
     *
     * @param familyInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FamilyInfoDTO> partialUpdate(FamilyInfoDTO familyInfoDTO) {
        log.debug("Request to partially update FamilyInfo : {}", familyInfoDTO);

        return familyInfoRepository
            .findById(familyInfoDTO.getId())
            .map(existingFamilyInfo -> {
                familyInfoMapper.partialUpdate(existingFamilyInfo, familyInfoDTO);

                return existingFamilyInfo;
            })
            .map(familyInfoRepository::save)
            .map(familyInfoMapper::toDto);
    }

    /**
     * Get all the familyInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilyInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FamilyInfos");
        return familyInfoRepository.findAll(pageable).map(familyInfoMapper::toDto);
    }

    /**
     * Get one familyInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FamilyInfoDTO> findOne(Long id) {
        log.debug("Request to get FamilyInfo : {}", id);
        return familyInfoRepository.findById(id).map(familyInfoMapper::toDto);
    }

    /**
     * Delete the familyInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FamilyInfo : {}", id);
        familyInfoRepository.deleteById(id);
    }
}
