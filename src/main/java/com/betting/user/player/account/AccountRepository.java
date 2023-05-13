package com.betting.user.player.account;

import com.betting.user.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    @Modifying
    @Query("update Account acc set acc.currentMoneyAmount=?1 where acc.id=?2")
    void updateAccount(double moneyAmount, Long id);

    Optional<Account> findAccountByPlayer(Player player);
}
