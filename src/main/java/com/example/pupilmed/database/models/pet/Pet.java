package com.example.pupilmed.database.models.pet;

import com.example.pupilmed.database.models.owner.Owner;
import jakarta.persistence.*;
import lombok.Builder;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

@Entity
@Table(name = "zwierze")
@Builder
public class Pet {
    @Id
    private int id;
    @Column(name="imie", length = 100)
    private String name;
    @Column(name="gatunek", length = 100)
    private String kind;
    @Column(name="rasa")
    private String type;

    @Column(name="data_urodzenia")
    private Date dateOfBirth;

    @Column(name="uwagi_o_zwierzeciu")
    private String additionalInfo;
    @ManyToOne
    @JoinColumn(name = "wlascicielid", referencedColumnName = "id")
    private Owner owner;


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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDate_of_birth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Owner getOwnerID() {
        return owner;
    }

    public Integer getAge(){
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(dateOfBirth);

        Calendar now = Calendar.getInstance();

        return now.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
    }

    public void setOwnerID(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", type='" + type + '\'' +
                ", date_of_birth='" + dateOfBirth + '\'' +
                ", additional_info='" + additionalInfo + '\'' +
                ", ownerID='" + owner + '\'' +
                '}';
    }

    public Pet(String name, String kind, String type, Date date_of_birth, String additional_info, Owner owner) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.dateOfBirth = date_of_birth;
        this.additionalInfo = additional_info;
        this.owner = owner;
    }

    public Pet(int id, String name, String kind, String type, Date date_of_birth, String additional_info, Owner ownerID) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.dateOfBirth = date_of_birth;
        this.additionalInfo = additional_info;
        this.owner = ownerID;
    }

    public Pet() {
    }
}
//TODO usuwanie kaskadowe jak robic