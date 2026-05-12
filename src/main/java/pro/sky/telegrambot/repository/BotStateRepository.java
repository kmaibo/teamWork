package pro.sky.telegrambot.repository;


import pro.sky.telegrambot.model.BotState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotStateRepository extends JpaRepository<BotState, Long> {
}
