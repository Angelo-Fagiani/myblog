package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired RatingRepository ratingRepository;

    public void save(Rating r){
        ratingRepository.save(r);
    }
}
