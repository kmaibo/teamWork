package org.ted.teamworkbankappliaction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ted.teamworkbankappliaction.dto.DynamicRuleDto;
import org.ted.teamworkbankappliaction.dto.RulesListResponse;
import org.ted.teamworkbankappliaction.service.DynamicRuleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
public class RuleController {

    private final DynamicRuleService ruleService;

    @PostMapping
    public ResponseEntity<DynamicRuleDto> createRule(@RequestBody @Valid DynamicRuleDto request) {
        DynamicRuleDto created = ruleService.createRule(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<RulesListResponse> getAllRules() {
        List<DynamicRuleDto> rules = ruleService.getAllRules();
        return ResponseEntity.ok(new RulesListResponse(rules));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        ruleService.deleteRule(productId);
        return ResponseEntity.noContent().build();
    }
}