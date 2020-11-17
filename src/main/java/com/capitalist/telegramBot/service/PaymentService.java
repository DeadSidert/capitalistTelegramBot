package com.capitalist.telegramBot.service;

import com.capitalist.telegramBot.model.Payment;
import com.capitalist.telegramBot.repo.JpaPaymentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final JpaPaymentRepository paymentRepository;

    public PaymentService(JpaPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findAllPaymentsNotSuccess(){
        return paymentRepository.findAllBySuccess();
    }

    public Payment findById(Integer id){
        return paymentRepository.findById(id).get();
    }

    public Payment update(Payment payment){
        return paymentRepository.save(payment);
    }
}
