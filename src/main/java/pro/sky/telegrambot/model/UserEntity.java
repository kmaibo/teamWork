package pro.sky.telegrambot.model;


import jakarta.persistence.*;

import java.util.Set;
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_recommendations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<ProductEntity> recommendations;

    // Геттеры
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Set<ProductEntity> getRecommendations() { return recommendations; }

}

