package com.betting.user.player.account.transaction;

import lombok.Data;

@Data
public class TransactionRequest {
    private Long accountId;
    private double moneyAmount;
    private boolean out;
}
