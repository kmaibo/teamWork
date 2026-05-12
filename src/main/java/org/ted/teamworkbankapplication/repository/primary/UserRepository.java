package org.ted.teamworkbankapplication.repository.primary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankapplication.model.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
