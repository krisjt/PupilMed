package com.example.pupilmed.models.database.recommendation;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "zalecenie")
@Builder
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="zalecenie", length = 1000)
    private String recommendation;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", recommendation='" + recommendation + '\'' +
                '}';
    }

    public Recommendation(int id, String recommendation) {
        this.id = id;
        this.recommendation = recommendation;
    }

    public Recommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Recommendation() {
    }
}
