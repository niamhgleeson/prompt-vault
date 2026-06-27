package com.example.promptvault.model;

//imports persistence api to save data permanently
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

//entity is a database table. Make User a table
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(
            strategy =
                    GenerationType.IDENTITY
    )
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    //to add extra rules, you need to insert @Column, so because we want it to be unique, it's there.
    @NotBlank
    @Column(unique=true)
    private String username;

    @Email
    @NotBlank
    @Column(unique=true)
    @JsonIgnore
    private String email;

    @NotBlank
    @JsonProperty(
            access =
                    JsonProperty.Access.WRITE_ONLY
    )
    private String password;

    private String role;

    private boolean enabled;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name) {
        this.name=name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(
            String surname) {
        this.surname=surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username) {
        this.username=username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email) {
        this.email=email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password) {
        this.password=password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(
            String role) {
        this.role=role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(
            boolean enabled) {
        this.enabled=enabled;
    }
}
