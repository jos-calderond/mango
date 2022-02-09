package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    public ExchangeRepository exchangeRepository;

    @Autowired
    public ProductRepository productRepository;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Object> doTransaction(@RequestParam String fromAccountNumber,
                                                @RequestParam String toAccountNumber,
                                                @RequestParam String description,
                                                @RequestParam Long amount,
                                                Authentication authentication){

        //Verificando parametros vacios
        if(fromAccountNumber == null)
            return new ResponseEntity<>("Invalid transaction source account!",HttpStatus.FORBIDDEN);
        if(toAccountNumber == null)
            return new ResponseEntity<>("Invalid transaction destination account!",HttpStatus.FORBIDDEN);
        if(description == null)
            return new ResponseEntity<>("Invalid transaction description!",HttpStatus.FORBIDDEN);
        if(amount == null || amount <= 0)
            return new ResponseEntity<>("Invalid transaction amount!",HttpStatus.FORBIDDEN);


        //verificando si las cuentas existen
        if(accountRepository.findByNumber(fromAccountNumber) == null)
            return new ResponseEntity<>("Source account does not exist!",HttpStatus.FORBIDDEN);
        if(accountRepository.findByNumber(toAccountNumber) == null)
            return new ResponseEntity<>("Destination account does not exist!",HttpStatus.FORBIDDEN);

        //verificando si las cuentas de origen y destino son iguales
        if(fromAccountNumber.equals(toAccountNumber))
            return new ResponseEntity<>("Source and destination accounts are the same!",HttpStatus.FORBIDDEN);

        //verificando que la cuenta de origen pertenezca al usuario
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Account> accounts = client.getAccounts();
        Iterator iter = accounts.iterator();
        Boolean found = false;
        Account srcAccount = null;
        while(iter.hasNext()){
            Account account = (Account) iter.next();
            if(account.getNumber().equals(fromAccountNumber)) {
                found = true;
                //obtenemos la cuenta de origen
                srcAccount = account;
                break;
            }
        }
        if(!found)
            return new ResponseEntity<>("Authenticated user does not own the source account!",HttpStatus.FORBIDDEN);

        //Verificando si la cuenta de origen tiene saldo para realizar la transaccion
        if(srcAccount.getBalance() < amount)
            return new ResponseEntity<>("Insufficient funds, ilegal operation!",HttpStatus.FORBIDDEN);

        //Generando la transaccion
        Account destAccount = accountRepository.findByNumber(toAccountNumber);

        Transaction srcTransaction = new Transaction(TransactionType.DEBIT, -amount, description, LocalDateTime.now(), srcAccount);
        Transaction destTransaction = new Transaction(TransactionType.CREDIT,amount, description, LocalDateTime.now(), destAccount);
        transactionRepository.save(srcTransaction);
        transactionRepository.save(destTransaction);

        srcAccount.setBalance(srcAccount.getBalance() - amount);
        destAccount.setBalance(destAccount.getBalance() + amount);

        //No es necesario explicitar el guardado, ya que Spring transactional lo realiza de forma automatica cuando recuperamos una entidad de base de datos con readOnly = false
        //accountRepository.save(srcAccount);
        //accountRepository.save(destAccount);

        //asignar puntos premio por la transaccion

        //buscamos el producto relacionado con transaccion (productoid=2 => Puntos por transaccion) y sumamos los puntos correspondientes
        Product product = productRepository.findById(2L).orElse(null);
        client.getWallet().setPoints(client.getWallet().getPoints() + product.getValue());

        //agregamos el correspondiente exchange
        exchangeRepository.save(new Exchange(LocalDateTime.now(), product.getValue(), product, client.getWallet()));


        return new ResponseEntity<>("Operation completed successfully", HttpStatus.CREATED);
    }
}
