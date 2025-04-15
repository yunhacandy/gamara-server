package gamara.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SpicyLevel {
    MORE_SPICY("More spicy than other stores"),
    SIMILAR_SPICY("Similar as other stores"),
    LESS_SPICY("Less spicy than other stores");

    private final String description;
}
