package com.example.pupilmed.models.database.visit;

import com.example.pupilmed.models.database.pet.Pet;
import com.example.pupilmed.models.database.recommendation.Recommendation;
import com.example.pupilmed.models.database.vet.Vet;
import jakarta.persistence.*;
import lombok.Builder;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "wizyta")
@Builder
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="data")
    private Date date;
    @Column(name="godzina")
    private Time hour;
    @Column(name="typ_wizyty")
    private String visitType;
    @Column(name="cena")
    private int price;
    @ManyToOne
    @JoinColumn(name = "weterynarzid", referencedColumnName = "id")
    private Vet vet;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "zalecenieid", referencedColumnName = "id")
    private Recommendation recommendation;

    @ManyToOne
    @JoinColumn(name = "zwierzeid", referencedColumnName = "id")
    private Pet pet;

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", visitType='" + visitType + '\'' +
                ", price='" + price + '\'' +
                ", vet=" + vet +
                ", pet=" + pet +
                ", recommendation=" + recommendation +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getHour() {
        return hour;
    }

    public void setHour(Time hour) {
        this.hour = hour;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Vet getVet() {
        return vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public Visit(int id, Date date, Time hour, String visitType, int price, Vet vet, Pet pet, Recommendation recommendation) {
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.visitType = visitType;
        this.price = price;
        this.vet = vet;
        this.pet = pet;
        this.recommendation = recommendation;
    }

    public Visit(Date date, Time hour, String visitType, int price, Vet vet, Pet pet, Recommendation recommendation) {
        this.date = date;
        this.hour = hour;
        this.visitType = visitType;
        this.price = price;
        this.vet = vet;
        this.pet = pet;
        this.recommendation = recommendation;
    }

    public Visit(Date date, Time hour, String visitType, int price, Vet vet, Pet pet) {
        this.date = date;
        this.hour = hour;
        this.visitType = visitType;
        this.price = price;
        this.vet = vet;
        this.pet = pet;
    }

    public Visit() {
    }
}
