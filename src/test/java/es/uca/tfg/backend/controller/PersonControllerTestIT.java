package es.uca.tfg.backend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.controller.PersonController;
import es.uca.tfg.backend.entity.ImagePath;
import es.uca.tfg.backend.entity.Interest;
import es.uca.tfg.backend.entity.Province;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.MapUserRegister;
import es.uca.tfg.backend.rest.UserChecker;
import es.uca.tfg.backend.rest.UserFilterDTO;
import es.uca.tfg.backend.service.PersonService;
import es.uca.tfg.backend.service.PostService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest

public class PersonControllerTestIT extends AbstractTest {
    /*
    @Autowired
    private MockMvc _mockMvc;
    @Autowired
    private WebApplicationContext _webApplicationContext;
    @Autowired
    private ObjectMapper _objectMapper;
    @MockBean
    private PersonService _personService;
    @MockBean
    private PostService _postService;
    @MockBean
    private UserRepository _userRepository;
    @MockBean
    private PersonRepository _personRepository;
    @MockBean
    private InterestRepository _interestRepository;
    @MockBean
    private ImagePathRepository _ImagePathRepository;
    @MockBean
    private CountryRepository _countryRepository;
    @MockBean
    private RegionRepository _regionRepository;
    @MockBean
    private ProvinceRepository _provinceRepository;
    @MockBean
    private MessageRepository _messageRepository;
    @MockBean
    private  PostRepository _postRepository;
    @MockBean
    private FileInputStream _fileInputStream;

    @InjectMocks
    PersonController _personController;

     */
    @Mock
    File _file;
    private String _sUploadPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\users\\";

    public PersonControllerTestIT() {
    }


    @Test
    void registerNewUserTest() throws Exception {
        //given
        //User user = new User("example@gmail.com", "password", "username", "user", "name", new Date());
        MapUserRegister mapUserRegister = new MapUserRegister("example@gmail.com", "name", "username", "password", new Date(), "Cádiz");
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
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
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
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());

