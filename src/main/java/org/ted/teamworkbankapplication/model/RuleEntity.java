package org.ted.teamworkbankapplication.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "dynamic_rules")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(name = "product_text", length = 2000)
    private String productText;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private List<QueryConditionEntity> rule;
}