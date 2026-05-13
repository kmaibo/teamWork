package org.ted.teamworkbankapplication.dto;

import lombok.*;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RulesListResponse {
    private List<DynamicRuleDto> data;
    }
