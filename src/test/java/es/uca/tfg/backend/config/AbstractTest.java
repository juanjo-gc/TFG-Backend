package es.uca.tfg.backend.config;

import java.io.FileInputStream;
import java.io.IOException;

import es.uca.tfg.backend.BackendApplication;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.service.EventService;
import es.uca.tfg.backend.service.PersonService;
import es.uca.tfg.backend.service.PostService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class AbstractTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    protected MockMvc _mockMvc;
    @Autowired
    protected WebApplicationContext _webApplicationContext;
    @Autowired
    protected ObjectMapper _objectMapper;
    @MockBean
    protected PersonService _personService;
    @MockBean
    protected PostService _postService;
    @MockBean
    protected EventService _eventService;
    @MockBean
    protected UserRepository _userRepository;
    @MockBean
    protected PersonRepository _personRepository;
    @MockBean
    protected InterestRepository _interestRepository;
    @MockBean
    protected ImagePathRepository _ImagePathRepository;
    @MockBean
    protected CountryRepository _countryRepository;
    @MockBean
    protected RegionRepository _regionRepository;
    @MockBean
    protected ProvinceRepository _provinceRepository;
    @MockBean
    protected LocationRepository _locationRepository;
    @MockBean
    protected MessageRepository _messageRepository;
    @MockBean
    protected  PostRepository _postRepository;
    @MockBean
    protected CommentRepository _commentRepository;
    @MockBean
    protected EventRepository _eventRepository;
    @MockBean
    protected NotificationRepository _notificationRepository;
    @MockBean
    protected TypeNotificationRepository _typeNotificationRepository;
    @MockBean
    protected FileInputStream _fileInputStream;

    protected void setUp() {
        _mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}