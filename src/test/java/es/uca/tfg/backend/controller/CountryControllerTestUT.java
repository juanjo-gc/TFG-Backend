package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.repository.CountryRepository;
import es.uca.tfg.backend.rest.CountryDTO;
import es.uca.tfg.backend.service.RegionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CountryControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Mock
    private CountryRepository _countryRepository;
    @Mock
    private RegionService _regionService;

    @InjectMocks
    private CountryController _controller;

    private Country _country = new Country("Test country");
    @Mock
    private Region _region;

    @Test
    public void createCountryWillCreateNewCountry() {
        //given
        CountryDTO dto = new CountryDTO("Test country");
        Mockito.when(_countryRepository.save(any(Country.class))).thenReturn(new Country(dto.get_sName()));
        //when
        Country country = _controller.createCountry(dto);
        //then
        Mockito.verify(_countryRepository).save(any(Country.class));
        Assertions.assertEquals("Test country", country.get_sName());
    }

    @Test
    public void updateCountryWillUpdateExistingCountry() {
        //given
        CountryDTO dto = new CountryDTO("Test country updated");
        Mockito.when(_countryRepository.findById(1)).thenReturn(Optional.of(_country));
        Mockito.when(_countryRepository.save(_country)).thenReturn(_country);
        //when
        Country country = _controller.updateCountry(dto, 1);
        //then
        Mockito.verify(_countryRepository).save(_country);
        Assertions.assertEquals("Test country updated", country.get_sName());
    }

    @Test
    public void updateCountryWillFailAsCountryDoesNotExist() {
        //given
        CountryDTO dto = new CountryDTO("Test country updated");
        Mockito.when(_countryRepository.save(_country)).thenReturn(_country);
        //when
        Country country = _controller.updateCountry(dto, 1);
        //then
        Mockito.verify(_countryRepository, Mockito.times(0)).save(_country);
        Assertions.assertEquals(0, country.get_iId());
    }

    @Test
    public void deleteCountryWillDeleteExistingCountry() {
        //given
        Country countryWithRegions = new Country("Test");
        Set<Region> aCountryRegions = new HashSet<>();
        for(int i = 0; i < 5; i++)
            aCountryRegions.add(new Region());
        countryWithRegions.set_setRegions(aCountryRegions);
        Mockito.when(_countryRepository.findById(1)).thenReturn(Optional.of(countryWithRegions));
        //when
        _controller.deleteCountry(1);
        //then
        Mockito.verify(_countryRepository).deleteById(1);
        Mockito.verify(_regionService, Mockito.times(5)).delete(anyInt());
    }
}
