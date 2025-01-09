package com.example.pupilmed.server.models;

public class VetResponse {
    private String name;
    private String surname;
    private String clinic_name;
    private String clinic_address;
    private String phone_number;

    public VetResponse(String name, String surname, String clinic_name, String clinic_address, String phone_number) {
        this.name = name;
        this.surname = surname;
        this.clinic_name = clinic_name;
        this.clinic_address = clinic_address;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getClinic_address() {
        return clinic_address;
    }

    public void setClinic_address(String clinic_address) {
        this.clinic_address = clinic_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
