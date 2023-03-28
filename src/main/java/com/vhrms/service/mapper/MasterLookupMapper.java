package com.vhrms.service.mapper;

import com.vhrms.domain.MasterLookup;
import com.vhrms.service.dto.MasterLookupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MasterLookup} and its DTO {@link MasterLookupDTO}.
 */
@Mapper(componentModel = "spring")
public interface MasterLookupMapper extends EntityMapper<MasterLookupDTO, MasterLookup> {}
