package com.vhrms.service.mapper;

import com.vhrms.domain.PersonalId;
import com.vhrms.service.dto.PersonalIdDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonalId} and its DTO {@link PersonalIdDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalIdMapper extends EntityMapper<PersonalIdDTO, PersonalId> {}
