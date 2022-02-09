package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Exchange;
import com.mindhub.homebanking.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    public List<Exchange> findByWallet(Wallet wallet);
}
