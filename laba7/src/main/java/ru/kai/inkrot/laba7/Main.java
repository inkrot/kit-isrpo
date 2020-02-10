 package ru.kai.inkrot.laba7;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Islam Zaripov
 */
public class Main {
    
    Scanner in;
    
    Main() {
        try {
            DataBase.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        in = new Scanner(System.in);
        
        System.out.println("--- Зарипов Ислам, группа 4441, вариант 76 ---");
        
        while (true) {
            showMenu();
            System.out.println("Выберите действие: ");
            int c = in.nextInt();
            processChoice(c);
        }
    }
    
    public void showMenu() {
        System.out.println("1 - Сбросить значения всех не ключевых полей на основе данных записанных в файле");
        System.out.println("2 - Вывести все значения столбца ЗДАНИЕ (building) из таблицы АУДИУТОРИИ (audiences)");
        System.out.println("3 - Вывести список самых младших сотрудников (ФИО, должность, телефон, возраст) в лексикографическом порядке");
        System.out.println("4 - Найти общую площадь аудиторий закреплённых за указанным ответственным");
        System.out.println("5 - Просмотр записей, хранящихся в БД");
        System.out.println("6 - Добавление записей в БД");
        System.out.println("7 - Удаление записей из БД");
        System.out.println("0 - Выйти");
    }
    
    public String[] input(String format) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные в следующем формате: " + format);
        String line = scanner.nextLine();
        return line.split(",");
    }
    
    public void processChoice(int c) {
        switch(c) {
            case 1:
                try {
                    BufferedReader br = new BufferedReader(new FileReader("data.txt"));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    String data = sb.toString();
                    String [] ids = data.split(",");
                    String tableName = ids[0];
                    for (int i = 1; i < ids.length; i++) {
                        DataBase.clear(tableName, Integer.valueOf(ids[i]));
                    }
                } catch (Exception e) {}
                break;
            case 2:
                ResultSet rs = DataBase.selectTable("audiences", "building");
                try {
                    while (rs.next()) {
                        System.out.println(rs.getInt("building"));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 3:
                System.out.println("Список самых младших сотрудников: ");
                ResultSet employes = DataBase.query("SELECT fio, position, tel, age FROM employees WHERE age=(SELECT MIN(age) FROM employees)");
                try {
                    while (employes.next()) {
                        System.out.println("- " 
                                + employes.getString("fio") 
                                + " (" + employes.getString("position") + ")" 
                                + " " + employes.getString("tel")
                                + ". Возраст: " + employes.getInt("age"));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println();
                break;
            case 4:
                System.out.println("Введите идентификатор ответственного: ");
                int id = in.nextInt();
                ResultSet sum = DataBase.query("SELECT SUM(square) AS square_sum FROM audiences WHERE employee_id=" + id);
                try {
                    if (sum.next()) {
                        int s = sum.getInt("square_sum");
                        System.out.println("Общая площадь: " + s);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 5:
                System.out.println("Записи БД:");
                System.out.println("- Таблица audiences:");
                System.out.println(DataBase.audiencesToString());
                System.out.println("- Таблица employees:");
                System.out.println(DataBase.employeesToString());
                break;
            case 6:
                int table = showSelectTable();
                if (table == 1) {
                    String d[] = input("<id>,<building>,<number>,<name>,<square>,<employee_id>,");
                    DataBase.inserAudience(
                            Integer.valueOf(d[0]), 
                            Integer.valueOf(d[1]), 
                            Integer.valueOf(d[2]), 
                            d[3], 
                            Double.valueOf(d[4]), 
                            Integer.valueOf(d[5]));
                } else if (table == 2) {
                    String d[] = input("<id>,<fio>,<position>,<tel>,<age>,");
                    DataBase.inserEmployees(
                            Integer.valueOf(d[0]), 
                            d[1],
                            d[2],
                            d[3],
                            Integer.valueOf(d[4]));
                } else System.out.println("Неправильный ввод");
                break;
            case 7:
                int t = showSelectTable();
                String tableName = "";
                if (t == 1) {
                    tableName = "audiences";
                } else if (t == 2) {
                    tableName = "employees";
                } else {
                    System.out.println("Неправильный ввод");
                    break;
                }
                int deleteId = in.nextInt();
                DataBase.delete(tableName, deleteId);
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Такого пункта нет!");
                break;
        }
    }
    
    public int showSelectTable() {
        System.out.println("Выберите таблицу:");
        System.out.println("1 - audiences");
        System.out.println("2 - employees");
        return in.nextInt();
    }
    
    public static void main(String[] args) {
        new Main();
    }
    
}