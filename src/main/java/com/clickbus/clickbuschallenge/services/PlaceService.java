package com.clickbus.clickbuschallenge.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clickbus.clickbuschallenge.dtos.Placedto;
import com.clickbus.clickbuschallenge.entities.Place;
import com.clickbus.clickbuschallenge.repositories.PlaceRepository;
import com.clickbus.clickbuschallenge.services.exceptions.ObjectNotFoundException;

@Service
public class PlaceService {
    @Autowired
    PlaceRepository repository;

    public Place create(Placedto placedto) {
        Place place = new Place(null, placedto.name(), placedto.slug(), placedto.city(), placedto.state());
        // BeanUtils.copyProperties(placedto, place);
        return repository.save(place);
    }

    public Place edit(Long id, Placedto placedto) {
        Place place = getPlace(id);
        BeanUtils.copyProperties(placedto, place);
        place.setUpdatedAt(LocalDateTime.now());
        return repository.save(place);
    }

    public Place getPlace(Long id) throws RuntimeException {
        Optional<Place> place = repository.findById(id);

        return place.orElseThrow(() -> new ObjectNotFoundException("Place not found!"));
    }

    public List<Place> listPlaces() {
        return repository.findAll();
    }

    /*
     * public List<Place> listPlacesByName(String name) {
     * return repository.findByName(name);
     * }
     */

    public void delete(Long id) {
        getPlace(id);
        repository.deleteById(id);
    }
}
