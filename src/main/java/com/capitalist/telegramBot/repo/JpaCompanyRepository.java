package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCompanyRepository extends JpaRepository<Company, Integer> {
}
