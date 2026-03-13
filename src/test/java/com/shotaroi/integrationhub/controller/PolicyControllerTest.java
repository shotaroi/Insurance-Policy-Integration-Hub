package com.shotaroi.integrationhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shotaroi.integrationhub.dto.ExternalPolicyRequestDTO;
import com.shotaroi.integrationhub.dto.PolicyStatusUpdateDTO;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import com.shotaroi.integrationhub.entity.enums.PolicyType;
import com.shotaroi.integrationhub.messaging.PolicyEventPublisher;
import com.shotaroi.integrationhub.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PolicyController.class)
@ActiveProfiles("test")
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PolicyService policyService;

    @MockBean
    private com.shotaroi.integrationhub.messaging.PolicyEventPublisher policyEventPublisher;

    @Test
    void createPolicy_ValidRequest_ReturnsCreated() throws Exception {
        ExternalPolicyRequestDTO request = new ExternalPolicyRequestDTO(
                "POL-001",
                "CUST-001",
                PolicyType.LIFE,
                new BigDecimal("150.00"),
                LocalDate.now(),
                PolicyStatus.PENDING
        );
        var response = new com.shotaroi.integrationhub.dto.PolicyResponseDTO(
                1L, "POL-001", "CUST-001", PolicyType.LIFE,
                new BigDecimal("150.00"), LocalDate.now(), PolicyStatus.PENDING,
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now()
        );
        when(policyService.createPolicy(any())).thenReturn(response);

        mockMvc.perform(post("/api/integration/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.policyNumber").value("POL-001"))
                .andExpect(jsonPath("$.policyType").value("LIFE"));

        verify(policyService).createPolicy(any());
    }

    @Test
    void createPolicy_InvalidRequest_ReturnsBadRequest() throws Exception {
        ExternalPolicyRequestDTO request = new ExternalPolicyRequestDTO(
                "",
                "CUST-001",
                PolicyType.LIFE,
                new BigDecimal("150.00"),
                LocalDate.now(),
                PolicyStatus.PENDING
        );

        mockMvc.perform(post("/api/integration/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPolicy_ExistingId_ReturnsOk() throws Exception {
        var response = new com.shotaroi.integrationhub.dto.PolicyResponseDTO(
                1L, "POL-001", "CUST-001", PolicyType.LIFE,
                new BigDecimal("150.00"), LocalDate.now(), PolicyStatus.ACTIVE,
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now()
        );
        when(policyService.getPolicyById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/integration/policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.policyNumber").value("POL-001"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(policyService).getPolicyById(1L);
    }

    @Test
    void updatePolicyStatus_ValidRequest_ReturnsOk() throws Exception {
        PolicyStatusUpdateDTO update = new PolicyStatusUpdateDTO(PolicyStatus.CANCELLED);
        var response = new com.shotaroi.integrationhub.dto.PolicyResponseDTO(
                1L, "POL-001", "CUST-001", PolicyType.LIFE,
                new BigDecimal("150.00"), LocalDate.now(), PolicyStatus.CANCELLED,
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now()
        );
        when(policyService.updatePolicyStatus(eq(1L), any())).thenReturn(response);

        mockMvc.perform(patch("/api/integration/policies/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(policyService).updatePolicyStatus(eq(1L), any());
    }
}
