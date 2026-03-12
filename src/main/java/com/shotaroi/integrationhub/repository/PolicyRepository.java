package com.shotaroi.integrationhub.repository;

import com.shotaroi.integrationhub.entity.PolicyEntity;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Policy entities.
 */
@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {

    Optional<PolicyEntity> findByPolicyNumber(String policyNumber);

    boolean existsByPolicyNumber(String policyNumber);

    List<PolicyEntity> findByCustomerId(String customerId);

    List<PolicyEntity> findByStatus(PolicyStatus status);
}
