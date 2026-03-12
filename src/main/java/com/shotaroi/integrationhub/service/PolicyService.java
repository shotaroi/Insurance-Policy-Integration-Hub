package com.shotaroi.integrationhub.service;

import com.shotaroi.integrationhub.dto.ExternalPolicyRequestDTO;
import com.shotaroi.integrationhub.dto.PolicyResponseDTO;
import com.shotaroi.integrationhub.dto.PolicyStatusUpdateDTO;
import com.shotaroi.integrationhub.entity.PolicyEntity;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import com.shotaroi.integrationhub.exception.PolicyNotFoundException;
import com.shotaroi.integrationhub.exception.DuplicatePolicyException;
import com.shotaroi.integrationhub.mapper.PolicyMapper;
import com.shotaroi.integrationhub.messaging.PolicyEventPublisher;
import com.shotaroi.integrationhub.repository.PolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for policy operations.
 * Handles validation, persistence, and event publishing.
 */
@Service
public class PolicyService {

    private static final Logger log = LoggerFactory.getLogger(PolicyService.class);

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;
    private final PolicyEventPublisher eventPublisher;

    public PolicyService(PolicyRepository policyRepository,
                         PolicyMapper policyMapper,
                         PolicyEventPublisher eventPublisher) {
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public PolicyResponseDTO createPolicy(ExternalPolicyRequestDTO request) {
        if (policyRepository.existsByPolicyNumber(request.policyNumber())) {
            throw new DuplicatePolicyException(request.policyNumber());
        }

        PolicyEntity entity = policyMapper.toEntity(request);
        entity = policyRepository.save(entity);
        PolicyResponseDTO response = policyMapper.toResponseDTO(entity);

        log.info("Policy created: policyNumber={}, id={}", entity.getPolicyNumber(), entity.getId());
        eventPublisher.publishPolicyCreated(response);

        return response;
    }

    @Transactional(readOnly = true)
    public PolicyResponseDTO getPolicyById(Long id) {
        PolicyEntity entity = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException(id));
        return policyMapper.toResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public PolicyResponseDTO getPolicyByNumber(String policyNumber) {
        PolicyEntity entity = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber));
        return policyMapper.toResponseDTO(entity);
    }

    @Transactional
    public PolicyResponseDTO updatePolicyStatus(Long id, PolicyStatusUpdateDTO update) {
        PolicyEntity entity = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException(id));

        PolicyStatus previousStatus = entity.getStatus();
        PolicyStatus newStatus = update.status();

        if (previousStatus == newStatus) {
            return policyMapper.toResponseDTO(entity);
        }

        entity.setStatus(newStatus);
        entity = policyRepository.save(entity);
        PolicyResponseDTO response = policyMapper.toResponseDTO(entity);

        log.info("Policy status updated: policyNumber={}, id={}, {} -> {}",
                entity.getPolicyNumber(), entity.getId(), previousStatus, newStatus);

        eventPublisher.publishPolicyUpdated(response, previousStatus);

        if (newStatus == PolicyStatus.CANCELLED) {
            eventPublisher.publishPolicyCancelled(response);
        }

        return response;
    }
}
