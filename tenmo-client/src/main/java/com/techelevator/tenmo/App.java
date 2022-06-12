package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import io.cucumber.java.bs.A;

import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private AccountService accountService;
    private TransferService transferService;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
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
        accountService = new AccountService(API_BASE_URL, currentUser);
        consoleService.printGetBalance(accountService.getBalance(currentUser.getUser().getId()));
    }

    private void viewTransferHistory() {
        transferService = new TransferService(API_BASE_URL, currentUser);
        Transfer[] transfers =
                transferService.getAllTransfersToFrom(currentUser.getUser().getId());
        System.out.println("--------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID        FROM       TO         AMOUNT");
        for(Transfer t:transfers){
            System.out.println(t.toString());
        }
        System.out.println("--------------------------------------------");

        //********
        //we need a way to view transfer details based on user input still
        //********
    }

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

    //needs work
    //needs a bunch of cleanup
	private void sendBucks() {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        TransferService transferService = new TransferService(API_BASE_URL,currentUser);
        User[] users = accountService.getUsers();
        Account[] accounts = accountService.listAccounts();
        Transfer[] transfers = transferService.listTransfer();
        Transfer lastTransfer = null;
        Long newIdNum = 0L;
        for(Transfer t : transfers){
            lastTransfer=t;
            if(lastTransfer.getTransferId()>t.getTransferId()){
                newIdNum = lastTransfer.getTransferId();
            }
            newIdNum++;
        }
        Scanner input = new Scanner(System.in);
        System.out.println("Choose the username of the bitch you wanna send money to");
        System.out.println("---------------------------------------------------------");
        System.out.println("Users");
        for(User user : users){
            System.out.println("---------------------------");
            System.out.println("Username: "+user.getUsername() +
                    " ID: "+user.getId());
        }
        Long userOfChoice = input.nextLong();
        if(!userOfChoice.equals(currentUser.getUser().getId())){
            System.out.println("How much money you wanna send this bich?");
            Long moneyToSend = input.nextLong();
            Account accountTo= null;
            Account accountFrom = null;
            Transfer transfer = new Transfer();
            for(Account receiver : accounts){
                if(receiver.getUserId().equals(userOfChoice)){
                    accountTo = receiver;
                }
                for(Account sender : accounts){
                    if(sender.getUserId().equals(currentUser.getUser().getId())){
                        accountFrom=sender;
                    }
                }
            }
            assert accountFrom != null;
            if((accountFrom.getBalance()>=moneyToSend) && (moneyToSend>0)){
                System.out.println("transaction approved..something something");

                accountService.updateWithdraw(accountService.withdraw(accountFrom.getUserId(), moneyToSend.doubleValue()), moneyToSend.doubleValue());
                accountService.updateDeposit(accountService.deposit(accountTo.getUserId(), moneyToSend.doubleValue()), moneyToSend.doubleValue());

                assert false;
                transfer.setTransferId(newIdNum);
                transfer.setAccountTo(accountTo.getAccountId());
                transfer.setAccountFrom(accountFrom.getAccountId());
                transfer.setAmount(moneyToSend.doubleValue());
                transferService.addTransfer(transfer, accountFrom.getAccountId(),accountTo.getAccountId(),moneyToSend.doubleValue());
            }else{
                System.out.println("sorry my guy, you broke af");
                //error handling
                //try catch's
                //maybe log it
            }
        }
        else{
            System.out.println("You can't send money to yourself. It borders embezzlement");
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub
    }

}
