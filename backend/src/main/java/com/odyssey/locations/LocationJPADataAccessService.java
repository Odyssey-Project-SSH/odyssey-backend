package com.odyssey.locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Repository("locationJPAService")
public class LocationJPADataAccessService implements LocationDao {

    private final LocationRepository locationRepository;

    public LocationJPADataAccessService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> selectAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Optional<Location> selectLocationById(Integer id) {
        return locationRepository.findById(id);
    }

    @Override
    public void insertLocation(Location location){locationRepository.save(location);
    }

    @Override
    public boolean existsLocationById(Integer id) {
        return locationRepository.existsLocationById(id);
    }

    @Override
    public boolean existsLocationByCityAndCountry(String city, String country) {
        return locationRepository.existsByCityAndCountry(city, country);
    }

    @Override
    public void deleteLocationById(Integer id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void updateLocation(Location location) {
        locationRepository.save(location);
    }
}
