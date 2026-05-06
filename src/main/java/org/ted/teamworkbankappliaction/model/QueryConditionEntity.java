package org.ted.teamworkbankappliaction.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "query_conditions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryConditionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Исправлено
    private UUID id;

    @Column(name = "query_type", nullable = false)
    private String query;

    @ElementCollection
    @CollectionTable(
            name = "query_condition_arguments",
            joinColumns = @JoinColumn(name = "condition_id")
    )
    @Column(name = "argument")
    private List<String> arguments;

    @Column(name = "negate", nullable = false)
    private Boolean negate;
}