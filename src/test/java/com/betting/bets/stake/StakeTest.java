package com.betting.bets.stake;

import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.impl.MultipleStakeType;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public abstract class StakeTest {
    protected StakeType stakeType = new MultipleStakeType();
}