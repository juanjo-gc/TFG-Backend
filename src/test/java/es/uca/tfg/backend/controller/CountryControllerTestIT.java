package es.uca.tfg.backend.integration.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.rest.CountryDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
@RunWith(SpringRunner.class)
public class CountryControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void createCountryWillCreateCountry() throws Exception {
        //given
        CountryDTO countryDTO = new CountryDTO("CountryTest");
        Country country = new Country("CountryTest");
        Mockito.when(_countryRepository.save(any(Country.class))).thenReturn(country);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/createCountry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(countryDTO)));
        //then
        resultActions.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals("CountryTest", _objectMapper.readValue
                (resultActions.andReturn().getResponse().getContentAsString(), Country.class)
                .get_sName());
        Mockito.verify(_countryRepository).save(any(Country.class));
    }

    @Test
    public void updateCountryWillUpdateExistingCountry() throws Exception {
        //given
        Country country = new Country("TestCountry");
        CountryDTO countryDTO = new CountryDTO("UpdatedCountry");
        Mockito.when(_countryRepository.findById(1)).thenReturn(Optional.of(country));
        Mockito.when(_countryRepository.save(country)).thenReturn(country);
        //when
        ResultActions resultActions = _mockMvc.perform(patch("/api/updateCountry/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(countryDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(_countryRepository).save(country);
        Assertions.assertEquals("UpdatedCountry", _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Country.class).get_sName());
    }

    @Test
    public void deleteCountryWillDeleteExistingCountry() throws Exception {
        //given
        Country country = Mockito.mock(Country.class);
        Region region = Mockito.mock(Region.class);
        Set<Region> aCountryRegions = new HashSet<>();
        for(int i = 0; i < 5; i++)
            aCountryRegions.add(new Region());
        Mockito.when(_countryRepository.findById(1)).thenReturn(Optional.of(country));
        Mockito.when(country.get_setRegions()).thenReturn(aCountryRegions);
        Mockito.when(region.get_iId()).thenReturn(anyInt());
        //when
        ResultActions resultActions = _mockMvc.perform(delete("/api/deleteCountry/1"));
        //then
        resultActions.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Mockito.verify(_regionService, Mockito.times(5)).delete(anyInt());
        Mockito.verify(_countryRepository).deleteById(1);
    }
}
