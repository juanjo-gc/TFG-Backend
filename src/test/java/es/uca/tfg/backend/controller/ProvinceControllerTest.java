package es.uca.tfg.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Country;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.CountryRepository;
import es.uca.tfg.backend.repository.ProvinceRepository;
import es.uca.tfg.backend.repository.RegionRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class ProvinceControllerTest extends AbstractTest {
    /*
    @Autowired
    private MockMvc _mockMvc;
    @Autowired
    private ObjectMapper _objectMapper;
    @Autowired
    private WebApplicationContext _webApplicationContext;
    @MockBean
    private ProvinceRepository _provinceRepository;
    @MockBean
    private RegionRepository _regionRepository;
    @MockBean
    private CountryRepository _countryRepository;

     */
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getCountryRegionsWhenCountryNotExist() throws Exception {
        //given
        Optional<Country> optionalCountry = Optional.of(Mockito.mock(Country.class));
        Mockito.when(_countryRepository.findById(anyInt())).thenReturn(optionalCountry);
        Mockito.when(optionalCountry.get().get_setRegions()).thenReturn(Collections.emptySet());
        //when
        ResultActions response = _mockMvc.perform(get("/api/getCountryRegions/0"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(Collections.emptyList(), _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class));
    }

    @Test
    public void getCountryRegionsWhenCountryExists() throws Exception {
        //given
        Optional<Country> optionalCountry = Optional.of(Mockito.mock(Country.class));
        //Region region = Mockito.mock(Region.class);
        Mockito.when(_countryRepository.findById(anyInt())).thenReturn(optionalCountry);
        Mockito.when(optionalCountry.get().get_setRegions()).thenReturn(Collections.singleton(new Region("Andalucía", new Country("España"))));
        //when
        ResultActions response = _mockMvc.perform(get("/api/getCountryRegions/0"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(1, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    //Para las provincias es análogo, por lo que se ha omitido
}
