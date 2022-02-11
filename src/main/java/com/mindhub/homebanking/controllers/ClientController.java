package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/clients")
    public List<ClientDTO> getClients() {
        return this.clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @GetMapping( value = "/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return this.clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }

    @PostMapping( value = "/clients")
    public ResponseEntity<Object> createClient(@RequestParam String firstName,
                                               @RequestParam String lastName,
                                               @RequestParam String email,
                                               @RequestParam String password) {

        //Validando cliente nuevo
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client nuevoCliente = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(nuevoCliente);

        //Creando cuenta nueva para Cliente nuevo
        String randomVin;
        do {
            randomVin = "VIN";
            for (int i = 0; i < 8; i++) {
                randomVin += String.valueOf(0 + (int) (Math.random() * ((9 - 0) + 1)));
            }
            // Si el VIN no existe en la BD
            if (this.accountRepository.findByNumber(randomVin) == null) {
                this.accountRepository.save(new Account(randomVin, LocalDateTime.now(), 0, nuevoCliente));

                //Agregando wallet a cliente nuevo
                Wallet wallet = new Wallet(0L,nuevoCliente);
                walletRepository.save(wallet);

                //Registrando los puntos. Productid=1 => Puntos por Cuenta nueva
                Product product = productRepository.findById(1L).orElse(null);
                wallet.setPoints(wallet.getPoints() + product.getValue());
                exchangeRepository.save(new Exchange(LocalDateTime.now(), product.getValue(), product, wallet));

                //return 201 OK
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }while(this.accountRepository.findByNumber(randomVin) != null);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/clients/current")
    public ClientDTO getCurrentUser(Authentication authentication) {
        return new ClientDTO(this.clientRepository.findByEmail(authentication.getName()));
    }
}
