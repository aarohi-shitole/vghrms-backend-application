package com.vhrms.service.mapper;

import com.vhrms.domain.PersonalDetails;
import com.vhrms.service.dto.PersonalDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonalDetails} and its DTO {@link PersonalDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalDetailsMapper extends EntityMapper<PersonalDetailsDTO, PersonalDetails> {}
