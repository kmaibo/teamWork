package org.ted.teamworkbankappliaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankappliaction.model.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
