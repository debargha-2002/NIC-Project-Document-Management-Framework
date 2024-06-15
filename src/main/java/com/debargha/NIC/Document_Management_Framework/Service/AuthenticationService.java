package com.debargha.NIC.Document_Management_Framework.Service;

import com.debargha.NIC.Document_Management_Framework.Model.Client;
import com.debargha.NIC.Document_Management_Framework.Payload.RegisterRequest;
import com.debargha.NIC.Document_Management_Framework.Payload.initRequest;
import com.debargha.NIC.Document_Management_Framework.Payload.initResponse;
import com.debargha.NIC.Document_Management_Framework.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientRepository ClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.debargha.NIC.Document_Management_Framework.JWT.JWTService JWTService;
    private final AuthenticationManager authenticationManager;

    public initResponse register(RegisterRequest request){
        var client = Client.builder()
                .clientId(request.getClientId())
                .clientSecret(passwordEncoder.encode(request.getClientSecret()))
                .build();
        client.setCreated_on(LocalDateTime.now());
        client.setExpiry_on(LocalDateTime.now().plusYears(2));
        ClientRepository.save(client);
        var jwtToken = JWTService.generateToken(client.getClientId());
        return initResponse.builder().token(jwtToken).build();
    }

    public initResponse authenticate(initRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getClientId(), request.getClientSecret())
        );
        var client = ClientRepository.findByClientId(request.getClientId()).orElseThrow();

        var jwtToken = JWTService.generateToken(client.getClientId());
        return (initResponse.builder().token(jwtToken).build());
    }
}
