package org.bal.frontend.dto;

import lombok.Getter;
import lombok.Setter;

public class QuoteDTO {

    @Getter @Setter
    private String quote;

    @Getter @Setter
    private int quoteId;

    @Getter @Setter
    private String name;
}
