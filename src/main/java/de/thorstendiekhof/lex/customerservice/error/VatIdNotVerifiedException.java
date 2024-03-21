package de.thorstendiekhof.lex.customerservice.error;

public class VatIdNotVerifiedException extends RuntimeException {

    public VatIdNotVerifiedException(String errorMessage){
        super(errorMessage );
    }
    
}
