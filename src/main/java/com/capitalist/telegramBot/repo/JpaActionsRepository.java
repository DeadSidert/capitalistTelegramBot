package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.Actions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaActionsRepository extends JpaRepository<Actions, Integer> {
}
