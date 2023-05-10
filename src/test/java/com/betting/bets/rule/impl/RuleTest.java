package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RuleTest {
    @Autowired
    protected Rule rule;
}