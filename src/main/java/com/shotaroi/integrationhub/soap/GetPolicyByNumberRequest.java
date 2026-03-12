package com.shotaroi.integrationhub.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"policyNumber"})
@XmlRootElement(name = "GetPolicyByNumberRequest", namespace = "http://shotaroi.com/integrationhub/soap/policy")
public class GetPolicyByNumberRequest {

    @XmlElement(required = true)
    private String policyNumber;

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
}
