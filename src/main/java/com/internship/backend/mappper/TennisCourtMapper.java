package com.internship.backend.mappper;

import com.internship.backend.dto.TennisCourtDTO;
import com.internship.backend.model.Location;
import com.internship.backend.model.TennisCourt;

public class TennisCourtMapper {

    public TennisCourt mapToTennisCourt(TennisCourtDTO tennisCourtDTO, Location location) {
        TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setName(tennisCourtDTO.getName());
        tennisCourt.setDetails(tennisCourtDTO.getDetails());
        tennisCourt.setLocation(location);
        return tennisCourt;
    }
}
