package com.debargha.NIC.Document_Management_Framework.Controller;

import com.debargha.NIC.Document_Management_Framework.JWT.JWTService;
import com.debargha.NIC.Document_Management_Framework.dto.RegisterRequest;
import com.debargha.NIC.Document_Management_Framework.dto.initRequest;
import com.debargha.NIC.Document_Management_Framework.dto.initResponse;
import com.debargha.NIC.Document_Management_Framework.Repository.ClientRepository;
import com.debargha.NIC.Document_Management_Framework.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class InitAuthController {

    private final AuthenticationService service;
    @Autowired
    private final ClientRepository clientRepository;
    @Autowired
    private JWTService jwtUtil;

    @PostMapping("/init")
    public ResponseEntity<initResponse> init(@RequestBody initRequest request) {
        if (clientRepository.findByClient_id(request.getClient_id()).isPresent()) {
            try {
                initResponse response = service.authenticate(request);
                response.setMessage("Logged in successfully");
                System.out.println("Logged in successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setClient_id(request.getClient_id());
            registerRequest.setClient_secret(request.getClient_secret());

            try {
                initResponse response = service.register(registerRequest);
                response.setMessage("New User created and logged in");
                System.out.println("New User created and logged in: ");
                return new ResponseEntity<>(response, HttpStatus.CREATED);

            } catch (Exception e) {
                System.out.println("Bad Request");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
