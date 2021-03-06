package com.abel.wallet.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abel.wallet.api.entities.Response;
import com.abel.wallet.api.entities.Transactions;
import com.abel.wallet.api.service.TransferService;

@RestController
public class TransferController {

    @Autowired
    TransferService transferService;

    @RequestMapping(value= "/walletToWallet", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> walletToWallet(
            @RequestBody Transactions transfer) throws InstantiationException, IllegalAccessException {
        return new ResponseEntity<Response>(transferService.walletToWallet(transfer.getSourceAccount(),
                transfer.getBeneficiaryAccount(), transfer.getAmount(), transfer.getSourceEmail()), HttpStatus.OK);
    }

    @RequestMapping(value= "/transferViaEmail", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> transferViaEmail(
            @RequestBody Transactions transfer) throws InstantiationException, IllegalAccessException {
        return new ResponseEntity<Response>(transferService.transferViaEmail(transfer.getSourceEmail(),
                transfer.getBeneficiaryEmail(), transfer.getAmount()), HttpStatus.OK);
    }

    @RequestMapping(value= "/topUpWallet", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> topUpWallet(
            @RequestBody Transactions transfer) throws InstantiationException, IllegalAccessException {
        return new ResponseEntity<Response>(transferService.topUpWallet(transfer.getSourceEmail(),
                transfer.getAmount()), HttpStatus.OK);
    }
}
