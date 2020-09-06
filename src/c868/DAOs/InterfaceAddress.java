package c868.DAOs;

import c868.Models.Address;


public interface InterfaceAddress {
    public int addAddress(Address address);
    public Address getAddress(int addressId);
    public void updateAddress(Address address);
}
