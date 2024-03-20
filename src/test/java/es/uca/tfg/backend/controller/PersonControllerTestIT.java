package es.uca.tfg.backend.integration.controller;

import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.rest.RegisterDTO;
import es.uca.tfg.backend.rest.UserChecker;
import es.uca.tfg.backend.rest.UserDTO;
import es.uca.tfg.backend.rest.UserFilterDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.File;
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
        RegisterDTO registerDTO = new RegisterDTO("example@gmail.com", "name", "username", "password", new Date(), "Cádiz");
        //given(_personService.update(any(Person.class))).willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));
        //when
        ResultActions response = _mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(registerDTO)));
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
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        UserDTO dto = new UserDTO(1, "user updated", "username updated", "", "",
                1, "", "", "", false, Collections.emptyList());
        Country country = new Country("Test country");
        Region region = new Region("Test region", country);
        Province province = new Province("Test province", region);
        Mockito.when(_provinceRepository.findById(1)).thenReturn(Optional.of(province));
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        ResultActions response = _mockMvc.perform(post("/api/updateUserAccountDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_objectMapper.writeValueAsString(dto)));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(dto.get_sName(), user.get_sName());
        Assertions.assertEquals(dto.get_sUsername(), user.get_sUsername());
        Assertions.assertEquals(province.get_sName(), user.get_province().get_sName());
        Mockito.verify(_userRepository).save(user);

    }

    @Test
    void updateInterestsTest() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        //User user = Mockito.mock(User.class);
        String[] asInterests = new String[] {"Música", "Videojuegos", "Deportes"};
        Set<Interest> aUserInterests = new HashSet<>();
        Interest interest = Mockito.mock(Interest.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
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
/*
    @Test
    void uploadUserProfileImageTest() throws Exception {
        //given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        Resource resource = new ClassPathResource("filename");
        ImagePath imagePath = new ImagePath();
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

 */

    @Test
    public void getUserInterestsTest() throws Exception {
        // Given
        User user = new User("example@gmail.com", "password", "username", "description", "user", "name", new Date(), new Province());
        List<Interest> interests = Arrays.asList(new Interest("Música"), new Interest("Videojuegos"));
        user.set_setInterests(interests.stream().collect(Collectors.toSet()));
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));

        // When
        ResultActions response = _mockMvc.perform(get("/api/getUserInterests/" + 1));
                //.andExpect(content().json("[{'_sName': 'Música'}, {'_sName': 'Videojuegos'}]"));

        // Then
        response.andDo(print())
                        .andExpect(status().is2xxSuccessful());
        Mockito.verify(_userRepository).findById(1);
        Assertions.assertEquals(2, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), List.class).size());
    }

    /*
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

     */

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
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(user.get_setFollowers()).thenReturn(setFollowers);
        Mockito.when(user.get_setFollowing()).thenReturn(setFollowing);
        Mockito.when(setFollowing.size()).thenReturn(1);
        Mockito.when(setFollowers.size()).thenReturn(2);
        //when
        ResultActions response = _mockMvc.perform(get("/api/getNumFollows/1"));
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
        ResultActions response = _mockMvc.perform(get("/api/findFirst7Users/user"));
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
        ResultActions response = _mockMvc.perform(get("/api/findFirst7Users/user"));
        //then
        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(3, _objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), ArrayList.class).size());
    }

    @Test
    public void filterUserIdsWhenThereAreNoInterestsAndNoLocation() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        List<User> aUsers = List.of(new User(), new User(), new User());
        Page<User> usersPage = new PageImpl<>(aUsers, pageable, aUsers.size());
        User user = Mockito.mock(User.class);

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
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.findFilteredUsers(null, null, null, null, null, null, user, PageRequest.of(0, 10))).thenReturn(usersPage);
        //when
        System.out.println("Parametro enviado: " + String.valueOf(filter.get_asInterests()));
        ResultActions response = _mockMvc.perform(post("/api/filterUsers/1/0")
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
