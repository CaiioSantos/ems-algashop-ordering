package com.algaworks.algashop.ordering.domain.valueobject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record Document(String value) {

    public Document{
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }


    @Override
    public String toString() {
        return value;
    }

}
