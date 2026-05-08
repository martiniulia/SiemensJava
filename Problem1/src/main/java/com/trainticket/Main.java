package com.trainticket;

import com.trainticket.ui.CustomerMenu;
import com.trainticket.ui.AdminMenu;
import com.trainticket.util.InputHelper;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start");
        
        while (true) {
            System.out.println("\n    Menu:");
            System.out.println("1. Customer Mode");
            System.out.println("2. Admin Mode");
            System.out.println("0. Exit");
            
            int choice = InputHelper.readInt("Select an option: ");
            if (choice == 1) {
                CustomerMenu.start();
            } else if (choice == 2) {
                AdminMenu.start();
            } else if (choice == 0) {
                System.out.println("Exit");
                break;
            } else {
                System.out.println("Invalid");
            }
        }
    }
}
