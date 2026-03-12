package com.shotaroi.integrationhub.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"policy", "success"})
@XmlRootElement(name = "CreatePolicyLegacyResponse", namespace = "http://shotaroi.com/integrationhub/soap/policy")
public class CreatePolicyLegacyResponse {

    private SoapPolicyType policy;
    private boolean success;

    public SoapPolicyType getPolicy() {
        return policy;
    }

    public void setPolicy(SoapPolicyType policy) {
        this.policy = policy;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
