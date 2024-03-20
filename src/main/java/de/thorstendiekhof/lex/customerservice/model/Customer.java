package de.thorstendiekhof.lex.customerservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import jakarta.persistence.Column;

@Entity
public class Customer {

    private @Id @GeneratedValue Long id;
    private @Column(nullable = false) String firstName;
    private @Column(nullable = false) String lastName;
    private @Column(length = 100) String notes;
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
