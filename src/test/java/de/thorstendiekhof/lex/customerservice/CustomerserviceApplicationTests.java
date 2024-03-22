package de.thorstendiekhof.lex.customerservice;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.thorstendiekhof.lex.customerservice.error.VatIdNotVerifiedException;
import de.thorstendiekhof.lex.customerservice.model.Customer;
import de.thorstendiekhof.lex.customerservice.repository.CustomerRepository;
import de.thorstendiekhof.lex.customerservice.service.CustomerVatIdValidationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerserviceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private CustomerVatIdValidationService vatIdValidationService;

	@MockBean
	private CustomerRepository repository;

	@Test
	void getAllCustomers_returnsTheMockCustomers() throws JSONException {
		List<Customer> mockCustomers = Arrays.asList(
				new Customer("Max", "Mustermann", "Notizen", "DE123456789", "Adresszusatz", "Straße 1", "12345",
						"Stadt", "Land"),
				new Customer("Erika", "Musterfrau", "Notizen", "DE987654321", "Adresszusatz", "Straße 2", "67890",
						"Stadt", "Land"));

		Mockito.when(repository.findAll()).thenReturn(mockCustomers);

		ResponseEntity<Customer[]> response = restTemplate.getForEntity("http://localhost:" + port + "/customers",
				Customer[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<Customer> responseCustomers = Arrays.asList(response.getBody());
		assertThat(responseCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
				.containsAll(mockCustomers);
	}

	@Test
	void getCustomer_returnsNotFoundWhenCustomerDoesNotExist() {
		Long customerId = 2L;
		Mockito.when(repository.findById(customerId)).thenReturn(Optional.empty());

		ResponseEntity<String> response = restTemplate
				.getForEntity("http://localhost:" + port + "/customers/" + customerId, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void getCustomer_returnsCustomerDetailsWhenCustomerExists() {
		Customer mockCustomer = new Customer("Max", "Mustermann", "Notizen", "DE123456789", "Adresszusatz", "Straße 1",
				"12345", "Stadt", "Land");
		mockCustomer.setId(1L);

		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(mockCustomer));

		ResponseEntity<Customer> response = restTemplate.getForEntity("http://localhost:" + port + "/customers/1",
				Customer.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(mockCustomer);
	}

	@SuppressWarnings("null")
	@Test
	void newCustomer_CreatesCustomerSuccessfully() {
		Customer newCustomer = new Customer("Max", "Mustermann", "Notizen", "DE123456789", "Adresszusatz", "Straße 1",
				"12345", "Stadt", "Land");

		Mockito.when(repository.save(any(Customer.class))).thenReturn(newCustomer);

		ResponseEntity<Customer> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				newCustomer, Customer.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getFirstName()).isEqualTo(newCustomer.getFirstName());
	}

	@Test
	void newCustomer_ReturnsBadRequestWhenFirstNameValidationFails() {
		Customer invalidCustomer = new Customer("", "Klausner",
				"Eine Notiz",
				null, "Adresszusatz", "Straße 1", "12345", "Stadt", "Land");

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				invalidCustomer, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("The first name cannot be");
	}

	@Test
	void newCustomer_ReturnsBadRequestWhenLastNameValidationFails() {
		Customer invalidCustomer = new Customer("Klaus", "",
				"Eine Notiz",
				null, "Adresszusatz", "Straße 1", "12345", "Stadt", "Land");

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				invalidCustomer, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("The last name cannot be");
	}

	@Test
	void newCustomer_ReturnsBadRequestWhenNotesValidationFails() {
		Customer invalidCustomer = new Customer("Klaus", "Klausner",
				"Eine sehr lange Notiz, die sicherlich länger als einhundert Zeichen ist, um die Validierung für die Länge der Notizen zu testen.",
				null, "Adresszusatz", "Straße 1", "12345", "Stadt", "Land");

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				invalidCustomer, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("The notes can be a maximum of 100 characters long");
	}

	@Test
	void newCustomer_ReturnsBadRequestWhenVatIdIsInvalid() {
		Customer customerWithInvalidVatId = new Customer("Max", "Mustermann", "Notizen", "DE123FAKE", "Adresszusatz",
				"Straße 1", "12345", "Stadt", "Land");

		Mockito.doThrow(new VatIdNotVerifiedException("The VAT ID is invalid.")).when(vatIdValidationService)
				.validate(customerWithInvalidVatId);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				customerWithInvalidVatId, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("The VAT ID is invalid.");

	}

	@SuppressWarnings("null")
	@Test
	void updateCustomer_UpdatesCustomerSuccessfully() {
		Long customerId = 1L;
		Customer existingCustomer = new Customer("Vorname", "Nachname", "Notizen", "DE123456789", "Adresszusatz",
				"Straße 1", "12345", "Stadt", "Land");
		existingCustomer.setId(customerId);
		Customer updatedCustomer = new Customer("GeänderterVorname", "GeänderterNachname", "GeänderteNotizen",
				"DE987654321", "NeuerAdresszusatz", "NeueStraße 2", "67890", "NeueStadt", "NeuesLand");
		updatedCustomer.setId(customerId);

		Mockito.when(repository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
		Mockito.when(repository.save(any(Customer.class))).thenReturn(updatedCustomer);

		ResponseEntity<Customer> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				updatedCustomer, Customer.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isEqualTo(updatedCustomer);
	}

	@SuppressWarnings("null")
	@Test
	void updateCustomer_CreatesNewCustomerWhenIdDoesNotExist() {
		Long nonExistentId = 2L;
		Customer newCustomer = new Customer("NeuerVorname", "NeuerNachname", "Notizen", "DE987654321", "Adresszusatz",
				"Straße 1", "12345", "Stadt", "Land");

		Mockito.when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(any(Customer.class))).thenReturn(newCustomer);

		ResponseEntity<Customer> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				newCustomer, Customer.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isEqualTo(newCustomer);
	}

	@Test
	void updateCustomer_ReturnsBadRequestWhenValidationFails() {
		Customer invalidCustomer = new Customer("", "", "Eine sehr lange Notiz...", null, "Adresszusatz",
				"Straße 1", "12345", "Stadt", "Land");

		ResponseEntity<Customer> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				invalidCustomer, Customer.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void updateCustomer_ReturnsBadRequestWhenVatIdIsInvalid() {
		Customer customerWithInvalidVatId = new Customer("Vorname", "Nachname", "Notizen", "INVALIDVATID",
				"Adresszusatz", "Straße 1", "12345", "Stadt", "Land");

		Mockito.doThrow(new VatIdNotVerifiedException("The VAT ID is invalid.")).when(vatIdValidationService)
				.validate(customerWithInvalidVatId);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/customers",
				customerWithInvalidVatId, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void deleteCustomer_DeletesCustomerSuccessfully() {
		Long customerId = 1L;

		doNothing().when(repository).deleteById(customerId);

		ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/customers/" + customerId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(repository, times(1)).deleteById(customerId);
	}

	@Test
	void deleteCustomer_HandlesNonExistentCustomerGracefully() {
		Long nonExistentCustomerId = 999L;
		
		doNothing().when(repository).deleteById(nonExistentCustomerId);
		
		ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/customers/" + nonExistentCustomerId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(repository, times(1)).deleteById(nonExistentCustomerId);
	}

}
