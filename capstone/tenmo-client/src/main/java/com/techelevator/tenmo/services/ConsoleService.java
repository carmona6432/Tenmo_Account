package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.login.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public Integer promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }
    public void printPendingRequests() {
        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To                     Amount");
        System.out.println("-------------------------------------------");
    }
   public void displayUsersFrame(){
       System.out.println(toString() + "\n" + "Users\n" + "ID          Name\n" + toString());
   }
   public void displayPendingTransaction(){
        System.out.println(toString() + "\n" + "Pending\n" + "ID          Name\n" + toString());
   }

   public String toString(){
        String frame = "-------------------------------------------";
        return frame;
   }
   public void displayTransfer(int transfer_id, String transfer_type, String transfer_status_type, String account_from, String account_to, BigDecimal amount){
        System.out.println(toString() + "\n Transfer Details \n" + toString());
        System.out.println("Transfer_id: " + transfer_id);
        System.out.println("From: " + account_from );
        System.out.println("To: " + account_to);
        System.out.println("Transfer type: " + transfer_type);
        System.out.println("Transfer status type: " + transfer_status_type);
        System.out.println("Amount: $" + amount);

   }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
