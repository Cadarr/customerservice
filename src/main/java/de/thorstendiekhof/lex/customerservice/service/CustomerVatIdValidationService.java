package de.thorstendiekhof.lex.customerservice.service;

import de.thorstendiekhof.lex.customerservice.model.Customer;

public interface CustomerVatIdValidationService {
    void validate(Customer customer);
}
