package com.debargha.NIC.Document_Management_Framework.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Getter
@Data
@Builder
@Setter
@Document(collection="client")
public class Client implements UserDetails {
    @Id
    private String clientId;
    private String clientSecret;
   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
   @Field("created_on")
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime created_on;
    @Field("expiry_on")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiry_on;

//    public Client() {
//        this.created_on = LocalDateTime.now(); // Set current date and time
//        this.expiry_on = this.created_on.plusYears(1); // Set expiry 1 year after createdOn
//    }

    // Implement UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Implement logic to return authorities/roles if needed
        return null;
    }

    @Override
    public String getPassword() {
        return clientSecret;
    }

    @Override
    public String getUsername() {
        return clientId;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Implement logic if needed
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Implement logic if needed
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Implement logic if needed
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Implement logic if needed
        return true;
    }

    // Getters and setters
//    public String getClientId() {
//        return clientId;
//    }
//
//    public void setClientId(String clientId) {
//        this.clientId = clientId;
//    }
//
//    public String getClientSecret() {
//        return clientSecret;
//    }
//
//    public void setClientSecret(String clientSecret) {
//        this.clientSecret = clientSecret;
//    }
//
//    public LocalDateTime getCreatedOn() {
//        return createdOn;
//    }
//
//    public void setCreatedOn(LocalDateTime createdOn) {
//        this.createdOn = createdOn;
//    }
//
//    public LocalDateTime getExpiryOn() {
//        return expiryOn;
//    }
//
//    public void setExpiryOn(LocalDateTime expiryOn) {
//        this.expiryOn = expiryOn;
//    }


}
