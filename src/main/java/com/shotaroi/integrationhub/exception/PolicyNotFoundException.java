package com.shotaroi.integrationhub.exception;

/**
 * Thrown when a policy cannot be found by ID or policy number.
 */
public class PolicyNotFoundException extends RuntimeException {

    public PolicyNotFoundException(Long id) {
        super("Policy not found with id: " + id);
    }

    public PolicyNotFoundException(String policyNumber) {
        super("Policy not found with policy number: " + policyNumber);
    }
}
