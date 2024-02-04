package es.uca.tfg.backend.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.repository.CountryRepository;
import es.uca.tfg.backend.repository.RegionRepository;
import es.uca.tfg.backend.rest.RegionDTO;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class RegionControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private RegionRepository _regionRepository;

    @Mock
    private CountryRepository _countryRepository;
    @Mock
    private RegionService _regionService;

    @InjectMocks
    private RegionController _controller;

    private Country _country = new Country("Test country");
    private Region _region = new Region("Test region", _country);

    @Test
    public void getCountryRegionsWillReturnRegionListAsCountryExists() {
        //given
        Country country = new Country("Test");
        List<Region> aCountryRegions = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aCountryRegions.add(new Region());
        country.set_setRegions(new HashSet<>(aCountryRegions));
        Mockito.when(_countryRepository.findById(1)).thenReturn(Optional.of(country));
        //when
        List<Region> aRegions = _controller.getCountryRegions(1);
        //then
        Assertions.assertEquals(5, aRegions.size());
    }
    @Test
    public void getCountryRegionsWillReturnEmptyListAsCountryDoesNotExist() {
        //given
        Country country = new Country("Test");
        List<Region> aCountryRegions = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            aCountryRegions.add(new Region());
        country.set_setRegions(new HashSet<>(aCountryRegions));
        //when
        List<Region> aRegions = _controller.getCountryRegions(1);
        //then
        Assertions.assertEquals(0, aRegions.size());
    }
    
    @Test
    public void updateRegionWillUpdateExistingRegion() {
        //given
        RegionDTO dto = new RegionDTO("Test region updated", "Test country");
        Mockito.when(_regionRepository.findById(1)).thenReturn(Optional.of(_region));
        Mockito.when(_countryRepository.findBy_sName("Test country")).thenReturn(Optional.of(_country));
        Mockito.when(_regionRepository.save(_region)).thenReturn(_region);
        //when
        Region region = _controller.updateRegion(dto, 1);
        //then
        Mockito.verify(_regionRepository).save(region);
        Assertions.assertEquals("Test region updated", region.get_sName());
    }

    @Test
    public void updateRegionWillFailAsRegionDoesNotExist() {
        //given
        RegionDTO dto = new RegionDTO("Test region updated", "Test country");
        Mockito.when(_regionRepository.save(_region)).thenReturn(_region);
        //when
        Region region = _controller.updateRegion(dto, 1);
        //then
        Mockito.verify(_regionRepository, Mockito.times(0)).save(_region);
        Assertions.assertEquals(0, region.get_iId());
    }
    
}
