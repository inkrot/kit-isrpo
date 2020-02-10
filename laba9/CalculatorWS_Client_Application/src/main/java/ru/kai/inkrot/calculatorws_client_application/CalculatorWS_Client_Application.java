/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kai.inkrot.calculatorws_client_application;

/**
 *
 * @author Admin
 */
public class CalculatorWS_Client_Application {
    public static void main(String[] args) {
        try {
            int i = 10;
            int j = 4;
            int result = add(i, j);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
    }
    
    
    private static int add(int i, int j) {
        ru.kai.inkrot.laba9.CalculatorWS_Service service = new ru.kai.inkrot.laba9.CalculatorWS_Service();
        ru.kai.inkrot.laba9.CalculatorWS port = service.getCalculatorWSPort();
        int result = port.add(i, j);
        return result;
    }
}
















