package com.example.pupilmed.models.database.owner;

import com.example.pupilmed.models.database.user.User;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "wlasciciel")
@Builder
public class Owner {
    @Id
    private int id;
    @Column(name="imie")
    private String name;
    @Column(name="nazwisko")
    private String surname;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uzytkownikid", referencedColumnName = "id")
    private User user;

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
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

    public User getUser() {
        return user;
    }

    public void setUserID(User userID) {
        this.user = userID;
    }

    public Owner() {
    }

    public Owner(String name, String surname, User userID) {
        this.name = name;
        this.surname = surname;
        this.user = userID;
    }

    public Owner(int id, String name, String surname, User userID) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.user = userID;
    }
}
