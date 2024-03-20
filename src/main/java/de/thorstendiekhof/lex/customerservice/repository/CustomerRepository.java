package de.thorstendiekhof.lex.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thorstendiekhof.lex.customerservice.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
