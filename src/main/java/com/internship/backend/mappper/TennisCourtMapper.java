package com.internship.backend.mappper;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.model.Location;
import com.internship.backend.model.TennisCourt;
import com.internship.backend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TennisCourtMapper {

    @Autowired
    private LocationRepository locationRepository;

    public TennisCourt tennisCourtMapper(TennisCourtDTO tennisCourtDTO) {
        TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setName(tennisCourtDTO.getName());
        tennisCourt.setDetails(tennisCourtDTO.getDetails());

        Location location = locationRepository.findById(tennisCourtDTO.getLocationId())
                .orElseThrow(()-> new RuntimeException("Location not found"));
        tennisCourt.setLocation(location);

        return tennisCourt;
    }
}
