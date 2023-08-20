package com.clickbus.clickbuschallenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clickbus.clickbuschallenge.entities.Place;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByName(String name);
}
