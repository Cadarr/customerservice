package de.thorstendiekhof.lex.customerservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class Customer {

    private @Id @GeneratedValue Long id;

    @NotNull(message = "The first name cannot be null")
    @NotEmpty(message = "The first name cannot be empty")
    private String firstName;

    @NotNull(message = "The last name cannot be null")
    @NotEmpty(message = "The last name cannot be empty")
    private String lastName;

    @Size(max = 100, message = "The notes can be a maximum of 100 characters long")
    private String notes;
    private String vatId;
    private String addressAddition;
    private String streetAndNumber;
    private String postalCode;
    private String city;
    private String country;

    Customer() {
    }

    Customer(String fistName, String lastName) {
        this.firstName = fistName;
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName, String notes, String vatId, String addressAddition,
            String streetAndNumber, String postalCode, String city, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.notes = notes;
        this.vatId = vatId;
        this.addressAddition = addressAddition;
        this.streetAndNumber = streetAndNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVatId() {
        return vatId;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }

    public String getAddressAddition() {
        return addressAddition;
    }

    public void setAddressAddition(String addressAddition) {
        this.addressAddition = addressAddition;
    }

    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public void setStreetAndNumber(String streetAndNumber) {
        this.streetAndNumber = streetAndNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Customer))
            return false;
        Customer customer = (Customer) o;
        return Objects.equals(this.id, customer.id) &&
                Objects.equals(this.firstName, customer.firstName) &&
                Objects.equals(this.lastName, customer.lastName) &&
                Objects.equals(this.notes, customer.notes) &&
                Objects.equals(this.vatId, customer.vatId) &&
                Objects.equals(this.addressAddition, customer.addressAddition) &&
                Objects.equals(this.streetAndNumber, customer.streetAndNumber) &&
                Objects.equals(this.postalCode, customer.postalCode) &&
                Objects.equals(this.city, customer.city) &&
                Objects.equals(this.country, customer.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.notes, this.vatId, this.addressAddition,
                this.streetAndNumber, this.postalCode, this.city, this.country);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", vatId='" + vatId + '\'' +
                ", streetAndNumber='" + streetAndNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}
