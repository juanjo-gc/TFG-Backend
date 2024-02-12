package es.uca.tfg.backend.service;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.Region;
import es.uca.tfg.backend.repository.RegionRepository;
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

import java.util.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class RegionServiceTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private RegionRepository _regionRepository;
    @Mock
    private ProvinceService _provinceService;

    @InjectMocks
    private RegionService _service;

    @Test
    public void deleteRegionWillDeleteRegionAndProvinces() {
        //given
        Region region = Mockito.mock(Region.class);
        Set<Province> aProvinces = new HashSet<>();
        for(int i = 0; i < 8; i++) {
            aProvinces.add(new Province());
        }
        Mockito.when(_regionRepository.findById(1)).thenReturn(Optional.of(region));
        Mockito.when(region.get_setProvinces()).thenReturn(aProvinces);
        //when
        _service.delete(1);
        //then
        Mockito.verify(_provinceService, Mockito.times(aProvinces.size())).delete(anyInt());
        Mockito.verify(_regionRepository).deleteById(1);
    }

    @Test
    public void deleteRegionWillFailAndWontHaveConsequencesAsRegionDoesNotExist() {
        //given
        Region region = Mockito.mock(Region.class);
        Set<Province> aProvinces = new HashSet<>();
        for(int i = 0; i < 8; i++) {
            aProvinces.add(new Province());
        }
        Mockito.when(region.get_setProvinces()).thenReturn(aProvinces);
        //when
        _service.delete(1);
        //then
        Mockito.verify(_provinceService, Mockito.times(0)).delete(anyInt());
        Mockito.verify(_regionRepository, Mockito.times(0)).deleteById(1);
    }
}
