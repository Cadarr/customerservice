package de.thorstendiekhof.lex.customerservice.error;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id){
        super("Could not find customer " + id);
    }
    
}
