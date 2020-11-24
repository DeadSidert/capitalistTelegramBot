package com.capitalist.telegramBot.service;

import com.capitalist.telegramBot.model.Channel;
import com.capitalist.telegramBot.repo.JpaChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final JpaChannelRepository channelRepository;

    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    public Channel update(Channel channel) {
        return channelRepository.save(channel);
    }

    public void delete(String id) {
        channelRepository.deleteById(id);
    }
}
