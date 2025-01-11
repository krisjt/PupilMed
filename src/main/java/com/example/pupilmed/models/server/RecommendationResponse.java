package com.example.pupilmed.models.server;

import java.sql.Time;
import java.util.Date;

public record RecommendationResponse(Integer id, String recommendation, Date visitDate, Time hour) {
}
