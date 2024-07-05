package com.internship.backend.service;

import com.internship.backend.repository.*;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class IdGeneratorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TennisCourtRepository tennisCourtRepository;

    @Autowired
    private PriceRepository priceRepository;

    public int getCurrentId(){
        long userCounter = userRepository.count();
        long authorityCounter = authorityRepository.count();
        long reservationCounter = reservationRepository.count();
        long locationCounter = locationRepository.count();
        long tennisCourtCounter = tennisCourtRepository.count();
        long priceCounter = priceRepository.count();

        return (int) (userCounter + authorityCounter + reservationCounter + locationCounter + tennisCourtCounter + priceCounter +1);
    }
}
