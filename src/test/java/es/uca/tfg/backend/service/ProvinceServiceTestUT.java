package es.uca.tfg.backend.service;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Location;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.LocationRepository;
import es.uca.tfg.backend.repository.ProvinceRepository;
import es.uca.tfg.backend.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class ProvinceServiceTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private ProvinceRepository _provinceRepository;
    @Mock
    private UserRepository _userRepository;
    @Mock
    private LocationRepository _locationRepository;

    @InjectMocks
    private ProvinceService _service;

    @Test
    public void deleteProvinceWillDeleteProvinceWithAllConsequences() {
        //given
        Province province = Mockito.mock(Province.class);
        List<User> aUsersInProvince = new ArrayList<>();
        List<Location> aLocationsInProvince = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            User user = new User();
            Location location = new Location();
            user.set_province(province);
            location.set_province(province);
            aUsersInProvince.add(user);
            aLocationsInProvince.add(location);
        }
        Mockito.when(_provinceRepository.findById(1)).thenReturn(Optional.of(province));
        Mockito.when(_userRepository.findBy_province(province)).thenReturn(aUsersInProvince);
        Mockito.when(_locationRepository.findBy_province(province)).thenReturn(aLocationsInProvince);
        //when
        _service.delete(1);
        //then
        Mockito.verify(_userRepository).findBy_province(province);
        Mockito.verify(_locationRepository).findBy_province(province);
        Mockito.verify(_provinceRepository).deleteById(1);
        for(int i = 0; i < 10; i++) {
            Assertions.assertNull(aUsersInProvince.get(i).get_province());
            Assertions.assertNull(aLocationsInProvince.get(i).get_province());
        }
    }

    @Test
    public void deleteProvinceWillFailAndWontHaveConsequencesAsProvinceDoesNotExist() {
        //given
        Province province = Mockito.mock(Province.class);
        List<User> aUsersInProvince = new ArrayList<>();
        List<Location> aLocationsInProvince = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            User user = new User();
            Location location = new Location();
            user.set_province(province);
            location.set_province(province);
            aUsersInProvince.add(user);
            aLocationsInProvince.add(location);
        }
        Mockito.when(province.get_iId()).thenReturn(12);
        Mockito.when(_userRepository.findBy_province(province)).thenReturn(aUsersInProvince);
        Mockito.when(_locationRepository.findBy_province(province)).thenReturn(aLocationsInProvince);
        //when
        _service.delete(1);
        //then
        Mockito.verify(_userRepository, Mockito.times(0)).findBy_province(province);
        Mockito.verify(_locationRepository, Mockito.times(0)).findBy_province(province);
        Mockito.verify(_provinceRepository, Mockito.times(0)).deleteById(1);
        for(int i = 0; i < 10; i++) {
            Assertions.assertEquals(12 ,aUsersInProvince.get(i).get_province().get_iId());
            Assertions.assertEquals(12, aLocationsInProvince.get(i).get_province().get_iId());
        }
    }


}
