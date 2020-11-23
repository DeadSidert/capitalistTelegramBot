package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaActionsRepository extends JpaRepository<Action, Integer> {

    @Query("SELECT a FROM Action a WHERE a.userId=:action")
    List<Action> findByUserId(@Param("action") int id);

    @Query("SELECT a FROM Action a WHERE a.userId=:userId and a.companyId=:companyId")
    Action findByUserIdAndCompany(@Param("userId") int id, @Param("companyId") int company);

    @Query("select a from Action a where a.companyId=:companyId")
    List<Action> findByCompanyName(@Param("companyId") int id);
}
