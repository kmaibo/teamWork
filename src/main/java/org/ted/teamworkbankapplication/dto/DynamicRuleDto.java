
package org.ted.teamworkbankapplication.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicRuleDto {
    private UUID id;
    private String productName;
    private UUID productId;
    private String productText;
    private List<QueryConditionDto> rule;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryConditionDto {

        private String query;
        private List<String> arguments;
        private boolean negate;

    }
}