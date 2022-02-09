package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ExchangeDTO;
import com.mindhub.homebanking.dtos.ExchangesHistoryDTO;
import com.mindhub.homebanking.dtos.ProductDTO;
import com.mindhub.homebanking.dtos.WalletDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Exchange;
import com.mindhub.homebanking.models.Product;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.ExchangeRepository;
import com.mindhub.homebanking.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ExchangeController {

    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public ExchangeRepository exchangeRepository;

    @Autowired
    public ProductRepository productRepository;

    @RequestMapping(value = "/exchanges", method = RequestMethod.GET)
    public ExchangesHistoryDTO getHistory(Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        List<ExchangeDTO> exchangeList = exchangeRepository.findByWallet(client.getWallet()).stream().map(ExchangeDTO::new).collect(Collectors.toList());
        return new ExchangesHistoryDTO(new WalletDTO(client.getWallet()), exchangeList);


    }
    @Transactional
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Object> doExchange(Authentication authentication, @RequestBody List<Long> productIds){
        Client client = clientRepository.findByEmail(authentication.getName());

        //validando inputs
        if(productIds == null)
            return new ResponseEntity<>("Product list is null", HttpStatus.FORBIDDEN);
        if(productIds.size() < 1)
            return new ResponseEntity<>("Invalid Product list", HttpStatus.FORBIDDEN);

        for(Long productId: productIds){
            if(productRepository.findById(productId).orElse(null) == null)
                return new ResponseEntity<>("Invalid transaction: At least one product does not exist", HttpStatus.FORBIDDEN);
        }

        Double subtotal = 0D;
        for(Long productId: productIds){
            subtotal += productRepository.findById(productId).get().getValue();
        }
        if(-subtotal > client.getWallet().getPoints())
            return new ResponseEntity<>("Invalid transaction: The customer does not have enough funds", HttpStatus.FORBIDDEN);

        for(Long productId: productIds){
            Product p = productRepository.findById(productId).get();
            exchangeRepository.save(new Exchange(LocalDateTime.now(),
                    p.getValue(),
                    p,
                    client.getWallet()));
            client.getWallet().setPoints(client.getWallet().getPoints() + p.getValue());
        }

        return new ResponseEntity<>("Transaction Completed!", HttpStatus.ACCEPTED);

    }

}
