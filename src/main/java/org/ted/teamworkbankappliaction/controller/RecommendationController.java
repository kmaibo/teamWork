
package org.ted.teamworkbankappliaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ted.teamworkbankappliaction.dto.DynamicRuleDto;
import org.ted.teamworkbankappliaction.service.DynamicRuleEvaluator;
import org.ted.teamworkbankappliaction.service.DynamicRuleService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleEvaluator dynamicRuleEvaluator;

    @GetMapping("/{userId}")
    public ResponseEntity<List<DynamicRuleDto>> getRecommendations(@PathVariable UUID userId) {
        List<DynamicRuleDto> allRules = dynamicRuleService.getAllRules();
        List<DynamicRuleDto> applicable = allRules.stream()
                .filter(rule -> dynamicRuleEvaluator.evaluate(rule, userId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicable);
    }
}