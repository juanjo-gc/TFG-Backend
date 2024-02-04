package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Operation;
import es.uca.tfg.backend.repository.AdminRepository;
import es.uca.tfg.backend.repository.OperationRepository;
import es.uca.tfg.backend.rest.OperationDTO;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class OperationControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private OperationRepository _operationRepository;

    @Mock
    private AdminRepository _adminRepository;

    @InjectMocks
    private OperationController _controller;

    @Test
    public void newOperationWillCreateNewOperation() {
        //given
        OperationDTO dto = new OperationDTO("Test operation", 1);
        Admin admin = Mockito.mock(Admin.class);
        Operation operation = new Operation(dto.get_sInformation(), admin);
        Mockito.when(_adminRepository.findById(1)).thenReturn(Optional.of(admin));
        Mockito.when(_operationRepository.save(any(Operation.class))).thenReturn(operation);
        //when
        Operation newOperation = _controller.newOperation(dto);
        //then
        Mockito.verify(_operationRepository).save(any(Operation.class));
        Assertions.assertEquals("Test operation", newOperation.get_sInformation());
    }

    @Test
    public void newOperationWillFailAsAdminDoesNotExist() {
        //given
        OperationDTO dto = new OperationDTO("Test operation", 1);
        Admin admin = Mockito.mock(Admin.class);
        Operation operation = new Operation(dto.get_sInformation(), admin);
        Mockito.when(_operationRepository.save(any(Operation.class))).thenReturn(operation);
        //when
        Operation newOperation = _controller.newOperation(dto);
        //then
        Mockito.verify(_operationRepository, Mockito.times(0)).save(any(Operation.class));
        Assertions.assertEquals(0, newOperation.get_iId());
    }
}
