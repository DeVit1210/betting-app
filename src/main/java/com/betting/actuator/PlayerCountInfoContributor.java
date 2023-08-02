package com.betting.actuator;

import com.betting.user.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.actuate.info.Info.Builder;

@Component
@RequiredArgsConstructor
public class PlayerCountInfoContributor implements InfoContributor {
    private final PlayerRepository playerRepository;

    @Override
    public void contribute(Builder builder) {
        long count = playerRepository.count();
        Map<String, Object> info = new HashMap<>();
        info.put("quantity", count);
        builder.withDetail("players-info", info);
    }
}
