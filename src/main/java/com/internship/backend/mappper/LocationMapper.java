package com.internship.backend.mappper;

import com.internship.backend.dto.LocationDTO;
import com.internship.backend.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location locationMapper(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.getName());
        location.setDetails(locationDTO.getDetails());
        return location;
    }
}
