package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.login.AuthenticatedUser;
import com.techelevator.tenmo.model.login.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.login.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private static final String LOG_FILE = "Log.txt";
    private AccountService accountService = new AccountService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            accountService.setToken(currentUser.getToken());
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        BigDecimal balance = accountService.getAccount().getBalance();
        System.out.println("The current balance for " + currentUser.getUser().getUsername() + " is $" + balance);
		
	}

	private void viewTransferHistory() {
        TransferUsername[] transfers;
		int code = consoleService.promptForInt("1. Received" + "\n" + "2. Sent" + "\n");
        if(code == 1){
            int id = accountService.getAccount().getAccountId();
            transfers = accountService.getTransfersToAccount(id);
            for(TransferUsername transfer: transfers){
                System.out.println("Transfer Id:" + transfer.getTransferId() + " From: " + transfer.getUsername() + " for $" + transfer.getAmount() + "\n");
            }
        } else if(code == 2){
            int id = accountService.getAccount().getAccountId();
            transfers = accountService.getTransfersFromAccount(id);
            for(TransferUsername transfer: transfers){
                System.out.println("Transfer Id: " + transfer.getTransferId() + " To:" + transfer.getUsername() + " for $" + transfer.getAmount() + "\n");
            }
        }
	}

	private void viewPendingRequests() {
        int id = accountService.getAccount().getAccountId();
        TransferUsername[] pendingRequests = accountService.getPendingRequests(id);
        for(TransferUsername request : pendingRequests) {
            System.out.println(request.getTransferId() + " " +
                    request.getUsername() +
                    " $" + request.getAmount());
        }
		
	}

	private void sendBucks() {
        Transfer transfer = new Transfer();
        System.out.println("-------------------------------------------\n" +
                "Users\n" +
                "ID          Name\n" +
                "-------------------------------------------");
        //Print available users and user Ids
        
        String username = consoleService.promptForString("Please Enter Recipient Username: ");
//        Account recipientAccount = accountService.getAccountByUsername(username);
//        if (recipientAccount == null) {
//            System.out.println("Recipient not found.");
//            return;

        BigDecimal balance = accountService.getAccount().getBalance();
        System.out.println("-------------------------------------------");
        System.out.println("Available Balance: $" + balance);


        BigDecimal amount = consoleService.promptForBigDecimal("Enter Amount to Send: ");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }
        if (amount.compareTo(balance) > 0) {
            System.out.println("Insufficient balance.");
            return;
        }
//        boolean success = transfer.executeTransfer(accountService.getAccount(), recipientAccount, amount);
//
//        if (success) {
//            System.out.println("Transfer successful!");
//        } else {
//            System.out.println("Transfer failed.");
//        }

		// TODO Auto-generated method stub
		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
    //The following method can be called to make a log of a transfer
    private void logChange(String action, double amount, double newBalance) {
        // Establish date and time
        LocalDateTime now = LocalDateTime.now();
        // Format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        // Combine
        String timeStamp = now.format(formatter);
        // Log entry variable
        String logEntry = String.format("%s %s: $%.2f $%.2f\n", timeStamp, action, amount, newBalance);

        try (PrintWriter output = new PrintWriter(new FileWriter(LOG_FILE, true))){
            output.print(logEntry);
        } catch (IOException e) {
            System.out.println("Error in log file creation.");
        }
    }
}
