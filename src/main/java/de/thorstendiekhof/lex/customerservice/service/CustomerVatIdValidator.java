package de.thorstendiekhof.lex.customerservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.thorstendiekhof.lex.customerservice.model.Customer;

@Component
public class CustomerVatIdValidator implements Validator {
    private static final Logger log = LoggerFactory.getLogger(CustomerVatIdValidator.class);

    @Value("${vatid.verification.api.url}")
    private String vatidApiUrlTemplate;

    private RestTemplate restTemplate = new RestTemplate();
    
    @SuppressWarnings("null")
    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @SuppressWarnings("null")
    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;
        if(customer.getVatId() == null || customer.getVatId().length() == 0 ) return;
        try {
            String vatId = customer.getVatId();
            String countryCode = vatId.substring(0, 2);
            String vatNumber = vatId.substring(2);
    
            String url = vatidApiUrlTemplate
                    .replace("{countryCode}", countryCode)
                    .replace("{vatNumber}", vatNumber);

            log.info("Validate the VAT ID with the GET request : " + url);
            ResponseEntity<VatResponse> response = restTemplate.getForEntity(url, VatResponse.class);

            VatResponse vatResponse = response.getBody();

            if (vatResponse != null && !vatResponse.isValid()) {
                errors.rejectValue("vatId", "VatId.validation.error", "The VAT ID is invalid.");
            }
        } catch (Exception e) {
            errors.rejectValue("vatId", "VatId.validation.error", "The VAT ID could not be verified.");
        }
    }

    public static class VatResponse {
        private boolean isValid;

        public boolean isValid() {
            return isValid;
        }

        public void setIsValid(boolean isValid) {
            this.isValid = isValid;
        }
    }
}
