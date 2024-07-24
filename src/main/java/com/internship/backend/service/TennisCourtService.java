package com.internship.backend.service;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.exceptions.TennisCourtAlreadyExistsException;
import com.internship.backend.exceptions.TennisCourtDoesNotExistsException;
import com.internship.backend.model.Location;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.LocationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TennisCourtService {
    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<TennisCourt> getAllTennisCourts(){
        return tennisCourtRepository.findAll();
    }

    public TennisCourt addTennisCourt(TennisCourt tennisCourt) throws TennisCourtAlreadyExistsException {
        Optional<Location> locationOptional = findLocation(tennisCourt.getLocation().getId());

        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();

            if (!location.getTennisCourt().contains(tennisCourt)) {
                List<TennisCourt> tennisCourtList = location.getTennisCourt();
                tennisCourtList.add(tennisCourt);
                location.setTennisCourt(tennisCourtList);
            } else {
                throw new TennisCourtAlreadyExistsException("TennisCourt already exists");
            }
        }

        return tennisCourtRepository.save(tennisCourt);
    }

    private Optional<Location> findLocation(int locationId) {
        return locationRepository.findAll()
                .stream()
                .filter(l -> l.getId() == locationId)
                .findFirst();
    }

    public TennisCourt parse(TennisCourtDTO tennisCourtDTO) {
        TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setName(tennisCourtDTO.getName());
        tennisCourt.setDetails(tennisCourtDTO.getDetails());

        for (Location elem : locationRepository.findAll()){
            if (elem.getId() == tennisCourtDTO.getLocationId()){
                tennisCourt.setLocation(elem);
            }
        }
        return tennisCourt;
    }

    public TennisCourt updateTennisCourt(int tennisCourtId, TennisCourt newTennisCourt) throws TennisCourtDoesNotExistsException {
        TennisCourt tennisCourt = tennisCourtRepository.findById(tennisCourtId).orElseThrow(()->new TennisCourtDoesNotExistsException("TennisCourt not found!"));

        tennisCourt.setName(newTennisCourt.getName());
        tennisCourt.setDetails(tennisCourt.getDetails());

        return tennisCourtRepository.save(tennisCourt);
    }

    public void deleteTennisCourt(int tennisCourtId) throws TennisCourtDoesNotExistsException {
        if(!locationRepository.existsById(tennisCourtId))
            throw new TennisCourtDoesNotExistsException("TennisCourt does not exist");

        locationRepository.deleteById(tennisCourtId);

        if(locationRepository.count() == 0){
            locationRepository.resetAutoIncrementId();
        }

    }

}
