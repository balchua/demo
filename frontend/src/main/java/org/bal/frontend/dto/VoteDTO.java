package org.bal.frontend.dto;

import lombok.Getter;
import lombok.Setter;

public class VoteDTO {
    @Getter @Setter
    private int count;

    @Getter @Setter
    private String quote;

    @Getter @Setter
    private int quoteId;
}