        Mockito.when(_userRepository.findBy_sUsername(user.get_sUsername())).thenReturn(user);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getUserFromUsername/" + user.get_sUsername())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(_objectMapper.writeValueAsString(user)));
    }

    @Test
    void getUserFromUsernameWillFailTest() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());

        Mockito.when(_userRepository.findBy_sUsername(user.get_sUsername())).thenReturn(null);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getUserFromUsername/" + user.get_sUsername())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(_objectMapper.writeValueAsString(new User())));
    }

    @Test
    void updateUserDetailsTest() throws Exception {
        //given
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());

        multiValueMap.add("id", "1");
        multiValueMap.add("name", "newName");
        multiValueMap.add("description", "newDescription");
        multiValueMap.add("email", "newExample@gmail.com");
        multiValueMap.add("username", "newUsername");

        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);

        //when
        ResultActions response = _mockMvc.perform(post("/api/updateUserDetails")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .params(multiValueMap));
        MockHttpServletResponse mockHttpServletResponse = response.andReturn().getResponse();
        String sResponseBody = mockHttpServletResponse.getContentAsString();
        MultiValueMap<String, String> mapResponseBody =
                UriComponentsBuilder.fromUriString("?" + sResponseBody).build().getQueryParams();

        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(user.get_sEmail(), "newExample@gmail.com");
        Mockito.verify(_userRepository).save(any(User.class));

    }

    @Test
    void updateInterestsTest() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        //User user = Mockito.mock(User.class);
        String[] asInterests = new String[] {"Música", "Videojuegos", "Deportes"};
        Set<Interest> aUserInterests = new HashSet<>();
        Interest interest = Mockito.mock(Interest.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_interestRepository.findBy_sName("Música")).thenReturn(new Interest("Música"));
        Mockito.when(_interestRepository.findBy_sName("Videojuegos")).thenReturn(new Interest("Videojuegos"));
        Mockito.when(_interestRepository.findBy_sName("Deportes")).thenReturn(new Interest("Deportes"));
        Mockito.when(_userRepository.save(any())).thenReturn(user);
        //Mockito.when(user.get_setInterests()).thenReturn(aUserInterests);
        //when
        ResultActions response = _mockMvc.perform(post("/api/uploadInterests")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("id", "1")
                .param("interests[]", asInterests));

        //then
        response.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Mockito.verify(_userRepository).save(any(User.class));
        Mockito.verify(_interestRepository, Mockito.times(3)).findBy_sName(anyString());
        Assertions.assertEquals(asInterests.length, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), Set.class).size());
    }

    @Test
    void getUserInterestTest() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        Optional<User> optionalUser = Optional.of(user);
        Interest[] aInterests = {new Interest("Música"), new Interest("Videojuegos")};
        user.set_setInterests(List.of(aInterests).stream().collect(Collectors.toSet()));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/api/getUserInterests/1");
        Mockito.when(_userRepository.findById(1)).thenReturn(optionalUser);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getUserInterests/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void uploadUserProfileImageTest() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        Resource resource = new ClassPathResource("filename");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("filename", resource.getFilename(), MediaType.MULTIPART_FORM_DATA_VALUE, new byte[1]);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        //when
        ResultActions response = _mockMvc.perform(post("/api/uploadProfileImage")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("id", "1")
                .param("file", String.valueOf(mockMultipartFile)));


        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void getUserInterestsTest() throws Exception {
        // Given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        List<Interest> interests = Arrays.asList(new Interest("Música"), new Interest("Videojuegos"));
        user.set_setInterests(interests.stream().collect(Collectors.toSet()));
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));

        // When
        ResultActions response = _mockMvc.perform(get("/api/getUserInterests/" + 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'_sName': 'Música'}, {'_sName': 'Videojuegos'}]"));

        // Then
        Mockito.verify(_userRepository).findById(1);
    }

    @Test
    public void getImagePathTest() throws Exception {
        // Given
        String sImageName = "image.jpg";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", _fileInputStream);
        Mockito.when(_file.getPath()).thenReturn(any(String.class));
        //Mockito.when(_fileInputStream.read()).thenReturn(any(Integer.class));

        // When
        _mockMvc.perform(get("/api/getImage/" + sImageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));

        // Then
        Mockito.verify(mockMultipartFile).getInputStream();
    }

    @Test
    public void setFollowWhenFollowingNewUser() throws Exception {
        //given
        //User followed = new User("followed@gmail.com", "password", "followed", "user", "followed", new Date());
        User user = Mockito.mock(User.class);
        User followed = Mockito.mock(User.class);
        boolean bIsFollowing = false;
        user.set_setFollowing(Collections.emptySet());
        Mockito.when(_userRepository.findBy_iId(0)).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(followed);
        //when
        ResultActions response = _mockMvc.perform(patch("/api/setFollow/0/1"));
        //then
        response.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(true, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), boolean.class));
    }

    @Test
    public void setFollowWhenUnfollowUser() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        User followed = Mockito.mock(User.class);
        Set<User> followingUsers = Mockito.mock(Set.class);

        Mockito.when(_userRepository.findBy_iId(0)).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(followed);
        Mockito.when(user.get_setFollowing()).thenReturn(followingUsers);
        Mockito.when(followingUsers.contains(any(User.class))).thenReturn(true);
        //when
        ResultActions response = _mockMvc.perform(patch("/api/setFollow/0/1"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(false, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), boolean.class));
    }

    @Test
    public void getNumFollowsWillReturnOneFollowedAndTwoFollowers() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Set<User> setFollowing = Mockito.mock(Set.class);
        Set<User> setFollowers = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(0)).thenReturn(user);
        Mockito.when(user.get_setFollowers()).thenReturn(setFollowers);
        Mockito.when(user.get_setFollowing()).thenReturn(setFollowing);
        Mockito.when(setFollowing.size()).thenReturn(1);
        Mockito.when(setFollowers.size()).thenReturn(2);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getNumFollows/0"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        ArrayList<Integer> aNumFollows = _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), ArrayList.class);
        Assertions.assertEquals(1, aNumFollows.get(0));
        Assertions.assertEquals(2, aNumFollows.get(1));
    }

    @Test
    public void getUserFollowingAndUserFollowsNoOneTest() throws Exception {
        //given
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findBy_sUsername("user")).thenReturn(user);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getFollowing/user"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(0, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    @Test
    public void getUsersByUsernameWhenThereAreNoMatches() throws Exception {
        //given
        Mockito.when(_userRepository.findFirst7By_sUsernameStartsWith("user")).thenReturn(Collections.emptyList());
        //Mockito.when(aUsersToShow.size()).thenReturn(7);
        //when
        ResultActions response = _mockMvc.perform(get("/api/findUsers/user"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(0, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), ArrayList.class).size());
    }

    @Test
    public void getUsersByUsernameWhenThereAreMoreThanSevenMatches() throws Exception {
        List<User> aUserList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            aUserList.add(new User("example" + i + "@gmail.com", "password", "username" + i, "user", "description", "name", new Date(), new Province()));
        }
        Mockito.when(_userRepository.findFirst7By_sUsernameStartsWith(anyString())).thenReturn(aUserList);
        //when
        ResultActions response = _mockMvc.perform(get("/api/findFirst7Users/user"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(7, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), ArrayList.class).size());
    }

    @Test
    public void getUsersByUsernameWhenThereAreMoreThanZeroAndLessSevenMatches() throws Exception {
        //given
        List<User> aUserList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            aUserList.add(new User("example" + i + "@gmail.com", "password", "username" + i, "description", "user", "name", new Date(), new Province()));
        }
        Mockito.when(_userRepository.findFirst7By_sUsernameStartsWith(anyString())).thenReturn(aUserList);
        //when
        ResultActions response = _mockMvc.perform(get("/api/findUsers/user"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(3, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), ArrayList.class).size());
    }

    @Test
    public void filterUserIdsWhenThereAreNoInterestsAndNoLocation() throws Exception {
        //given
        List<Integer> aUserIds = List.of(1, 2, 3, 4);
        UserFilterDTO filter = new UserFilterDTO(Collections.emptyList(), null, null, null);
        //UserFilterDTO filter = Mockito.mock(UserFilterDTO.class);
        //public UserFilterDTO(List<String> asInterests, String sCountry, String sRegion, String sProvince) {
        /*
        Mockito.when(filter.get_asInterests()).thenReturn(new ArrayList<String>(Arrays.asList("Música", "Videojuegos")));
        Mockito.when(filter.get_sCountry()).thenReturn(null);
        Mockito.when(filter.get_sRegion()).thenReturn(null);
        Mockito.when(filter.get_sProvince()).thenReturn(null);

         */
        //Mockito.when(_userRepository.findUserIdsByLocation()).thenReturn(aUserIds);
        Mockito.when(_userRepository.findFilteredUsers(null, null, null, null, null, null, any(User.class), any(Pageable.class)));
        //when
        System.out.println("Parametro enviado: " + String.valueOf(filter.get_asInterests()));
        ResultActions response = _mockMvc.perform(post("/api/filterUsers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(filter)));
        //List<Integer> result = _personController.filterUsers(filter, 0);
        /*
        ResultActions response = _mockMvc.perform(post("/api/filterUsers/1")
                .param("asInterests", String.valueOf(filter.get_asInterests()))
                .param("sCountry", filter.get_sCountry())
                .param("sRegion", filter.get_sRegion())
                .param("sProvince", filter.get_sProvince()));

         */

        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());

    }

}
