package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.ImagePath;
import es.uca.tfg.backend.entity.Person;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.entity.ImagePath;
import es.uca.tfg.backend.rest.MapUserRegister;
import es.uca.tfg.backend.rest.UserChecker;
import es.uca.tfg.backend.repository.InterestRepository;
import es.uca.tfg.backend.repository.PersonRepository;
import es.uca.tfg.backend.repository.ImagePathRepository;
import es.uca.tfg.backend.repository.UserRepository;
import es.uca.tfg.backend.service.PersonService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
import java.util.List;

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
    private EntityManager entityManager;

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
        System.out.println("Despues del cambio: " + user.get_sDescription());
        return user;
    }

    @PostMapping("/uploadInterests")
    public User saveInterests(@RequestParam("id") int iId, @RequestParam("interests[]") String[] aSInterests) {
        System.out.println("ID: " + iId + " longitud del vector: " + aSInterests.length + " Elemento del vector: " + aSInterests[0]);
        User user = _userRepository.findBy_iId(iId);
        user.get_setInterests().clear();

        for(int i = 0; i < aSInterests.length; i++) {
            user.get_setInterests().add(_interestRepository.findBy_sName(aSInterests[i]));
        }
        user = _userRepository.save(user);

        return user;
    }

    /*
    @GetMapping("getProfileImage")
    public MultipartFile getProfileImage(int iId) {

    }

     */

    @PostMapping("/uploadProfileImage")
    public String saveProfileImage(@RequestParam("id") int iId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        User user = _userRepository.findBy_iId(iId);
        ImagePath profileImagePath = user.get_profileImagePath();
        System.out.println(_sUploadPath);
        Path path = Paths.get(_sUploadPath);

        /*

        if(profileImagePath != null) {
            File file = new File(_sUploadPath + profileImagePath
            user.get_setImagePath().remove(_imagePathRepository.findBy_iId(imagePathProfile.get_iId()));
            file.delete();
        }
        */


        String sFilename = user.get_iId() + "-" + multipartFile.getOriginalFilename();
        File file = new File(_sUploadPath + sFilename);
        System.out.println("Guardando la imagen con nombre: " + sFilename);
        System.out.println("Ruta: " + path.toString());
        multipartFile.transferTo(file);

        ImagePath imagePath = new ImagePath(sFilename);
        imagePath = _imagePathRepository.save(imagePath);
        user.get_setImagePath().add(imagePath);
        user = _userRepository.save(user);
        return "Guardado";
    }

    @PostMapping("/uploadImages")
    public String saveImages(@RequestParam("id") int iId, @RequestParam("file[]") MultipartFile[] aMultipartFile) throws IOException {
        User user = _userRepository.findBy_iId(iId);
        System.out.println(_sUploadPath);

        /*
        for(ImagePath imagePath: user.get_setImagePath()) {
            if(!imagePath.is_bIsProfile()) {
                _imagePathRepository.delete(imagePath);
                user.get_setImagePath().remove(imagePath);
            }
        }
         */

        for(int i = 0; i < aMultipartFile.length; i++) {
            String sFilename = user.get_iId() + "-" + aMultipartFile[i].getOriginalFilename();
            File file = new File(_sUploadPath + sFilename);
            aMultipartFile[i].transferTo(file);
            System.out.println("Guardada la imagen " + sFilename + " en la ruta " + _sUploadPath);
            ImagePath imagePath = new ImagePath(sFilename);
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
        //String sImageName = "2-unnamed.png";
        String sImageName = user.get_profileImagePath().get_sName();
        if(sImageName == null)
            sImageName = "GenericAvatar.png";
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

    @PostMapping("/testReq")
    public String prueba(@RequestParam("file[]") MultipartFile[] multipartFiles) {
        /*
        for(int i = 0; i < multipartFiles.length; i++) {
            String sFilename = multipartFiles[i].getOriginalFilename();
            System.out.println("Imagen " + i+1 + " " + sFilename);
        }
        */
        //System.out.println(multipartFiles.getOriginalFilename());
        /*
        _interestRepository.save(new Interest("Deportes"));
        _interestRepository.save(new Interest("Música"));
        _interestRepository.save(new Interest("Videojuegos"));
         */
        String sFilename = multipartFiles[1].getOriginalFilename();
        System.out.println(multipartFiles[0].getOriginalFilename());
        System.out.println(sFilename);
        System.out.println(multipartFiles[2].getOriginalFilename());

        return "Holabuena";
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

    @GetMapping("/dropdb")
    @Transactional
    public void dropSchema() {
        entityManager.createNativeQuery("drop schema backend;").executeUpdate();
        entityManager.createNativeQuery("create schema backend;").executeUpdate();
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
