package com.abel.wallet.api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abel.wallet.api.entities.Response;
import com.abel.wallet.api.entities.Token;
import com.abel.wallet.api.entities.User;
import com.abel.wallet.api.entities.Wallet;
import com.abel.wallet.api.repo.TokenRepo;
import com.abel.wallet.api.repo.UserRepo;
import com.abel.wallet.api.repo.WalletRepo;
import com.abel.wallet.api.util.WalletUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    TokenRepo tokenRepo;
    @Autowired
    WalletRepo walletRepo;

    public Response createUser(String firstName, String lastName, String emailAddress, String phoneNumber,
                               String password, String username) {
        User user = new User();//create a new user 
        Token token = new Token();//create a new token
        WalletUtils utils = new WalletUtils();//create a new ippmsUtils 
        Response resp = new Response();//create a response object 
        String code;
        //int retVal = 0;

        List<User> allUsersList = userRepo.findAll(); //this.genericService.loadControlEntity(User.class);//load all users
        User existingUser = allUsersList.stream()
                .filter(users -> emailAddress.equals(users.getEmailAddress()))//get user with the email address supplied
                .findAny()
                .orElse(null);

        if(WalletUtils.isNull(existingUser)) {//if there is no user with the supplied email address, create the user
        	
        	//ensure/check that all user details are supplied 
            if(WalletUtils.isNotNullOrEmpty(firstName) && WalletUtils.isNotNullOrEmpty(lastName) && WalletUtils.isNotNullOrEmpty(emailAddress)
                    && WalletUtils.isNotNullOrEmpty(phoneNumber) && WalletUtils.isNotNullOrEmpty(password)  && WalletUtils.isNotNullOrEmpty(username)) {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmailAddress(emailAddress);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(password);
                user.setUsername(username);
                userRepo.save(user);
                
                //this method creates User Wallet
		        createUserWallet(user);

		        //this method sends mail to user for email validation
                try {
                    code = utils.generateUniqueId();
                    utils.sendMail(emailAddress, code);
                    token.setEmailAddress(emailAddress);
                    token.setToken(code);
                    tokenRepo.save(token);
                    
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

                resp.setResponseCode("00");
                resp.setResponseMessage("User Created Successfully!");
            }
            else {
                resp.setResponseCode("03");
                resp.setResponseMessage("Null Value/s Cannot Be passed");
            }

        }
        else {
            resp.setResponseCode("02");
            resp.setResponseMessage("User Already Exist!");
        }

        return resp;
    }

    private void createUserWallet(User user) {
        String walletno;

        //generate wallet number
        walletno = WalletUtils.GeneratingRandomNumericString();

        Wallet wallet = new Wallet();//create a new wallet object

        walletno = WalletUtils.GeneratingRandomNumericString();
		wallet.setUser(user);
        wallet.setWalletNumber(walletno);
        wallet.setMaximumWithdrawal(20000);
        walletRepo.save(wallet);
    }

    public Response validateEmail(String emailAddress, String token) throws IllegalAccessException, InstantiationException {
        Response resp = new Response();
        int retVal = 0;
        
        Token tokenExist = tokenRepo.findByEmailAddressAndToken(emailAddress, token);

        if(WalletUtils.isNotNull(tokenExist)){//if token exists
            User user = new User();
            Wallet wallet = new Wallet();

            user.setEmailAddress(emailAddress);
            user.setEmailVerificationInd(1);//indicate that email has been verified

            wallet.setMaximumWithdrawal(50000);
            walletRepo.save(wallet);

            resp.setResponseCode("00");
            resp.setResponseMessage("Email Validation Successful");
        }
        else{
            resp.setResponseCode("04");
            resp.setResponseMessage("Invalid token/user");
        }

        return resp;
    }


}
