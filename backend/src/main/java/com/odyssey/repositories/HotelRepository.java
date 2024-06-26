package com.odyssey.repositories;

import com.odyssey.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    boolean existsHotelById(Integer id);
    boolean existsHotelByNameAndLocationId(String name, Integer locationId);
    List<Hotel> findByLocationId(Integer locationId);
}
