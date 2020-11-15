package com.capitalist.telegramBot.security;

import com.capitalist.telegramBot.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationService {

    public final boolean authorize(User user){
        log.debug("Authorizing {} to use", user);
        String role = user.getRole();
        return "admin".equalsIgnoreCase(role);
    }
}
