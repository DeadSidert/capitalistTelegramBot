package com.capitalist.telegramBot.service;

import com.capitalist.telegramBot.model.Company;
import com.capitalist.telegramBot.repo.JpaCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private JpaCompanyRepository companyRepository;

    @Autowired
    public CompanyService(JpaCompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyService() {
    }

    public Company update(Company company){
        return companyRepository.save(company);
    }

    public Company getOrCreate(int id){
        return get(id).orElseGet(() -> companyRepository.save(new Company()));
    }

    public Optional<Company> get(int id){
        return companyRepository.findById(id);
    }

    public List<Company> getCompanies(){
        return companyRepository.findAll();
    }

    public void delete(int id){
        companyRepository.deleteById(id);
    }
}
