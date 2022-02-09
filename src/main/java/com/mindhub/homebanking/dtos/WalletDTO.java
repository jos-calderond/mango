package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Wallet;

public class WalletDTO {
    private Long id;
    private Long points;

    public WalletDTO() {
    }

    public WalletDTO(Wallet wallet) {
        this.id = wallet.getId();
        this.points = wallet.getPoints();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}
