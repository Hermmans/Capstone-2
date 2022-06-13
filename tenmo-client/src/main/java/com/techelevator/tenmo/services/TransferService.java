package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public TransferService(String URL, AuthenticatedUser authenticatedUser) {
        this.user = authenticatedUser;
        this.API_BASE_URL = URL;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    public Transfer[] getAllTransfersToFrom(Long id) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/" + id, HttpMethod.GET,
                            makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer[] listTransfer() {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer addTransfer(Transfer transfer, Long accountFrom, Long accountTo, Double amount) {
        Transfer t = new Transfer();
        try {
            t = restTemplate.exchange(API_BASE_URL + "transfer/" + accountFrom + "/" + accountTo + "/" + amount,
                    HttpMethod.POST,
                    makeTransferEntity(t),
                    Transfer.class).getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return t;
    }

    public TransferStatus[] listTransferStatus() {
        TransferStatus[] transferStatuses = null;
        try {
            ResponseEntity<TransferStatus[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            TransferStatus[].class);
            transferStatuses = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferStatuses;
    }

    public TransferType[] listTransferTypes() {
        TransferType[] transferTypes = null;
        try {
            ResponseEntity<TransferType[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfers/",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            TransferType[].class);
            transferTypes = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferTypes;
    }

    public TransferType getTransferTypeById(Long id) {
        TransferType transferType = null;
        try {
            ResponseEntity<TransferType> response =
                    restTemplate.exchange(API_BASE_URL + "transfertype/" + id, HttpMethod.GET,
                            makeAuthEntity(), TransferType.class);
            transferType = response.getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferType;
    }

    public TransferStatus getTransferStatusById(Long id) {
        TransferStatus transferStatus = null;
        try {
            ResponseEntity<TransferStatus> response =
                    restTemplate.exchange(API_BASE_URL + "transferstatus/" + id, HttpMethod.GET,
                            makeAuthEntity(), TransferStatus.class);
            transferStatus = response.getBody();

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferStatus;
    }

}



























//    public Transfer[] listTransfers(Long id) {
//        Transfer[] transfers = null;
//        try {
//            transfers =
//                    restTemplate.exchange(API_BASE_URL + "transfers/" + id,
//                            HttpMethod.GET,
//                            makeAuthEntity(),
//                            Transfer[].class).getBody();
//
//        } catch (RestClientResponseException rcre) {
//            BasicLogger.log(rcre.getRawStatusCode() + " : " + rcre.getStatusText());
//        } catch (ResourceAccessException rae) {
//            BasicLogger.log(rae.getMessage());
//        }
//        return transfers;
//    }
//
//
//    public Transfer[] getTransfersById() {
//        Transfer[] transfers = null;
//        try {
//            transfers = restTemplate.exchange(API_BASE_URL + "transfer/user/" + user.getUser().getId(),
//                    HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
//        } catch (RestClientResponseException rcre) {
//            BasicLogger.log(rcre.getRawStatusCode() + " : " + rcre.getStatusText());
//        } catch (ResourceAccessException rae) {
//            BasicLogger.log(rae.getMessage());
//        }
//        return transfers;
//    }


    //this might be right?
    //or it's this IN the accountservice class



//    public void sendMoney(Long id){
//        Transfer transfer = new Transfer();
//
//        User[] users = getUsers();
//
//        transfer.setAccountFrom(id);
//
//        Scanner scan = new Scanner(System.in);
//
//        for(User user : users){
//            System.out.println("User : "+user.getUsername() + " ID : "+user.getId());
//        }
//        System.out.println("Enter the ID of the user you are sending to..");
//
//        transfer.setAccountTo(scan.nextLong());
//
//        System.out.println("Enter the amount of moola you're sending to the user..");
//
//        transfer.setAmount(scan.nextDouble());
//
//        transfer.setTransferTypeId(2L);
//        transfer.setTransferStatusId(2L);
//    }
//
//    public Transfer[] getAllTransferById(Long id){
//        Transfer[] transfers = null;
//        try{
//            transfers = restTemplate.exchange(API_BASE_URL+"transfer/"+id,
//                    HttpMethod.GET,
//                    makeAuthEntity(),
//                    Transfer[].class).getBody();
//        }catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        return transfers;
//    }
//
//    public List<Transfer> getAllTransferbyId(Long id){
//        List<Transfer> transfers = new ArrayList<>();
//        try{
//            transfers = restTemplate.getForObject(API_BASE_URL+"transfers/"+id, List.class);
//        }catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        return transfers;
//    }
//
//
//
//    public List<Transfer> getAllTransfer(){
//        List<Transfer> transfers = new ArrayList<>();
//        try{
//            transfers = restTemplate.getForObject(API_BASE_URL+"transfers/", List.class);
//        }catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        return transfers;
//    }
//
//    public void printTransfers() {
//        List<Transfer> list = getAllTransfer();
//        for (Transfer transfer : list) {
//            System.out.println(transfer.getTransferId());
//        }
//    }}





//    public List<Transfer> getAllTransfer(){
//        List<Transfer> transfers = new ArrayList<>();
//        try{
//            transfers = restTemplate.getForObject(API_BASE_URL+"transfer", List.class);
//        }catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        return transfers;
//    }