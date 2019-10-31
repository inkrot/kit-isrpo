package ru.inkrot.kit.part3.model;

import java.sql.*;
import java.util.List;

public class DataBase {

    public static Connection connection;

    public static boolean connect() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:" + Configuration.DB_FILE_NAME;
        connection = DriverManager.getConnection(url);
        return true;
    }

    private static ResultSet selectTable(String table, String selector) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT " + selector + " FROM " + table);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ResultSet selectTable(String table) {
        return selectTable(table, "*");
    }

    public static ResultSet getOrders() {
        return selectTable("orders", "id, customer, customer_tel, COALESCE((SELECT SUM(p.price) FROM phones p WHERE p.id IN (SELECT phone_id FROM orders_phones WHERE order_id=orders.id)), 0) AS price, timestamp");
    }

    public static ResultSet getPhones() {
        return selectTable("phones");
    }

    public static ResultSet getPhones(int orderId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM phones WHERE id IN (SELECT phone_id FROM orders_phones WHERE order_id = " + orderId + ")");
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertOrder(String customer, String customerTel, List<Phone> phones) {
        try {
            connection.setAutoCommit(false);

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO orders(customer, customer_tel, timestamp) VALUES(?, ?, ?)");
            stmt.setString(1, customer);
            stmt.setString(2, customerTel);
            stmt.setLong(3, System.currentTimeMillis());
            stmt.execute();
            ResultSet rs = connection.createStatement().executeQuery("SELECT last_insert_rowid() as id");
            int insertedId = rs.getInt("id");
            for (Phone phone : phones) {
                addPhoneToOrder(insertedId, phone.getId());
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static Order getOrder(int id) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, customer, customer_tel, COALESCE((SELECT SUM(p.price) FROM phones p WHERE p.id IN (SELECT phone_id FROM orders_phones WHERE order_id=orders.id)), 0) AS price, timestamp FROM orders WHERE id=" + id);
            return new Order(
                    rs.getInt("id"),
                    rs.getString("customer"),
                    rs.getString("customer_tel"),
                    rs.getInt("price"),
                    rs.getTimestamp("timestamp")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void updateOrder(int id, String customer, String customerTel, List<Phone> phones) {
        try {
            connection.setAutoCommit(false);

            PreparedStatement stmt = connection.prepareStatement("UPDATE orders SET customer=?, customer_tel=? WHERE id=" + id);
            stmt.setString(1, customer);
            stmt.setString(2, customerTel);
            stmt.execute();
            connection.createStatement().executeUpdate("DELETE FROM orders_phones WHERE order_id=" + id);
            for (Phone phone : phones) {
                addPhoneToOrder(id, phone.getId());
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static void deleteOrder(int id) {
        try {
            connection.setAutoCommit(false);

            connection.createStatement().executeUpdate("DELETE FROM orders_phones WHERE order_id=" + id);
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM orders WHERE id=?");
            stmt.setInt(1, id);
            stmt.execute();

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private static void addPhoneToOrder(int orderId, int phoneId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO orders_phones(order_id, phone_id) VALUES(?, ?)");
            stmt.setInt(1, orderId);
            stmt.setInt(2, phoneId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    int columns = rs.getMetaData().getColumnCount();
    // Перебор строк с данными
    while(rs.next()) {
        for (int i = 1; i <= columns; i++) {
            System.out.print(rs.getString(i) + "\t");
        }
        System.out.println();
    }
    */

}
