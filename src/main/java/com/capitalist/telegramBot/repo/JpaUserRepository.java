package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Integer> {

    @Override
    Optional<User> findById(Integer integer);
}
