package com.shorewreaks.shorewreaks.shorewreaks;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private String username, email, name, lastname;

    public Users(String username, String email, String name, String lastname) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("name",name);
        result.put("lastname",lastname);
        result.put("email",email);

        return result;
    }
}
