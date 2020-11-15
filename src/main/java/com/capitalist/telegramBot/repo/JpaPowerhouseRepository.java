package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.Powerhouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPowerhouseRepository extends JpaRepository<Powerhouse, Integer> {
}
