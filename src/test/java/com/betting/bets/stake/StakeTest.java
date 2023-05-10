package com.betting.bets.stake;

import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.impl.MultipleStakeType;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public abstract class StakeTest {
    protected StakeType stakeType = new MultipleStakeType();
}