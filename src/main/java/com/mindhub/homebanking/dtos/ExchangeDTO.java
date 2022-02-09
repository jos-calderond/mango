package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Exchange;
import com.mindhub.homebanking.models.Product;
import com.mindhub.homebanking.models.Wallet;

import java.time.LocalDateTime;

public class ExchangeDTO {
    private Long id;
    private LocalDateTime date;
    private int points;
    private ProductDTO product;
    //private WalletDTO wallet;

    public ExchangeDTO() {
    }

    public ExchangeDTO(Exchange exchange) {
        this.id = exchange.getId();
        this.date = exchange.getDate();
        this.points = exchange.getPoints();
        this.product = new ProductDTO(exchange.getProduct());
        //this.wallet = new WalletDTO(exchange.getWallet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

}
