package de.thorstendiekhof.lex.customerservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.thorstendiekhof.lex.customerservice.error.CustomerNotFoundException;
import de.thorstendiekhof.lex.customerservice.model.Customer;
import de.thorstendiekhof.lex.customerservice.repository.CustomerRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
public class CustomerController {

    private final CustomerRepository repository;

    CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    List<Customer> getCustomers() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    @PostMapping("/customers")
    Customer newCustomer(@RequestBody Customer newCustomer) {
        return repository.save(newCustomer);
    }
    
    @SuppressWarnings("null")
    @GetMapping("/customers/{id}")
    Customer getCustomer(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @SuppressWarnings("null")
    @PutMapping("/customers/{id}")
    Customer updateCustomer(@PathVariable Long id, @RequestBody Customer newCustomer) {        
        return repository.findById(id).map(customer -> {
            customer.setFirstName(newCustomer.getFirstName());
            customer.setLastName(newCustomer.getLastName());
            customer.setNotes(newCustomer.getNotes());
            customer.setVatId(newCustomer.getVatId());
            customer.setAddressAddition(newCustomer.getAddressAddition());
            customer.setStreetAndNumber(newCustomer.getStreetAndNumber());
            customer.setPostalCode(newCustomer.getPostalCode());
            customer.setCity(newCustomer.getCity());
            customer.setCountry(newCustomer.getCountry());
            return repository.save(customer);
        }).orElseGet(() -> {
            return repository.save(newCustomer);
        });
    }

    @SuppressWarnings("null")
    @DeleteMapping("/customers/{id}")
    void deleteCustomers(@PathVariable Long id){
        repository.deleteById(id);
    }
}
