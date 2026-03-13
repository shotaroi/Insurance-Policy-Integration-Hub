package com.shotaroi.integrationhub.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlePolicyNotFound_Returns404ProblemDetail() {
        PolicyNotFoundException ex = new PolicyNotFoundException(999L);

        ProblemDetail result = handler.handlePolicyNotFound(ex);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getDetail()).contains("999");
        assertThat(result.getTitle()).isEqualTo("Policy Not Found");
    }

    @Test
    void handleDuplicatePolicy_Returns409ProblemDetail() {
        DuplicatePolicyException ex = new DuplicatePolicyException("POL-001");

        ProblemDetail result = handler.handleDuplicatePolicy(ex);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(result.getDetail()).contains("POL-001");
        assertThat(result.getTitle()).isEqualTo("Duplicate Policy");
    }
}
