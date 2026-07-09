package com.nhannkl;

import java.util.Scanner;

import com.nhannkl.service.MacService;
import com.nhannkl.service.MacServiceImpl;

public class Main {

    private final MacService macService;

    public Main(MacService macService) {
        this.macService = macService;
    }

    public static void main(String[] args) {
        MacService macServiceForInjection = new MacServiceImpl("mySuperSecretKeyForMacDemo1234567890!", "HmacSHA256");
        Main main = new Main(macServiceForInjection);

        MacService macService = main.getMacService();

        Scanner scanner = new Scanner(System.in);
        System.out.printf("Enter your choice (1 - Generating, 2 - Checking): ");
        String choiceString = scanner.nextLine();
        int choice = Integer.parseInt(choiceString);
        if (choice == 1) {
            System.out.printf("Enter your message: ");
            String message = scanner.nextLine();
            System.out.println("MAC: " + macService.generateMac(message));
        } else if (choice == 2) {
            System.out.printf("Enter your MAC: ");
            String macToVefiry = scanner.nextLine();

            System.out.printf("Enter your message: ");
            String message = scanner.nextLine();
            System.out.println("Result: " + macService.verifyMac(message, macToVefiry));
        }

        scanner.close();
        
    }

    public MacService getMacService() {
        return macService;
    }
    
}