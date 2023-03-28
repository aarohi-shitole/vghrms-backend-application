package com.vhrms.repository;

import com.vhrms.domain.Reporting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reporting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingRepository extends JpaRepository<Reporting, Long>, JpaSpecificationExecutor<Reporting> {}
