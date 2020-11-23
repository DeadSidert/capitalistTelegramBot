package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT p from Payment p where p.success=false")
    List<Payment> findAllBySuccess();
}
