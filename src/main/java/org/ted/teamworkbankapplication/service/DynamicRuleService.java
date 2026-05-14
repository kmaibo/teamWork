package org.ted.teamworkbankapplication.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ted.teamworkbankapplication.dto.DynamicRuleDto;
import org.ted.teamworkbankapplication.dto.RuleStatDto;
import org.ted.teamworkbankapplication.model.QueryConditionEntity;
import org.ted.teamworkbankapplication.model.RuleEntity;
import org.ted.teamworkbankapplication.model.RuleStat;
import org.ted.teamworkbankapplication.repository.secondary.DynamicRuleRepository;
import org.ted.teamworkbankapplication.repository.secondary.RuleStatRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DynamicRuleService {

    private final DynamicRuleRepository ruleRepository;
    private final RuleStatRepository ruleStatRepository;

    @Transactional(readOnly = true)
    public List<DynamicRuleDto> getAllRules() {
        return ruleRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteRule(UUID productId) {
        ruleRepository.deleteByProductId(productId);
    }

    @Transactional(readOnly = true)
    public DynamicRuleDto getRuleById(UUID id) {
        return ruleRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Правило не найдено с идентификатором: " + id));
    }

    @Transactional(readOnly = true)
    public DynamicRuleDto getRuleByProductId(UUID productId) {
        return ruleRepository.findByProductId(productId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("правило не найдено с productId: " + productId));
    }

    public DynamicRuleDto updateRule(UUID id, DynamicRuleDto dto) {
        RuleEntity existing = ruleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Правило не найдено с идентификатором: " + id));

        // Вручную обновляем поля
        existing.setProductName(dto.getProductName());
        existing.setProductId(dto.getProductId());
        existing.setProductText(dto.getProductText());

        if (dto.getRule() != null) {
            existing.getRule().clear();
            existing.getRule().addAll(dto.getRule().stream()
                    .map(this::mapToQueryEntity)
                    .collect(Collectors.toList()));
        }

        return mapToDto(ruleRepository.save(existing));
    }

    @Transactional(readOnly = true)
    public boolean existsByProductId(UUID productId) {
        return ruleRepository.existsByProductId(productId);
    }

    public DynamicRuleDto createRule(@Valid DynamicRuleDto request) {
        RuleEntity entity = mapToEntity(request);
        entity.setId(null);

        RuleEntity savedEntity = ruleRepository.save(entity);
        return mapToDto(savedEntity);
    }


    private DynamicRuleDto mapToDto(RuleEntity entity) {
        if (entity == null) return null;

        DynamicRuleDto dto = new DynamicRuleDto();
        dto.setId(entity.getId());
        dto.setProductName(entity.getProductName());
        dto.setProductId(entity.getProductId());
        dto.setProductText(entity.getProductText());

        if (entity.getRule() != null) {
            dto.setRule(entity.getRule().stream()
                    .map(this::mapToQueryDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private RuleEntity mapToEntity(DynamicRuleDto dto) {
        if (dto == null) return null;

        RuleEntity entity = new RuleEntity();
        entity.setId(dto.getId());
        entity.setProductName(dto.getProductName());
        entity.setProductId(dto.getProductId());
        entity.setProductText(dto.getProductText());

        if (dto.getRule() != null) {
            entity.setRule(dto.getRule().stream()
                    .map(this::mapToQueryEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private QueryConditionEntity mapToQueryEntity(DynamicRuleDto.QueryConditionDto qDto) {
        QueryConditionEntity qEntity = new QueryConditionEntity();
        qEntity.setQuery(qDto.getQuery());
        qEntity.setArguments(qDto.getArguments());
        qEntity.setNegate(qDto.isNegate());
        return qEntity;
    }

    private DynamicRuleDto.QueryConditionDto mapToQueryDto(QueryConditionEntity qEntity) {
        DynamicRuleDto.QueryConditionDto qDto = new DynamicRuleDto.QueryConditionDto();
        qDto.setQuery(qEntity.getQuery());
        qDto.setArguments(qEntity.getArguments());
        qDto.setNegate(qEntity.isNegate());
        return qDto;
    }

    public List<RuleStatDto> getAllRuleStats() {
        List<RuleStat> stats = ruleStatRepository.findAll();
        List<RuleStatDto> statDtos = stats.stream()
                .map(stat -> new RuleStatDto(stat.getRule().getId(), stat.getCount()))
                .collect(Collectors.toList());
        List<RuleEntity> allRules = ruleRepository.findAll();
        for (RuleEntity rule : allRules) {
            if (statDtos.stream().noneMatch(dto -> dto.getRuleId().equals(rule.getId()))) {
                statDtos.add(new RuleStatDto(rule.getId(), 0L));
            }
        }
        return statDtos;
    }
}