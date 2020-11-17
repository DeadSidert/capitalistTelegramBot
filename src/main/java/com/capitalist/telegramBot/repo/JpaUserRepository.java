package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Integer> {

    @Override
    Optional<User> findById(Integer integer);

    @Query("SELECT count(u) from User u where u.referId=:ref")
    int countReferals(@Param("ref") Integer id);

    @Query("SELECT u from User u WHERE u.referId=:userId")
    List<User> findAllRefers(@Param("userId") int id);

    @Query("SELECT u FROM User u WHERE u.referId<>0")
    List<User> usersWithRefer();


}
