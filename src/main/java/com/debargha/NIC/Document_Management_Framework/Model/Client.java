package com.debargha.NIC.Document_Management_Framework.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
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
    private String client_id;
    private String client_secret;
   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
   @Field("created_on")
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime created_on;
    @Field("expiry_on")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiry_on;

    // Implement UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Implement logic to return authorities/roles if needed
        return null;
    }

    @Override
    public String getPassword() {
        return client_secret;
    }

    @Override
    public String getUsername() {
        return client_id;
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



}
