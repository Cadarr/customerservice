package de.thorstendiekhof.lex.customerservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@ControllerAdvice
public class VatIdNotVerifiedExceptionAdvice {

    @ExceptionHandler(VatIdNotVerifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(
            VatIdNotVerifiedException ex, WebRequest request) {

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("property", "vatId");
        errorDetails.put("message", ex.getMessage());
        messages.add(errorDetails);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("path", request.getDescription(false).substring(4));
        body.put("messages", messages);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}