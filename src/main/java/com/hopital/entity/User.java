package com.hopital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private Integer idRole;
    private LocalDateTime createdAt;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getCreatedAt() {
        return createdAt == null ? null : createdAt.toString();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = (createdAt == null || createdAt.isBlank()) ? null : Timestamp.valueOf(createdAt).toLocalDateTime();
    }
}
