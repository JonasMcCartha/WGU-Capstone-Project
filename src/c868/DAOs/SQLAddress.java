package c868.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import c868.Main.MainApp;
import c868.Models.Address;


public class SQLAddress implements InterfaceAddress {
    private final static Connection conn = MainApp.conn;
    
    
    // Constructor
    public SQLAddress() {
    }
    
    
    // Retrieve highest primary key value from table.
    private int getMaxId() {
        int maxID = 0;
        String maxIdQuery = "SELECT MAX(addressId) FROM address";
        
        try {
            Statement statement = conn.createStatement(); 
            ResultSet result = statement.executeQuery(maxIdQuery);
            if(result.next()) {
                maxID = result.getInt(1);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return maxID + 1;
    }
    
    
    // Add new address.
    @Override
    public int addAddress(Address address) {
        String addAddressQuery = String.join(" ", 
            "INSERT INTO address (addressId,  address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)",
            "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)"
        );
    
        int addressId = getMaxId();
        try {
            PreparedStatement statement = conn.prepareStatement(addAddressQuery);
            statement.setInt(1, addressId);
            statement.setString(2, address.getAddress1());
            statement.setString(3, address.getAddress2());
            statement.setInt(4, address.getCity().getCityId());
            statement.setString(5, address.getPostalCode());
            statement.setString(6, address.getPhone());
            statement.setString(7, MainApp.user.username);
            statement.setString(8, MainApp.user.username);
            
            statement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return addressId;
    }

    
    // Look up address by ID.
    @Override
    public Address getAddress(int addressId) {
        String getAddressQuery = "SELECT * FROM address WHERE addressId = ?"; 
        Address fetchedAddress = new Address();

        try{
            PreparedStatement statement = conn.prepareStatement(getAddressQuery);
            statement.setInt(1, addressId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                InterfaceCity city = new SQLCity();
                fetchedAddress.setAddressId(resultSet.getInt("addressId"));
                fetchedAddress.setAddress1(resultSet.getString("address"));
                fetchedAddress.setAddress2(resultSet.getString("address2"));
                fetchedAddress.setCity(city.getCity(resultSet.getInt("cityId")));
                fetchedAddress.setPostalCode(resultSet.getString("postalCode"));
                fetchedAddress.setPhone(resultSet.getString("phone"));
            } else {
                return null;
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return fetchedAddress;
    }
    
    
    // Update an address.
    @Override
    public void updateAddress(Address address) {
        String updateAddressQuery = String.join(" ",
            "UPDATE address",
            "SET address=?, address2=?, cityId=?, postalCode=?, phone=?, lastUpdate=NOW(), lastUpdateBy=?",
            "WHERE addressId = ?"
        );
    
        try {
            PreparedStatement statement = conn.prepareStatement(updateAddressQuery);
            statement.setString(1, address.getAddress1());
            statement.setString(2, address.getAddress2());
            statement.setInt(3, address.getCity().getCityId());
            statement.setString(4, address.getPostalCode());
            statement.setString(5, address.getPhone());
            statement.setString(6, MainApp.user.username);
            statement.setInt(7, address.getAddressId());
            statement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

