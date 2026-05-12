package pro.sky.telegrambot.listener;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.UserEntity;
import pro.sky.telegrambot.model.Users;

import java.util.List;
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByChatId(long chatId);

    List<UserEntity> findByFirstNameIgnoreCaseOrLastNameIgnoreCase(String query, String query1);
}
