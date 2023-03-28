package com.vhrms.service.mapper;

import com.vhrms.domain.FamilyInfo;
import com.vhrms.service.dto.FamilyInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FamilyInfo} and its DTO {@link FamilyInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface FamilyInfoMapper extends EntityMapper<FamilyInfoDTO, FamilyInfo> {}
