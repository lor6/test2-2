package com.stackify.models;

import org.springframework.hateoas.ResourceSupport;

public class User extends ResourceSupport {
    private String email;
    private String name;

    public User() {
    }

    public User(String email, String name) {
        super();
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
