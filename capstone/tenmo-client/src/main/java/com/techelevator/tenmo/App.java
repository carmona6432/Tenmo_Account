package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.login.AuthenticatedUser;
import com.techelevator.tenmo.model.login.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.login.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private static final String LOG_FILE = "Log.txt";
    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            accountService.setToken(currentUser.getToken());
            transferService.setToken(currentUser.getToken());
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
        Transfer[] transfers;
        int code = consoleService.promptForInt(consoleService.toString() + "\n1. Received" + "\n" + "2. Sent" + "\n" + "3.Look up transfer by transfer id" + consoleService.toString() + "\n");
        if (code == 1) {
            int id = accountService.getAccount().getAccountId();
            transfers = transferService.getTransfersToAccount(id);
            for (Transfer transfer : transfers) {
                consoleService.displayTransfer(transfer.getTransferId(), transferService.getTransferTypeById(transfer.getTransferTypeId()),transferService.getTransferStatusById(transfer.getTransferStatusId()),currentUser.getUser().getUsername(),transfer.getUsername(),transfer.getAmount());
            }
        } else if (code == 2) {
            int id = accountService.getAccount().getAccountId();
            transfers = transferService.getTransfersFromAccount(id);
            for (Transfer transfer : transfers) {
                consoleService.displayTransfer(transfer.getTransferId(), transferService.getTransferTypeById(transfer.getTransferTypeId()),transferService.getTransferStatusById(transfer.getTransferStatusId()),transfer.getUsername(),currentUser.getUser().getUsername(),transfer.getAmount());
            }
        } else if (code == 3){
            int id = consoleService.promptForInt("Please enter transfer id: ");
            Transfer transfer = transferService.getTransferById(id);
            consoleService.displayTransfer(transfer.getTransferId(), transferService.getTransferTypeById(transfer.getTransferTypeId()),transferService.getTransferStatusById(transfer.getTransferStatusId()),accountService.getUsernameByAccountId(transfer.getAccountFrom()),accountService.getUsernameByAccountId(transfer.getAccountTo()),transfer.getAmount());

        }
    }

	private void viewPendingRequests() {
        int id = accountService.getAccount().getAccountId();
        List<Transfer> pendingRequests = transferService.getPendingRequests(id);
        if (pendingRequests.isEmpty()) {
            System.out.println("You have no pending transfer requests.");
            return;
        }
        consoleService.printPendingRequests();
        for (Transfer request : pendingRequests) {
            System.out.printf("%-12d%-23s$ %7.2f%n",
                    request.getTransferId(),
                    request.getUsername(),
                    request.getAmount());
        }
        System.out.println("---------");
        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
        if (transferId != 0) {
            approveOrRejectTransfer(transferId);
        }
//        int id = accountService.getAccount().getAccountId();
//        Transfer[] pendingRequests = transferService.getPendingRequests(id);
//        for(Transfer request : pendingRequests) {
//            System.out.println(consoleService.toString() + "\n" + request.getTransferId() + " " +
//                    request.getUsername() +
//                    " $" + request.getAmount() + "\n" + consoleService.toString());
//        }

	}

	private void sendBucks() {
        Transfer transfer = new Transfer();
        consoleService.displayUsersFrame();
        for (Account account : accountService.getAccounts()) {
            System.out.println(account.getUserId() + "        " + account.getUsername());
        }
        int userId = consoleService.promptForInt("Please Enter Recipient Id: ");
        Account recipientAccount = accountService.getAccountByUserId(userId);
        if (recipientAccount == null) {
            System.out.println("Recipient not found.");
            return;
        }
        if (recipientAccount.getUserId() == currentUser.getUser().getId()) {
            System.out.println("Can't send money to yourself Dumb Dumb.");
            return;
        }
        int toAccountId = recipientAccount.getAccountId();

        BigDecimal balance = accountService.getAccount().getBalance();
        System.out.println(consoleService.toString() + "\nAvailable Balance: $" + balance);

        BigDecimal amount = consoleService.promptForBigDecimal("Enter Amount to Send: ");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }
        if (amount.compareTo(balance) > 0) {
            System.out.println("Insufficient balance.");
            return;
        }
        transfer.setTransferTypeId(2);
        transfer.setAccountFrom(accountService.getAccountByUserId(currentUser.getUser().getId()).getAccountId());
        transfer.setAccountTo(recipientAccount.getAccountId());
        transfer.setAmount(amount);
        transfer.setTransferStatusId(2);
        transfer.setTransferStatusId(1);
        transferService.sendTransfer(transfer);

        consoleService.displayTransfer(transferService.sendTransfer(transfer).getTransferId(),transferService.getTransferTypeById(2), transferService.getTransferStatusById(2),currentUser.getUser().getUsername(),accountService.getUsernameByAccountId(userId),amount);
        transferService.sendTransfer(transfer);
