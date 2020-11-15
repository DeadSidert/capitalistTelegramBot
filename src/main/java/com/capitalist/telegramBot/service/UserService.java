package com.capitalist.telegramBot.service;

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

    @Value("${bot.url}")
    String url;

    @Autowired
    public UserService(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService() {
    }

    public User update(User user){
        String ref = url + "?start=" + user.getUserId();
        user.setReferencesUrl(ref);
        return userRepository.save(user);
    }

    public void deleteById(Integer id){
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
