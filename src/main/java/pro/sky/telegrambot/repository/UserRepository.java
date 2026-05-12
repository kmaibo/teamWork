package pro.sky.telegrambot.repository;

import pro.sky.telegrambot.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByFirstNameIgnoreCaseOrLastNameIgnoreCase(String firstName, String lastName);
}