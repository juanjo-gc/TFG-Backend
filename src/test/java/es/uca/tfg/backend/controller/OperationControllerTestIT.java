package es.uca.tfg.backend.integration.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Operation;
import es.uca.tfg.backend.rest.OperationDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@RunWith(SpringRunner.class)
public class OperationControllerTestIT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void newOperationWillCreateOperaion() throws Exception {
        //given
        Admin admin = Mockito.mock(Admin.class);
        OperationDTO operationDTO = new OperationDTO("TestInfo", 1);
        Operation operation = new Operation("TestInfo", new Admin());
        Mockito.when(_adminRepository.findById(1)).thenReturn(Optional.of(admin));
        Mockito.when(_operationRepository.save(any(Operation.class))).thenReturn(operation);
        //when
        ResultActions resultActions = _mockMvc.perform(post("/api/newOperation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(operationDTO)));
        //then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals("TestInfo", _objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Operation.class).get_sInformation());
        Mockito.verify(_operationRepository).save(any(Operation.class));
    }
}
