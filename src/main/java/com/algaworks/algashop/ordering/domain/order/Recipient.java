package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.commons.Document;
import com.algaworks.algashop.ordering.domain.commons.FullName;
import com.algaworks.algashop.ordering.domain.commons.Phone;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Recipient(FullName fullName, Document document, Phone phone) {
    public Recipient {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
    }
}
