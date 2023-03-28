package com.vhrms.repository;

import com.vhrms.domain.PersonalId;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PersonalId entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalIdRepository extends JpaRepository<PersonalId, Long>, JpaSpecificationExecutor<PersonalId> {}
