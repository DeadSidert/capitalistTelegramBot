package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaCompanyRepository extends JpaRepository<Company, Integer> {

    @Query("select c from Company c where c.name<>'Без названия' ")
    List<Company> findCompaniesWithName();
}
