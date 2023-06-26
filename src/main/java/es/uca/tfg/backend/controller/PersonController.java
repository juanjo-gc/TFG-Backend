package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.entity.ImagePath;
import es.uca.tfg.backend.repository.*;
import es.uca.tfg.backend.rest.MapUserRegister;
import es.uca.tfg.backend.rest.UserChecker;
import es.uca.tfg.backend.rest.UserFilterDTO;
import es.uca.tfg.backend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class PersonController {

    @Autowired
    private PersonService _personService;
    @Autowired
    private UserRepository _userRepository;
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


    private String _sUploadPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\users\\";
    @PostMapping("/register")
    public String registerNewUser(@RequestBody MapUserRegister mapUserRegister) {
        String sMessage;
        System.out.println(mapUserRegister.get_sEmail());
        System.out.println(mapUserRegister.get_sPassword());

        if(_personRepository.countBy_sUsername(mapUserRegister.get_sUsername()) == 0) {
            User user = _personRepository.save(new User(mapUserRegister.get_sEmail(), mapUserRegister.get_sPassword(),
                                                        mapUserRegister.get_sUsername(), "user", mapUserRegister.get_sName(),
                                                        mapUserRegister.get_tBirthDate()));
            //mapUserRegister.set_sMessage("Usuario creado correctamente.");
            sMessage = "Usuario creado correctamente.";
        } else {
            //mapUserRegister.set_sMessage("Error. El nombre de usuario ya existe.");
            sMessage = "Error. El nombre de usuario ya existe.";
        }
        return sMessage;
    }

    @PostMapping("/checkUser")
    public boolean isUsernameTaken(@RequestBody UserChecker.UsernameChecker usernameChecker) {
        if(_personRepository.countBy_sUsername(usernameChecker.get_sUsername()) != 0) {
            usernameChecker.set_bIsUsernameTaken(true);
        }
        return usernameChecker.is_bIsUsernameTaken();
    }

    @PostMapping("/checkEmail")
    public boolean isEmailTaken(@RequestBody UserChecker.EmailChecker emailChecker) {
        if(_personRepository.countBy_sEmail(emailChecker.get_sEmail()) != 0) {
            emailChecker.set_bIsEmailTaken(true);
        }
        return emailChecker.is_bIsEmailTaken();
    }

    @PostMapping("/login")
    public Person authenticateLogin(@RequestBody UserChecker.LoginChecker loginChecker) {
        System.out.println(loginChecker.get_sEmail() + " " + loginChecker.get_sPassword());
        return _personService.authenticate(loginChecker.get_sEmail(), loginChecker.get_sPassword());
    }

    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable("userId") int iUserId) {
        Optional<User> optUser = _userRepository.findById(iUserId);
        return optUser.isPresent() ? optUser.get() : new User();
    }

    @GetMapping("/getUserFromUsername/{username}")
    public User getUserFromUsername(@PathVariable("username") String sUsername) {
        System.out.println(sUsername);
        User user = _userRepository.findBy_sUsername(sUsername);
        return user != null ? user : new User();
    }

    @PostMapping("/updateUserDetails")
    public User updateUserDetails(@RequestParam("id") int iId, @RequestParam("name") String sName, @RequestParam("description") String sDescription,
                                  @RequestParam("email") String sEmail, @RequestParam("username") String sUsername) {
        User user = _userRepository.findBy_iId(iId);
        System.out.println(iId + " Nombre " + sName + " Descr " + sDescription + " User " + sUsername + " email: " +sEmail);
        user.set_sName(sName);
        user.set_sUsername(sUsername);
        user.set_sEmail(sEmail);
        user.set_sDescription(sDescription);
        user = _userRepository.save(user);
        //System.out.println("Despues del cambio: " + user.get_sDescription());
        return user;
    }

    @PostMapping("/uploadInterests")
    public User saveInterests(@RequestParam("id") int iId, @RequestParam("interests[]") String[] aSInterests) {


        System.out.println("ID: " + iId + " longitud del vector: " + aSInterests.length + " Elemento del vector: " + aSInterests[0]);
        User user = _userRepository.findBy_iId(iId);
        System.out.println("Nº de intereses actuales del usuario: " + user.get_setInterests().size());

        user.get_setInterests().clear();

        for(int i = 0; i < aSInterests.length; i++) {
            System.out.println("Añadiendo interes: " + _interestRepository.findBy_sName(aSInterests[i]).get_sName());
            user.get_setInterests().add(_interestRepository.findBy_sName(aSInterests[i]));
        }
        user = _userRepository.save(user);

        return user;
    }

    @GetMapping("/getUserInterests/{userId}")
    public List<Interest> getUserInterests(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? optionalUser.get().get_setInterests().parallelStream().toList() : Collections.emptyList();
    }

    /*
    @GetMapping("getProfileImage")
    public MultipartFile getProfileImage(int iId) {

    }

     */

    @PostMapping("/uploadProfileImage")
    public String saveProfileImage(@RequestParam("id") int iId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        User user = _userRepository.findBy_iId(iId);
        System.out.println(_sUploadPath);
        Path path = Paths.get(_sUploadPath);

        if(user.get_profileImagePath() != null) {
            File file = new File(_sUploadPath + user.get_profileImagePath().get_sName());
            file.delete();
        }

        String sFilename = user.get_iId() + "-" + multipartFile.getOriginalFilename();
        File file = new File(_sUploadPath + sFilename);
        System.out.println("Guardando la imagen con nombre: " + sFilename);
        System.out.println("Ruta: " + path.toString());
        multipartFile.transferTo(file);

        ImagePath imagePath = new ImagePath(sFilename);
        imagePath.set_user(user);
        imagePath = _imagePathRepository.save(imagePath);
        user.set_profileImagePath(imagePath);
        user = _userRepository.save(user);
        return "Guardado";
    }

    @PostMapping("/uploadImages")
    public String saveImages(@RequestParam("id") int iId, @RequestParam("file[]") MultipartFile[] aMultipartFile) throws IOException {
        User user = _userRepository.findBy_iId(iId);
        System.out.println(_sUploadPath);

        for(int i = 0; i < aMultipartFile.length; i++) {
            String sFilename = user.get_iId() + "-" + aMultipartFile[i].getOriginalFilename();
            File file = new File(_sUploadPath + sFilename);
            aMultipartFile[i].transferTo(file);
            System.out.println("Guardada la imagen " + sFilename + " en la ruta " + _sUploadPath);
            ImagePath imagePath = new ImagePath(sFilename);
            imagePath.set_user(user);
            imagePath = _imagePathRepository.save(imagePath);
            user.get_setImagePath().add(imagePath);
            user = _userRepository.save(user);
        }
        return "Imágenes subidas correctamente";
    }

    @GetMapping("/getProfileImage/{id}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getProfileImage(@PathVariable("id") int iId) throws FileNotFoundException {
        User user = _userRepository.findBy_iId(iId);
        String sImageName = "GenericAvatar.png";
        if(user.get_profileImagePath() != null)
            sImageName = user.get_profileImagePath().get_sName();
        System.out.print(sImageName);
        File file = new File(_sUploadPath + sImageName);
        FileInputStream fileInputStream = new FileInputStream(file);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(fileInputStream));
    }

    @GetMapping("/getImage/{imageName}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImagePath(@PathVariable("imageName") String sImageName) throws FileNotFoundException {
        /*
        User user = _userRepository.findBy_iId(2);
        ImagePath uimg = user.get_setImagePath().iterator().next();
        String sFilename = uimg.get_sName();

        char cFormat = sFilename.toCharArray()[sFilename.length() - 3];

        MediaType contentType = Character.compare(cFormat, 'j') == 0 ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
        System.out.println("Content type : " + contentType);
        System.out.println(_sUploadPath+uimg.get_sName());
        //InputStream inputStream = getClass().getResourceAsStream(_sUploadPath + uimg.get_sName());
        //System.out.println(inputStream);
        File file = new File(_sUploadPath + sFilename);
        FileInputStream fileInputStream = new FileInputStream(file);

         */
        File file = new File(_sUploadPath + sImageName);
        FileInputStream fileInputStream = new FileInputStream(file);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(fileInputStream));
    }

    @GetMapping("/getImageNames/{userId}")
    public List<ImagePath> getImageNames(@PathVariable("userId") int iUserId) {
        Optional<User> optionalUser = _userRepository.findById(iUserId);
        return optionalUser.isPresent() ? optionalUser.get().get_setImagePath().parallelStream().toList() : Collections.emptyList();
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

        if(user.get_setFollowing().contains(userToFollow)) {
            user.get_setFollowing().remove(userToFollow);
            System.out.println("Deja de seguir");
        } else {
            user.get_setFollowing().add(userToFollow);
            bIsFollowing = true;
            System.out.println("Siguiendo ahora");
        }
        user = _userRepository.save(user);
        return bIsFollowing;
    }

    @GetMapping("/getNumFollows/{userId}")
    public ArrayList<Integer> getNumFollows(@PathVariable("userId") int iUserId) {
        ArrayList<Integer> numFollows = new ArrayList<>();
        User user = _userRepository.findBy_iId(iUserId);

        numFollows.add(user.get_setFollowing().size());
        numFollows.add(user.get_setFollowers().size());

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
        System.out.println(sUsername);
        for(User follower: user.get_setFollowers()) {
            System.out.println(follower.get_sUsername());
        }
        return user.get_iId() != 0 ? user.get_setFollowers().stream().toList() : Collections.emptyList();
    }

    @GetMapping("/findUsers/{username}")
    public List<User> getUsersByUsername(@PathVariable("username") String sUsername) {
        return _userRepository.findFirst7By_sUsernameStartsWith(sUsername);
    }

    @PostMapping(value = "/filterUsers/{userId}")
    public List<Integer> filterUsers(@RequestBody UserFilterDTO filter, @PathVariable("userId") int iUserId) {
        List<Integer> aiFilteredUserIdsByInterest;
        List<Integer> aiFilteredUserIds = _userRepository.findUserIdsByLocation(
                _provinceRepository.findBy_sName(filter.get_sProvince()),
                _regionRepository.findBy_sName(filter.get_sRegion()),
                _countryRepository.findBy_sName(filter.get_sCountry()));


        if(!filter.get_asInterests().isEmpty()) {
            int iNumberOfInterests = filter.get_asInterests().size();
             aiFilteredUserIdsByInterest = _userRepository.findUserIdsByOptionalInterests(
                    iNumberOfInterests >= 1 ? _interestRepository.findBy_sName(filter.get_asInterests().get(0)) : null,
                    iNumberOfInterests >= 2 ? _interestRepository.findBy_sName(filter.get_asInterests().get(1)) : null,
                    iNumberOfInterests >= 3 ? _interestRepository.findBy_sName(filter.get_asInterests().get(2)) : null
            );
        } else {
            aiFilteredUserIdsByInterest = _userRepository.findAllIds();
        }

        System.out.println("Antes de filtro: " + aiFilteredUserIds.toString() + " (Llegó la ID "+ iUserId);

        aiFilteredUserIds = aiFilteredUserIds.stream().filter(iId -> aiFilteredUserIdsByInterest.contains(iId) && iId != iUserId).collect(Collectors.toList());

        System.out.println("Despues de filtro: " + aiFilteredUserIds.toString());

        List<Integer> aiFollowedIds = _userRepository.findFollowingUserIds(_userRepository.findBy_iId(iUserId));

        System.out.println("Despues de filtro 2: " + aiFilteredUserIds.stream().filter(iId -> !aiFollowedIds.contains(Integer.valueOf(iId))).collect(Collectors.toList()).toString());

        //TODO filtrar las ids para que no se envíen las ids de los usuarios seguidos por quien envia la peticion
        return aiFilteredUserIds.stream().filter(iId -> !aiFollowedIds.contains(Integer.valueOf(iId))).collect(Collectors.toList());
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

    @GetMapping("/customGet")
    void customGet() {
        /*
        Country country = _countryRepository.save(new Country("España"));
        _regionRepository.save(new Region("Andalucía", country));
        _regionRepository.save(new Region("Comunidad Valenciana", country));
        _provinceRepository.save(new Province("Cádiz", _regionRepository.findById(1).get()));
        _provinceRepository.save(new Province("Sevilla", _regionRepository.findById(1).get()));
        _provinceRepository.save(new Province("Málaga", _regionRepository.findById(1).get()));
        User user = _userRepository.findBy_iId(2);
        user.set_province(_provinceRepository.findById(1).get());
        _userRepository.save(user);

         */
        System.out.println(_userRepository.findUserIdsByOptionalInterests(null, null, null).toString());

    }
        /*
    Esto funciona pero no es la mejor manera
    @GetMapping("getImages")
    public String getUsersImages() throws IOException {
        User user = _userRepository.findBy_iId(2);

        ImagePath imagePath = user.get_setImagePath().iterator().next();
        String sFilename = imagePath.get_sName();
        byte[] fileContent = FileUtils.readFileToByteArray(new File(_sUploadPath + sFilename));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        System.out.println(_sUploadPath);
        return encodedString;
    }
     */
}
