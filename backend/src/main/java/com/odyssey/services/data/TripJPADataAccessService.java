package com.odyssey.services.data;

import com.odyssey.daos.TripDao;
import com.odyssey.models.Trip;
import com.odyssey.repositories.TripRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("tripJPAService")
public class TripJPADataAccessService implements TripDao {

    private final TripRepository tripRepository;

    public TripJPADataAccessService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public List<Trip> selectAllTrips() {
        return tripRepository.findAll();
    }

    @Override
    public Optional<Trip> selectTripById(Integer id) {
        return tripRepository.findById(id);
    }

    @Override
    public List<Trip> selectTripsByUserId(Integer userId) {
        return tripRepository.findTripsByUserId(userId);
    }

    @Override
    public void insertTrip(Trip trip) {
        tripRepository.save(trip);
    }

    @Override
    public boolean existsTripById(Integer id) {
        return tripRepository.existsTripById(id);
    }

    @Override
    public boolean existsTripByUserIdAndStartDate(Integer userId, LocalDate startDate) {
        return tripRepository.existsTripByUserIdAndStartDate(userId, startDate);
    }

    @Override
    public void deleteTripById(Integer id) {
        tripRepository.deleteById(id);
    }

    @Override
    public void updateTrip(Trip trip) {
        tripRepository.save(trip);
    }
}
