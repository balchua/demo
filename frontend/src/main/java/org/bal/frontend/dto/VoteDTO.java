package org.bal.frontend.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class VoteDTO implements Comparable<VoteDTO> {
    @Getter
    @Setter
    private int count;

    @Getter
    @Setter
    private String quote;

    @Getter
    @Setter
    private int quoteId;

    @Override
    public int compareTo(@NonNull VoteDTO o) {
        if (count == o.count)
            return 0;
        else if (count < o.count)
            return 1;
        else
            return -1;
    }

}
