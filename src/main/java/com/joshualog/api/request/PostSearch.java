package com.joshualog.api.request;

import static java.lang.Math.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {
    public static final int MAX_SIZE = 2000;
    @Builder.Default
    private Integer size = 10;
    @Builder.Default
    private Integer page = 1;
    public long getOffset() {
        return (long) (max(page, 1) - 1) * min(size, MAX_SIZE);
    }
}
