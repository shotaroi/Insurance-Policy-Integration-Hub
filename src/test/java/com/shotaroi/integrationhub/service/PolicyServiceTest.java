package com.shotaroi.integrationhub.service;

import com.shotaroi.integrationhub.dto.ExternalPolicyRequestDTO;
import com.shotaroi.integrationhub.dto.PolicyStatusUpdateDTO;
import com.shotaroi.integrationhub.entity.PolicyEntity;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import com.shotaroi.integrationhub.entity.enums.PolicyType;
import com.shotaroi.integrationhub.exception.DuplicatePolicyException;
import com.shotaroi.integrationhub.exception.PolicyNotFoundException;
import com.shotaroi.integrationhub.mapper.PolicyMapper;
import com.shotaroi.integrationhub.messaging.PolicyEventPublisher;
import com.shotaroi.integrationhub.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private PolicyMapper policyMapper;

    @Mock
    private PolicyEventPublisher eventPublisher;

    @InjectMocks
    private PolicyService policyService;

    @Test
    void createPolicy_Success_PublishesEvent() {
        ExternalPolicyRequestDTO request = new ExternalPolicyRequestDTO(
                "POL-001", "CUST-001", PolicyType.LIFE,
                new BigDecimal("150.00"), LocalDate.now(), PolicyStatus.PENDING
        );
        PolicyEntity entity = createPolicyEntity(1L, "POL-001");
        var responseDTO = new com.shotaroi.integrationhub.dto.PolicyResponseDTO(
                1L, "POL-001", "CUST-001", PolicyType.LIFE,
                new BigDecimal("150.00"), LocalDate.now(), PolicyStatus.PENDING,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(policyRepository.existsByPolicyNumber("POL-001")).thenReturn(false);
        when(policyMapper.toEntity(request)).thenReturn(entity);
        when(policyRepository.save(any())).thenReturn(entity);
        when(policyMapper.toResponseDTO(entity)).thenReturn(responseDTO);

        var result = policyService.createPolicy(request);

        assertThat(result).isNotNull();
        assertThat(result.policyNumber()).isEqualTo("POL-001");
        verify(eventPublisher).publishPolicyCreated(responseDTO);
    }

    @Test
    void createPolicy_DuplicatePolicyNumber_ThrowsException() {
        ExternalPolicyRequestDTO request = new ExternalPolicyRequestDTO(
                "POL-001", "CUST-001", PolicyType.LIFE,
                new BigDecimal("150.00"), LocalDate.now(), PolicyStatus.PENDING
        );
        when(policyRepository.existsByPolicyNumber("POL-001")).thenReturn(true);

        assertThatThrownBy(() -> policyService.createPolicy(request))
                .isInstanceOf(DuplicatePolicyException.class)
                .hasMessageContaining("POL-001");

        verify(policyRepository, never()).save(any());
        verify(eventPublisher, never()).publishPolicyCreated(any());
    }

    @Test
    void getPolicyById_NotFound_ThrowsException() {
        when(policyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.getPolicyById(999L))
                .isInstanceOf(PolicyNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void updatePolicyStatus_Success_PublishesEvent() {
        PolicyEntity entity = createPolicyEntity(1L, "POL-001");
        entity.setStatus(PolicyStatus.ACTIVE);
        PolicyStatusUpdateDTO update = new PolicyStatusUpdateDTO(PolicyStatus.CANCELLED);
        var responseDTO = new com.shotaroi.integrationhub.dto.PolicyResponseDTO(
                1L, "POL-001", "CUST-001", PolicyType.LIFE,
                new BigDecimal("150.00"), LocalDate.now(), PolicyStatus.CANCELLED,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(policyRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(policyRepository.save(any())).thenReturn(entity);
        when(policyMapper.toResponseDTO(any())).thenReturn(responseDTO);

        var result = policyService.updatePolicyStatus(1L, update);

        assertThat(result.status()).isEqualTo(PolicyStatus.CANCELLED);
        verify(eventPublisher).publishPolicyUpdated(responseDTO, PolicyStatus.ACTIVE);
        verify(eventPublisher).publishPolicyCancelled(responseDTO);
    }

    private PolicyEntity createPolicyEntity(Long id, String policyNumber) {
        PolicyEntity entity = new PolicyEntity();
        entity.setId(id);
        entity.setPolicyNumber(policyNumber);
        entity.setCustomerId("CUST-001");
        entity.setPolicyType(PolicyType.LIFE);
        entity.setPremiumAmount(new BigDecimal("150.00"));
        entity.setStartDate(LocalDate.now());
        entity.setStatus(PolicyStatus.PENDING);
        return entity;
    }
}
