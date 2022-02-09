package com.mindhub.homebanking.dtos;

import java.util.List;

public class ExchangesHistoryDTO {
    private WalletDTO wallet;
    private List<ExchangeDTO> history;

    public ExchangesHistoryDTO() {
    }

    public ExchangesHistoryDTO(WalletDTO wallet, List<ExchangeDTO> history) {
        this.wallet = wallet;
        this.history = history;
    }

    public WalletDTO getWallet() {
        return wallet;
    }

    public void setWallet(WalletDTO wallet) {
        this.wallet = wallet;
    }

    public List<ExchangeDTO> getHistory() {
        return history;
    }

    public void setHistory(List<ExchangeDTO> history) {
        this.history = history;
    }
}
