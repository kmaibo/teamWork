package org.ted.teamworkbankapplication.telegrambot.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankapplication.telegrambot.src.model.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByFirstNameIgnoreCaseOrLastNameIgnoreCase(String firstName, String lastName);
}