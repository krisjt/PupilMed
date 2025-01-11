package com.example.pupilmed.models.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "typ_wizyty")
@Builder
public class VisitType {
    @Id
    @Column(name="typ_wizyty")
    String visitType;
    @Column(name="koszt")
    Integer price;

    @Override
    public String toString() {
        return "VisitType{" +
                "visitType='" + visitType + '\'' +
                ", price=" + price +
                '}';
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public VisitType(String visitType, Integer price) {
        this.visitType = visitType;
        this.price = price;
    }

    public VisitType() {
    }
}
