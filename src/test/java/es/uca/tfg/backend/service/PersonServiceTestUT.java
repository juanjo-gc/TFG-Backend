package es.uca.tfg.backend.service;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.Person;
import es.uca.tfg.backend.repository.PersonRepository;
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
public class PersonServiceTestUT  extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private PersonRepository _personRepository;
    @InjectMocks
    private PersonService _service;

    @Test
    public void authenticatePersonWillFindAndReturnAuthenticatedPerson() {
        //given
        Person person = Mockito.mock(Person.class);
        Mockito.when(_personRepository.findBy_sEmail(anyString())).thenReturn(person);
        Mockito.when(person.get_iId()).thenReturn(10);
        Mockito.when(person.get_sPassword()).thenReturn("testpass");
        Mockito.when(person.get_sEmail()).thenReturn("test@gmail.com");
        Mockito.when(person.checkPassword("testpass")).thenReturn(true);
        //when
        Person authPerson = _service.authenticate(person.get_sEmail(), person.get_sPassword());
        //then
        Mockito.verify(person).checkPassword(person.get_sPassword());
        Assertions.assertEquals(person.get_iId(), authPerson.get_iId());
    }

    @Test
    public void authenticatePersonWontBeAbleToAuthenticatePersonAndWillReturnEmptyPerson() {
        //given
        Person person = Mockito.mock(Person.class);
        Mockito.when(_personRepository.findBy_sEmail(anyString())).thenReturn(person);
        Mockito.when(person.get_iId()).thenReturn(10);
        Mockito.when(person.get_sPassword()).thenReturn("testpass");
        Mockito.when(person.get_sEmail()).thenReturn("test@gmail.com");
        Mockito.when(person.checkPassword("testpass")).thenReturn(false);
        //when
        Person authPerson = _service.authenticate(person.get_sEmail(), person.get_sPassword());
        //then
        Mockito.verify(person).checkPassword(person.get_sPassword());
        Assertions.assertEquals(0, authPerson.get_iId());
    }
}
