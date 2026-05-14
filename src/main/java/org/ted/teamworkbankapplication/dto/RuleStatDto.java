package org.ted.teamworkbankapplication.dto;

import java.util.UUID;

public class RuleStatDto {
    private UUID ruleId;
    private long count;


    public RuleStatDto(UUID ruleId, long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public long getCount() {
        return count;
    }
}
