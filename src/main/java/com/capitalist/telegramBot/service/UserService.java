package com.capitalist.telegramBot.service;

import com.capitalist.telegramBot.model.Company;
import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.repo.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private JpaUserRepository userRepository;
    private final CompanyService companyService;

    @Value("${bot.url}")
    String url;

    @Autowired
    public UserService(JpaUserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public UserService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public User update(User user){
        if (user.getCompanyId() == 0){
            Company company = new Company();
            companyService.update(company);
            user.setCompanyId(company.getCompanyId());
        }
        String ref = url + "?start=" + user.getUserId();
        user.setReferencesUrl(ref);
        return userRepository.save(user);
    }

    public void deleteById(Integer id){
        User user = get(id).get();
        if (user.getCompanyId() != 0){
            companyService.delete(id);
        }

        userRepository.deleteById(id);
    }

    public User getOrCreate(Integer id){
        return get(id).orElseGet(
                () -> update(new User(id)));
    }

    public Optional<User> get(int chatId) {
        return userRepository.findById(chatId);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
