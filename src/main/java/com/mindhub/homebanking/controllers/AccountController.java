package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Exchange;
import com.mindhub.homebanking.models.Product;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.ExchangeRepository;
import com.mindhub.homebanking.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @GetMapping(value = "/accounts", produces = "application/hal+json")
    public List<AccountDTO> getAccounts() {
        //return this.accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication) {

        Client client = this.clientRepository.findByEmail(authentication.getName());
        Set<Account> accounts = client.getAccounts();

        Iterator iter = accounts.iterator();
        while (iter.hasNext()) {
            Account account = (Account) iter.next();
            if (account.getId() == id) {
                return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
            }
        }
        return null;
    }

    @GetMapping( value = "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() < 3) {
            String randomVin;
            do {
                randomVin = "VIN";
                for (int i = 0; i < 8; i++) {
                    randomVin += String.valueOf(0 + (int) (Math.random() * ((9 - 0) + 1)));
                }
                if (this.accountRepository.findByNumber(randomVin) == null) {
                    this.accountRepository.save(new Account(randomVin, LocalDateTime.now(), 0, client));

                    // Asignandole puntos
                    // ProductId=3 => crear nueva tarjeta
                    Product product = productRepository.findById(1L).orElse(null);
                    client.getWallet().setPoints(client.getWallet().getPoints() + product.getValue());
                    exchangeRepository.save(new Exchange(LocalDateTime.now(), product.getValue(), product, client.getWallet()));

                    //return 201
                    return new ResponseEntity<>("Account created successfully!",HttpStatus.CREATED);
                }
            }while(this.accountRepository.findByNumber(randomVin) != null);

        }
        //return 403 forbidden
        return new ResponseEntity<>("Error creating an account: Forbidden Request", HttpStatus.FORBIDDEN);
    }

    @GetMapping( value = "/clients/current/accountss")
    public List<AccountDTO> getAccounts(Authentication authentication) {
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        //return currentClient.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        return  currentClient.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }
}