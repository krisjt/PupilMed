package com.example.pupilmed.database.models.vet;

import com.example.pupilmed.database.models.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Weterynarz")
public class Vet {
    @Id
    private int id;
    @Column(name="imie")
    private String name;
    @Column(name="nazwisko")
    private String surname;
    @Column(name="nazwa_kliniki")
    private String clinicName;
    @Column(name="adres_kliniki")
    private String clinicAddress;

//    @Column(name="uzytkownikid")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uzytkownikid", referencedColumnName = "id")
    private User user;

    public Vet() {
    }

    public Vet(String name, String surname,
               String clinicName, String clinicAddress,
               User user) {
        this.name = name;
        this.surname = surname;
        this.clinicName = clinicName;
        this.clinicAddress = clinicAddress;
        this.user = user;
    }

    public Vet(int id, String name, String surname,
               String clinicName, String clinicAddress,
               User user) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.clinicName = clinicName;
        this.clinicAddress = clinicAddress;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Weterynarz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", clinicName='" + clinicName + '\'' +
                ", clinicAddress='" + clinicAddress + '\'' +
                ", user=" + user +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUserId(User userId) {
        this.user = userId;
    }

}

//@Transient jak nie chcemy zeby bylo w bazie