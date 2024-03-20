package es.uca.tfg.backend.controller;
import es.uca.tfg.backend.config.AbstractTest;
import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.RegisterDTO;
import es.uca.tfg.backend.rest.UserChecker;
import es.uca.tfg.backend.rest.UserDTO;
import es.uca.tfg.backend.service.PersonService;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;

@WebMvcTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class PersonControllerTestUT extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private PersonService _personService;
    @Mock
    private UserRepository _userRepository;
    @Mock
    private AdminRepository _adminRepository;
    @Mock
    private PersonRepository _personRepository;
    @Mock
    private InterestRepository _interestRepository;
    @Mock
    private ImagePathRepository _imagePathRepository;
    @Mock
    private CountryRepository _countryRepository;
    @Mock
    private RegionRepository _regionRepository;
    @Mock
    private ProvinceRepository _provinceRepository;
    @Mock
    private AboutMeAnswerRepository _answerRepository;
    @Mock
    private AboutMeQuestionRepository _questionRepository;
    @Mock
    private CategoryRepository _categoryRepository;
    @Mock
    private TypeNotificationRepository _typeNotificationRepository;
    @Mock
    private FAQRepository _faqRepository;

    @InjectMocks
    private PersonController _controller;

    @Test
    public void registerUserWillRegisterNewUser() {
        //given
        Province province = Mockito.mock(Province.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date tBirthDate = calendar.getTime();
        RegisterDTO dto = new RegisterDTO("test@gmail.com", "Test", "TestUsername", "TestPassword", tBirthDate, "");
        User user = new User(dto.get_sEmail(), dto.get_sPassword(),
                dto.get_sUsername(), "", "user", dto.get_sName(),
                dto.get_tBirthDate(), dto.get_sProvince() == null ? null : province);
        Mockito.when(_personRepository.countBy_sUsername(dto.get_sUsername())).thenReturn(0l);
        Mockito.when(_personRepository.save(any(Person.class))).thenReturn(user);
        //when
        String sMessage = _controller.registerNewUser(dto);
        //then
        Mockito.verify(_personRepository).save(any(Person.class));
        Assertions.assertEquals("Usuario creado correctamente.", sMessage);
    }

    @Test
    public void registerUserWillFailAsUsernameIsAlreadyRegistered() {
        //given
        Province province = Mockito.mock(Province.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date tBirthDate = calendar.getTime();
        RegisterDTO dto = new RegisterDTO("test@gmail.com", "Test", "TestUsername", "TestPassword", tBirthDate, "");
        User user = new User(dto.get_sEmail(), dto.get_sPassword(),
                dto.get_sUsername(), "", "user", dto.get_sName(),
                dto.get_tBirthDate(), dto.get_sProvince() == null ? null : province);
        Mockito.when(_personRepository.countBy_sUsername(dto.get_sUsername())).thenReturn(1l);
        Mockito.when(_personRepository.save(any(Person.class))).thenReturn(user);
        //when
        String sMessage = _controller.registerNewUser(dto);
        //then
        Mockito.verify(_personRepository, Mockito.times(0)).save(any(Person.class));
        Assertions.assertEquals("Error. El nombre de usuario ya existe.", sMessage);
    }

    @Test
    public void isUsernameTakenWillReturnTrueAsUsernameIsAlreadyTaken() {
        //given
        UserChecker.UsernameChecker dto = new UserChecker.UsernameChecker("TestUsername");
        Mockito.when(_personRepository.countBy_sUsername(dto.get_sUsername())).thenReturn(1l);
        //when
        boolean bIsUsernameTaken = _controller.isUsernameTaken(dto);
        //then
        Assertions.assertTrue(bIsUsernameTaken);
    }

    @Test
    public void isUsernameTakenWillReturnFalseAsUsernameIsNotTaken() {
        //given
        UserChecker.UsernameChecker dto = new UserChecker.UsernameChecker("TestUsername");
        Mockito.when(_personRepository.countBy_sUsername(dto.get_sUsername())).thenReturn(0l);
        //when
        boolean bIsUsernameTaken = _controller.isUsernameTaken(dto);
        //then
        Assertions.assertFalse(bIsUsernameTaken);
    }

    @Test
    public void isEmailTakenWillReturnTrueAsEmailIsTaken() {
        //given
        UserChecker.EmailChecker dto = new UserChecker.EmailChecker("TestEmail@gmail.com");
        Mockito.when(_personRepository.countBy_sEmail(dto.get_sEmail())).thenReturn(1l);
        //when
        boolean bIsEmailTaken = _controller.isEmailTaken(dto);
        //then
        Assertions.assertTrue(bIsEmailTaken);
    }

    public void isEmailTakenWillReturnFalseAsEmailIsNotTaken() {
        //given
        UserChecker.EmailChecker dto = new UserChecker.EmailChecker("TestEmail@gmail.com");
        Mockito.when(_personRepository.countBy_sEmail(dto.get_sEmail())).thenReturn(0l);
        //when
        boolean bIsEmailTaken = _controller.isEmailTaken(dto);
        //then
        Assertions.assertFalse(bIsEmailTaken);
    }

    @Test
    public void getUserWillReturnUser() {
        //given
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(user.get_iId()).thenReturn(1);
        //when
        User returnedUser = _controller.getUser(1);
        //then
        Assertions.assertEquals(1, returnedUser.get_iId());
    }

    @Test
    public void getUserWillReturnFailAndReturnEmptyUserAsUserDoesNotExist() {
        //given
        User user = Mockito.mock(User.class);
        Mockito.when(user.get_iId()).thenReturn(1);
        //when
        User returnedUser = _controller.getUser(1);
        //then
        Assertions.assertEquals(0, returnedUser.get_iId());
    }

    @Test
    public void getUserFromUsernameWillReturnUser() {
        //given
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findBy_sUsername("TestUsername")).thenReturn(user);
        Mockito.when(user.get_iId()).thenReturn(1);
        //when
        User returnedUser = _controller.getUserFromUsername("TestUsername");
        //then
        Assertions.assertEquals(1, returnedUser.get_iId());
    }

    @Test
    public void getUserFromUsernameWillFailAndReturnEmptyUserAsUsernameDoesNotExist() {
        //given
        User user = Mockito.mock(User.class);
        Mockito.when(user.get_iId()).thenReturn(1);
        //when
        User returnedUser = _controller.getUserFromUsername("TestUsername");
        //then
        Assertions.assertEquals(0, returnedUser.get_iId());
    }

    @Test
    public void updateUserDetailsWillUpdateExistingUser() {
        //given
        UserDTO dto = new UserDTO(1, "Testname updated", "TestUsername updated", "test@gmail.com", "Test description updated",
                -1, "password", "passwordUpdated", "passwordUpdated", true, new ArrayList<>());
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        User returnedUser = _controller.updateUserDetails(dto);
        //then
        Mockito.verify(user).set_sName(dto.get_sName());
        Mockito.verify(user).set_sUsername(dto.get_sUsername());
        Mockito.verify(user).set_province(null);
    }

    @Test
    public void updateUserDetailsWilLFailAsUserDoesNotExist() {
        //given
        UserDTO dto = new UserDTO(1, "Testname updated", "TestUsername updated", "test@gmail.com", "Test description updated",
                -1, "password", "passwordUpdated", "passwordUpdated", true, new ArrayList<>());
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        User returnedUser = _controller.updateUserDetails(dto);
        //then
        Mockito.verify(user, Mockito.times(0)).set_sName(dto.get_sName());
        Mockito.verify(user, Mockito.times(0)).set_sUsername(dto.get_sUsername());
        Mockito.verify(user, Mockito.times(0)).set_province(null);
    }

    @Test
    public void updateUserInformationWillUpdateExistingUserDescription() {
        //given
        UserDTO dto = new UserDTO(1, "Testname updated", "TestUsername updated", "test@gmail.com", "Test description updated",
                -1, "password", "passwordUpdated", "passwordUpdated", true, new ArrayList<>());
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        User returnedUser = _controller.updateUserInformation(dto);
        //then
        Mockito.verify(user).set_sDescription(dto.get_sDescription());
    }

    @Test
    public void updateUserInformationWillFailAsUserDoesNotExist() {
        //given
        UserDTO dto = new UserDTO(1, "Testname updated", "TestUsername updated", "test@gmail.com", "Test description updated",
                -1, "password", "passwordUpdated", "passwordUpdated", true, new ArrayList<>());
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        User returnedUser = _controller.updateUserInformation(dto);
        //then
        Mockito.verify(user).set_sDescription(dto.get_sDescription());
    }

    @Test
    public void uploadProfileImageWillUploadImageAndUserDidntHaveProfileImage() throws IOException {
        //given
        User user = new User();
        ImagePath imagePath = new ImagePath();
        user.set_profileImagePath(null);
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_imagePathRepository.save(any(ImagePath.class))).thenReturn(imagePath);
        //Mockito.when(user.get_profileImagePath()).thenReturn(null);
        //Mockito.when(user.get_iId()).thenReturn(1);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("filename.jpg");
        //when
        ImagePath profileImg = _controller.saveProfileImage(1, multipartFile);
        //then
        Mockito.verify(_imagePathRepository).save(any(ImagePath.class));
        Mockito.verify(multipartFile, Mockito.times(1)).transferTo(any(File.class));
        Mockito.verify(_userRepository).save(user);
    }

    @Test
    public void uploadProfileImageWillUploadImageAndUserAlreadyHadProfileImage() throws IOException {
        //given
        User user = new User();
        ImagePath currentProfileImg = new ImagePath();
        user.set_profileImagePath(currentProfileImg);
        ImagePath imagePath = new ImagePath();
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_imagePathRepository.save(any(ImagePath.class))).thenReturn(imagePath);
        //Mockito.when(user.get_profileImagePath()).thenReturn(null);
        //Mockito.when(user.get_iId()).thenReturn(1);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("filename.jpg");
        //when
        ImagePath profileImg = _controller.saveProfileImage(1, multipartFile);
        //then
        Mockito.verify(_imagePathRepository).save(any(ImagePath.class));
        Mockito.verify(multipartFile, Mockito.times(0)).transferTo(any(File.class));
        Mockito.verify(_userRepository).save(user);
    }

    @Test
    public void saveInterestsWillUpdateExistingUserInterests() {
        //given
        User user = new User();
        String[] asInterests = {"TestInterest1", "TestInterest2"};
        Interest interest1 = new Interest(asInterests[0]);
        Interest interest2 = new Interest(asInterests[1]);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_interestRepository.findBy_sName(asInterests[0])).thenReturn(interest1);
        Mockito.when(_interestRepository.findBy_sName(asInterests[1])).thenReturn(interest2);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        Set<Interest> aInterests = _controller.saveInterests(1, asInterests);
        //then
        Assertions.assertEquals(2, aInterests.size());
    }

    @Test
    public void saveInterestsWillFailAndReturnEmptySetAsUserDoesNotExist() {
        //given
        User user = new User();
        String[] asInterests = {"TestInterest1", "TestInterest2"};
        Interest interest1 = new Interest(asInterests[0]);
        Interest interest2 = new Interest(asInterests[1]);
        Mockito.when(_interestRepository.findBy_sName(asInterests[0])).thenReturn(interest1);
        Mockito.when(_interestRepository.findBy_sName(asInterests[1])).thenReturn(interest2);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        Set<Interest> aInterests = _controller.saveInterests(1, asInterests);
        //then
        Assertions.assertEquals(0, aInterests.size());
        Assertions.assertEquals(Collections.emptySet(), aInterests);
    }

    @Test
    public void uploadUserImageWillUploadImage() throws IOException {
        //given
        User user = Mockito.mock(User.class);
        Set<ImagePath> aUserImages = new HashSet<>();
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        ImagePath imagePath = new ImagePath();
        Mockito.when(_imagePathRepository.save(any(ImagePath.class))).thenReturn(imagePath);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(user.get_iId()).thenReturn(1);
        Mockito.when(user.get_setImagePath()).thenReturn(aUserImages);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("filename.jpg");
        //when
        ImagePath uploadedImage = _controller.uploadUserImage(1, multipartFile);
        //then
        Mockito.verify(multipartFile).transferTo(any(File.class));
        Mockito.verify(_imagePathRepository).save(any(ImagePath.class));
        Mockito.verify(_userRepository).save(user);
    }

    @Test
    public void uploadUserImageWillFailAsUserDoesNotExist() throws IOException {
        //given
        User user = Mockito.mock(User.class);
        Set<ImagePath> aUserImages = new HashSet<>();
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        ImagePath imagePath = new ImagePath();
        Mockito.when(_imagePathRepository.save(any(ImagePath.class))).thenReturn(imagePath);
        Mockito.when(user.get_iId()).thenReturn(1);
        Mockito.when(user.get_setImagePath()).thenReturn(aUserImages);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("filename.jpg");
        //when
        ImagePath uploadedImage = _controller.uploadUserImage(1, multipartFile);
        //then
        Mockito.verify(multipartFile, Mockito.times(0)).transferTo(any(File.class));
        Mockito.verify(_imagePathRepository, Mockito.times(0)).save(any(ImagePath.class));
        Mockito.verify(_userRepository, Mockito.times(0)).save(user);
    }

    @Test
    public void updateUserPrivacityOptionsWillUpdateExistingUserPrivacityOptions() {
        //given
        UserDTO dto = new UserDTO(1, "Testname updated", "TestUsername updated", "test@gmail.com", "Test description updated",
                -1, "password", "passwordUpdated", "passwordUpdated", true, new ArrayList<>());
        String sPasswordSalt = RandomStringUtils.randomAlphanumeric(32);
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        Mockito.when(user.get_iId()).thenReturn(1);
        Mockito.when(user.get_sPasswordSalt()).thenReturn(sPasswordSalt);
        Mockito.when(user.checkPassword(dto.get_sCurrentPassword())).thenReturn(true);
        //when
        User updatedUser = _controller.updateUserPrivacityOptions(dto);
        //then
        Mockito.verify(user).set_sEmail(dto.get_sEmail());
        Mockito.verify(user).set_bIsPrivate(dto.is_bIsPrivate());
        Mockito.verify(user).set_sPassword(DigestUtils.md5DigestAsHex((dto.get_sNewPassword() + sPasswordSalt).getBytes()));
        Mockito.verify(_userRepository).save(user);
        Assertions.assertEquals(1, updatedUser.get_iId());
    }

    @Test
    public void updateUserPrivacityOptionsWillFailAndReturnEmptyUserAsUserDoesNotExist() {
        //given
        UserDTO dto = new UserDTO(1, "Testname updated", "TestUsername updated", "test@gmail.com", "Test description updated",
                -1, "password", "passwordUpdated", "passwordUpdated", true, new ArrayList<>());
        String sPasswordSalt = RandomStringUtils.randomAlphanumeric(32);
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        Mockito.when(user.get_iId()).thenReturn(1);
        Mockito.when(user.get_sPasswordSalt()).thenReturn(sPasswordSalt);
        Mockito.when(user.checkPassword(dto.get_sCurrentPassword())).thenReturn(true);
        //when
        User updatedUser = _controller.updateUserPrivacityOptions(dto);
        //then
        Mockito.verify(user, Mockito.times(0)).set_sEmail(dto.get_sEmail());
        Mockito.verify(user, Mockito.times(0)).set_bIsPrivate(dto.is_bIsPrivate());
        Mockito.verify(user, Mockito.times(0)).set_sPassword(DigestUtils.md5DigestAsHex((dto.get_sNewPassword() + sPasswordSalt).getBytes()));
        Mockito.verify(_userRepository, Mockito.times(0)).save(user);
        Assertions.assertEquals(0, updatedUser.get_iId());
    }

    @Test
    public void saveImagesWillUploadSentImages() throws IOException {
        //given
        User user = Mockito.mock(User.class);
        ImagePath imagePath = Mockito.mock(ImagePath.class);
        Set<ImagePath> aUserImages = Mockito.mock(Set.class);
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        MultipartFile multipartFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile[] aMultipartFile = new MultipartFile[]{ multipartFile, multipartFile1 };
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("image1.jpg");
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("image2.jpg");
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_imagePathRepository.save(any(ImagePath.class))).thenReturn(new ImagePath());
        Mockito.when(user.get_setImagePath()).thenReturn(aUserImages);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        String sMessage = _controller.saveImages(1, aMultipartFile);
        //then
        Mockito.verify(multipartFile).getOriginalFilename();
        Mockito.verify(multipartFile1).getOriginalFilename();
        Mockito.verify(aUserImages, Mockito.times(2)).add(any(ImagePath.class));
        Mockito.verify(_userRepository).save(user);
        Assertions.assertEquals("Imágenes subidas correctamente.", sMessage);
    }

    @Test
    public void saveImagesWillFailAndReturnErrorMessageAsUserDoesNotExist() throws IOException {
        //given
        User user = Mockito.mock(User.class);
        ImagePath imagePath = Mockito.mock(ImagePath.class);
        Set<ImagePath> aUserImages = Mockito.mock(Set.class);
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        MultipartFile multipartFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile[] aMultipartFile = new MultipartFile[]{ multipartFile, multipartFile1 };
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("image1.jpg");
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("image2.jpg");
        Mockito.when(_imagePathRepository.save(any(ImagePath.class))).thenReturn(new ImagePath());
        Mockito.when(user.get_setImagePath()).thenReturn(aUserImages);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        String sMessage = _controller.saveImages(1, aMultipartFile);
        //then
        Mockito.verify(multipartFile, Mockito.times(0)).getOriginalFilename();
        Mockito.verify(multipartFile1, Mockito.times(0)).getOriginalFilename();
        Mockito.verify(aUserImages, Mockito.times(0)).add(any(ImagePath.class));
        Mockito.verify(_userRepository, Mockito.times(0)).save(user);
        Assertions.assertEquals("Ha ocurrido un error al subir las imágenes.", sMessage);
    }










    @Test
    public void isUserFollowingWillReturnTrueAsUserFollowsTargetUser() {
        //given
        User user = Mockito.mock(User.class);
        User target = Mockito.mock(User.class);
        Set<User> following = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(2)).thenReturn(target);
        Mockito.when(user.get_setFollowing()).thenReturn(following);
        Mockito.when(following.contains(target)).thenReturn(true);
        //when
        boolean bIsFollowing = _controller.isUserFollowing(1, 2);
        //then
        Mockito.verify(following).contains(target);
        Assertions.assertTrue(bIsFollowing);
    }

    @Test
    public void isUserFollowingWillReturnFalseAsUserDoesNotFollowTargetUser() {
        //given
        User user = Mockito.mock(User.class);
        User target = Mockito.mock(User.class);
        Set<User> following = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(2)).thenReturn(target);
        Mockito.when(user.get_setFollowing()).thenReturn(following);
        Mockito.when(following.contains(target)).thenReturn(false);
        //when
        boolean bIsFollowing = _controller.isUserFollowing(1, 2);
        //then
        Mockito.verify(following).contains(target);
        Assertions.assertFalse(bIsFollowing);
    }

    @Test
    public void setFollowWillIncludeTargetUserInUserFollowingSetAsUserDoesNotFollowYetTarget() {
        //given
        User user = Mockito.mock(User.class);
        User target = Mockito.mock(User.class);
        Set<User> following = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(2)).thenReturn(target);
        Mockito.when(user.get_setFollowing()).thenReturn(following);
        Mockito.when(following.contains(any(User.class))).thenReturn(false);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        boolean bIsNowFollowing = _controller.setFollow(1, 2);
        //then
        Mockito.verify(following).contains(target);
        Mockito.verify(following).add(target);
        Assertions.assertTrue(bIsNowFollowing);
    }

    @Test
    public void setFollowWillExcludeTargetUserInUserFollowingSetAsUserAlreadyFollowedTarget() {
        //given
        User user = Mockito.mock(User.class);
        User target = Mockito.mock(User.class);
        Set<User> following = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(2)).thenReturn(target);
        Mockito.when(user.get_setFollowing()).thenReturn(following);
        Mockito.when(following.contains(any(User.class))).thenReturn(true);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        boolean bIsNowFollowing = _controller.setFollow(1, 2);
        //then
        Mockito.verify(following).contains(target);
        Mockito.verify(following).remove(target);
        Assertions.assertFalse(bIsNowFollowing);
    }

    @Test
    public void getNumFollowsWillReturnFollowUserInformation() {
        //given
        User user = Mockito.mock(User.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Set<User> following = Mockito.mock(Set.class);
        Set<User> followers = Mockito.mock(Set.class);
        Mockito.when(user.get_setFollowing()).thenReturn(following);
        Mockito.when(user.get_setFollowers()).thenReturn(followers);
        Mockito.when(following.size()).thenReturn(5);
        Mockito.when(followers.size()).thenReturn(4);
        //when
        ArrayList<Integer> aiNumFollows = _controller.getNumFollows(1);
        //then
        Mockito.verify(following).size();
        Mockito.verify(followers).size();
        Assertions.assertEquals(5, aiNumFollows.get(0));
        Assertions.assertEquals(4, aiNumFollows.get(1));
    }

    @Test
    public void getNumFollowsWillReturnErrorIndicativeInformation() {
        //given
        User user = Mockito.mock(User.class);
        Set<User> following = Mockito.mock(Set.class);
        Set<User> followers = Mockito.mock(Set.class);
        Mockito.when(user.get_setFollowing()).thenReturn(following);
        Mockito.when(user.get_setFollowers()).thenReturn(followers);
        Mockito.when(following.size()).thenReturn(5);
        Mockito.when(followers.size()).thenReturn(4);
        //when
        ArrayList<Integer> aiNumFollows = _controller.getNumFollows(1);
        //then
        Mockito.verify(following, Mockito.times(0)).size();
        Mockito.verify(followers, Mockito.times(0)).size();
        Assertions.assertEquals(-1, aiNumFollows.get(0));
        Assertions.assertEquals(-1, aiNumFollows.get(1));
    }

    @Test
    public void suspendReactivateAccountWillSuspendExistingAccount() {
        //given
        User user = new User();
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        boolean bIsSuspended = _controller.suspendReactivateAccount(1, 1);
        //then
        Mockito.verify(_userRepository).save(user);
        Assertions.assertTrue(bIsSuspended);
    }

    @Test
    public void suspendReactivateAccountWillReactivateSuspendedAccount() {
        //given
        User user = new User();
        user.set_bIsSuspended(true);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        boolean bIsSuspended = _controller.suspendReactivateAccount(1, 0);
        //then
        Mockito.verify(_userRepository).save(user);
        Assertions.assertFalse(bIsSuspended);
    }

    @Test
    public void suspendReactivateAccountWillFailAndWontModifyUserAccount() {
        //given
        User user = new User();
        user.set_bIsSuspended(true);
        Mockito.when(_userRepository.save(user)).thenReturn(user);
        //when
        boolean bIsSuspended = _controller.suspendReactivateAccount(1, 0);
        //then
        Mockito.verify(_userRepository, Mockito.times(0)).save(user);
        Assertions.assertFalse(bIsSuspended);
    }

    @Test
    public void blockUnblockUserWillBlockTargetUser() {
        //given
        User user = Mockito.mock(User.class);
        User target = Mockito.mock(User.class);
        Set<User> blockedBy = Mockito.mock(Set.class);
        Set<User> following = Mockito.mock(Set.class);
        Set<User> targetFollowing = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(target));
        Mockito.when(target.get_setBlockedBy()).thenReturn(blockedBy);
        Mockito.when(user.get_setFollowing()).thenReturn(following);
        Mockito.when(target.get_setFollowing()).thenReturn(targetFollowing);
        Mockito.when(blockedBy.contains(user)).thenReturn(false);
        //when
        boolean bIsBlocked = _controller.blockUnblockUser(1, 2);
        //then
        Mockito.verify(blockedBy).add(user);
        Mockito.verify(following).remove(target);
        Mockito.verify(targetFollowing).remove(user);
        Mockito.verify(_userRepository).save(target);
        Assertions.assertTrue(bIsBlocked);
    }

    @Test
    public void blockUnblockUserWillUnblockTargetUser() {
        //given
        User user = Mockito.mock(User.class);
        User target = Mockito.mock(User.class);
        Set<User> blockedBy = Mockito.mock(Set.class);
        Set<User> following = Mockito.mock(Set.class);
        Set<User> targetFollowing = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(target));
        Mockito.when(target.get_setBlockedBy()).thenReturn(blockedBy);
        Mockito.when(user.get_setFollowing()).thenReturn(following);
        Mockito.when(target.get_setFollowing()).thenReturn(targetFollowing);
        Mockito.when(blockedBy.contains(user)).thenReturn(true);
        //when
        boolean bIsBlocked = _controller.blockUnblockUser(1, 2);
        //then
        Mockito.verify(blockedBy).remove(user);
        Mockito.verify(_userRepository).save(target);
        Assertions.assertFalse(bIsBlocked);
    }

    @Test
    public void checkBlockWillReturnTrueAsUserHasTargetBlocked() {
        //given
        User user = Mockito.mock(User.class);
        User target = Mockito.mock(User.class);
        Set<User> blockedBy = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(target));
        Mockito.when(target.get_setBlockedBy()).thenReturn(blockedBy);
        Mockito.when(blockedBy.contains(user)).thenReturn(true);
        //when
        boolean bIsBlocked = _controller.checkBlock(1, 2);
        //then
        Assertions.assertTrue(bIsBlocked);
    }

    @Test
    public void checkBlockWillReturnFalseAsUserHasNotTargetBlocked() {
        //given
        User user = Mockito.mock(User.class);
        User target = Mockito.mock(User.class);
        Set<User> blockedBy = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(_userRepository.findById(2)).thenReturn(Optional.of(target));
        Mockito.when(target.get_setBlockedBy()).thenReturn(blockedBy);
        Mockito.when(blockedBy.contains(user)).thenReturn(false);
        //when
        boolean bIsBlocked = _controller.checkBlock(1, 2);
        //then
        Assertions.assertFalse(bIsBlocked);
    }

    @Test
    public void getBlockedByWillReturnListOfUsersWhoBlockedUser() {
        //given
        User user = Mockito.mock(User.class);
        Set<User> aBlockedBy = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(user.get_setBlockedBy()).thenReturn(aBlockedBy);
        Mockito.when(aBlockedBy.size()).thenReturn(5);
        //when
        Set<User> aUsersWhoBlockedUser = _controller.getBlockedBy(1);
        //then
        Assertions.assertEquals(5, aUsersWhoBlockedUser.size());
    }

    @Test
    public void getBlockedByWillFailAndReturnEmptySet() {
        //given
        User user = Mockito.mock(User.class);
        Set<User> aBlockedBy = Mockito.mock(Set.class);
        Mockito.when(user.get_setBlockedBy()).thenReturn(aBlockedBy);
        Mockito.when(aBlockedBy.size()).thenReturn(5);
        //when
        Set<User> aUsersWhoBlockedUser = _controller.getBlockedBy(1);
        //then
        Assertions.assertEquals(Collections.emptySet(), aUsersWhoBlockedUser);
    }

    @Test
    public void checkMutualBlockWillReturnTrueAsAtLeastOneUserHasTheOtherUserBlocked() {
        //given
        User user = Mockito.mock(User.class);
        User blocked = Mockito.mock(User.class);
        Set<User> aBlocked = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(2)).thenReturn(blocked);
        Mockito.when(user.get_setBlockedBy()).thenReturn(aBlocked);
        Mockito.when(blocked.get_setBlockedBy()).thenReturn(aBlocked);
        Mockito.when(aBlocked.contains(user)).thenReturn(true);
        Mockito.when(aBlocked.contains(blocked)).thenReturn(false);
        //when
        boolean bIsMutual = _controller.checkMutualBlock(1, 2);
        //then
        Assertions.assertFalse(bIsMutual);
    }

    @Test
    public void checkMutualBlockWillReturnTrueAsNoOneHasEachOtherBlocked() {
        //given
        User user = Mockito.mock(User.class);
        User blocked = Mockito.mock(User.class);
        Set<User> aBlocked = Mockito.mock(Set.class);
        Mockito.when(_userRepository.findBy_iId(1)).thenReturn(user);
        Mockito.when(_userRepository.findBy_iId(2)).thenReturn(blocked);
        Mockito.when(user.get_setBlockedBy()).thenReturn(aBlocked);
        Mockito.when(blocked.get_setBlockedBy()).thenReturn(aBlocked);
        Mockito.when(aBlocked.contains(user)).thenReturn(false);
        Mockito.when(aBlocked.contains(blocked)).thenReturn(false);
        //when
        boolean bIsMutual = _controller.checkMutualBlock(1, 2);
        //then
        Assertions.assertTrue(bIsMutual);
    }
}
