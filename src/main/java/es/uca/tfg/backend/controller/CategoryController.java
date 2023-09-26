package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Admin;
import es.uca.tfg.backend.entity.Category;
import es.uca.tfg.backend.entity.Person;
import es.uca.tfg.backend.repository.AdminRepository;
import es.uca.tfg.backend.repository.CategoryRepository;
import es.uca.tfg.backend.repository.PersonRepository;
import es.uca.tfg.backend.rest.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryRepository _categoryRepository;
    @Autowired
    private PersonRepository _personRepository;

    @GetMapping("/getAllCategories")
    public List<Category> getAllCategories() {
        return _categoryRepository.findAll();
    }

    @PostMapping("/createCategory")
    public boolean createCategory(@RequestBody CategoryDTO categoryDTO) {
        System.out.println("Llega: " + categoryDTO.get_sName());
        Category category = _categoryRepository.save(new Category(categoryDTO.get_sName(), categoryDTO.get_sDisplayName()));
        return category.get_iId() != 0;
    }
}
