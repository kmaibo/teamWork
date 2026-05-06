package org.ted.teamworkbankappliaction.dto;

import lombok.*;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RulesListResponse {
    private List<DynamicRuleDto> data;
    }
