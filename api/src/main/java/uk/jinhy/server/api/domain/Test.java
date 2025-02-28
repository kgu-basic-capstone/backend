package uk.jinhy.server.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Test {
    private final int id;
    private final String message;
}
