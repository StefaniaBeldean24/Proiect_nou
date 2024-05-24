package com.internship.backend.service;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.dto.PriceDTO;
import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.model.Location;
import com.internship.backend.model.Price;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.LocationRepository;
import com.internship.backend.repository.PriceRepository;
import com.internship.backend.repository.TennisCourtRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.internship.backend.exceptions.LocationDoesNotExistException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
//import org.springframework.security.access.prepost.PreAuthorize;

@Service
public class AdminService{

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    @Autowired
    private PriceRepository priceRepository;

    //@Autowired
    private ModelMapper modelMapper;


    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Location addLocation(Location location){
        //transform from dto to base class
//        Location baseLocation = new Location();
//
//        baseLocation.setId(locationDTO.getId());
//        baseLocation.setName(locationDTO.getName());
//        baseLocation.setDetails(locationDTO.getDetails());
//
//        List<TennisCourt> tennisCourts = new ArrayList<>();
//        tennisCourts = tennisCourtRepository.findAll();
//        baseLocation.setTennisCourt(tennisCourts);

        return locationRepository.save(location);
    }

    public Location updateLocation(int locationId, Location location) throws LocationDoesNotExistException {
        Location updatedLocation = locationRepository.findById(locationId).orElseThrow(()->new LocationDoesNotExistException("Location not found"));

        updatedLocation.setName(location.getName());
        updatedLocation.setDetails(location.getDetails());

        return locationRepository.save(updatedLocation);
    }

    public void deleteLocation(int locationId) throws LocationDoesNotExistException {
        if(!locationRepository.existsById(locationId)){
            throw new LocationDoesNotExistException("Location not found");
        }
        locationRepository.deleteById(locationId);
    }


    public TennisCourt addTennisCourt(TennisCourt tennisCourt) {
        return tennisCourtRepository.save(tennisCourt);
    }

    public TennisCourtDTO getTennisCourt(int id){
        TennisCourt tennisCourt = tennisCourtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TennisCourt not found"));
        return modelMapper.map(tennisCourt, TennisCourtDTO.class);
    }

    public Price addPrice(PriceDTO priceDTO)
    {
        TennisCourt tennisCourt = tennisCourtRepository.findById(priceDTO.getTennisCourtId())
                .orElseThrow(() -> new EntityNotFoundException("TennisCourt not found"));

        Price price = new Price();
        price.setTennisCourt(tennisCourt);
        price.setPeriodOfDay(priceDTO.getPeriodOfDay());
        price.setSeason(priceDTO.getSeason());

        return priceRepository.save(price);
    }

}
