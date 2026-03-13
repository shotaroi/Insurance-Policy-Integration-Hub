package com.shotaroi.integrationhub.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"policy", "found"})
@XmlRootElement(name = "GetPolicyByNumberResponse", namespace = "http://shotaroi.com/integrationhub/soap/policy")
public class GetPolicyByNumberResponse {

    private SoapPolicyType policy;
    private boolean found;

    public SoapPolicyType getPolicy() {
        return policy;
    }

    public void setPolicy(SoapPolicyType policy) {
        this.policy = policy;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
