package com.odyssey.services;

import com.odyssey.models.Location;

import java.time.LocalDateTime;

public class FlightNamingService {

    public static String getFlightName(Location origin, Location destination, LocalDateTime departure) {
        String originName = origin.getCity().toUpperCase();
        String destinationName = destination.getCity().toUpperCase();
        String time = departure.toString().replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll(" ", "")
                .replaceAll("\\.", "");
        String name = (originName.length() >= 3 ? originName.substring(0, 3) : originName) + "-" +
                (destinationName.length() >= 3 ? destinationName.substring(0, 3) : destinationName) + "@" +
                time;
        return name;
    }

}
