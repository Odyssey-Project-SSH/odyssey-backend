package com.odyssey.localCuisine;

import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.exception.UnprocessableEntityException;
import com.odyssey.fileService.FileService;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class LocalCuisineService {

    private final LocalCuisineDao localCuisineDao;
    private final LocationDao locationDao;
    private final CloudinaryService cloudinaryService;

    public LocalCuisineService(
            @Qualifier("localCuisineJPAService") LocalCuisineDao localCuisineDao,
            @Qualifier("locationJPAService") LocationDao locationDao,
            CloudinaryService cloudinaryService
    ) {
        this.localCuisineDao = localCuisineDao;
        this.locationDao = locationDao;
        this.cloudinaryService = cloudinaryService;
    }

    public List<LocalCuisine> getAllLocalCuisines() {
        return localCuisineDao.selectAllLocalCuisines();
    }

    public List<LocalCuisine> getLocalCuisinesByLocationId(Integer locationId) {
        if (!locationDao.existsLocationById(locationId)) {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(locationId));
        }
        return localCuisineDao.selectLocalCuisinesByLocationId(locationId);
    }

    public LocalCuisine getLocalCuisine(Integer id) {
        return localCuisineDao.selectLocalCuisineById(id)
                .orElseThrow(() -> new ResourceNotFoundException("local cuisine with id [%s] not found".formatted(id)));
    }

    public void addLocalCuisine(LocalCuisineRegistrationDto dto) {
        if (localCuisineDao.existsLocalCuisineByNameAndLocationId(dto.name(), dto.locationId())) {
            throw new DuplicateResourceException("local cuisine already exists");
        }
        Location location = locationDao.selectLocationById(dto.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(dto.locationId())));

        File file = FileService.convertFile(dto.image());

        try {
            String url = cloudinaryService.uploadImage(file, "localCuisine");
            LocalCuisine localCuisine = new LocalCuisine(
                    dto.name(), dto.description(), url, location
            );
            localCuisineDao.insertLocalCuisine(localCuisine);
        }
        catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }
    }

    public void deleteLocalCuisine(Integer id) {
        if (localCuisineDao.existsLocalCuisineById(id)) {
            localCuisineDao.deleteLocalCuisineById(id);
        } else {
            throw new ResourceNotFoundException("local cuisine with id [%s] not found".formatted(id));
        }
    }

    public void updateLocalCuisine(Integer id, LocalCuisineUpdateDto dto) {
        LocalCuisine existingLocalCuisine = getLocalCuisine(id);
        if (localCuisineDao.existsLocalCuisineByNameAndLocationId(dto.name(), dto.locationId())) {
            throw new DuplicateResourceException("local cuisine already exists");
        }
        Location location = locationDao.selectLocationById(dto.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".formatted(dto.locationId())));

        boolean changes = false;

        if (dto.name() != null && !dto.name().equals(existingLocalCuisine.getName())) {
            existingLocalCuisine.setName(dto.name());
            changes = true;
        }
        if (dto.description() != null && !dto.description().equals(existingLocalCuisine.getDescription())) {
            existingLocalCuisine.setDescription(dto.description());
            changes = true;
        }
        if (dto.locationId() != null && !dto.locationId().equals(existingLocalCuisine.getLocation().getId())) {
            existingLocalCuisine.setLocation(location);
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        try {
            File file = FileService.convertFile(dto.file());
            String newUrl = cloudinaryService.uploadImage(file, "localCuisine");
            cloudinaryService.deleteImageByUrl(existingLocalCuisine.getImage());
            existingLocalCuisine.setImage(newUrl);
        }
        catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }

        localCuisineDao.updateLocalCuisine(existingLocalCuisine);
    }
}
