package com.capitalist.telegramBot.controllers;


import com.capitalist.telegramBot.model.User;
import com.capitalist.telegramBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CapitalistRestController {

    private final UserService userService;

    @RequestMapping(value = "/getPay", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getPay(@RequestBody JsonDTO jsonDTO) {
        if (jsonDTO == null){
            return "NOT";
        }
        String type = jsonDTO.getUs_key().split("_");
        User user = userService.getOrCreate(Integer.parseInt(jsonDTO.getUs_key().split("_")[1]));

        user.set
        return "OK";
    }
}
