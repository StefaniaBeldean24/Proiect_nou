package com.internship.backend.service;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.model.Location;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.LocationRepository;
import com.internship.backend.repository.TennisCourtRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TennisCourtService {
    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<TennisCourt> getAllTennisCourts(){
        return tennisCourtRepository.findAll();
    }

    public TennisCourt addTennisCourt(TennisCourt tennisCourt){
       // check if location has this tennis court. if not add it
        for (Location elem : locationRepository.findAll()){
            if (elem.getId() == tennisCourt.getLocation().getId()){
                if (!elem.getTennisCourt().contains(tennisCourt)) {
                    List<TennisCourt> aux = elem.getTennisCourt();
                    aux.add(tennisCourt);
                    elem.setTennisCourt(aux);
                }
            }
        }
        return tennisCourtRepository.save(tennisCourt);
    }

    public TennisCourt fromDTO(TennisCourtDTO tennisCourtDTO) {
        TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setId(tennisCourtDTO.getId());
        tennisCourt.setName(tennisCourtDTO.getName());
        tennisCourt.setDetails(tennisCourtDTO.getDetails());

        for (Location elem : locationRepository.findAll()){
            if (elem.getId() == tennisCourtDTO.getLocationId()){
                tennisCourt.setLocation(elem);
            }
        }
        return tennisCourt;
    }

    public TennisCourt updateTennisCourt(int tennisCourtId, TennisCourt newTennisCourt){
        TennisCourt tennisCourt = tennisCourtRepository.findById(tennisCourtId).orElseThrow(()->new EntityNotFoundException("TennisCourt not found!"));

        tennisCourt.setName(newTennisCourt.getName());
        tennisCourt.setDetails(tennisCourt.getDetails());

        return tennisCourtRepository.save(tennisCourt);
    }

    public void deleteTennisCourt(int tennisCourtId){
        if(!locationRepository.existsById(tennisCourtId))
            throw new EntityNotFoundException("TennisCourt does not exist");

        if (tennisCourtRepository.count() == 0) {
            tennisCourtRepository.resetAutoIncrementId();
        }

        locationRepository.deleteById(tennisCourtId);
    }
}

