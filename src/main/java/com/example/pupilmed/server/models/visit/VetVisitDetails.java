package com.example.pupilmed.server.models.visit;

import com.example.pupilmed.database.models.recommendation.Recommendation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public record VetVisitDetails(Date date, Date hour, String visitType, String ownerName, String ownerSurname,
                              String vetName, String vetSurname, String ownerPhoneNumber, String vetPhoneNumber,
                              Integer price, String petName, String petType, String petKind, Integer petAge, Recommendation recommendation) {
}

//date - wizyta
//hour - wizyta
//typ_wizyty - wizyta
//vet imie nazwisko i numer telefonu - wizyta
//owner imie nazwisko i numer telefpnu - przez zwirrze do owner
//cena - wizyta
//gatunek wiek rasa i imie - wizyta
