package com.shotaroi.integrationhub.soap;

import com.shotaroi.integrationhub.dto.ExternalPolicyRequestDTO;
import com.shotaroi.integrationhub.dto.PolicyResponseDTO;
import com.shotaroi.integrationhub.entity.enums.PolicyStatus;
import com.shotaroi.integrationhub.entity.enums.PolicyType;
import com.shotaroi.integrationhub.service.PolicyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.math.BigDecimal;

/**
 * SOAP endpoint for legacy system integration.
 * Exposes GetPolicyByNumber and CreatePolicyLegacy operations.
 */
@Endpoint
public class PolicySoapEndpoint {

    private static final Logger log = LoggerFactory.getLogger(PolicySoapEndpoint.class);
    private static final String NAMESPACE_URI = "http://shotaroi.com/integrationhub/soap/policy";

    private final PolicyService policyService;

    public PolicySoapEndpoint(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetPolicyByNumberRequest")
    @ResponsePayload
    public GetPolicyByNumberResponse getPolicyByNumber(@RequestPayload GetPolicyByNumberRequest request) {
        log.info("SOAP GetPolicyByNumber request for policyNumber={}", request.getPolicyNumber());
        GetPolicyByNumberResponse response = new GetPolicyByNumberResponse();
        try {
            PolicyResponseDTO dto = policyService.getPolicyByNumber(request.getPolicyNumber());
            response.setPolicy(toSoapPolicyType(dto));
            response.setFound(true);
        } catch (Exception e) {
            log.warn("Policy not found: {}", request.getPolicyNumber());
            response.setFound(false);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreatePolicyLegacyRequest")
    @ResponsePayload
    public CreatePolicyLegacyResponse createPolicyLegacy(@RequestPayload CreatePolicyLegacyRequest request) {
        log.info("SOAP CreatePolicyLegacy request for policyNumber={}", request.getPolicyNumber());
        CreatePolicyLegacyResponse response = new CreatePolicyLegacyResponse();
        try {
            ExternalPolicyRequestDTO dto = new ExternalPolicyRequestDTO(
                    request.getPolicyNumber(),
                    request.getCustomerId(),
                    PolicyType.valueOf(request.getPolicyType()),
                    request.getPremiumAmount(),
                    request.getStartDate(),
                    PolicyStatus.PENDING
            );
            PolicyResponseDTO created = policyService.createPolicy(dto);
            response.setPolicy(toSoapPolicyType(created));
            response.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to create policy via SOAP", e);
            response.setSuccess(false);
            response.setPolicy(new SoapPolicyType());
        }
        return response;
    }

    private SoapPolicyType toSoapPolicyType(PolicyResponseDTO dto) {
        SoapPolicyType soap = new SoapPolicyType();
        soap.setId(dto.id() != null ? dto.id() : 0L);
        soap.setPolicyNumber(dto.policyNumber());
        soap.setCustomerId(dto.customerId());
        soap.setPolicyType(dto.policyType().name());
        soap.setPremiumAmount(dto.premiumAmount());
        soap.setStartDate(dto.startDate());
        soap.setStatus(dto.status().name());
        return soap;
    }
}
