package com.vhrms.service.mapper;

import com.vhrms.domain.FormValidator;
import com.vhrms.service.dto.FormValidatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormValidator} and its DTO {@link FormValidatorDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormValidatorMapper extends EntityMapper<FormValidatorDTO, FormValidator> {}
