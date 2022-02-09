package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {


    @Autowired
    public PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }


    // Adds clients in runtime memory, should be run once when the database is empty

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository,
                                      WalletRepository walletRepository,
                                      ProductRepository productRepository,
                                      ExchangeRepository exchangeRepository) {
        return (args) -> {
            // save a couple of customers
            //Cliente 1
            Client client1 = new Client("Jack", "Bauer", "jbauer@mindhub.com", passwordEncoder.encode("jack"));
            clientRepository.save(client1);

            Account c1a1 = new Account("VIN001", LocalDateTime.now(), 5000, client1);
            Account c1a2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, client1);
            accountRepository.save(c1a1);
            accountRepository.save(c1a2);

            Transaction t1 = new Transaction(TransactionType.CREDIT, 400, "pago por servicios", LocalDateTime.now(), c1a1);
            Transaction t2 = new Transaction(TransactionType.DEBIT, -130, "Pago cuenta de la Agua", LocalDateTime.now(), c1a1);
            Transaction t3 = new Transaction(TransactionType.DEBIT, -300, "Pago cuenta de la Television", LocalDateTime.now(), c1a2);
            Transaction t4 = new Transaction(TransactionType.DEBIT, -250, "Pago cuenta de la Internet", LocalDateTime.now(), c1a2);
            Transaction t5 = new Transaction(TransactionType.CREDIT, 600, "Pago por servicios", LocalDateTime.now(), c1a2);
            transactionRepository.save(t1);
            transactionRepository.save(t2);
            transactionRepository.save(t3);
            transactionRepository.save(t4);
            transactionRepository.save(t5);

            Card cc1 = new Card(client1.getFirstName() + " " + client1.getLastName(),
                    CardType.DEBIT,
                    CardColor.GOLD,
                    "3654-7263-0054-3525",
                    582,
                    LocalDateTime.now().plusYears(5).plusMonths(10),
                    LocalDateTime.now(),
                    client1);

            Card cc2 = new Card(client1.getFirstName() + " " + client1.getLastName(),
                    CardType.CREDIT,
                    CardColor.TITANIUM,
                    "7263-3525-3654-0054",
                    117,
                    LocalDateTime.now().plusYears(5),
                    LocalDateTime.now(),
                    client1);

            cardRepository.save(cc1);
            cardRepository.save(cc2);

            //Cliente 2
            Client client2 = new Client("Chloe", "O'Brian", "cobrian@mindhub.com", passwordEncoder.encode("chloe"));
            clientRepository.save(client2);
            walletRepository.save(new Wallet(0L, client2));

            Account c2a1 = new Account("VIN003", LocalDateTime.now(), 15000, client2);
            Account c2a2 = new Account("VIN004", LocalDateTime.now().plusWeeks(1), 6500, client2);
            Account c2a3 = new Account("VIN005", LocalDateTime.now().minusHours(8), 3400, client2);
            accountRepository.save(c2a1);
            accountRepository.save(c2a2);
            accountRepository.save(c2a3);

            Transaction t6 = new Transaction(TransactionType.CREDIT, 400, "Ventas Facebook", LocalDateTime.now(), c2a1);
            Transaction t7 = new Transaction(TransactionType.CREDIT, 400, "ventas linio", LocalDateTime.now(), c2a2);
            Transaction t8 = new Transaction(TransactionType.DEBIT, -400, "Mecanico", LocalDateTime.now(), c2a3);
            transactionRepository.save(t6);
            transactionRepository.save(t7);
            transactionRepository.save(t8);

            Card cc3 = new Card(client2.getFirstName() + " " + client2.getLastName(),
                    CardType.CREDIT,
                    CardColor.SILVER,
                    "3263-0025-3004-0069",
                    992,
                    LocalDateTime.now().plusYears(5).plusDays(13),
                    LocalDateTime.now(),
                    client2);
            cardRepository.save(cc3);


            //Cliente 3
            Client client3 = new Client("Kim", "Bauer", "kbauer@mindhub.com", passwordEncoder.encode("kim"));
            clientRepository.save(client3);
            walletRepository.save(new Wallet(0L, client3));
            Account c3a1 = new Account("VIN006", LocalDateTime.now(), 4000, client3);
            accountRepository.save(c3a1);
            Transaction t9 = new Transaction(TransactionType.DEBIT, -100, "Mensualidad GYM", LocalDateTime.now(), c3a1);
            Transaction t10 = new Transaction(TransactionType.DEBIT, -700, "Supermercado", LocalDateTime.now(), c3a1);
            transactionRepository.save(t9);
            transactionRepository.save(t10);

            //Loans

            Loan loan1 = new Loan("Prestamo Hipotecario", 500000, Arrays.asList(12, 24, 36, 48, 60));
            Loan loan2 = new Loan("Prestamo Personal", 100000, Arrays.asList(6, 12, 24));
            Loan loan3 = new Loan("Prestamo Automotriz", 300000, Arrays.asList(6, 12, 24, 36));
            loanRepository.save(loan1);
            loanRepository.save(loan2);
            loanRepository.save(loan3);

            ClientLoan c1l1 = new ClientLoan(client1, loan1, 400000, 60);
            ClientLoan c1l2 = new ClientLoan(client1, loan2, 50000, 12);

            ClientLoan c2l2 = new ClientLoan(client2, loan2, 100000, 24);
            ClientLoan c2l3 = new ClientLoan(client2, loan3, 200000, 36);

            clientLoanRepository.save(c1l1);
            clientLoanRepository.save(c1l2);
            clientLoanRepository.save(c2l2);
            clientLoanRepository.save(c2l3);

            //Clientes sin cuenta aun
            Client client4 = new Client("David", "Palmer", "dpalmer@mindhub.com", passwordEncoder.encode("david"));
            clientRepository.save(client4);
            walletRepository.save(new Wallet(0L, client4));

            Client client5 = new Client("Michelle", "Dessler", "mdessler@mindhub.com", passwordEncoder.encode("michelle"));
            clientRepository.save(client5);
            walletRepository.save(new Wallet(0L, client5));

            //Cuenta Admin
            Client admin = new Client("Admin", "", "admin@admin.com", passwordEncoder.encode("admin"));
            clientRepository.save(admin);
            walletRepository.save(new Wallet(0L, admin));


            Wallet w1 = new Wallet(0L, client1);
            walletRepository.save(w1);


            //Productos operacionales
            productRepository.save(new Product("Puntos", "Obtenidos por la creación de una cuenta nueva",40, ProductType.OPERATIONAL));
            productRepository.save(new Product("Puntos", "Obtenidos por transferencia",5, ProductType.OPERATIONAL));
            productRepository.save(new Product("Puntos", "Obtenidos por tarjeta nueva",50, ProductType.OPERATIONAL));
            productRepository.save(new Product("Puntos", "Obtenidos por préstamo hipotecario",100, ProductType.OPERATIONAL));
            productRepository.save(new Product("Puntos", "Obtenidos por préstamo automotriz",80, ProductType.OPERATIONAL));
            productRepository.save(new Product("Puntos", "Obtenidos por préstamo personal",30, ProductType.OPERATIONAL));


            //PRODUCTOS CANJEABLES
            Product prod1 = new Product("Televisor", "LED 43\" 43E5610 Full HD Android Smart TV 2020/21", -500, ProductType.TECHNOLOGY);
            Product prod2 = new Product("Celular", "Smartphone 5G\" 32GB Almacenamiento, 3 Camaras con estabilizador y compresion H.264", -650, ProductType.TECHNOLOGY);
            Product prod3 = new Product("Webcam", "Mangotech\" Full HD HDR ideal para reuniones y streamers.", -200, ProductType.TECHNOLOGY);
            Product prod4 = new Product("Smart Kitchen", "Cocina Inteligente\" 10.000 recetas, bajo consumo A++ y 7.5L de capacidad.", -300, ProductType.TECHNOLOGY);
            Product prod5 = new Product("Cargador Solar", "Energia Solar 25W\" Multriple entrada micro usb, usb C y apple conections.", -100, ProductType.TECHNOLOGY);
            Product prod6 = new Product("Parlante Karaoke", "Wireless Microphone\" Rango de autonomia de 15M, eficiencia energetica A+, coneccion bluetooth.", -150, ProductType.TECHNOLOGY);
            productRepository.save(prod1);
            productRepository.save(prod2);
            productRepository.save(prod3);
            productRepository.save(prod4);
            productRepository.save(prod5);
            productRepository.save(prod6);

            Product prod7 = new Product("Salto del Laja", "2 dias y una noche en Salto del Laja y alrededores.", -1000, ProductType.TRAVEL);
            Product prod8 = new Product("Laguna San Rafael", "3 dias y 2 noches en crucero por laguna. Incluye traslado desde Santiago.", -3500, ProductType.TRAVEL);
            Product prod9 = new Product("San Pedro de Atacama", "3 dias y 2 noches en San Pedro de Atacama. Incluye traslado desde Santiago.", -3500, ProductType.TRAVEL);
            Product prod10 = new Product("Bahia Inglesa", "Vuelo express a playa nortina, día completo con desayuno, almuerzo y cena en Lounge.", -1500, ProductType.TRAVEL);
            Product prod11 = new Product("Buenos Aires", "5 dias y 4 noches al destino Rioplatence.", -3000, ProductType.TRAVEL);
            Product prod12 = new Product("Machu Pichu", "Paquete Full-Experience a la maravilla Peruana.", -5000, ProductType.TRAVEL);
            productRepository.save(prod7);
            productRepository.save(prod8);
            productRepository.save(prod9);
            productRepository.save(prod10);
            productRepository.save(prod11);
            productRepository.save(prod12);

            Product prod13 = new Product("24 donuts in Kingdun Donuts", " 24 deliciosas donas surtidas, con glaseado, relleno y diversidad de tops", -25, ProductType.DISCOUNTS);
            Product prod14 = new Product("Free full combo in Mr Donals", "Cupon para canje de un combo full por cualquier compra", -20, ProductType.DISCOUNTS);
            Product prod15 = new Product("50% OFF in tickets in CineStar", "Llavate tus tickets a mitad de precio! Valido para 4 boletos.", -40, ProductType.DISCOUNTS);
            Product prod16 = new Product("25% OFF in GoKart in Patoland", "Descuento para tu experiencia en karting en Patoland", -60, ProductType.DISCOUNTS);
            Product prod17 = new Product("10% OFF in Optica GoodLook", "Descuento en atenciones o compras en optica GoodLook", -50, ProductType.DISCOUNTS);
            Product prod18 = new Product("Free enrollment in Smart Squad", "Matricula gratuita en inscripcion a gimnasio.", -65, ProductType.DISCOUNTS);
            productRepository.save(prod13);
            productRepository.save(prod14);
            productRepository.save(prod15);
            productRepository.save(prod16);
            productRepository.save(prod17);
            productRepository.save(prod18);

            Exchange exchange = new Exchange(LocalDateTime.now(), prod1.getValue(), prod1, w1);
            exchangeRepository.save(exchange);
        };
    }
}
