package ru.kai.inkrot.laba7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Islam Zaripov
 */
public class DataBase {
    
    public static Connection connection;
    
    public static void connect() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        String user = "inkrot";
        String password = "1234";
        String url = "jdbc:derby://localhost:1527/laba7_db";
        connection = DriverManager.getConnection(url, user, password);
    }
    
    public static ResultSet selectTable(String table, String selector) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT " + selector + " FROM " + table);
            return stmt.executeQuery();
        } catch (SQLException e) {
            return null;
        }
    }
    
    public static ResultSet selectTable(String table) {
        return selectTable(table, "*");
    }
    
    public static String audiencesToString() {
        ResultSet rows = DataBase.query("SELECT * FROM audiences");
        try {
            String str = "";
            while (rows.next()) {
                str += "#" + rows.getInt("id") + " "
                        + rows.getInt("building") + " "
                        + rows.getInt("number") + " "
                        + rows.getString("name") + " "
                        + rows.getDouble("square") + " "
                        + rows.getInt("employee_id") + "\n";
            }
            return str;
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    
    public static String employeesToString() {
        ResultSet rows = DataBase.query("SELECT * FROM employees");
        try {
            String str = "";
            while (rows.next()) {
                str += "#" + rows.getInt("id") + " "
                        + rows.getString("fio") + " "
                        + "["+ rows.getString("position") + "] "
                        + rows.getString("tel") + " "
                        + rows.getInt("age") + "\n";
            }
            return str;
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public static ResultSet query(String sql) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void inserAudience(int id, int building, int number, String name, double square, int employeeId) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO audiences VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setInt(2, building);
            stmt.setInt(3, number);
            stmt.setString(4, name);
            stmt.setDouble(5, square);
            stmt.setInt(6, employeeId);
            stmt.execute();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {}
        }
    }
    
    public static void inserEmployees(int id, String fio, String position, String tel, int age) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO employees VALUES(?, ?, ?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, fio);
            stmt.setString(3, position);
            stmt.setString(4, tel);
            stmt.setInt(5, age);
            stmt.execute();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {}
        }
    }
    
    public static void clear(String tableName, int id) {
        String fileds = "";
        if (tableName.toLowerCase().equals("audiences")) 
            fileds = "building=null, number=null, name=null, square=null, employee_id=null";
        else if (tableName.toLowerCase().equals("employees")) 
            fileds = "fio=null, position=null, tel=null, age=null";
        else return;
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE " + tableName + " SET " + fileds + " WHERE id=" + id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void delete(String tableName, int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id=" + id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    
    
    
    
    /*
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
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/
}
