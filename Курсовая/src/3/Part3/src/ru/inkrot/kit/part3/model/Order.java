package ru.inkrot.kit.part3.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Order extends DataBaseEntity {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy в HH:mm");

    private String customer, customerTel;
    private Timestamp timestamp;
    private int price;
    private List<Phone> phones = new ArrayList<>();

    public Order(int id, String customer, String customerTel, int price, Timestamp timestamp) {
        super(id);
        this.customer = customer;
        this.customerTel = customerTel;
        this.price = price;
        this.timestamp = timestamp;
        ResultSet rs = DataBase.getPhones(id);
        try {
            while(rs.next())
                this.phones.add(
                        new Phone(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("price")
                        )
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        if (timestamp == null) return "";
        return dateFormat.format(timestamp);
    }

    public List<Phone> getPhones() {
        return phones;
    }
}
