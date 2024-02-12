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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest
@RunWith(SpringRunner.class)
public class RegionControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getCountryRegionsWillReturnRegionList() throws Exception {
        //given
        Country country = Mockito.mock(Country.class);
        Set<Region> aRegions = new HashSet<>();
        for(int i = 0; i < 5; i++)
            aRegions.add(new Region());
        Mockito.when(_countryRepository.findById(1)).thenReturn(Optional.of(country));
        Mockito.when(country.get_setRegions()).thenReturn(aRegions);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getCountryRegions/1"));
        //then
        response.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(5, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void getCountryRegionsWillFailAndReturnEmptyList() throws Exception {
        //given
        Country country = Mockito.mock(Country.class);
        Set<Region> aRegions = new HashSet<>();
        for(int i = 0; i < 5; i++)
            aRegions.add(new Region());
        Mockito.when(country.get_setRegions()).thenReturn(aRegions);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getCountryRegions/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(Collections.emptyList(),  _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class));
    }

    @Test
    public void createRegionWillCreateNewRegion() throws Exception {
        //given
        RegionDTO dto = new RegionDTO("Test region", "Test country");
        Country country = new Country();
        Region region = new Region(dto.get_sName(), country);
        Mockito.when(_countryRepository.findBy_sName(dto.get_sCountry())).thenReturn(Optional.of(country));
        Mockito.when(_regionRepository.save(any(Region.class))).thenReturn(region);
        //when
        ResultActions response = _mockMvc.perform(post("/api/createRegion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(dto)));
        //then
        response.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Mockito.verify(_regionRepository).save(any(Region.class));
        Assertions.assertEquals(dto.get_sName(), _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Region.class).get_sName());
    }

    @Test
    public void createRegionWillFailAndReturnEmptyRegion() throws Exception{
        //given
        RegionDTO dto = new RegionDTO("Test region", "Test country");
        Country country = new Country();
        Region region = new Region(dto.get_sName(), country);
        Mockito.when(_regionRepository.save(any(Region.class))).thenReturn(region);
        //when
        ResultActions response = _mockMvc.perform(post("/api/createRegion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(dto)));
        //then
        response.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Mockito.verify(_regionRepository, Mockito.times(0)).save(any(Region.class));
        Assertions.assertNull(_objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Region.class).get_sName());
    }

    @Test
    public void updateRegionWillUpdateRegion() throws Exception{
        //given
        RegionDTO dto = new RegionDTO("Test region updated", "Test country updated");
        Country country = new Country("Test country updated");
        Region region = new Region();
        Mockito.when(_regionRepository.findById(1)).thenReturn(Optional.of(region));
        Mockito.when(_countryRepository.findBy_sName(dto.get_sCountry())).thenReturn(Optional.of(country));
        Mockito.when(_regionRepository.save(any(Region.class))).thenReturn(region);
        //when
        ResultActions response = _mockMvc.perform(patch("/api/updateRegion/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(dto)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_regionRepository).save(any(Region.class));
        Assertions.assertEquals(dto.get_sName(), _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Region.class).get_sName());
        Assertions.assertEquals(dto.get_sCountry(), _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Region.class).get_country().get_sName());
    }

    @Test
    public void updateRegionWillFailAndReturnEmptyRegion() throws Exception{
        //given
        RegionDTO dto = new RegionDTO("Test region updated", "Test country updated");
        Country country = new Country("Test country updated");
        Region region = new Region();
        Mockito.when(_countryRepository.findBy_sName(dto.get_sCountry())).thenReturn(Optional.of(country));
        Mockito.when(_regionRepository.save(any(Region.class))).thenReturn(region);
        //when
        ResultActions response = _mockMvc.perform(patch("/api/updateRegion/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(dto)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_regionRepository, Mockito.times(0)).save(any(Region.class));
        Assertions.assertNull(_objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Region.class).get_sName());
    }


}
