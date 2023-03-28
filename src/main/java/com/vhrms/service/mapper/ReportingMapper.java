package com.vhrms.service.mapper;

import com.vhrms.domain.Reporting;
import com.vhrms.service.dto.ReportingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reporting} and its DTO {@link ReportingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportingMapper extends EntityMapper<ReportingDTO, Reporting> {}
