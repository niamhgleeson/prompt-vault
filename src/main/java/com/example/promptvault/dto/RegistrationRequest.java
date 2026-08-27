package com.example.promptvault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {

    @NotBlank
    @Size(
            min = 1,
            max = 100
    )
    private String name;

    @NotBlank
    @Size(
            min = 1,
            max = 100
    )
    private String surname;

    @NotBlank
    @Size(
            min = 3,
            max = 100
    )
    private String username;

    @Email
    @NotBlank
    @Size(
            max = 255
    )
    private String email;

    @NotBlank
    @Size(
            min = 15,
            max = 100
    )
    private String password;

    public RegistrationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(
            String surname
    ) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username
    ) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password
    ) {
        this.password = password;
    }
}