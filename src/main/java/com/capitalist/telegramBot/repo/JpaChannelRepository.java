package com.capitalist.telegramBot.repo;

import com.capitalist.telegramBot.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChannelRepository extends JpaRepository<Channel, String> {
}
