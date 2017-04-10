package com.example.gaya.searchpeople;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class DBclass extends TableServiceEntity {
    public DBclass(String username, String password) {
        this.partitionKey = password;
        this.rowKey = username;
    }

    public DBclass() {
    }

    private String email;
    private String phoneNumber;
    private String name;
    private String surname;
    private String age;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}