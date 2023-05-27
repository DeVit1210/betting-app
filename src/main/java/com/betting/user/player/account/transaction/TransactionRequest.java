package com.betting.user.player.account.transaction;

import lombok.Getter;

@Getter
public record TransactionRequest(Long accountId, double moneyAmount, boolean out) {
}
