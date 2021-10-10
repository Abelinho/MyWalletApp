package com.abel.wallet.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abel.wallet.api.entities.Response;
import com.abel.wallet.api.entities.Transactions;
import com.abel.wallet.api.entities.User;
import com.abel.wallet.api.entities.Wallet;
import com.abel.wallet.api.repo.TransactionRepo;
import com.abel.wallet.api.repo.UserRepo;
import com.abel.wallet.api.repo.WalletRepo;
import com.abel.wallet.api.util.WalletUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransferService  {

    @Autowired
    UserRepo userRepo;
    @Autowired
    WalletRepo walletRepo;
    @Autowired
    TransactionRepo transactionRepo;
    Response resp = new Response();

    public Response walletToWallet(String sourceAccount, String beneficiaryAccount, Double amount, String sourceEmail) throws IllegalAccessException, InstantiationException {
        Wallet sourceWallet = new Wallet();//create a new source wallet
        Wallet beneficiaryWallet = new Wallet();//create a beneficiary wallet

          User user = userRepo.findByEmailAddress(sourceEmail);//get user with the email supplied
        
        if(WalletUtils.isNotNull(user)) {//if user is not null
        	
        	 sourceWallet = walletRepo.findByWalletNumber(sourceAccount);//get wallet with the src acct supplied
        	 beneficiaryWallet = walletRepo.findByWalletNumber(beneficiaryAccount); //get benefic wallet using ben acct supplied 

        }
        else{//if user is null
            resp.setResponseCode("07");
            resp.setResponseMessage("User Not Found");
            return resp;
        }



       if(WalletUtils.isNotNull(beneficiaryWallet)) {//if beneficiary wallet is not null ie if the beneficiary acct supplied in the request body has an associated wallet
            if (sourceWallet.getMaximumWithdrawal() > amount) {//if maximum withdrawable amount is not exceeded
                if (sourceWallet.getWalletBalance() > amount) {//if available balance in wallet is not exceeded
                    //debit and update source wallet
                    Double newBal = sourceWallet.getWalletBalance() - amount;
                    sourceWallet.setWalletBalance(newBal);
                    walletRepo.save(sourceWallet);
                    
                    
                    //credit and update beneficiary wallet
                    Double newBal2 = beneficiaryWallet.getWalletBalance() + amount;
                    beneficiaryWallet.setWalletBalance(newBal2);

                    walletRepo.save(beneficiaryWallet);
                    
                    //save transaction
                    Transactions trans = new Transactions();//create a new transaction
                    trans.setAmount(amount);
                    trans.setBeneficiaryAccount(beneficiaryAccount);
                    trans.setBeneficiaryWallet(beneficiaryWallet);
                    trans.setSourceWallet(sourceWallet);
                    trans.setSourceUser(user);
                    transactionRepo.save(trans);
                    
                    //return response
                    resp.setResponseCode("00");
                    resp.setResponseMessage("Transfer Successful");
                } else {
                    resp.setResponseCode("05");
                    resp.setResponseMessage("Insufficient Funds");
                }
            } else {
                resp.setResponseCode("06");
                resp.setResponseMessage("Amount is greater than maximum transfer limit");
            }
        }
        else{
            resp.setResponseCode("08");
            resp.setResponseMessage("Invalid Beneficiary Account");
        }
        return resp;
    }

    public Response transferViaEmail(String sourceEmail, String beneficiaryEmail, Double amount) throws IllegalAccessException, InstantiationException {
        Wallet sourceWallet = new Wallet();//create a new source wallet
        Wallet beneficiaryWallet = new Wallet();//create a new beneficiary wallet

        User sourceUser = userRepo.findByEmailAddress(sourceEmail);//get src user with the email addr supplied
        User beneficiaryUser = userRepo.findByEmailAddress(beneficiaryEmail);//get ben user with the email addr supplied	
        
        if(WalletUtils.isNotNull(sourceUser) && WalletUtils.isNotNull(beneficiaryUser)) {//if source and beneficiary user are not null
        	
        	sourceWallet = walletRepo.findByUser(sourceUser);//get the src user's wallet
        	beneficiaryWallet = walletRepo.findByUser(beneficiaryUser);//get the benef user's wallet	
        }
        else{
            resp.setResponseCode("07");
            resp.setResponseMessage("User Not Found");
            return resp;
        }

        if(sourceWallet.getMaximumWithdrawal() > amount){//if max withdrawable amount is not exceeded
            if(sourceWallet.getWalletBalance() > amount) {//if wallet balance is not exceeded
                //debit and update source wallet
                Double newBal = sourceWallet.getWalletBalance() - amount;
                sourceWallet.setWalletBalance(newBal);
                walletRepo.save(sourceWallet);
                
                
                //credit and update beneficiary wallet
                Double newBal2 = beneficiaryWallet.getWalletBalance() + amount;
                beneficiaryWallet.setWalletBalance(newBal2);
                walletRepo.save(beneficiaryWallet);
                
                //save transaction
                Transactions trans = new Transactions();
                trans.setAmount(amount);
                trans.setSourceAccount(sourceWallet.getWalletNumber());
                trans.setBeneficiaryAccount(beneficiaryWallet.getWalletNumber());
                trans.setBeneficiaryWallet(beneficiaryWallet);
                trans.setSourceWallet(sourceWallet);
                trans.setSourceUser(sourceUser);
                trans.setBeneficiaryUser(beneficiaryUser);
                transactionRepo.save(trans);
                
                //return response
                resp.setResponseCode("00");
                resp.setResponseMessage("Transfer Successful");
            }
            else{
                resp.setResponseCode("05");
                resp.setResponseMessage("Insufficient Funds");
            }
        }
        else{
            resp.setResponseCode("06");
            resp.setResponseMessage("Amount is greater than maximum transfer limit");
        }

        return resp;
    }

    public Response topUpWallet(String email, Double amount) throws InstantiationException, IllegalAccessException {

    	User user = userRepo.findByEmailAddress(email);//get the user with the supplied email 
    	
        if(WalletUtils.isNotNull(user)){//if User is not null
        	Wallet userWallet = walletRepo.findByUser(user);
        
            Double topUp = userWallet.getWalletBalance() + amount;
            userWallet.setWalletBalance(topUp);//top up wallet and update wallet balance
            walletRepo.save(userWallet);
            
            
            resp.setResponseCode("00");
            resp.setResponseMessage("Top Up Successful");
        }
        else{
            resp.setResponseCode("07");
            resp.setResponseMessage("User Not Found");
        }

        return resp;
    }
}
