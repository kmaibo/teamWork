package org.ted.teamworkbankappliaction.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class QueryConditionDto {
    private String query;
    private List<String> arguments;
    private Boolean negate;
}


