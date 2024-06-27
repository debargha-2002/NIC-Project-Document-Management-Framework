package com.debargha.NIC.Document_Management_Framework.Service;

import com.debargha.NIC.Document_Management_Framework.Config.DateConfig;
import com.debargha.NIC.Document_Management_Framework.Model.Client;
import com.debargha.NIC.Document_Management_Framework.Repository.ClientRepository;
import com.debargha.NIC.Document_Management_Framework.dto.SignUpRequestDto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
     @Autowired
    private ClientRepository clientRepository;

    private static final int OTP_LENGTH = 4;

    public Optional<Client> signup(String client_id, SignUpRequestDto signUpRequest) {
        if (!DateConfig.isValidDate(signUpRequest.getDob())) {
            throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy");
        }
        Optional<Client> client = clientRepository.findByClient_id(client_id);
        if (client.isEmpty()) {
            System.out.println("Client not found");
            throw new IllegalArgumentException("Client_id not found");
        }
        else{
            client.map(client1->{ client1.setMobile_no(signUpRequest.getMobile_No());
                                  client1.setEmail_id(signUpRequest.getEmail_Id());
                                  client1.setName(signUpRequest.getName());
                                  client1.setGender(signUpRequest.getGender());
                                  client1.setDob(signUpRequest.getDob());
                                  client1.setAddress(signUpRequest.getAddress());
                                  return clientRepository.save(client1);

                });

            return client;
        }

    }

    public String generateOtp() {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            digits.add(i);
        }

        Collections.shuffle(digits);

        StringBuilder otpBuilder = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otpBuilder.append(digits.get(i));
        }

        return otpBuilder.toString();
    }

    public Optional<String> login(long mobile_no) {
        Optional<Client> optionalClient = clientRepository.findByMobile_no(mobile_no);
        return optionalClient.isPresent() ? Optional.of(generateOtp()) : Optional.empty();
    }

}
