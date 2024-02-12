package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.ProvinceDTO;
import es.uca.tfg.backend.service.ProvinceService;
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

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class ProvinceControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private ProvinceRepository _provinceRepository;
    @Mock
    private RegionRepository _regionRepository;
    @Mock
    private CountryRepository _countryRepository;
    @Mock
    private UserRepository _userRepository;
    @Mock
    private LocationRepository _locationRepository;
    @Mock
    private ProvinceService _provinceService;

    @InjectMocks
    private ProvinceController _controller;

    @Test
    public void getRegionProvincesWillReturnListOfProvinces() {
        //given
        Region region = new Region();
        Set<Province> aRegionProvinces = Set.of(new Province(),new Province());
        region.set_setProvinces(aRegionProvinces);
        Mockito.when(_regionRepository.findById(1)).thenReturn(Optional.of(region));
        //when
        List<Province> aProvinces = _controller.getRegionProvinces(1);
        //then
        Assertions.assertEquals(2, aProvinces.size());
    }

    @Test
    public void getRegionProvincesWillReturnEmptyListAsRegionDoesNotExist() {
        //given
        Region region = new Region();
        Set<Province> aRegionProvinces = Set.of(new Province(),new Province());
        region.set_setProvinces(aRegionProvinces);
        //when
        List<Province> aProvinces = _controller.getRegionProvinces(1);
        //then
        Assertions.assertEquals(Collections.emptyList(), aProvinces);
    }

    @Test
    public void createProvinceWillCreateNewProvince() {
        //given
        ProvinceDTO dto = new ProvinceDTO("Test province", "Test region");
        Region region = Mockito.mock(Region.class);
        Province province = new Province(dto.get_sName(), region);
        Mockito.when(_regionRepository.findBy_sName("Test region")).thenReturn(Optional.of(region));
        Mockito.when(_provinceRepository.save(any(Province.class))).thenReturn(province);
        //when
        Province newProvince = _controller.createProvince(dto);
        //then
        Mockito.verify(_provinceRepository).save(any(Province.class));
        Assertions.assertEquals("Test province", newProvince.get_sName());
    }

    @Test
    public void createProvinceWillFailAndReturnEmptyProvinceAsRegionDoesNotExist() {
        //given
        ProvinceDTO dto = new ProvinceDTO("Test province", "Test region");
        Region region = Mockito.mock(Region.class);
        Province province = new Province(dto.get_sName(), region);
        Mockito.when(_provinceRepository.save(any(Province.class))).thenReturn(province);
        //when
        Province newProvince = _controller.createProvince(dto);
        //then
        Mockito.verify(_provinceRepository, Mockito.times(0)).save(any(Province.class));
        Assertions.assertEquals(null, newProvince.get_sName());
    }

    @Test
    public void updateProvinceWillUpdateProvinceData() {
        //given
        ProvinceDTO dto = new ProvinceDTO("Test province updated", "Test region");
        Province province = Mockito.mock(Province.class);
        Region region = Mockito.mock(Region.class);
        Mockito.when(_provinceRepository.findById(1)).thenReturn(Optional.of(province));
        Mockito.when(_regionRepository.findBy_sName(dto.get_sRegion())).thenReturn(Optional.of(region));
        Mockito.when(_provinceRepository.save(province)).thenReturn(province);
        //when
        Province updatedProvince = _controller.updateProvince(dto, 1);
        //then
        Mockito.verify(province).set_sName(dto.get_sName());
        Mockito.verify(province).set_region(region);
        Mockito.verify(_provinceRepository).save(province);
    }

    @Test
    public void updateProvinceWillFailAndReturnEmptyProvinceAsProvinceOrRegionDoesNotExist() {
        //given
        ProvinceDTO dto = new ProvinceDTO("Test province updated", "Test region");
        Province province = Mockito.mock(Province.class);
        Region region = Mockito.mock(Region.class);
        Mockito.when(_regionRepository.findBy_sName(dto.get_sRegion())).thenReturn(Optional.of(region));
        Mockito.when(_provinceRepository.save(province)).thenReturn(province);
        //when
        Province updatedProvince = _controller.updateProvince(dto, 1);
        //then
        Mockito.verify(province, Mockito.times(0)).set_sName(dto.get_sName());
        Mockito.verify(province, Mockito.times(0)).set_region(region);
        Mockito.verify(_provinceRepository, Mockito.times(0)).save(province);
        Assertions.assertEquals(0, updatedProvince.get_iId());
    }

    @Test
    public void getUserProvinceWillReturnUserProvince() {
        //given
        Province province = Mockito.mock(Province.class);
        User user = Mockito.mock(User.class);
        Mockito.when(user.get_province()).thenReturn(province);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(province.get_iId()).thenReturn(10);
        //when
        Province userProvince = _controller.getUserProvince(1);
        //then
        Assertions.assertEquals(10, userProvince.get_iId());
    }

    @Test
    public void getUserProvinceWillFailAndReturnEmptyProvinceAsUserDoesNotExist() {
        //given
        Province province = Mockito.mock(Province.class);
        User user = Mockito.mock(User.class);
        Mockito.when(user.get_province()).thenReturn(province);
        Mockito.when(province.get_iId()).thenReturn(10);
        //when
        Province userProvince = _controller.getUserProvince(1);
        //then
        Assertions.assertEquals(0, userProvince.get_iId());
    }
}
