package es.uca.tfg.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.entity.ImagePath;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.RegisterDTO;
import es.uca.tfg.backend.rest.UserChecker;
import es.uca.tfg.backend.rest.UserDTO;
import es.uca.tfg.backend.rest.UserFilterDTO;
import es.uca.tfg.backend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PersonController {
    @Autowired
    private PersonService _personService;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private AdminRepository _adminRepository;
    @Autowired
    private PersonRepository _personRepository;
    @Autowired
    private InterestRepository _interestRepository;
    @Autowired
    private ImagePathRepository _imagePathRepository;

    @Autowired
    private CountryRepository _countryRepository;

    @Autowired
    private RegionRepository _regionRepository;

    @Autowired
    private ProvinceRepository _provinceRepository;

    @Autowired
    private AboutMeAnswerRepository _answerRepository;

    @Autowired
    private AboutMeQuestionRepository _questionRepository;

    @Autowired
    private CategoryRepository _categoryRepository;

    @Autowired
    private TypeNotificationRepository _typeNotificationRepository;

    @Autowired
    private FAQRepository _faqRepository;


    private String _sUploadPath = new FileSystemResource("").getFile().getAbsolutePath() + "/app/static/images/users/";

    @PostMapping("/register")
    public String registerNewUser(@RequestBody RegisterDTO registerDTO) {
        String sMessage;

        if (_personRepository.countBy_sUsername(registerDTO.get_sUsername()) == 0) {

            User user = _personRepository.save(new User(registerDTO.get_sEmail(), registerDTO.get_sPassword(),
                    registerDTO.get_sUsername(), "", "user", registerDTO.get_sName(),
                    registerDTO.get_tBirthDate(), registerDTO.get_sProvince() == null ? null : _provinceRepository.findBy_sName(registerDTO.get_sProvince())));
            sMessage = "Usuario creado correctamente.";
        } else {
            sMessage = "Error. El nombre de usuario ya existe.";
        }
        return sMessage;
    }

    @PostMapping("/checkUser")
    public boolean isUsernameTaken(@RequestBody UserChecker.UsernameChecker usernameChecker) {
        if (_personRepository.countBy_sUsername(usernameChecker.get_sUsername()) != 0) {
            usernameChecker.set_bIsUsernameTaken(true);
        }
        return usernameChecker.is_bIsUsernameTaken();
    }

    @PostMapping("/checkEmail")
    public boolean isEmailTaken(@RequestBody UserChecker.EmailChecker emailChecker) {
        if (_personRepository.countBy_sEmail(emailChecker.get_sEmail()) != 0) {
            emailChecker.set_bIsEmailTaken(true);
        }
        return emailChecker.is_bIsEmailTaken();
    }

    @PostMapping("/login")
    public Person authenticateLogin(@RequestBody UserChecker.LoginChecker loginChecker) {
        return _personService.authenticate(loginChecker.get_sEmail(), loginChecker.get_sPassword());
    }

    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable("userId") int iUserId) {
        Optional<User> optUser = _userRepository.findById(iUserId);
        return optUser.isPresent() ? optUser.get() : new User();
    }

    @GetMapping("/getUserFromUsername/{username}")
    public User getUserFromUsername(@PathVariable("username") String sUsername) {
        User user = _userRepository.findBy_sUsername(sUsername);
        return user != null ? user : new User();
    }

    @PostMapping("/updateUserAccountDetails")
    public User updateUserDetails(@RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = _userRepository.findById(userDTO.get_iUserId());
        Optional<Province> optionalProvince = _provinceRepository.findById(userDTO.get_iProvinceId());
        if (optionalUser.isPresent()) {
            optionalUser.get().set_sName(userDTO.get_sName());
            optionalUser.get().set_sUsername(userDTO.get_sUsername());
            optionalUser.get().set_province(optionalProvince.isPresent() ? optionalProvince.get() : null);
            return _userRepository.save(optionalUser.get());
        } else {
            return new User();
        }
    }

    @PostMapping("/updateUserDescription")
    public User updateUserInformation(@RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = _userRepository.findById(userDTO.get_iUserId());
        if (optionalUser.isPresent()) {
            optionalUser.get().set_sDescription(userDTO.get_sDescription());
            return _userRepository.save(optionalUser.get());
        } else {
            return new User();
        }
    }


    @PostMapping("/uploadInterests")
    public Set<Interest> saveInterests(@RequestParam("id") int iId, @RequestParam("interests[]") String[] aSInterests) {
        Optional<User> optionalUser = _userRepository.findById(iId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Interest> aInterests = new HashSet<>();
            for (int i = 0; i < aSInterests.length; i++) {
                aInterests.add(_interestRepository.findBy_sName(aSInterests[i]));
            }
            user.set_setInterests(aInterests);
            user = _userRepository.save(user);

            return user.get_setInterests();
        } else {
            return Collections.emptySet();
        }
    }

    @GetMapping("/getUserInterests/{userId}")
    public List<Interest> getUserInterests(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? optionalUser.get().get_setInterests().parallelStream().toList() : Collections.emptyList();
    }

    @PostMapping("/updateUserPrivacityOptions")
    public User updateUserPrivacityOptions(@RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = _userRepository.findById(userDTO.get_iUserId());
        boolean bErrorHappened = false;
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.set_sEmail(userDTO.get_sEmail());
            user.set_bIsPrivate(userDTO.is_bIsPrivate());
            if (userDTO.get_sCurrentPassword() != null) {
                if (user.checkPassword(userDTO.get_sCurrentPassword())) {
                    user.set_sPassword(DigestUtils.md5DigestAsHex((userDTO.get_sNewPassword() + user.get_sPasswordSalt()).getBytes()));
                } else {
                    bErrorHappened = true;
                }
            }
            return bErrorHappened ? new User() : _userRepository.save(user);
        } else {
            return new User();
        }
    }

    @PostMapping("/uploadProfileImage")
    public ImagePath saveProfileImage(@RequestParam("id") int iId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        User user = _userRepository.findBy_iId(iId);
        Path path = Paths.get(_sUploadPath);
        String sFilename = user.get_iId() + "-profileImg-" + multipartFile.getOriginalFilename();
        ImagePath imagePath = _imagePathRepository.save(new ImagePath(sFilename));
        sFilename = imagePath.get_iId() + "-" + sFilename;
        if (user.get_profileImagePath() != null) {          // Tenía foto de perfil
            user.get_profileImagePath().set_tDeleteDate(LocalDateTime.now());
        }

        File file = new File(_sUploadPath + sFilename);
        multipartFile.transferTo(file);

        imagePath.set_user(user);
        imagePath.set_sName(sFilename + "-" + imagePath.get_iId());
        user.set_profileImagePath(imagePath);
        user.get_profileImagePath().set_sName(sFilename);
        user = _userRepository.save(user);

        return imagePath;
    }

    @PostMapping("/uploadImages")
    public String saveImages(@RequestParam("id") int iId, @RequestParam("file[]") MultipartFile[] aMultipartFile) throws IOException {
        Optional<User> optionalUser = _userRepository.findById(iId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            for (int i = 0; i < aMultipartFile.length; i++) {
                String sFilename = aMultipartFile[i].getOriginalFilename();
                ImagePath imagePath = _imagePathRepository.save(new ImagePath(sFilename));
                sFilename = user.get_iId() + "-" + imagePath.get_iId() + "-" + sFilename;
                imagePath.set_sName(sFilename);
                File file = new File(_sUploadPath + sFilename);
                aMultipartFile[i].transferTo(file);
                user.get_setImagePath().add(imagePath);
            }
            user = _userRepository.save(user);
            return "Imágenes subidas correctamente.";
        } else {
            return "Ha ocurrido un error al subir las imágenes.";
        }

    }

    @PostMapping("/uploadUserImage")
    public ImagePath uploadUserImage(@RequestParam("userId") int iUserId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String sFilename = user.get_iId() + "-" + multipartFile.getOriginalFilename();
            File file = new File(_sUploadPath + sFilename);
            multipartFile.transferTo(file);
            ImagePath imagePath = _imagePathRepository.save(new ImagePath(sFilename));
            user.get_setImagePath().add(imagePath);
            _userRepository.save(user);
            return imagePath;
        } else {
            return new ImagePath();
        }
    }

    @GetMapping("/getProfileImage/{id}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getProfileImage(@PathVariable("id") int iId) throws FileNotFoundException {
        Optional<User> optionalUser = _userRepository.findById(iId);
        String sImageName = "GenericAvatar.png";
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.get_profileImagePath() != null)
                sImageName = user.get_profileImagePath().get_sName();
        }
        File file = new File(_sUploadPath + sImageName);
        FileInputStream fileInputStream = new FileInputStream(file);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(fileInputStream));
    }

    @GetMapping("/getImage/{imageName}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImagePath(@PathVariable("imageName") String sImageName) throws FileNotFoundException {
        File file = new File(_sUploadPath + sImageName);
        FileInputStream fileInputStream = new FileInputStream(file);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(fileInputStream));
    }

    @GetMapping("/getImageNames/{userId}")

    public List<ImagePath> getImageNames(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? optionalUser.get().get_setImagePath().stream().toList() : Collections.emptyList();
    }

    @GetMapping("/checkFollow/{userId}/{followingId}")
    public boolean isUserFollowing(@PathVariable("userId") int iUserId, @PathVariable("followingId") int iFollowingId) {
        return _userRepository.findBy_iId(iUserId).get_setFollowing().contains(_userRepository.findBy_iId(iFollowingId));
    }

    @PatchMapping("/setFollow/{userId}/{followId}")
    public boolean setFollow(@PathVariable("userId") Integer iUserId, @PathVariable("followId") Integer iFollowId) {
        boolean bIsFollowing = false;

        User user = _userRepository.findBy_iId(iUserId);
        User userToFollow = _userRepository.findBy_iId(iFollowId);
        if (user.get_setFollowing().contains(userToFollow)) {
            user.get_setFollowing().remove(userToFollow);
        } else {
            user.get_setFollowing().add(userToFollow);
            bIsFollowing = true;
        }
        user = _userRepository.save(user);
        return bIsFollowing;
    }

    @GetMapping("/getNumFollows/{userId}")
    public ArrayList<Integer> getNumFollows(@PathVariable("userId") int iUserId) {
        ArrayList<Integer> numFollows = new ArrayList<>();
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if(optionalUser.isPresent()) {
            numFollows.add(optionalUser.get().get_setFollowing().size());
            numFollows.add(optionalUser.get().get_setFollowers().size());
        } else {
            numFollows.add(-1);
            numFollows.add(-1);
        }
        return numFollows;
    }

    @GetMapping("/getFollowing/{username}")
    public List<User> getFollowing(@PathVariable("username") String sUsername) {
        User user = _userRepository.findBy_sUsername(sUsername);
        return user.get_iId() != 0 ? user.get_setFollowing().stream().toList() : Collections.emptyList();
    }

    @GetMapping("/getFollowers/{username}")
    public List<User> getFollowers(@PathVariable("username") String sUsername) {
        User user = _userRepository.findBy_sUsername(sUsername);
        return user.get_iId() != 0 ? user.get_setFollowers().stream().toList() : Collections.emptyList();
    }

    @GetMapping("/findFirst7Users/{username}")
    public List<User> getUsersByUsername(@PathVariable("username") String sUsername) {
        return _userRepository.findFirst7By_sUsernameStartsWith(sUsername);
    }

    @PostMapping(value = "/filterUsers/{userId}/{pageNumber}")
    public Page<User> filterUsers(@RequestBody UserFilterDTO filter, @PathVariable("userId") int iUserId, @PathVariable("pageNumber") int iPageNumber) throws JsonProcessingException {
        int iNumberOfInterests = filter.get_asInterests().size();
        Optional<Province> optionalProvince = _provinceRepository.findByName(filter.get_sProvince());
        Optional<Region> optionalRegion = _regionRepository.findBy_sName(filter.get_sRegion());
        Optional<Country> optionalCountry = _countryRepository.findBy_sName(filter.get_sCountry());
        User user = _userRepository.findById(iUserId).get();
        return _userRepository.findFilteredUsers(
                optionalProvince.isPresent() ? optionalProvince.get() : null,
                optionalRegion.isPresent() ? optionalRegion.get() : null,
                optionalCountry.isPresent() ? optionalCountry.get() : null,
                iNumberOfInterests >= 1 ? _interestRepository.findBy_sName(filter.get_asInterests().get(0)) : null,
                iNumberOfInterests >= 2 ? _interestRepository.findBy_sName(filter.get_asInterests().get(1)) : null,
                iNumberOfInterests >= 3 ? _interestRepository.findBy_sName(filter.get_asInterests().get(2)) : null,
                user, PageRequest.of(iPageNumber, 10));
    }

    @GetMapping("/countUsers")
    public long countUsers() {
        return _userRepository.count();
    }

    @PatchMapping("/suspendReactivateAccount/{userId}/{suspend}")
    public boolean suspendReactivateAccount(@PathVariable("userId") int iUserId, @PathVariable("suspend") int iSuspend) {
        boolean bSuspend = iSuspend == 1;
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.set_bIsSuspended(bSuspend);
            user = _userRepository.save(user);
            return user.is_bIsSuspended();
        } else {
            return false;
        }
    }

    //Devuelve true si el usuario ha desbloqueado al otro usuario
    @PatchMapping("/blockUnblockUser/{userId}/{blockedId}")
    public boolean blockUnblockUser(@PathVariable("userId") int iUserId, @PathVariable("blockedId") int iBlockedId) {
        User user = _userRepository.findById(iUserId).get();
        User blocked = _userRepository.findById(iBlockedId).get();
        boolean bIsBlocked = blocked.get_setBlockedBy().contains(user);
        if (!bIsBlocked) {      // No bloqueado
            blocked.get_setBlockedBy().add(user);
            blocked.get_setFollowing().remove(user);
            user.get_setFollowing().remove(blocked);    // Solo hace falta actualizar y guardar la parte directa
            _userRepository.save(user);
        } else {    // Ya estaba bloqueado
            blocked.get_setBlockedBy().remove(user);
        }
        _userRepository.save(blocked);
        return !bIsBlocked;
    }

    @GetMapping("/checkBlock/{userId}/{blockedId}")
    public boolean checkBlock(@PathVariable("userId") int iUserId, @PathVariable("blockedId") int iBlockedId) {
        User user = _userRepository.findById(iUserId).get();
        User blocked = _userRepository.findById(iBlockedId).get();
        return blocked.get_setBlockedBy().contains(user);
    }

    @GetMapping("/getBlockedByUsers/{userId}")
    public Set<User> getBlockedBy(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? optionalUser.get().get_setBlockedBy() : Collections.emptySet();
    }

    @GetMapping("/checkMutualBlock/{userId}/{blockedId}")
    public boolean checkMutualBlock(@PathVariable("userId") int iUserId, @PathVariable("blockedId") int iBlockedId) {
        User user = _userRepository.findBy_iId(iUserId);
        User blocked = _userRepository.findBy_iId(iBlockedId);
        return !user.get_setBlockedBy().contains(blocked) && !blocked.get_setBlockedBy().contains(user);
    }

    /*

    @GetMapping("/loadDB")
    public void loadDB() {
        System.out.println("Recibida petición de inicialización de bd");
        System.out.println("Nº admins: " + _adminRepository.count());

        if(_adminRepository.count() == 0) {
            System.out.println("Inicializando base de datos...");
            _adminRepository.save(new Admin("admin@admin.com", "admin", "admin", "Admin"));
            Country country = _countryRepository.save(new Country("España"));
            Region region =_regionRepository.save(new Region("Andalucía", country));
            for(String sName: List.of("Cádiz", "Huelva", "Sevilla", "Málaga", "Córdoba", "Jaén", "Granada", "Almería"))
                _provinceRepository.save(new Province(sName, region));
            for(String sName: List.of("Borrada previamente", "Denunciar un evento", "Denunciar una publicación", "Denunciar un usuario", "Reportar un error"))
                _categoryRepository.save(new Category(sName));
            for(String sName: List.of("Música", "Videojuegos", "Deportes", "Arte"))
                _interestRepository.save(new Interest(sName));
            for(String sName: List.of("NewFollow", "FollowRequest", "NewEventAssistant", "NewEventComment", "NewEventPhoto", "NewMessage", "NewPostLike", "NewPostComment", "FollowRequestAccepted", "BehaviorWarning", "Announcement"))
                _typeNotificationRepository.save(new TypeNotification(sName));
            for(String sQuestion: List.of("¿Cuál es tu comida favorita?", "Qué tipo de música escuchas?", "¿Cuál es tu artista musical favorito?", "Cuál es tu principal hobby?", "¿Qué te gusta hacer cuando quedas con amigos?", "¿Cómo te definirías en una frase?", "¿Qué es lo que más valoras en una persona?"))
                _questionRepository.save(new AboutMeQuestion(sQuestion));
            _faqRepository.save(new FAQ("¿Si tengo una cuenta privada, los demás usuarios pueden ver mi información del perfil?",
                    "Sí, los demás usuarios pueden ver tu información básica, como puede ser la descripción, intereses, preguntas 'Conóceme' y fotos. La opción de cuenta privada se encarga de ocultar las " +
                            "publicaciones, eventos a los que hayas asistido y publicaciones que te hayan gustado de aquellos usuarios que no te sigan."));
            _faqRepository.save(new FAQ("Me ha llegado una notificación de comportamiento inadecuado. ¿Qué castigo implica?",
                    "Nuestros administradores pueden enviar avisos en caso de que se infrinjan las normas de la plataforma. De esta forma, se eliminan ciertas conductas no deseadas en el uso de " +
                            "la plataforma. Ten en cuenta que, cuantos más avisos tengas, más probabilidad hay de que un administrador te suspenda la cuenta, así que te rogamos que cumplas con la normativa de la página."));
            //_faqRepository.save(new FAQ("Pregunta frecuente 3", "Respuesta 3"));
            Calendar calendar = Calendar.getInstance();
            calendar.set(2000, Calendar.AUGUST, 10);
            _userRepository.save(new User("juanjo@gmail.com", "juanjo", "juanjogc", "Soy estudiante de GII en la UCA.", "User", "Juanjo", calendar.getTime(), _provinceRepository.findBy_sName("Cádiz")));
            calendar.set(2000, Calendar.JUNE, 1);
            _userRepository.save(new User("davidg@gmail.com", "davidg", "davidgr", "Amante de las películas y del arte.", "User", "David", calendar.getTime(), _provinceRepository.findBy_sName("Cádiz")));
            calendar.set(2000, Calendar.MAY, 17);
            _userRepository.save(new User("diego@gmail.com", "diego", "diegofc", "Me gustan mucho los videojuegos y el deporte.", "User", "Diego", calendar.getTime(), _provinceRepository.findBy_sName("Cádiz")));
            calendar.set(2000, Calendar.SEPTEMBER, 11);
            _userRepository.save(new User("davidm@gmail.com", "davidm", "davidmo", "Siempre he sido fanático de los videojuegos.", "User", "David", calendar.getTime(), _provinceRepository.findBy_sName("Sevilla")));
            calendar.set(2000, Calendar.OCTOBER, 21);
            _userRepository.save(new User("alejandro@gmail.com", "alejandro", "alejandrogm", "Calistenia como forma de vida.", "User", "Ale", calendar.getTime(), _provinceRepository.findBy_sName("Sevilla")));
        } else {
        System.out.println("La base de datos ya estaba inicializada");
        }

    }
    */
}


    /*
    @GetMapping("/removeImages")
    public void removeImages() {
        List<User> listUser = _userRepository.findAll();
        for(User user: listUser) {
            user.get_setImagePath().clear();
            user = _userRepository.save(user);
        }
        List<ImagePath> listImages = _imagePathRepository.findAll();
        for(ImagePath imagePath: listImages) {
            _imagePathRepository.delete(imagePath);
        }
    }
     */

    /*
    @GetMapping("/dropdb")
    @Transactional
    public void dropSchema() {
        entityManager.createNativeQuery("drop schema backend;").executeUpdate();
        entityManager.createNativeQuery("create schema backend;").executeUpdate();
    }

     */

