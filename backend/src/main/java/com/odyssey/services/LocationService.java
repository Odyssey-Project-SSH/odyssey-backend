package com.odyssey.services;

import com.odyssey.services.cloudinary.CloudinaryService;
import com.odyssey.daos.LocationDao;
import com.odyssey.dtos.LocationRegistrationDto;
import com.odyssey.services.file.FileService;
import com.odyssey.dtos.LocationUpdateDto;
import com.odyssey.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.odyssey.exception.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private final CloudinaryService cloudinaryService;
    private final LocationDao locationDao;

    public LocationService(
            CloudinaryService cloudinaryService,
            @Qualifier("locationJPAService") LocationDao locationDao
    ) {
        this.cloudinaryService = cloudinaryService;
        this.locationDao = locationDao;
    }

    public List<Location> getAllLocations() {
        return locationDao.selectAllLocations();
    }

    public Location getLocation(Integer id) {
        return locationDao.selectLocationById(id)
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(id)));
    }

    public void addLocation(LocationRegistrationDto dto) {
        if (locationDao.existsLocationByCityAndCountry(dto.city(), dto.country())) {
            throw new DuplicateResourceException("location already exists");
        }
        File file = FileService.convertFile(dto.file());
        try {
            String url = cloudinaryService.uploadImage(file, "locations");
            Location location = new Location(
                    dto.city(),
                    dto.country(),
                    url
            );
            locationDao.insertLocation(location);
        }
        catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }
    }

    public void deleteLocation(Integer id) {
        if (locationDao.existsLocationById(id)) {
            locationDao.deleteLocationById(id);
        } else {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(id));
        }
    }

    public void updateLocation(Integer id, LocationUpdateDto dto) {
        Location location = getLocation(id);

        boolean changes = false;

        if (dto.city() != null && !dto.city().equals(location.getCity())) {
            location.setCity(dto.city());
            changes = true;
        }
        if (dto.country() != null && !dto.country().equals(location.getCountry())) {
            location.setCountry(dto.country());
            changes = true;
        }

        try {
            if (dto.file() != null) {
                File file = FileService.convertFile(dto.file());
                String newUrl = cloudinaryService.uploadImage(file, "locations");
                cloudinaryService.deleteImageByUrl(location.getPicture());
                location.setPicture(newUrl);
                changes = true;
            }
        } catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        locationDao.updateLocation(location);
    }
}
