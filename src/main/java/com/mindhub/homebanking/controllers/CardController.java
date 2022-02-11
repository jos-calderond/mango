package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    public ExchangeRepository exchangeRepository;

    @Autowired
    public ProductRepository productRepository;

    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam String cardType, @RequestParam String cardColor, Authentication authentication) {
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if (client.getCards().stream().filter(card -> card.getType() == CardType.valueOf(cardType)).count() < 3) {
            String randomNumber = "";

            //Random Card Number
            for (int i = 0; i < 16; i++) {
                randomNumber += String.valueOf(0 + (int) (Math.random() * ((9 - 0) + 1)));
            }

            randomNumber = randomNumber.substring(0, 4) + "-" + randomNumber.substring(4, 8) + "-" + randomNumber.substring(8, 12) + "-" + randomNumber.substring(12, 16);

            Card newCard = new Card(client.getFirstName() + " " + client.getLastName(),
                    CardType.valueOf(cardType),
                    CardColor.valueOf(cardColor),
                    randomNumber,
                    (100 + (int) (Math.random() * ((999 - 100) + 1))),
                    LocalDateTime.now().plusYears(5),
                    LocalDateTime.now(),
                    client
            );
            cardRepository.save(newCard);

            // Asignandole puntos
            // ProductId=3 => crear nueva tarjeta
            Product product = productRepository.findById(3L).orElse(null);
            client.getWallet().setPoints(client.getWallet().getPoints() + product.getValue());
            exchangeRepository.save(new Exchange(LocalDateTime.now(), product.getValue(), product, client.getWallet()));

            // return Response 201
            return new ResponseEntity<>("New " + cardType.toLowerCase() + " card created successfully!", HttpStatus.CREATED);
        } else {
            // return Response 403
            return new ResponseEntity<>("Maximum " + cardType.toLowerCase() + " cards reached!", HttpStatus.FORBIDDEN);
        }
    }
}
