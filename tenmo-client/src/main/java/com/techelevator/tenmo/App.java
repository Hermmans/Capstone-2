package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.util.*;

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
        Double rejectedSum=0D;
        Double pendingSum=0D;

        System.out.println("--------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID       STATUS        FROM/TO         AMOUNT");

        Set<String> detailsInOrder = new TreeSet<>();
        String toAdd = "";

        for(TransferDetail td: transferDetails){
            if(td.getUsernameFrom().equals(currentUser.getUser().getUsername())) {
                toAdd=   td.getTransferId()+"     " +td.getTransferStatusDesc()+"     " + "To: " + td.getUsernameTo()+"     " + consoleService.printPrettyMoney(td.getAmount());
                detailsInOrder.add(toAdd);

                if(td.getTransferStatusDesc().equalsIgnoreCase("approved")){
                    sumTo +=td.getAmount();
                }
                else if(td.getTransferStatusDesc().equalsIgnoreCase("pending")){
                    pendingSum+=td.getAmount();
                }
                else{
                    rejectedSum+=td.getAmount();}
            } if(td.getUsernameTo().equals(currentUser.getUser().getUsername())) {
                toAdd= td.getTransferId()+"     " +td.getTransferStatusDesc() +"     "+ "From: " + td.getUsernameFrom() +"     " + consoleService.printPrettyMoney(td.getAmount());
                detailsInOrder.add(toAdd);
                if(td.getTransferStatusDesc().equalsIgnoreCase("approved")) {
                    sumFrom += td.getAmount();
                }
                else if(td.getTransferStatusDesc().equalsIgnoreCase("pending")){
                    pendingSum+=td.getAmount();
                } else{
                    rejectedSum+=td.getAmount();}
            }
        }

        //stores the details of the transactions into a treeset so it doesn't print duplicates and prints in order
        for(String string : detailsInOrder){
            System.out.println(string);
        }

        System.out.println("--------------------------------------------");
        System.out.println("Outgoing : "+consoleService.printPrettyMoney(sumTo));
        System.out.println("Incoming : "+consoleService.printPrettyMoney(sumFrom));
        System.out.println("Pending  : "+consoleService.printPrettyMoney(pendingSum));
        System.out.println("Rejected : "+consoleService.printPrettyMoney(rejectedSum));
        System.out.println("--------------------------------------------");

        Long transferId = 0L;

        transferId = consoleService.promptForLong("Please enter transfer ID to view details (0 to cancel): ");

        for(TransferDetail td : transferDetails){
            if(td.getTransferId().equals(transferId)){
                System.out.println(td.toString());
            }
        }if(!doesTransferExist(transferId)){
            System.out.println("***Transfer ID ["+transferId+"] does not exist***");
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

        Long userChoice = 0L;
        userChoice = consoleService.promptForLong("Please enter transfer ID to approve/reject (0 to cancel): ");
        Transfer[] transfers = transferService.listTransfer();
        Long statusIdChoice = 0L;

        for(Transfer t :transfers){
            if(t.getTransferId().equals(userChoice)){

                Account accountTo = accountService.getAccountByAccountId(t.getAccountTo());
                Account accountFrom = accountService.getAccountById(currentUser.getUser().getId());
                User user = accountService.getUsersById(accountTo.getUserId());

                statusIdChoice = approvalScreen();
                if(statusIdChoice==1){
                    if(accountFrom.getBalance()>=t.getAmount()) {
                        System.out.println("Approved");
                        transferService.updateTransfer(t, 1L, 2L, userChoice);
                        accountService.updateWithdraw(accountService.withdraw(currentUser.getUser().getId(), t.getAmount()), t.getAmount());
                        accountService.updateDeposit(accountService.deposit(user.getId(), t.getAmount()), t.getAmount());
                    }
                    else{
                        System.out.println("\n***Users balance cannot be negative by ["+consoleService.printPrettyMoney(accountFrom.getBalance()-t.getAmount())+"]***");

                    }

                } else if(statusIdChoice==2){
                    System.out.println("Rejected");
                    transferService.updateTransfer(t,1L,3L,userChoice);
                } else if(statusIdChoice==0){
                    System.out.println("Exit");
                }
            }
        }
    }

    private void sendBucks(){
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        TransferService transferService = new TransferService(API_BASE_URL,currentUser);
        Account accountTo = null;
        Account accountFrom = null;
        Transfer transfer = new Transfer();
        Long sendTo = 0L;
        sendTo = listUsers();

        if(!sendTo.equals(currentUser.getUser().getId())) {
            accountTo = accountService.getAccountById(sendTo);
            accountFrom = accountService.getAccountById(currentUser.getUser().getId());
        }

        Double moneyToSend = moneyToSend("Enter amount: ");

        if(moneyToSend>0 && moneyToSend<= accountFrom.getBalance()){

            accountService.updateWithdraw(accountService.withdraw(accountFrom.getUserId(), moneyToSend), moneyToSend);
            assert accountTo != null;
            accountService.updateDeposit(accountService.deposit(accountTo.getUserId(), moneyToSend), moneyToSend);

            transfer.setTransferTypeId(2L);
            transfer.setTransferStatusId(2L);
            transfer.setAccountTo(accountTo.getAccountId());
            transfer.setAccountFrom(accountFrom.getAccountId());
            transfer.setAmount(moneyToSend);

            transferService.addTransfer(transfer);

            consoleService.transactionApproved(moneyToSend);

        }else if(accountFrom.getBalance()<moneyToSend){
            System.out.println("\n***Users balance cannot be negative by ["+consoleService.printPrettyMoney(accountFrom.getBalance()-moneyToSend)+"]***");
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
        transferService.addTransfer(transfer);

    }

    private Long listUsers() {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        User[] users = accountService.getUsers();
        System.out.println("----------------------------------------");
        System.out.println("Users");
        System.out.printf("|%-2s|%-4s|", "ID", "Name");
        System.out.println("");
        System.out.println("----------------------------------------");

        List<Long> userIds = new ArrayList<>();
        Long aLong = 0L;
        for (User user : users) {
            userIds.add(user.getId());
            if (!user.getId().equals(currentUser.getUser().getId()))
                System.out.println("| " + user.getId() + " | " + user.getUsername() + " |");
        }
        System.out.println("---------------------------");
        boolean exists;
        aLong = consoleService.promptForLong("Enter ID of user you are sending to: (0 to cancel): ");
        exists = accountService.userExists(aLong);
        if(!exists){
            System.out.println("");
            System.out.println("***User ["+aLong+"] does not exist. Please try again.***");
            System.out.println("");
            aLong= listUsers();
        } else if(aLong.equals(currentUser.getUser().getId())){
            System.out.println("");
            System.out.println("***Transaction not permitted***");
            System.out.println("");
            aLong= listUsers();
        }
        return  aLong;
    }


    private boolean doesTransferExist(Long id){
        boolean exists = false;
        transferService = new TransferService(API_BASE_URL, currentUser);
        Transfer[] transfers = transferService.getAllTransfersToFrom(currentUser.getUser().getId());
        for(Transfer t: transfers){
            if(t.getTransferId().equals(id)){
                exists = true;
            }
        }
        return exists;
    }


    private Long approvalScreen(){
        TransferService transferService = new TransferService(API_BASE_URL,currentUser);
        System.out.println("1: Approve \n2: Reject \n0: Don't approve or reject\n");
        Long userChoice = consoleService.promptForLong("Please choose an option: ");
        return userChoice;
    }

    private Double moneyToSend(String prompt){
        return consoleService.promptForDouble(prompt);
    }

}