//        Transfer createTransfer = transferService.sendTransfer(transfer);
//        consoleService.displayTransfer(createTransfer.getTransferId(),transferService.getTransferTypeById(2), transferService.getTransferStatusById(2),currentUser.getUser().getUsername(),accountService.getUsernameByAccountId(userId),amount);
        }

	private void requestBucks() {
        Transfer request = new Transfer();
        consoleService.displayUsersFrame();
        for (Account account : accountService.getAccounts()) {
            System.out.println(account.getUserId() + "        " + account.getUsername());
        }
        int userId = consoleService.promptForInt("Please Enter Recipient Id: ");
        Account recipientAccount = accountService.getAccountByUserId(userId);
        if (recipientAccount.getUserId() == currentUser.getUser().getId()) {
            System.out.println("You cannot request TE Bucks from yourself.");
            return;
        }
        if (recipientAccount == null) {
            System.out.println("Recipient ID is not valid.");
            return;
        }
        BigDecimal amount = consoleService.promptForBigDecimal("Please Enter The Amount You would like to request $");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("The amount must be greater than zero.");
            return;
        }
        request.setTransferTypeId(1);
        request.setAccountFrom(recipientAccount.getAccountId());
        request.setAccountTo(accountService.getAccountByUserId(currentUser.getUser().getId()).getAccountId());
        request.setAmount(amount);
        request.setTransferStatusId(1);
        try {
            transferService.sendRequest(request);
            System.out.println("Transfer request sent successfully.");
        } catch (Exception e) {
            System.out.println("Error creating transfer request");
        }
    }
    private void approveOrRejectTransfer(int transferId) {
        //viewPendingRequests();
        //List<Transfer> pendingTransfers = transferService.getPendingTransfersByUserId();



//        for (Transfer transfer : pendingTransfers) {
//            System.out.println("Transfer ID: " + transfer.getTransferId() +
//                    " | From: " + accountService.getUsernameByAccountId(transfer.getAccountFrom()) +
//                    " | Amount: $" + transfer.getAmount());
//        }

        if (transferId == 0) {
            return;
        }

        Transfer selectedTransfer = transferService.getTransferById(transferId);
        if (selectedTransfer == null || selectedTransfer.getAccountTo() != accountService.getAccountByUserId(currentUser.getUser().getId()).getAccountId()) {
            System.out.println("Invalid transfer ID.");
            return;
        }

        System.out.println("\n1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");
        int choice = consoleService.promptForInt("Please choose an option: ");

        if (choice == 1) {
            approveTransfer(selectedTransfer);
        } else if (choice == 2) {
            rejectTransfer(selectedTransfer);
        } else if (choice == 0) {
            System.out.println("No action taken.");
        } else {
            System.out.println("Invalid option. No action taken.");
        }
    }

    private void approveTransfer(Transfer transfer) {
        Account senderAccount = accountService.getAccountByAccountId(transfer.getAccountTo());
        if (senderAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
            System.out.println("Insufficient funds to approve this transfer.");
            return;
        }

        transfer.setTransferStatusId(2);
        Account recipientAccount = accountService.getAccountByAccountId(transfer.getAccountFrom());

        BigDecimal senderNewBalance = senderAccount.getBalance().subtract(transfer.getAmount());
        BigDecimal recipientNewBalance = recipientAccount.getBalance().add(transfer.getAmount());

        senderAccount.setBalance(senderNewBalance);
        recipientAccount.setBalance(recipientNewBalance);

//        accountService.updateAccount(senderAccount);
//        accountService.updateAccount(recipientAccount);

        updateTransferStatus(transfer);
        System.out.println("Transfer approved successfully.");
    }

    private void rejectTransfer(Transfer transfer) {
        transfer.setTransferStatusId(3);
        updateTransferStatus(transfer);
        System.out.println("Transfer rejected.");
    }

    private void updateTransferStatus(Transfer transfer) {
        try {
            transferService.updateTransfer(transfer);
        } catch (Exception e) {
            System.out.println("Error updating transfer");
        }
    }

}
