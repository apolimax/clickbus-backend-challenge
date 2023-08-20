package com.clickbus.clickbuschallenge.services;

import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.clickbus.clickbuschallenge.dtos.Placedto;
import com.clickbus.clickbuschallenge.entities.Place;
import com.clickbus.clickbuschallenge.repositories.PlaceRepository;
import com.clickbus.clickbuschallenge.services.exceptions.ObjectNotFoundException;

@SpringBootTest
public class PlaceServiceTest {
    @InjectMocks
    PlaceService service;

    @Mock
    PlaceRepository repository;

    private static final String NAME = "trip-1";
    private static final String CITY = "João Pessoa";
    private static final String STATE = "Paraiba";
    private static final Long ID = 1L;

    Place place = new Place(ID, NAME, "trip-1", CITY, STATE);
    Placedto placedto = new Placedto(NAME, "trip-1", CITY, STATE);
    Optional<Place> optionalPlace = Optional.of(new Place(ID, NAME, "trip-1", CITY, STATE));

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenListPlacesReturnSuccess() {
        Mockito.when(repository.findAll()).thenReturn(List.of(place));

        List<Place> places = service.listPlaces();

        Assertions.assertNotNull(places);
        Assertions.assertEquals(Place.class, places.get(0).getClass());
        Assertions.assertEquals(NAME, places.get(0).getName());
        Assertions.assertEquals(STATE, places.get(0).getState());
        Assertions.assertEquals(CITY, places.get(0).getCity());
        Assertions.assertEquals(ID, places.get(0).getId());
    }

    @Test
    void whenGetPlaceReturnSuccess() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(optionalPlace);

        Place place = service.getPlace(ID);

        Assertions.assertNotNull(place);
        Assertions.assertEquals(Place.class, place.getClass());
        Assertions.assertEquals(NAME, place.getName());
        Assertions.assertEquals(STATE, place.getState());
        Assertions.assertEquals(CITY, place.getCity());
        Assertions.assertEquals(ID, place.getId());
    }

    @Test
    void whenGetPlaceReturnRunTimeException() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenThrow(new ObjectNotFoundException("Place not found!"));

        try {
            service.getPlace(ID);
        } catch (ObjectNotFoundException e) {
            Assertions.assertEquals(ObjectNotFoundException.class, e.getClass());
            Assertions.assertEquals("Place not found!", e.getMessage());
        }
    }

    @Test
    void whenCreateReturnSuccess() {
        Mockito.when(repository.save(Mockito.any())).thenReturn(place);

        Place response = service.create(placedto);

        Assertions.assertEquals(Place.class, response.getClass());
        Assertions.assertEquals(NAME, response.getName());
        Assertions.assertEquals(CITY, response.getCity());
        Assertions.assertEquals(STATE, response.getState());
    }

    @Test
    void whenEditReturnSuccess() {
        Mockito.when(repository.save(Mockito.any())).thenReturn(place);
        Mockito.when(repository.findById(Mockito.any())).thenReturn(optionalPlace);

        Place place = service.edit(ID, placedto);

        Assertions.assertNotNull(place);
        Assertions.assertEquals(NAME, place.getName());
        Assertions.assertEquals(STATE, place.getState());
        Assertions.assertEquals(CITY, place.getCity());
    }

    @Test
    void whenEditReturnObjectNotFoundException() {
        Mockito.when(repository.save(Mockito.any())).thenReturn(place);
        Mockito.when(repository.findById(ID)).thenThrow(new ObjectNotFoundException("Place not found!"));

        try {
            service.edit(ID, placedto);
        } catch (ObjectNotFoundException e) {
            Assertions.assertEquals(ObjectNotFoundException.class, e.getClass());
            Assertions.assertEquals("Place not found!", e.getMessage());
            Mockito.verify(repository, Mockito.times(0)).save(place);
        }
    }

    @Test
    void whenDeleteReturnSuccess() {
        Mockito.when(repository.findById(ID)).thenReturn(optionalPlace);
        Mockito.doNothing().when(repository).deleteById(Mockito.anyLong());

        service.delete(ID);

        verify(repository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void whenDeleteReturnObjectNotFoundException() {
        Mockito.when(repository.findById(Mockito.anyLong()))
                .thenThrow(new ObjectNotFoundException(("Place not found!")));
        Mockito.doNothing().when(repository).deleteById(Mockito.anyLong());

        try {
            service.delete(ID);
        } catch (ObjectNotFoundException e) {
            Assertions.assertEquals(ObjectNotFoundException.class, e.getClass());
            Mockito.verify(repository, Mockito.times(0)).deleteById(ID);
        }

    }

}
