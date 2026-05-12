package org.ted.teamworkbankapplication.telegrambot.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankapplication.telegrambot.src.model.BotState;

public interface BotStateRepository extends JpaRepository<BotState, Long> {
}
