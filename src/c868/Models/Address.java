package c868.Models;

import c868.Exceptions.AddressException;
import c868.Exceptions.CityException;

public class Address {
    public int addressId;
    public String address1;
    public String address2;
    public City city;
    public String postalCode;
    public String phone;

    
    // Constructor
    public Address() {
        seedAddress();
    }
    
    
    /* Getters */
    public int getAddressId() {
        return this.addressId;
    }
    public String getAddress1() {
        return this.address1;
    }
    public String getAddress2() {
        return this.address2;
    }
    public City getCity() {
        return this.city;
    }
    public String getPhone() {
        return this.phone;
    }
    public String getPostalCode() {
        return this.postalCode;
    }
    
    
    /* Setters */
    public void setAddressId (int addressId) {
        this.addressId = addressId;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public void setCity (City city) {
        this.city = city;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setPostalCode (String postalCode) {
        this.postalCode = postalCode;
    }
    
    
    @Override
    public String toString() {
        String fullAddress = String.join(" ",
            getAddress1(),
            getAddress2(),
            getCity().getCityName(),
            getPostalCode()
        );

        return fullAddress;
    }
    
    private void seedAddress() {
        setAddress1("");
        setAddress2("");
        setPostalCode("");
        setPhone("");
    }
    
    
    // Validation
    public boolean validate() throws AddressException {
        if (this.address1.equals("")) {
            throw new AddressException("Please enter an address.");
        }
        if (this.postalCode.equals("")) {
            throw new AddressException("Please enter a postal code.");
        }
        if (this.phone.equals("")) {
            throw new AddressException("Please enter a phone number.");
        }
        try {
            this.city.validate();
        }
        catch (CityException e) {
            throw new AddressException(e.getMessage());
        }
        return true;
    }
}
