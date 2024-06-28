package com.debargha.NIC.Document_Management_Framework.Service;

import com.debargha.NIC.Document_Management_Framework.Model.Client;
import com.debargha.NIC.Document_Management_Framework.Model.Role;
import com.debargha.NIC.Document_Management_Framework.dto.RegisterRequest;
import com.debargha.NIC.Document_Management_Framework.dto.initRequest;
import com.debargha.NIC.Document_Management_Framework.dto.initResponse;
import com.debargha.NIC.Document_Management_Framework.Repository.ClientRepository;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                .client_id(request.getClient_id())
                .client_secret(passwordEncoder.encode(request.getClient_secret()))
                .build();
        client.setCreated_on(LocalDateTime.now());
        client.setExpiry_on(LocalDateTime.now().plusYears(2));
        client.setRole(Role.USER);
        ClientRepository.save(client);
        var jwtToken = JWTService.generateToken(client.getClient_id());
        return initResponse.builder().token(jwtToken).build();
    }

    public initResponse authenticate(initRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getClient_id(), request.getClient_secret())
        );
        var client = ClientRepository.findByClient_id(request.getClient_id()).orElseThrow();

        var jwtToken = JWTService.generateToken(client.getClient_id());
        return (initResponse.builder().token(jwtToken).build());
    }
}
