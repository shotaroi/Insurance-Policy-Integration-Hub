package com.shotaroi.integrationhub.soap;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * JAXB adapter for LocalDate serialization in SOAP messages.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate unmarshal(String value) {
        return value == null ? null : LocalDate.parse(value, FORMATTER);
    }

    @Override
    public String marshal(LocalDate value) {
        return value == null ? null : value.format(FORMATTER);
    }
}
