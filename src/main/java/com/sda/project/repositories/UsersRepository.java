package com.sda.project.repositories;

import com.sda.project.entities.User;
import com.sda.project.entities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findAllByStatus(UserStatus status);
}
