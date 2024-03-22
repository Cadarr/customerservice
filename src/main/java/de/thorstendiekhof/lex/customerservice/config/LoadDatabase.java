package de.thorstendiekhof.lex.customerservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.thorstendiekhof.lex.customerservice.model.Customer;
import de.thorstendiekhof.lex.customerservice.repository.CustomerRepository;

@Configuration
public class LoadDatabase {

        private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

        @Bean
        CommandLineRunner initDatabase(CustomerRepository repository) {
                return args -> {
                        Customer customerA = new Customer("Max", "Mustermann", "Max ist ein Beispielkunde.",
                                        "DE123456789",
                                        "HH 2. Stock", "Musterstraße 1", "12345", "Musterstadt", "Deutschland");
                        Customer customerB = new Customer("Maria", "Müller", "Maria mag Musik.", "DE987654321", null,
                                        "Beispielallee 2", "23456", "Beispielstadt", "Deutschland");
                        Customer customerC = new Customer("Johannes", "Jäger", "Johannes ist Jäger.", "DE123456798",
                                        "Hinterm Hochsitz", "Jägerweg 3", "34567", "Jägerstadt", "Deutschland");
                        Customer customerD = new Customer("Anna", "Alpen", "Anna liebt die Berge.", "ATU12345678", null,
                                        "Alpenstraße 4", "45678", "Alpenstadt", "Österreich");
                        Customer customerE = new Customer("Max", "Mustermann", "Max ist ein begeisterter Wanderer.",
                                        "ATU99999999", null,
                                        "Wanderweg 1", "12345", "Bergdorf", "Österreich");

                        Customer customerF = new Customer("Julia", "Jung", "Julia liebt Klettertouren.", "ATU88888888",
                                        null,
                                        "Kletterpfad 2", "23456", "Felsental", "Österreich");

                        Customer customerG = new Customer("Simon", "Sonnig", "Simon ist ein Ski-Enthusiast.",
                                        "ATU77777777", null,
                                        "Schneegasse 3", "34567", "Winterberg", "Österreich");

                        log.info("Preloading " + repository.save(customerA));
                        log.info("Preloading " + repository.save(customerB));
                        log.info("Preloading " + repository.save(customerC));
                        log.info("Preloading " + repository.save(customerD));
                        log.info("Preloading " + repository.save(customerE));
                        log.info("Preloading " + repository.save(customerF));
                        log.info("Preloading " + repository.save(customerG));

                };
        }

}
