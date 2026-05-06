
package org.ted.teamworkbankappliaction.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ted.teamworkbankappliaction.dto.DynamicRuleDto;
import org.ted.teamworkbankappliaction.model.QueryConditionEntity;
import org.ted.teamworkbankappliaction.model.RuleEntity;
import org.ted.teamworkbankappliaction.repository.DynamicRuleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DynamicRuleService {

    private final DynamicRuleRepository ruleRepository;
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<DynamicRuleDto> getAllRules() {
        return ruleRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, DynamicRuleDto.class))
                .collect(Collectors.toList());
    }


    public void deleteRule(UUID productId) {
        ruleRepository.deleteByProductId(productId);
    }

    @Transactional(readOnly = true)
    public DynamicRuleDto getRuleById(UUID id) {
        return ruleRepository.findById(id)
                .map(entity -> modelMapper.map(entity, DynamicRuleDto.class))
                .orElseThrow(() -> new RuntimeException("Правило не найдено с идентификатором: " + id));
    }

    @Transactional(readOnly = true)
    public DynamicRuleDto getRuleByProductId(UUID productId) {
        return ruleRepository.findByProductId(productId)
                .map(entity -> modelMapper.map(entity, DynamicRuleDto.class))
                .orElseThrow(() -> new RuntimeException("правило не найдено с productId: " + productId));
    }

    public DynamicRuleDto updateRule(UUID id, DynamicRuleDto dto) {
        // Проверяем, существует ли правило
        RuleEntity existing = ruleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Правило не найдено с идентификатором: " + id));

        // Обновляем поля
        existing.setProductName(dto.getProductName());
        existing.setProductId(dto.getProductId());
        existing.setProductText(dto.getProductText());

        // Обновляем условия (правила)
        if (dto.getRule() != null) {
            existing.getRule().clear();
            existing.getRule().addAll(dto.getRule().stream()
                    .map(queryDto -> modelMapper.map(queryDto, QueryConditionEntity.class))
                    .collect(Collectors.toList()));
        }

        // Сохраняем изменения
        existing = ruleRepository.save(existing);

        return modelMapper.map(existing, DynamicRuleDto.class);
    }
    @Transactional(readOnly = true)
    public boolean existsByProductId(UUID productId) {
        return ruleRepository.existsByProductId(productId);
    }

    public DynamicRuleDto createRule(@Valid DynamicRuleDto request) {
        RuleEntity entity = modelMapper.map(request, RuleEntity.class);
        entity.setId(null); // ID генерируется БД
        entity = ruleRepository.save(entity);
        return modelMapper.map(entity, DynamicRuleDto.class);
    }
}