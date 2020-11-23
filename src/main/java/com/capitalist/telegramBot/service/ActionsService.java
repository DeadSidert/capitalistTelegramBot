package com.capitalist.telegramBot.service;

import com.capitalist.telegramBot.model.Action;
import com.capitalist.telegramBot.repo.JpaActionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActionsService {

    private final JpaActionsRepository actionsRepository;

    @Autowired
    public ActionsService(JpaActionsRepository actionsRepository) {
        this.actionsRepository = actionsRepository;
    }

    public Action update(Action actions){
        return actionsRepository.save(actions);
    }

    public List<Action> findByUserId(int id){
        return actionsRepository.findByUserId(id);
    }

    public Action findByUserIdAndCompany(int id, int companyId){
        return actionsRepository.findByUserIdAndCompany(id, companyId);
    }

    public List<Action> findAll(){
        return actionsRepository.findAll();
    }

    public List<Action> findByCompany(int id){
        return actionsRepository.findByCompanyName(id);
    }

}
