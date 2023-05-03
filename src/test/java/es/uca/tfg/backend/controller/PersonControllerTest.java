package es.uca.tfg.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.rest.MapUserRegister;
import es.uca.tfg.backend.repository.InterestRepository;
import es.uca.tfg.backend.repository.PersonRepository;
import es.uca.tfg.backend.repository.ImagePathRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.rest.UserChecker;
import es.uca.tfg.backend.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class PersonControllerTest {
    @Autowired
    private MockMvc _mockMvc;
    @Autowired
    private ObjectMapper _objectMapper;
    @MockBean
    private PersonService _personService;
    @MockBean
    private UserRepository _userRepository;
    @MockBean
    private PersonRepository _personRepository;
    @MockBean
    private InterestRepository _interestRepository;
    @MockBean
    private ImagePathRepository _ImagePathRepository;
    private String _sUploadPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\users\\";


    @Test
    void registerNewUserTest() throws Exception {
        //given
        //User user = new User("example@gmail.com", "password", "username", "user", "name", new Date());
        MapUserRegister mapUserRegister = new MapUserRegister("example@gmail.com", "name", "username", "password", new Date());
        //given(_personService.update(any(Person.class))).willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));
        //when
        ResultActions response = _mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(mapUserRegister)));
        //then
        /*
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._sName", is(user.get_sName())))
                .andExpect(jsonPath("$._sEmail", is(user.get_sEmail())))
                .andExpect(jsonPath("$._sPassword", is(user.get_sPassword())))
                .andExpect(jsonPath("$._sUsername", is(user.get_sUsername())));
         */
        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("Usuario creado correctamente."));
        Mockito.verify(_personRepository).save(any(User.class));
    }

    @Test
    void checkUsernameTest() throws Exception {
        //given
        UserChecker.UsernameChecker usernameChecker = new UserChecker.UsernameChecker("username");

        //when
        ResultActions response = _mockMvc.perform(post("/api/checkUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(usernameChecker)));
        //then
        response.andDo(print())
                .andExpect(content().string("false"));
    }

    @Test
    void checkEmailTest() throws Exception {
        //given
        UserChecker.EmailChecker emailChecker = new UserChecker.EmailChecker("example@gmail.com");

        //when
        ResultActions response = _mockMvc.perform(post("/api/checkUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(emailChecker)));
        //then
        response.andDo(print())
                .andExpect(content().string("false"));
    }

    @Test
    void loginTest() throws Exception {
        //given
        UserChecker.LoginChecker loginChecker = new UserChecker.LoginChecker("example@gmail.com", "password");
        User user = new User("example@gmail.com", "password", "username", "user", "name", new Date());
        Mockito.when(_personService.authenticate(loginChecker.get_sEmail(), loginChecker.get_sPassword())).thenReturn(user);

        //when
        ResultActions response = _mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(loginChecker)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(_objectMapper.writeValueAsString(user)));

    }

    @Test
    void getUserFromUsernameTest() throws Exception {
        //given
        User user1 = new User("example@gmail.com", "password", "username", "user", "name", new Date());
        User user2 = new User("example2@gmail.com", "password2", "username2", "user2", "name2", new Date());

        Mockito.when(_userRepository.findBy_sUsername(user1.get_sUsername())).thenReturn(user1);
        Mockito.when(_userRepository.findBy_sUsername(user2.get_sUsername())).thenReturn(user2);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getUserFromUsername/" + user1.get_sUsername())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(_objectMapper.writeValueAsString(user1)));
    }

    @Test
    void getUserFromUsernameWillFailTest() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "user", "name", new Date());

        Mockito.when(_userRepository.findBy_sUsername(user.get_sUsername())).thenReturn(null);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getUserFromUsername/" + user.get_sUsername())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(_objectMapper.writeValueAsString(new User())));
    }



}
