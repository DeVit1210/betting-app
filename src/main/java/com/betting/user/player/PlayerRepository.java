package com.betting.user.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findPlayerByUsername(String username);
    Optional<Player> findPlayerByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query("update Player p set p.isEnabled=true where p.username=?1")
    void enablePlayer(String username);

    @Transactional
    @Modifying
    @Query("update Player p set p.isNonLocked=true where p.username=?1")
    void unlockPlayer(String username);

    @Transactional
    @Modifying
    @Query("update Player p set p.password=?2 where p.username=?1")
    void updatePassword(String username, String encodedNewPassword);
}