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
import com.abel.wallet.api.entities.User;
import com.abel.wallet.api.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/createUser", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> createNewUser(@RequestBody User user) {
    	Response response = userService.createUser(user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getPhoneNumber(),
                user.getPassword(), user.getUsername());
    	HttpStatus httpStatus = response.getResponseCode().equals("00")?HttpStatus.OK:HttpStatus.BAD_REQUEST;
        return new ResponseEntity<Response>(response, httpStatus);
    }//userService.createUser(user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getPhoneNumber(),
    //user.getPassword(), user.getUsername())

    @RequestMapping(value= "/validateEmail", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE},
    produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> validateEmail(
            @RequestBody User user) throws InstantiationException, IllegalAccessException {
        return new ResponseEntity<Response>(userService.validateEmail(user.getEmailAddress(), user.getToken()), HttpStatus.OK);
    }

}
