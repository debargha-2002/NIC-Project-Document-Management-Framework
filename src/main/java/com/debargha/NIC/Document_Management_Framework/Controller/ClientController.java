package com.debargha.NIC.Document_Management_Framework.Controller;


import com.debargha.NIC.Document_Management_Framework.Model.Client;
import com.debargha.NIC.Document_Management_Framework.Service.ClientService;
import com.debargha.NIC.Document_Management_Framework.dto.SignUpRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ClientController {
    @Autowired
    ClientService clientService;

    @PostMapping("/signup")
    public ResponseEntity<Client> signup(@RequestParam String clientId, @RequestBody SignUpRequestDto signUpRequest) {
        System.out.println("signUpRequest received");
        return clientService.signup(clientId, signUpRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam long mobileNo) {
        System.out.println("login request received");
        Optional<String> otp = clientService.login(mobileNo);
        if(otp.isPresent()) {
            return ResponseEntity.ok(otp.get());
        }
        else{
            return ResponseEntity.badRequest().body("Invalid mobile no. / Mobile no. not found");
        }


    }
}
