package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.OilPump;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOilPumpRepository extends JpaRepository<OilPump, Integer> {
}
