package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public LoanRepository loanRepository;

    @Autowired
    public ClientLoanRepository clientLoanRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    public ExchangeRepository exchangeRepository;

    @Autowired
    public ProductRepository productRepository;

    @RequestMapping(value = "/loans", method = RequestMethod.GET)
    public List<LoanDTO> getLoans(Authentication authentication) {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplication) {

        //Validacion inputs
        if (loanApplication.getLoanId() == null || loanApplication.getLoanId() <= 0)
            return new ResponseEntity<>("Invalid LoanId", HttpStatus.FORBIDDEN);
        if (loanApplication.getPayments() <= 0)
            return new ResponseEntity<>("Invalid payment number", HttpStatus.FORBIDDEN);
        if (loanApplication.getAmount() == null || loanApplication.getAmount() <= 0)
            return new ResponseEntity<>("Invalid loan amount ", HttpStatus.FORBIDDEN);
        if (loanApplication.getToAccountNumber() == null || loanApplication.getToAccountNumber() == "")
            return new ResponseEntity<>("Invalid account number ", HttpStatus.FORBIDDEN);


        //Validacion de prestamo
        Loan loan = loanRepository.findById(loanApplication.getLoanId()).orElse(null);
        if (loan == null)
            return new ResponseEntity<>("Invalid Loan", HttpStatus.FORBIDDEN);

        //Validacion monto maximo
        if (loanApplication.getAmount() > loan.getMaxAmount())
            return new ResponseEntity<>("Invalid amount, provided amount exceeds the maximum", HttpStatus.FORBIDDEN);

        //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        boolean found = false;
        for (int payment : loan.getPayments()) {
            if (payment == loanApplication.getPayments()) {
                found = true;
                break;
            }
        }
        if (!found) {
            return new ResponseEntity<>("Invalid payment options, the selected payments are not available for this loan", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de destino exista
        Account destAccount = accountRepository.findByNumber(loanApplication.getToAccountNumber());
        if (destAccount == null)
            return new ResponseEntity<>("Invalid destination account", HttpStatus.FORBIDDEN);

        //Verificar que la cuenta de destino pertenezca al cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());
        if (destAccount.getClient() != client) {
            return new ResponseEntity<>("Invalid Owner, you're not the owner of the destination account", HttpStatus.FORBIDDEN);
        }



        //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        ClientLoan clientLoan = new ClientLoan(client, loan, loanApplication.getAmount() * 1.2, loanApplication.getPayments());
        clientLoanRepository.save(clientLoan);

        //Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la
        //descripción concatenando el nombre del préstamo y la frase “loan approved”
        Transaction transaction = new Transaction(TransactionType.CREDIT,loanApplication.getAmount(),loan.getName()+": loan approved", LocalDateTime.now(), destAccount);
        transactionRepository.save(transaction);

        //Se debe actualizar la cuenta de destino sumando el monto solicitado.
        destAccount.setBalance(destAccount.getBalance() + loanApplication.getAmount());


        // Asignandole puntos por solicitar nueva
        // ProductId=4 => Hipotecario
        if(loan.getName().equals("Prestamo Hipotecario")){
            Product product = productRepository.findById(4L).orElse(null);
            client.getWallet().setPoints(client.getWallet().getPoints() + product.getValue());
            exchangeRepository.save(new Exchange(LocalDateTime.now(), product.getValue(), product, client.getWallet()));
        }
        // ProductId=5 => Automotriz
        if(loan.getName().equals("Prestamo Automotriz")){
            Product product = productRepository.findById(5L).orElse(null);
            client.getWallet().setPoints(client.getWallet().getPoints() + product.getValue());
            exchangeRepository.save(new Exchange(LocalDateTime.now(), product.getValue(), product, client.getWallet()));
        }

        // ProductId=6 => Personal
        if(loan.getName().equals("Prestamo Personal")){
            Product product = productRepository.findById(6L).orElse(null);
            client.getWallet().setPoints(client.getWallet().getPoints() + product.getValue());
            exchangeRepository.save(new Exchange(LocalDateTime.now(), product.getValue(), product, client.getWallet()));
        }


        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

}
