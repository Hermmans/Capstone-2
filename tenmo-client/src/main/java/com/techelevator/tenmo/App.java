package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

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
        accountService = new AccountService(API_BASE_URL, currentUser);
        TransferDetail[] transferDetails = transferService.getAllTransferFrom(currentUser.getUser().getId());
        Double sumTo = 0D;
        Double sumFrom = 0D;

        System.out.println("--------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID       STATUS        FROM/TO         AMOUNT");

        for(TransferDetail td: transferDetails){
            if(td.getUsernameFrom().equals(currentUser.getUser().getUsername())) {
                System.out.println(td.getTransferId() + "    " +td.getTransferStatusDesc() +"    " + "To: " + td.getUsernameTo() + "    " + consoleService.printPrettyMoney(td.getAmount()));
                sumTo +=td.getAmount();
            }
            else if(td.getUsernameTo().equals(currentUser.getUser().getUsername())) {
                System.out.println(td.getTransferId() + "    " +td.getTransferStatusDesc() +"    " + "From: " + td.getUsernameFrom() + "    " + consoleService.printPrettyMoney(td.getAmount()));
                sumFrom += td.getAmount();
            }
        }
        System.out.println("--------------------------------------------");
        System.out.println("Outgoing Sum : "+consoleService.printPrettyMoney(sumTo));
        System.out.println("Incoming Sum : "+consoleService.printPrettyMoney(sumFrom));
        System.out.println("--------------------------------------------");

        Long transferId = consoleService.promptForLong("Please enter transfer ID to view details (0 to cancel): ");
        for(TransferDetail td : transferDetails){
            if(td.getTransferId().equals(transferId))
            System.out.println(td.toString());
        }
    }


    private void viewPendingRequests() {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        TransferService transferService = new TransferService(API_BASE_URL,currentUser);
        TransferDetail[] transferDetails = transferService.getAllTransferFrom(currentUser.getUser().getId());

        for(TransferDetail td: transferDetails){
            if(td.getUsernameFrom().equals(currentUser.getUser().getUsername())) {
                if(td.getTransferStatusDesc().equalsIgnoreCase("pending")){
                    System.out.println(td.getTransferId() + "    " + "To: " + td.getUsernameTo() + "    " + consoleService.printPrettyMoney(td.getAmount()));
                }
            }
        }

        Long userChoice = consoleService.promptForLong("Please enter transfer ID to approve/reject (0 to cancel): ");
        Transfer[] transfers = transferService.listTransfer();
        Long statusIdChoice = 0L;

        for(Transfer t :transfers){
            if(t.getTransferId().equals(userChoice)){

                Account account = accountService.getAccountByAccountId(t.getAccountTo());
                User user = accountService.getUsersById(account.getUserId());

                statusIdChoice = approvalScreen();
                if(statusIdChoice==1){
                    System.out.println("Approved");
                    transferService.updateTransfer(t,2L,2L,userChoice);
                    accountService.updateWithdraw(accountService.withdraw(currentUser.getUser().getId(), t.getAmount()), t.getAmount());
                    accountService.updateDeposit(accountService.deposit(user.getId(), t.getAmount()), t.getAmount());
                } else if(statusIdChoice==2){
                    System.out.println("Rejected");
                    transferService.updateTransfer(t,1L,3L,userChoice);
                } else if(statusIdChoice==0){
                    System.out.println("Exit");
                }
            }
        }
    }

    private Long approvalScreen(){
        TransferService transferService = new TransferService(API_BASE_URL,currentUser);
        System.out.println("1: Approve \n2: Reject \n0: Don't approve or reject\n");
        Long userChoice = consoleService.promptForLong("Please choose an option: ");
        return userChoice;
    }

    private void sendBucks(){
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        TransferService transferService = new TransferService(API_BASE_URL,currentUser);
        Account accountTo = null;
        Account accountFrom = null;
        Transfer transfer = new Transfer();

        Long sendTo = listUsers();

        if(!sendTo.equals(currentUser.getUser().getId())) {
            accountTo = accountService.getAccountById(sendTo);
            accountFrom = accountService.getAccountById(currentUser.getUser().getId());
        }

        Double moneyToSend = moneyToSend("Enter amount: ");

        if(moneyToSend>0 && moneyToSend<= accountFrom.getBalance()){

            accountService.updateWithdraw(accountService.withdraw(accountFrom.getUserId(), moneyToSend), moneyToSend);
            assert accountTo != null;
            accountService.updateDeposit(accountService.deposit(accountTo.getUserId(), moneyToSend), moneyToSend);

//            transfer.setTransferTypeId(transfer.getTransferTypeId());
//            transfer.setTransferStatusId(transfer.getTransferStatusId());


            transfer.setAccountTo(accountTo.getAccountId());
            transfer.setAccountFrom(accountFrom.getAccountId());
            transfer.setAmount(moneyToSend);
            transferService.addTransfer(transfer, accountFrom.getAccountId(),accountTo.getAccountId(),moneyToSend);
            consoleService.transactionApproved(moneyToSend);

        }else if(accountFrom.getBalance()<moneyToSend){
            System.out.println("\n***Users balance cannot be negative ["+consoleService.printPrettyMoney(accountFrom.getBalance()-moneyToSend)+"]***");
        }
        else if (moneyToSend<=0){
            System.out.println("");
            System.out.println("***Transactions with ["+consoleService.printPrettyMoney(moneyToSend)+"] are not allowed***");
        }
    }

    private void requestBucks() {
        accountService = new AccountService(API_BASE_URL, currentUser);
        transferService = new TransferService(API_BASE_URL,currentUser);

        Account loggedIn = accountService.getAccountById(currentUser.getUser().getId());
        Account requestSendFrom = accountService.getAccountById(listUsers());

        Double requestedAmount = moneyToSend("Enter amount: ");

//        if(moneyToSend>loggedIn.getBalance()){
//            System.out.println("\n***Users balance cannot be negative ["+consoleService.printPrettyMoney(loggedIn.getBalance()-moneyToSend)+"]***");
//        }

        if(requestedAmount<=0){
            System.out.println("");
            System.out.println("***Transactions with ["+consoleService.printPrettyMoney(requestedAmount)+"] are not allowed***");
        }

        consoleService.transactionProcessed(requestedAmount);
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(1L);
        transfer.setTransferStatusId(1L);
        transfer.setAccountTo(loggedIn.getAccountId());
        transfer.setAccountFrom(requestSendFrom.getAccountId());
        transfer.setAmount(requestedAmount);
        transferService.addTransferFull(transfer, transfer.getTransferStatusId(),transfer.getTransferTypeId(), requestSendFrom.getAccountId(),loggedIn.getAccountId(),requestedAmount);

    }

    private Long listUsers(){
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        User[] users = accountService.getUsers();
        System.out.println("----------------------------------------");
        System.out.println("Users");
        System.out.printf("|%-2s|%-4s|" ,"ID", "Name");
        System.out.println("");
        System.out.println("----------------------------------------");
        for(User user : users){
            if(!user.getId().equals(currentUser.getUser().getId()))
                System.out.println("| "+user.getId()+" | "+user.getUsername()+" |");
        }
        System.out.println("---------------------------");
        return consoleService.promptForLong("Enter ID of user you are sending to: (0 to cancel): ");
    }

    private Double moneyToSend(String prompt){
        return consoleService.promptForDouble(prompt);
    }

}
