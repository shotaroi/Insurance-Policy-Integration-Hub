package com.shotaroi.integrationhub.exception;

/**
 * Thrown when attempting to create a policy with a duplicate policy number.
 */
public class DuplicatePolicyException extends RuntimeException {

    public DuplicatePolicyException(String policyNumber) {
        super("Policy already exists with policy number: " + policyNumber);
    }
}
