package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.Account;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private TransferService transferService;
    private AccountService accountService;

    @Autowired
    public ConsoleService(TransferService transferService, AccountService accountService) {
        this.transferService = transferService;
        this.accountService = accountService;
    }

    public ConsoleService() {

    }

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

    public Long promptForLong(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public int promptForInt(String prompt) {
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

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printGetBalance(Account acct) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        System.out.println("Your current account balance is: " + nf.format(acct.getBalance()));
    }

//    public void viewTransferHistory() {
//        transferService = new TransferService(API_BASE_URL, currentUser);
//        Transfer[] transfer =
//                transferService.getAllTransfersToFrom(this.currentUser.getUser().getId());
//        System.out.println("--------------------------------------------");
//        System.out.println("Transfers");
//        System.out.println("ID        FROM       TO         AMOUNT");
//        for (Transfer t : transfer) {
//            System.out.println(toStringById(t.getAccountFrom()));
//        }
//        System.out.println("--------------------------------------------");
//    }

    //if the id I'm passing in (accountFROM) == id
    //print FROM
    //else print TO

}