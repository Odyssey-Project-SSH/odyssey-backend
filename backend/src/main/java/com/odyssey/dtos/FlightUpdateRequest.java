package com.odyssey.dtos;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record FlightUpdateRequest(
        LocalDateTime departure, Integer originId, Integer destinationId
) {
}
