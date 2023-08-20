package com.clickbus.clickbuschallenge.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clickbus.clickbuschallenge.dtos.Placedto;
import com.clickbus.clickbuschallenge.entities.Place;
import com.clickbus.clickbuschallenge.services.PlaceService;
import com.clickbus.clickbuschallenge.services.exceptions.ObjectNotFoundException;

@RestController
@RequestMapping("/places")
public class PlaceController {
    @Autowired
    PlaceService service;

    @GetMapping
    public ResponseEntity<List<Place>> getAll() {
        List<Place> places = service.listPlaces();

        return ResponseEntity.ok().body(places);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getPlace(@PathVariable Long id) {
        try {
            Place place = service.getPlace(id);
            return ResponseEntity.ok().body(place);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found!");
            // return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody Placedto placedto) {
        Place place = service.create(placedto);

        return ResponseEntity.status(HttpStatus.CREATED).body(place);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Place> editPlace(@PathVariable Long id, @RequestBody Placedto placedto) {
        Place place = service.edit(id, placedto);

        return ResponseEntity.ok().body(place);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Place> deletePlace(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * @GetMapping(value = "/name/{name}")
     * public ResponseEntity<List<Place>> getPlacesByName(@PathVariable String name)
     * {
     * List<Place> places = service.listPlacesByName(name);
     * return ResponseEntity.status(HttpStatus.OK).body(places);
     * }
     */
}
